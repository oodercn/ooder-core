package net.ooder.esd.dsm.gen.agg;

import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggEntityRoot;
import net.ooder.esd.dsm.aggregation.context.AggProxyRoot;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRef;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.gen.BaseAggCallabel;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.server.context.MinServerActionContextImpl;
import net.ooder.annotation.AggregationType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class GenAggAPIJava  extends BaseAggCallabel {

    DomainInst domainInst;
    AggEntityConfig esdClassConfig;
    Set<String> allTemps;
    ChromeProxy chrome;

    public GenAggAPIJava(DomainInst domainInst, AggEntityConfig esdClassConfig, Set<String> allTemps, ChromeProxy chrome) {
        super();
        this.domainInst = domainInst;
        this.esdClassConfig = esdClassConfig;
        this.allTemps = allTemps;
        this.chrome = chrome;


    }

    @Override
    public List<JavaSrcBean> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        GenJava javaGen = GenJava.getInstance(domainInst.getProjectVersionName());
        List<JavaSrcBean> srcFiles = new ArrayList<>();
        String simClassName = esdClassConfig.getSourceClass().getName();
        String className = esdClassConfig.getSourceClass().getClassName();
        allTemps.addAll(esdClassConfig.getJavaTempIds());
        for (String javaTempId : allTemps) {
            JavaTemp javatemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            if (javatemp != null && !javatemp.getRangeType().equals(RangeType.MODULEVIEW) && javatemp.getAggregationType().equals(AggregationType.ENTITY)) {
                AggProxyRoot root = new AggProxyRoot(domainInst, esdClassConfig);
                String packageName = className.substring(0, className.lastIndexOf("."));
                root.setClassName(simClassName);
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
