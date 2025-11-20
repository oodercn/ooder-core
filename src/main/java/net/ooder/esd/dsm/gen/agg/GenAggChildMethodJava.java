package net.ooder.esd.dsm.gen.agg;

import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggDomainRoot;
import net.ooder.esd.dsm.aggregation.context.AggEntityRoot;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRef;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.gen.GenJavaTask;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.server.context.MinServerActionContextImpl;
import net.ooder.annotation.AggregationType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class GenAggChildMethodJava extends GenJavaTask {

    DomainInst domainInst;
    MethodConfig methodConfig;
    JavaTemp javatemp;
    String moduleName;
    ChromeProxy chrome;

    public GenAggChildMethodJava(DomainInst domainInst, String moduleName, MethodConfig methodConfig, JavaTemp javatemp, ChromeProxy chrome) {
        super();
        this.domainInst = domainInst;
        this.methodConfig = methodConfig;
        this.javatemp = javatemp;
        this.moduleName = moduleName;
        this.chrome = chrome;

    }

    @Override
    public List<JavaSrcBean> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        String projectName = domainInst.getProjectVersionName();
        GenJava javaGen = GenJava.getInstance(projectName);
        List<JavaSrcBean> srcFiles = new ArrayList<>();
        if (methodConfig.getViewClass() != null) {
            String entityClassName = methodConfig.getViewClass().getClassName();
            AggEntityConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(entityClassName,true);
            if (moduleName != null) {
                esdClassConfig.getAggregationBean().setModuleName(moduleName.toLowerCase());
            }
            List<AggEntityRef> dsmRefs = DSMFactory.getInstance().getAggregationManager().getEntityRefByName(methodConfig.getViewClass().getClassName(), domainInst.getDomainId(), projectName);
            AggEntityRoot javaRoot = new AggEntityRoot(domainInst, esdClassConfig, dsmRefs);
            String basePath = domainInst.getPackageName() + "." + esdClassConfig.getESDClass().getEntityClass().getName().toLowerCase();
            javaRoot.setBasepath(basePath);
            String packageName = basePath;
            if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("") && !javatemp.getPackagePostfix().equals("..")) {
                packageName = packageName + "." + javatemp.getPackagePostfix();
            }
            String className = StringUtility.replace(javatemp.getNamePostfix(), "**", esdClassConfig.getESDClass().getEntityClass().getName());
            javaRoot.setClassName(className);
            javaRoot.setPackageName(packageName);
            File file = javaGen.createJava(javatemp, javaRoot, chrome);
            JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, domainInst, javatemp.getJavaTempId());
            srcBean.setSourceClassName(methodConfig.getSourceClassName());
            srcBean.setMethodName(methodConfig.getMethodName());
            srcFiles.add(srcBean);
            BuildFactory.getInstance().createSource(srcBean.getClassName(), javaRoot, javatemp, srcBean);
            classList.add(srcBean.getClassName());
        }

        return srcFiles;
    }

}
