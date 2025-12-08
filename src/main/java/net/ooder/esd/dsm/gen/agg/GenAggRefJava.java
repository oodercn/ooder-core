package net.ooder.esd.dsm.gen.agg;

import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggEntityRefRoot;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRef;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRefProxy;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.gen.GenJavaTask;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenAggRefJava extends GenJavaTask {

    DomainInst domainInst;
    AggEntityConfig esdClassConfig;
    Set<String> allTemps;
    ChromeProxy chrome;

    public GenAggRefJava(DomainInst domainInst, AggEntityConfig esdClassConfig, Set<String> allTemps, ChromeProxy chrome) {
        super();
        this.domainInst = domainInst;
        this.esdClassConfig = esdClassConfig;
        this.allTemps = allTemps;
        this.chrome = chrome;
    }

    @Override
    public List<JavaGenSource> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        String projectName = domainInst.getProjectVersionName();
        GenJava javaGen = GenJava.getInstance(projectName);
        List<JavaGenSource> srcFiles = new ArrayList<>();
        if (allTemps == null) {
            allTemps = domainInst.getJavaTempIds();
        }
        for (String javaTempId : allTemps) {
            JavaTemp javatemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            if (javatemp != null && !javatemp.getRangeType().equals(RangeType.MODULEVIEW) && javatemp.getRangeType().equals(RangeType.ENTITYREF)) {
                Set<AggEntityRefProxy> proxyList = new HashSet<>();
                Set<AggEntityRef> refs = DSMFactory.getInstance().getAggregationManager().getEntityRefByName(esdClassConfig.getESDClass().getClassName(), esdClassConfig.getDomainId(), javatemp.getRefType(), projectName);
                for (AggEntityRef ref : refs) {
                    AggEntityRefProxy proxy = new AggEntityRefProxy(ref, esdClassConfig.getDomainId());
                    proxyList.add(proxy);
                }

                AggEntityRefRoot javaRoot = new AggEntityRefRoot(domainInst, esdClassConfig, proxyList, javatemp.getRefType());
                String basePath = domainInst.getPackageName();
                javaRoot.setBasepath(basePath);
                String packageName = basePath + "." + esdClassConfig.getSourceClass().getName().toLowerCase();
                if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("")) {
                    packageName = packageName + "." + javatemp.getPackagePostfix();
                }
                String className = StringUtility.replace(javatemp.getNamePostfix(), "**", esdClassConfig.getSourceClass().getName());
                javaRoot.setClassName(className);
                javaRoot.setPackageName(packageName);
                File file = GenJava.getInstance(domainInst.getProjectVersionName()).createJava(javatemp, javaRoot, chrome);
                JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, domainInst, javaTempId);
                srcBean.setEntityClassName(esdClassConfig.getESDClass().getClassName());

                JavaGenSource javaGenSource = BuildFactory.getInstance().createSource(srcBean.getClassName(), javaRoot, javatemp, srcBean);
                srcFiles.add(javaGenSource);
                classList.add(srcBean.getClassName());
            }
        }

        return srcFiles;
    }

}
