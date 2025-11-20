package net.ooder.esd.dsm.gen.agg;

import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggMenuRoot;
import net.ooder.esd.dsm.aggregation.context.AggRoot;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRef;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.gen.BaseAggCallabel;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.util.OODUtil;
import net.ooder.annotation.AggregationType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class GenAggModuleJava extends BaseAggCallabel {

    DomainInst domainInst;
    Set<String> allTemps;
    ChromeProxy chrome;

    public GenAggModuleJava(DomainInst domainInst, Set<String> allTemps, ChromeProxy chrome) {
        super();
        this.domainInst = domainInst;
        this.allTemps = allTemps;
        this.chrome = chrome;
    }

    @Override
    public List<JavaSrcBean> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        String projectName = domainInst.getProjectVersionName();
        GenJava javaGen = GenJava.getInstance(projectName);
        AggRoot javaRoot = new AggRoot(domainInst);
        List<JavaSrcBean> srcFiles = new ArrayList<>();
        if (allTemps == null) {
            allTemps = domainInst.getJavaTempIds();
        }
        for (String javaTempId : allTemps) {
            JavaTemp javatemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            if (javatemp != null && !javatemp.getRangeType().equals(RangeType.MODULEVIEW) && javatemp.getRangeType().equals(RangeType.MODULE)) {
                String packageName = domainInst.getPackageName();
                if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("")) {
                    packageName = packageName + "." + javatemp.getPackagePostfix();
                }
                String className = StringUtility.replace(javatemp.getNamePostfix(), "**", OODUtil.formatJavaName( domainInst.getEuPackage() , true));
                javaRoot.setPackageName(packageName);
                javaRoot.setClassName(className);
                File file = javaGen.createJava(javatemp, javaRoot, chrome);
                JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, domainInst, javaTempId);
                srcFiles.add(srcBean);
                BuildFactory.getInstance().createSource(srcBean.getClassName(), javaRoot, javatemp, srcBean);
                classList.add(srcBean.getClassName());

            }
        }
        return srcFiles;

    }
}
