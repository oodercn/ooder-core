package net.ooder.esd.dsm.gen.agg;

import net.ooder.annotation.AggregationType;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMInst;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.gen.GenJavaTask;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.jds.core.esb.EsbUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GenAggCustomJava extends GenJavaTask {

    public AggViewRoot viewRoot;
    public CustomViewBean viewBean;
    public AggregationType aggregationType;
    public String moduleName;
    public String className;
    public ChromeProxy chrome;

    public GenAggCustomJava(AggViewRoot viewRoot, CustomViewBean viewBean, ChromeProxy chrome) {

        this.viewRoot = viewRoot;
        this.viewBean = viewBean;
        this.className = viewRoot.getClassName();
        this.moduleName = className.substring(0, className.lastIndexOf(".")).toLowerCase();
        this.aggregationType = AggregationType.NAVIGATION;
        try {
            viewTemps = BuildFactory.getInstance().getTempManager().getAggregationTemps(aggregationType);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        ;

        this.chrome = chrome;

    }

    public GenAggCustomJava(AggViewRoot viewRoot, CustomViewBean viewBean, AggregationType aggregationType, String moduleName, String className, ChromeProxy chrome) {
        super();
        this.viewRoot = viewRoot;
        this.viewBean = viewBean;
        this.moduleName = moduleName;

        this.aggregationType = aggregationType;
        this.className = className;
        this.chrome = chrome;
        try {
            viewTemps = BuildFactory.getInstance().getTempManager().getAggregationTemps(aggregationType);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    public void reSetCustomView(CustomViewBean customViewBean) {
        this.viewBean = customViewBean;
        if (viewRoot != null) {
            viewRoot.reSetCustomView(customViewBean);
        }

    }

    @Override
    public List<JavaGenSource> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        GenJava javaGen = GenJava.getInstance(viewRoot.getDsmBean().getProjectVersionName());
        DSMInst domainInst = viewRoot.getDsmBean();
        Set allTemps = domainInst.getJavaTempIds();
        ModuleViewType moduleViewType = viewBean.getModuleViewType();
        if (moduleName.startsWith(domainInst.getEuPackage() + ".")) {
            moduleName = moduleName.substring((domainInst.getEuPackage() + ".").length());
        }
        JavaRoot javaRoot = BuildFactory.getInstance().buildJavaRoot(viewRoot, viewBean, moduleName, className);
        List<JavaGenSource> genSources = new ArrayList<>();
        String simClassName = className.substring(className.lastIndexOf(".") + 1);
        for (JavaTemp javatemp : viewTemps) {
            if (javatemp.getRangeType() != null && javatemp.getViewType().equals(moduleViewType.getDefaultView()) && allTemps.contains(javatemp.getJavaTempId())) {
                boolean canGen = true;
                String expression = javatemp.getExpression();
                if (expression != null && !expression.equals("")) {
                    canGen = EsbUtil.parExpression(javatemp.getExpression(), JDSActionContext.getActionContext().getContext(), javaRoot, Boolean.class);
                }
                if (canGen) {
                    String genClassName = StringUtility.replace(javatemp.getNamePostfix(), "**", simClassName);
                    String packageName = domainInst.getPackageName();
                    if (moduleName.startsWith(domainInst.getEuPackage() + ".")) {
                        moduleName = moduleName.substring((domainInst.getEuPackage() + ".").length());
                    }

                    if (!moduleName.equals("") && !moduleName.toLowerCase().equals(simClassName.toLowerCase())) {
                        packageName = packageName + "." + moduleName.toLowerCase();
                    }

                    if (!moduleName.toLowerCase().endsWith("." + simClassName.toLowerCase())) {
                        packageName = packageName + "." + simClassName.toLowerCase();
                    }

                    if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("") && !javatemp.getPackagePostfix().equals("..")) {
                        packageName = packageName + "." + javatemp.getPackagePostfix();
                    }
                    javaRoot.setClassName(genClassName);
                    javaRoot.setPackageName(packageName);
                    File file = javaGen.createJava(javatemp, javaRoot, null);
                    JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, domainInst, javatemp.getJavaTempId());

                    domainInst.addJavaBean(srcBean);
                    JavaGenSource javaGenSource = BuildFactory.getInstance().createSource(srcBean.getClassName(), javaRoot, javatemp, srcBean);
                    genSources.add(javaGenSource);
                    classList.add(srcBean.getClassName());
                }
            }
        }

        return genSources;

    }
}
