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
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.repository.database.context.TableRoot;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.dsm.repository.database.ref.TableRef;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.annotation.UserSpace;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GenTableJava extends GenJavaTask {

    public RepositoryInst repositoryInst;
    public DSMTableProxy tableProxy;
    public Set<String> tempIds;
    public ChromeProxy chrome;
    public UserSpace userSpace;

    public GenTableJava(RepositoryInst repositoryInst, DSMTableProxy tableProxy, Set<String> tempIds, UserSpace userSpace, ChromeProxy chrome) {
        super();
        this.repositoryInst = repositoryInst;
        this.tableProxy = tableProxy;
        this.tempIds = tempIds;
        this.chrome = chrome;
        this.userSpace = userSpace;
    }

    @Override
    public List<JavaGenSource> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        List<JavaGenSource> genSources = new ArrayList<>();
        if (chrome == null) {
            chrome = this.getCurrChromeDriver();
        }
        List<TableRef> tableRefs = DSMFactory.getInstance().getRepositoryManager().getTableRefByName(tableProxy.getTableName(), repositoryInst.getProjectVersionName());
        if (tempIds == null) {
            tempIds = repositoryInst.getJavaTempIds();
        }
        GenJava genJava = GenJava.getInstance(repositoryInst.getProjectVersionName());
        for (String javaTempId : tempIds) {
            JavaTemp javatemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            if (javatemp != null && javatemp.getRangeType().equals(RangeType.TABLE) && javatemp.getDsmType().equals(DSMType.REPOSITORY)) {
                TableRoot root = new TableRoot(repositoryInst, tableProxy, tableRefs, userSpace);
                String basePath = repositoryInst.getPackageName() + "." + tableProxy.getFieldName().toLowerCase();
                root.setBasepath(basePath);
                String packageName = basePath;
                if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("")) {
                    packageName = packageName + "." + javatemp.getPackagePostfix();
                }
                String className = StringUtility.replace(javatemp.getNamePostfix(), "**", tableProxy.getClassName());
                root.setClassName(className);
                root.setPackageName(packageName);
                File file = GenJava.getInstance(repositoryInst.getProjectVersionName()).createJava(javatemp, root, chrome);
                JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, repositoryInst, javaTempId);

                JavaGenSource javaGenSource=    BuildFactory.getInstance().createSource(srcBean.getClassName(), root, javatemp, srcBean);
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
