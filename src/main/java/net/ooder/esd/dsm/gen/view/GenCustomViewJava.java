package net.ooder.esd.dsm.gen.view;

import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.gen.GenJavaTask;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.ViewInst;
import net.ooder.jds.core.esb.EsbUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GenCustomViewJava extends GenJavaTask {

    public AggViewRoot viewRoot;
    public CustomViewBean viewBean;
    public String moduleName;
    public String className;

    public GenCustomViewJava(AggViewRoot viewRoot, CustomViewBean viewBean, String className, ChromeProxy chrome) {
        super();
        this.chrome = chrome;
        this.viewRoot = viewRoot;
        this.viewBean = viewBean;
        this.className = className;
        this.moduleName = viewRoot.getModuleName();
        try {
            ModuleViewType moduleViewType = viewBean.getModuleViewType();
            this.viewTemps = BuildFactory.getInstance().getTempManager().getCustomModuleViewTemps(moduleViewType.getDefaultView());
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<JavaSrcBean> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        if (chrome == null) {
            chrome = this.getCurrChromeDriver();
        }
        GenJava javaGen = GenJava.getInstance(viewRoot.getDsmBean().getProjectVersionName());
        chrome.printLog("开始通用模型视图：" + className, true);
        String simClassName = className.substring(className.lastIndexOf(".") + 1);
        String packageName = className.substring(0, className.lastIndexOf("."));
        DomainInst domainInst = (DomainInst) viewRoot.getDsmBean();
        ViewInst defaultView = domainInst.getViewInst();
        JavaRoot javaRoot = BuildFactory.getInstance().buildJavaRoot(viewRoot, viewBean, moduleName, className);
        List<JavaSrcBean> srcFiles = new ArrayList<>();

        for (JavaTemp javatemp : viewTemps) {
            boolean canGen = true;
            String expression = javatemp.getExpression();
            if (expression != null && !expression.equals("")) {
                canGen = EsbUtil.parExpression(javatemp.getExpression(), JDSActionContext.getActionContext().getContext(), javaRoot, Boolean.class);
            }

            if (canGen && !skipsTempIds.contains(javatemp.getJavaTempId())) {
                String genClassName = StringUtility.replace(javatemp.getNamePostfix(), "**", simClassName);
                javaRoot.setClassName(genClassName);
                if (moduleName != null && !moduleName.equals("")) {
                    packageName = packageName + "." + moduleName;
                }
                if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("") && !javatemp.getPackagePostfix().equals("..")) {
                    packageName = packageName + "." + javatemp.getPackagePostfix();
                }
                javaRoot.setPackageName(packageName);
                File file = javaGen.createJava(javatemp, javaRoot, chrome);
                JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, defaultView, javatemp.getJavaTempId());
                srcFiles.add(srcBean);
                defaultView.addJavaBean(srcBean);
                BuildFactory.getInstance().createSource(srcBean.getClassName(), javaRoot, javatemp, srcBean);
                classList.add(srcBean.getClassName());

            }
        }
        return srcFiles;
    }


    public AggViewRoot getViewRoot() {
        return viewRoot;
    }

    public void setViewRoot(AggViewRoot viewRoot) {
        this.viewRoot = viewRoot;
    }

    public CustomViewBean getViewBean() {
        return viewBean;
    }

    public void setViewBean(CustomViewBean viewBean) {
        this.viewBean = viewBean;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}
