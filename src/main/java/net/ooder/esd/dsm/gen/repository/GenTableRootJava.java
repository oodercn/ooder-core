package net.ooder.esd.dsm.gen.repository;

import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.gen.BaseAggCallabel;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.repository.RepositoryRoot;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.annotation.UserSpace;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class GenTableRootJava extends BaseAggCallabel {

    public RepositoryInst repositoryInst;
    public List<DSMTableProxy> tables;
    public Set<String> tempIds;
    public ChromeProxy chrome;
    public UserSpace userSpace;

    public GenTableRootJava(RepositoryInst repositoryInst, List<DSMTableProxy> tables, Set<String> tempIds, UserSpace userSpace, ChromeProxy chrome) {
        super();
        this.repositoryInst = repositoryInst;
        this.tables = tables;
        this.tempIds = tempIds;
        this.chrome = chrome;
        this.userSpace=userSpace;
    }

    @Override
    public List<JavaSrcBean> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        List<JavaSrcBean> srcFiles = new ArrayList<>();
        if (chrome == null) {
            chrome = this.getCurrChromeDriver();
        }
        RepositoryRoot allRoot = new RepositoryRoot(repositoryInst, tables,userSpace);
        if (tempIds == null) {
            tempIds = repositoryInst.getJavaTempIds();
        }

        for (String javaTempId : tempIds) {
            JavaTemp javatemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
            if (javatemp != null && javatemp.getRangeType().equals(RangeType.MODULE)) {
                String packageName = repositoryInst.getPackageName();
                if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("")) {
                    packageName = packageName + "." + javatemp.getPackagePostfix();
                }
                String className = StringUtility.replace(javatemp.getNamePostfix(), "**", StringUtility.formatJavaName(repositoryInst.getSpace(), true));
                allRoot.setPackageName(packageName);
                allRoot.setClassName(className);
                chrome.printLog("start create [" + className + "]", true);
                File file = GenJava.getInstance(repositoryInst.getProjectVersionName()).createJava(javatemp, allRoot, chrome);
                JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, repositoryInst, javaTempId);
                srcFiles.add(srcBean);
                BuildFactory.getInstance().createSource(srcBean.getClassName(), allRoot, javatemp, srcBean);
                classList.add(srcBean.getClassName());
            }


        }
        return srcFiles;
    }

    public RepositoryInst getRepositoryInst() {
        return repositoryInst;
    }

    public void setRepositoryInst(RepositoryInst repositoryInst) {
        this.repositoryInst = repositoryInst;
    }

    public List<DSMTableProxy> getTables() {
        return tables;
    }

    public void setTables(List<DSMTableProxy> tables) {
        this.tables = tables;
    }

    public Set<String> getTempIds() {
        return tempIds;
    }

    public void setTempIds(Set<String> tempIds) {
        this.tempIds = tempIds;
    }

    public ChromeProxy getChrome() {
        return chrome;
    }

    public void setChrome(ChromeProxy chrome) {
        this.chrome = chrome;
    }

    public UserSpace getUserSpace() {
        return userSpace;
    }

    public void setUserSpace(UserSpace userSpace) {
        this.userSpace = userSpace;
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome==null){
            chrome=new LogSetpLog();
        }
        return chrome;
    }
}
