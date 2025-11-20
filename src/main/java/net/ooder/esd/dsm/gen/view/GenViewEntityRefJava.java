package net.ooder.esd.dsm.gen.view;

import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.gen.BaseAggCallabel;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.repository.EntityRefRoot;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.repository.config.EntityConfig;
import net.ooder.esd.dsm.repository.entity.EntityRef;
import net.ooder.esd.dsm.repository.entity.EntityRefProxy;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.ViewInst;
import net.ooder.esd.dsm.view.context.ViewEntityRefRoot;
import net.ooder.esd.dsm.view.context.ViewEntityRoot;
import net.ooder.esd.dsm.view.ref.ViewEntityRef;
import net.ooder.esd.dsm.view.ref.ViewEntityRefProxy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class GenViewEntityRefJava extends BaseAggCallabel {

    public DomainInst domainInst;
    public MethodConfig methodConfig;
    public Set<String> tempIds;
    public ChromeProxy chrome;

    public GenViewEntityRefJava(DomainInst domainInst, MethodConfig methodConfig, ChromeProxy chrome) {
        super();
        this.chrome = chrome;
        this.domainInst = domainInst;
        this.methodConfig = methodConfig;
        this.tempIds = tempIds;
    }

    @Override
    public List<JavaSrcBean> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        List<JavaSrcBean> srcFiles = new ArrayList<>();
        if (methodConfig.getViewClass() != null) {
            List<ViewEntityRef> dsmRefs = DSMFactory.getInstance().getViewManager().getViewEntityRefByName(methodConfig.getViewClass().getClassName(), domainInst.getDomainId());
            ViewEntityRoot root = new ViewEntityRoot(domainInst, methodConfig, dsmRefs);
            List<JavaTemp> viewTemps = BuildFactory.getInstance().getTempManager().getRangeTemps(RangeType.REF);
            for (JavaTemp javatemp : viewTemps) {
                ESDClass entityClass = methodConfig.getTopSourceClass().getEntityClass();

                List<ViewEntityRef> refs = DSMFactory.getInstance().getViewManager().getViewEntityRefByName(entityClass.getClassName(),  domainInst.getDomainId(), javatemp.getRefType());
                for (ViewEntityRef ref : refs) {
                    ViewEntityRefProxy proxy = new ViewEntityRefProxy(ref);
                    ViewEntityRefRoot tempRoot = new ViewEntityRefRoot(domainInst, methodConfig, root, javatemp, proxy);
                    String packageName = tempRoot.getBasePackage();
                    if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("")) {
                        packageName = packageName + "." + javatemp.getPackagePostfix();
                    }
                    String className = StringUtility.replace(javatemp.getNamePostfix(), "**", methodConfig.getViewClass().getName());
                    if (methodConfig.isModule()) {
                        className = methodConfig.getJavaSimpleName();
                    }

                    tempRoot.getImports().remove(packageName + ".*");
                    tempRoot.setClassName(className);
                    tempRoot.setPackageName(packageName);
                    File file = GenJava.getInstance( domainInst.getProjectVersionName()).createJava(javatemp, tempRoot, chrome);
                    JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, domainInst, javatemp.getJavaTempId());
                    srcBean.setSourceClassName(methodConfig.getSourceClassName());
                    srcBean.setMethodName(methodConfig.getMethodName());
                    srcFiles.add(srcBean);
                    BuildFactory.getInstance().createSource(srcBean.getClassName(), tempRoot, javatemp, srcBean);
                    classList.add(srcBean.getClassName());
                }
            }

        }

        return srcFiles;
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome==null){
            chrome=new LogSetpLog();
        }
        return chrome;
    }
}
