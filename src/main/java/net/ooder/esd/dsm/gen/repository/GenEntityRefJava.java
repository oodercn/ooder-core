package net.ooder.esd.dsm.gen.repository;

import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.gen.BaseAggCallabel;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.repository.EntityRefRoot;
import net.ooder.esd.dsm.repository.EntityRoot;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.repository.config.EntityConfig;
import net.ooder.esd.dsm.repository.entity.EntityRef;
import net.ooder.esd.dsm.repository.entity.EntityRefProxy;
import net.ooder.esd.dsm.temp.JavaTemp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class GenEntityRefJava extends BaseAggCallabel {

    public RepositoryInst repositoryInst;
    public EntityConfig entityConfig;
    public Set<String> tempIds;
    public ChromeProxy chrome;

    public GenEntityRefJava(RepositoryInst repositoryInst, EntityConfig entityConfig, Set<String> tempIds, ChromeProxy chrome) {
        super();
        this.chrome = chrome;
        this.repositoryInst = repositoryInst;
        this.entityConfig = entityConfig;
        this.tempIds = tempIds;
    }

    @Override
    public List<JavaSrcBean> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        List<JavaSrcBean> srcFiles = new ArrayList<>();
        if (tempIds == null) {
            tempIds = repositoryInst.getJavaTempIds();
        }
        for (String javaTempId : tempIds) {
            JavaTemp javatemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            if (javatemp != null && javatemp.getRangeType().equals(RangeType.ENTITYREF)) {
                List<EntityRefProxy> proxyList = new ArrayList<>();
                List<EntityRef> refs = DSMFactory.getInstance().getRepositoryManager().getEntityRefByName(entityConfig.getESDClass().getClassName(), repositoryInst.getProjectVersionName(), javatemp.getRefType());
                for (EntityRef ref : refs) {
                    EntityRefProxy proxy = new EntityRefProxy(ref);
                    proxyList.add(proxy);
                }
                EntityRefRoot tempRoot = new EntityRefRoot(repositoryInst, entityConfig, proxyList, javatemp.getRefType());
                String basePath = repositoryInst.getPackageName();
                tempRoot.setBasepath(basePath);
                String packageName = basePath + "." + entityConfig.getRootClassName().toLowerCase();
                if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("")) {
                    packageName = packageName + "." + javatemp.getPackagePostfix();
                }
                String className = StringUtility.replace(javatemp.getNamePostfix(), "**", entityConfig.getRootClassName());
                tempRoot.setClassName(className);
                tempRoot.setPackageName(packageName);
                File file = GenJava.getInstance(repositoryInst.getProjectVersionName()).createJava(javatemp, tempRoot, chrome);
                JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, repositoryInst, javaTempId);
                srcFiles.add(srcBean);
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
