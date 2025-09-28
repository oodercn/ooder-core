package net.ooder.esd.dsm.gen.agg;

import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggMenuRoot;
import net.ooder.esd.dsm.aggregation.context.AggServiceRoot;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRef;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.gen.BaseAggCallabel;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.annotation.AggregationType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class GenAggRootJava extends BaseAggCallabel {

    DomainInst domainInst;
    AggEntityConfig esdClassConfig;
    Set<String> allTemps;
    ChromeProxy chrome;

    public GenAggRootJava(DomainInst domainInst, AggEntityConfig esdClassConfig, Set<String> allTemps, ChromeProxy chrome) {
        super();
        this.domainInst = domainInst;
        this.esdClassConfig = esdClassConfig;
        this.allTemps = allTemps;
        this.chrome = chrome;
    }

    @Override
    public List<JavaSrcBean> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        String projectName=domainInst.getProjectVersionName();
        GenJava javaGen = GenJava.getInstance(projectName);
        List<JavaSrcBean> srcFiles = new ArrayList<>();
        List<AggEntityRef> dsmRefs = DSMFactory.getInstance().getAggregationManager().getEntityRefByName(esdClassConfig.getSourceClassName(), domainInst.getDomainId(), projectName);
        if (allTemps == null || allTemps.isEmpty()) {
            allTemps = domainInst.getJavaTempIds();
        }
        allTemps.addAll(esdClassConfig.getJavaTempIds());
        for (String javaTempId : allTemps) {
            JavaTemp javatemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            if (javatemp != null && !javatemp.getRangeType().equals(RangeType.MODULEVIEW) && javatemp.getAggregationType().equals(AggregationType.MODULE)) {
                AggServiceRoot root = new AggServiceRoot(domainInst, esdClassConfig, dsmRefs);
                String basePath = domainInst.getPackageName() + "." + esdClassConfig.getESDClass().getEntityClass().getName().toLowerCase();
                root.setBasepath(basePath);
                String packageName = basePath;
                if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("")) {
                    packageName = packageName + "." + javatemp.getPackagePostfix();
                }
                String className = StringUtility.replace(javatemp.getNamePostfix(), "**", esdClassConfig.getESDClass().getEntityClass().getName());
                root.setClassName(className);
                root.setPackageName(packageName);
                File file = javaGen.createJava(javatemp, root, chrome);
                JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, domainInst, javaTempId);
                srcBean.setEntityClassName(esdClassConfig.getESDClass().getEntityClassName());
                srcFiles.add(srcBean);
            }
        }
        return srcFiles;

    }
}
