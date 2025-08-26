package net.ooder.esd.dsm.gen.agg;

import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggEntityRoot;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRef;
import net.ooder.esd.dsm.gen.BaseAggCallabel;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.annotation.AggregationType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenAggEntityJava extends BaseAggCallabel {

    DomainInst domainInst;
    AggEntityConfig esdClassConfig;
    AggregationType aggregationType;
    Set<String> allTemps;
    ChromeProxy chrome;

    public GenAggEntityJava(DomainInst domainInst, AggEntityConfig esdClassConfig, AggregationType aggregationType, Set<String> allTemps, ChromeProxy chrome) {
        super();
        this.domainInst = domainInst;
        this.esdClassConfig = esdClassConfig;
        this.allTemps = allTemps;
        this.aggregationType = aggregationType;
        this.chrome = chrome;

    }

    @Override
    public List<JavaSrcBean> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        String projectName = domainInst.getProjectVersionName();
        GenJava javaGen = GenJava.getInstance(projectName);
        List<JavaSrcBean> srcFiles = new ArrayList<>();
        List<AggEntityRef> dsmRefs = DSMFactory.getInstance().getAggregationManager().getEntityRefByName(esdClassConfig.getSourceClassName(), domainInst.getDomainId(), projectName);
        if (allTemps == null) {
            allTemps = new HashSet<>();
        }
        allTemps.addAll(esdClassConfig.getJavaTempIds());
        for (String javaTempId : allTemps) {
            JavaTemp javatemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            if (javatemp != null && javatemp.getAggregationType().equals(aggregationType)) {
                AggEntityRoot root = new AggEntityRoot(domainInst, esdClassConfig, dsmRefs);
                String entityName = esdClassConfig.getESDClass().getEntityClass().getName().toLowerCase();
                String moduleName = root.getModuleName();
                String spaceSpl =  domainInst.getEuPackage()+ ".";
                if (moduleName.startsWith(spaceSpl)) {
                    moduleName = moduleName.substring(spaceSpl.length());
                }

                String basePath = domainInst.getPackageName() + "." + entityName;
                if (moduleName != null && !moduleName.equals("") && !moduleName.equals( domainInst.getEuPackage() )) {
                    basePath = domainInst.getPackageName() + "." + moduleName.toLowerCase();
                    if (!basePath.endsWith("." + entityName)) {
                        basePath = basePath + "." + entityName;
                    }
                }

                root.setBasepath(basePath.toLowerCase());
                String packageName = basePath.toLowerCase();
                if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("") && !packageName.endsWith("." + javatemp.getPackagePostfix())) {
                    packageName = packageName + "." + javatemp.getPackagePostfix();
                }
                String className = esdClassConfig.getESDClass().getName();
                if (esdClassConfig.getRootClass() != null) {
                    className = esdClassConfig.getRootClass().getName();
                }

                className = StringUtility.replace(javatemp.getNamePostfix(), "**", className);
                root.setClassName(className);
                root.setPackageName(packageName);
                File file = javaGen.createJava(javatemp, root, chrome);
                JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, domainInst, javaTempId);
                srcBean.setEntityClassName(esdClassConfig.getESDClass().getEntityClass().getClassName());
                srcFiles.add(srcBean);
            }
        }
        return srcFiles;
    }

}
