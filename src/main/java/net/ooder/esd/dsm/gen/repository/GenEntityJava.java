package net.ooder.esd.dsm.gen.repository;

import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.gen.GenJavaTask;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.repository.EntityRoot;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.repository.config.EntityConfig;
import net.ooder.esd.dsm.repository.database.context.TableRoot;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.dsm.repository.database.ref.TableRef;
import net.ooder.esd.dsm.repository.entity.EntityRef;
import net.ooder.esd.dsm.temp.JavaTemp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class GenEntityJava extends GenJavaTask {

    public RepositoryInst repositoryInst;
    public EntityConfig entityConfig;
    public  Set<String> tempIds;
    public ChromeProxy chrome;
    public GenEntityJava(RepositoryInst repositoryInst, EntityConfig entityConfig, Set<String> tempIds,ChromeProxy chrome) {
        super();
        this.chrome=chrome;
        this.repositoryInst = repositoryInst;
        this.entityConfig = entityConfig;
        this.tempIds = tempIds;
    }

    @Override
    public List<JavaGenSource> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        List<JavaGenSource> genSources = new ArrayList<>();
        List<EntityRef> tableRefs = DSMFactory.getInstance().getRepositoryManager().getEntityRefByName(entityConfig.getRootClassName(), repositoryInst.getProjectVersionName());
        if (tempIds == null) {
            tempIds = repositoryInst.getJavaTempIds();
        }

        for (String javaTempId : tempIds) {
            JavaTemp javatemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            if (javatemp != null && javatemp.getRangeType().equals(RangeType.ENTITY) && javatemp.getDsmType().equals(DSMType.REPOSITORY)) {
                EntityRoot root = new EntityRoot(repositoryInst, entityConfig, tableRefs);
                String basePath = repositoryInst.getPackageName() + "." + entityConfig.getRootClassName().toLowerCase();
                root.setBasepath(basePath);
                String packageName = basePath;
                if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("")) {
                    packageName = packageName + "." + javatemp.getPackagePostfix();
                }
                String className = StringUtility.replace(javatemp.getNamePostfix(), "**", entityConfig.getRootClassName());
                root.setClassName(className);
                root.setPackageName(packageName);
                File file = GenJava.getInstance(repositoryInst.getProjectVersionName()).createJava(javatemp, root, chrome);
                JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, repositoryInst, javaTempId);

                JavaGenSource javaGenSource=   BuildFactory.getInstance().createSource(srcBean.getClassName(), root, javatemp, srcBean);
                genSources.add(javaGenSource);
                classList.add(srcBean.getClassName());
            }
        }
        return genSources;
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome==null){
            chrome=new LogSetpLog();
        }
        return chrome;
    }
}
