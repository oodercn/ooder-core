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
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.repository.RepositoryRoot;
import net.ooder.esd.dsm.repository.database.context.TableRefRoot;
import net.ooder.esd.dsm.repository.database.context.TableRoot;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.dsm.repository.database.ref.TableRef;
import net.ooder.esd.dsm.repository.database.ref.TableRefProxy;
import net.ooder.esd.dsm.temp.JavaTemp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class GenTableRefJava extends BaseAggCallabel {

    public RepositoryInst repositoryInst;
    public TableRef ref;
    public Set<String> tempIds;
    public ChromeProxy chrome;

    public GenTableRefJava(RepositoryInst repositoryInst, TableRef ref, Set<String> tempIds, ChromeProxy chrome) {
        super();
        this.chrome = chrome;
        this.repositoryInst = repositoryInst;
        this.ref = ref;
        this.tempIds = tempIds;
    }

    @Override
    public List<JavaSrcBean> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        List<JavaSrcBean> srcFiles = new ArrayList<>();
        TableRefProxy proxy = new TableRefProxy(ref);
        TableRefRoot tempRoot = new TableRefRoot(repositoryInst, new TableRefProxy(ref));
        if (tempIds == null) {
            tempIds = repositoryInst.getJavaTempIds();
        }

        for (String javaTempId : tempIds) {
            JavaTemp javatemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            if (javatemp != null && javatemp.getRangeType().equals(RangeType.REF) && javatemp.getRefType() != null && javatemp.getRefType().equals(ref.getRef())
                    && (javatemp.getDsmType() == null || javatemp.getDsmType().equals(DSMType.REPOSITORY))) {
                String basePath = repositoryInst.getPackageName();
                tempRoot.setBasepath(basePath);
                String packageName = basePath + "." + proxy.getMainTable().getFieldName().toLowerCase();
                if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("")) {
                    packageName = packageName + "." + javatemp.getPackagePostfix();
                }
                String className = StringUtility.replace(javatemp.getNamePostfix(), "**", proxy.getMainTable().getClassName());
                tempRoot.setClassName(className);
                tempRoot.setPackageName(packageName);
                File file = GenJava.getInstance(repositoryInst.getProjectVersionName()).createJava(javatemp, tempRoot, chrome);
                JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, repositoryInst, javaTempId);
                srcFiles.add(srcBean);
                BuildFactory.getInstance().createSource(srcBean.getClassName(), tempRoot, javatemp, srcBean);
                classList.add(srcBean.getClassName());
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
