package net.ooder.esd.dsm.gen.repository;

import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.DSMInst;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.enums.RepositoryType;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.gen.GenJavaTask;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.util.OODUtil;
import net.ooder.jds.core.esb.EsbUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GenRepositoryViewJava extends GenJavaTask {

    public AggViewRoot viewRoot;
    public CustomViewBean viewBean;
    public String moduleName;
    public String fullClassName;
    public String simClassName;
    public boolean clear;
    public RepositoryType[] repositoryTypes = new RepositoryType[]{RepositoryType.VO, RepositoryType.DO, RepositoryType.REPOSITORY, RepositoryType.REPOSITORYIMPL};
    public ChromeProxy chrome;

    public GenRepositoryViewJava(AggViewRoot viewRoot, CustomViewBean viewBean, String moduleName, String fullClassName, boolean clear, ChromeProxy chrome, RepositoryType... repositoryTypes) {
        super();
        this.viewRoot = viewRoot;
        this.viewBean = viewBean;
        this.clear = clear;

        try {
            DomainInst domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(viewBean.getDomainId());
            if (domainInst != null) {
                String euPackageName = domainInst.getEuPackage();
                if (moduleName.equals(euPackageName)) {
                    moduleName = "";
                } else if (moduleName.startsWith(euPackageName + ".")) {
                    moduleName = moduleName.substring((euPackageName + ".").length());
                }

            }
        } catch (JDSException e) {
            e.printStackTrace();
        }

        if (fullClassName.indexOf(".") > -1) {
            simClassName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            simClassName = OODUtil.formatJavaName(simClassName, true);
            String packageName = fullClassName.substring(0, fullClassName.lastIndexOf("."));
            this.fullClassName = packageName + "." + simClassName;//
        } else {
            fullClassName = OODUtil.formatJavaName(fullClassName, true);
            simClassName = fullClassName;
        }


        this.moduleName = moduleName;
        if (repositoryTypes != null) {
            this.repositoryTypes = repositoryTypes;
        }

        this.chrome = chrome;
    }

    @Override
    public List<JavaGenSource> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        if (chrome == null) {
            chrome = this.getCurrChromeDriver();
        }
        DSMInst dsmBean = viewRoot.getDsmBean();
        GenJava javaGen = GenJava.getInstance(dsmBean.getProjectVersionName());
        ModuleViewType moduleViewType = viewBean.getModuleViewType();
        JavaRoot javaRoot = BuildFactory.getInstance().buildJavaRoot(viewRoot, viewBean, moduleName, fullClassName);
        List<JavaGenSource> genSources = new ArrayList<>();
        List<JavaTemp> viewTemps = BuildFactory.getInstance().getTempManager().getRepositoryCatTemps(moduleViewType.getDefaultView(), RangeType.MODULEVIEW, repositoryTypes);
        chrome.printLog("开始创建模型视图：" + fullClassName, true);
        for (JavaTemp javatemp : viewTemps) {
            boolean canGen = true;
            String expression = javatemp.getExpression();
            String genClassName = StringUtility.replace(javatemp.getNamePostfix(), "**", simClassName);
            if (expression != null && !expression.equals("")) {
                canGen = EsbUtil.parExpression(javatemp.getExpression(), JDSActionContext.getActionContext().getContext(), javaRoot, Boolean.class);
            }
            if (canGen) {
                javaRoot.setClassName(genClassName);
                String packageName = dsmBean.getPackageName();
                if (!moduleName.equals("")) {
                    packageName = packageName + "." + moduleName.toLowerCase();
                }

                if (!moduleName.equals(simClassName.toLowerCase()) && !moduleName.endsWith("." + simClassName.toLowerCase()) && !simClassName.toLowerCase().endsWith(moduleName.toLowerCase())) {
                    packageName = packageName + "." + simClassName.toLowerCase();
                }


                if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("") && !javatemp.getPackagePostfix().equals("..")) {
                    packageName = packageName + "." + javatemp.getPackagePostfix();
                }
                javaRoot.setPackageName(packageName);
                String realClassName = packageName + "." + genClassName;
                Class clazz = null;
                try {
                    clazz = ClassUtility.loadClass(realClassName);
                } catch (ClassNotFoundException e) {

                }
                JavaSrcBean srcBean = null;
                if (clazz == null || clear) {
                    File file = javaGen.createJava(javatemp, javaRoot, chrome);
                    srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, dsmBean, javatemp.getJavaTempId());
                    dsmBean.addJavaBean(srcBean);
                } else {
                    srcBean = dsmBean.getJavaSrcByClassName(realClassName);
                    if (srcBean != null && srcBean.getJavaTempId() == null) {
                        srcBean.setJavaTempId(javatemp.getJavaTempId());
                    }
                }
                JavaGenSource javaGenSource=BuildFactory.getInstance().createSource(srcBean.getClassName(), javaRoot, javatemp, srcBean);
                genSources.add(javaGenSource);
                classList.add(srcBean.getClassName());
            }

        }

        return genSources;
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome == null) {
            chrome = new LogSetpLog();
        }
        return chrome;
    }
}
