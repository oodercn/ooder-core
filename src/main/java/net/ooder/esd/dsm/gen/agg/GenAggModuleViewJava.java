package net.ooder.esd.dsm.gen.agg;

import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.context.AggModuleViewRoot;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.gen.BaseAggCallabel;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.annotation.AggregationType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenAggModuleViewJava extends BaseAggCallabel {

    DomainInst domainInst;
    String className;
    List<CustomModuleBean> moduleBeans;
    ChromeProxy chrome;

    public GenAggModuleViewJava(DomainInst domainInst, List<CustomModuleBean> moduleBeans, String className, ChromeProxy chrome) {
        super();
        this.domainInst = domainInst;
        this.moduleBeans = moduleBeans;
        this.className = className;
        this.chrome = chrome;
    }

    @Override
    public List<JavaSrcBean> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        String projectName = domainInst.getProjectVersionName();
        GenJava javaGen = GenJava.getInstance(projectName);
        AggModuleViewRoot allRoot = new AggModuleViewRoot(domainInst, className, moduleBeans);
        List<JavaSrcBean> srcFiles = new ArrayList<>();
        String simpleClassName = className.substring(className.lastIndexOf(".") + 1);
        String packageName = className.substring(0, className.lastIndexOf("."));
        List<JavaTemp> javatemps = BuildFactory.getInstance().getTempManager().getAggregationTemps(AggregationType.VIEW);
        for (JavaTemp javatemp : javatemps) {
            if (javatemp != null && javatemp.getRangeType() != null && javatemp.getRangeType().equals(RangeType.MODULEVIEW)) {
                if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("") && !javatemp.getPackagePostfix().equals("..")) {
                    packageName = packageName + "." + javatemp.getPackagePostfix();
                }
                simpleClassName = StringUtility.replace(javatemp.getNamePostfix(), "**", simpleClassName);
                allRoot.setPackageName(packageName);
                allRoot.setClassName(simpleClassName);
                File file = javaGen.createJava(javatemp, allRoot, chrome);
                JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, domainInst, javatemp.getJavaTempId());
                try {
                    javaGen.dynCompile(srcBean.getClassName(), srcBean.getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                srcFiles.add(srcBean);
            }
        }
        return srcFiles;

    }
}
