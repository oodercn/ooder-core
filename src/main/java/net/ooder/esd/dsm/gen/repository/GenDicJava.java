package net.ooder.esd.dsm.gen.repository;

import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.gen.BaseAggCallabel;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.context.DicViewRoot;
import net.ooder.esd.tool.properties.item.TabListItem;

import net.ooder.jds.core.esb.EsbUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GenDicJava<T extends TabListItem> extends BaseAggCallabel {

    RepositoryInst repositoryInst;
    List<T> subItems;
    String moduleName;
    String simClassName;
    T treeItem;
    public ChromeProxy chrome;

    public GenDicJava(RepositoryInst repositoryInst, List<T> subItems, String moduleName, String simClassName) {
        super();
        this.subItems = subItems;
        this.init(repositoryInst, moduleName, simClassName);
    }


    void init(RepositoryInst repositoryInst, String moduleName, String simClassName) {
        this.repositoryInst = repositoryInst;
        String euPackageName = repositoryInst.getPackageName();
        if (moduleName.equals(euPackageName)) {
            moduleName = "";
        } else if (moduleName.startsWith(euPackageName + ".")) {
            moduleName = moduleName.substring((euPackageName + ".").length());
        }
        this.moduleName = moduleName;
        this.simClassName = simClassName;
    }


    @Override
    public List<JavaSrcBean> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        GenJava javaGen = GenJava.getInstance(repositoryInst.getProjectVersionName());
        if (chrome == null) {
            chrome = this.getCurrChromeDriver();
        }
        List<JavaSrcBean> srcFiles = new ArrayList<>();
        List<JavaTemp> viewTemps = BuildFactory.getInstance().getTempManager().getDSMTypeTemps(DSMType.REPOSITORY);
        if (simClassName.indexOf(".") > 0) {
            simClassName = simClassName.substring(simClassName.lastIndexOf(".") + 1);
        }
        for (JavaTemp javatemp : viewTemps) {
            if (javatemp.getRangeType() != null && javatemp.getRangeType().equals(RangeType.MODULEVIEW) && javatemp.getViewType() != null && javatemp.getViewType().equals(ViewType.DIC)) {
                boolean canGen = true;
                boolean canReGen = false;
                String genClassName = StringUtility.replace(javatemp.getNamePostfix(), "**", simClassName);
                DicViewRoot javaRoot = new DicViewRoot(repositoryInst, moduleName, subItems, genClassName);
                String expression = javatemp.getExpression();
                if (expression != null && !expression.equals("")) {
                    canGen = EsbUtil.parExpression(javatemp.getExpression(), JDSActionContext.getActionContext().getContext(), javaRoot, Boolean.class);
                }
                if (canGen) {
                    String packageName = repositoryInst.getPackageName() + "." + moduleName.toLowerCase();
                    if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("") && !javatemp.getPackagePostfix().equals("..")) {
                        packageName = packageName + "." + javatemp.getPackagePostfix();
                    }
                    javaRoot.setPackageName(packageName);
                    String realClassName = packageName + "." + genClassName;
                    Class clazz = null;
                    try {
                        clazz = ClassUtility.loadClass(realClassName);
                    } catch (ClassNotFoundException e) {

                    }
                    if (clazz == null || canReGen) {
                        File file = javaGen.createJava(javatemp, javaRoot, chrome);
                        JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, repositoryInst, javatemp.getJavaTempId());
                        srcFiles.add(srcBean);
                        if (treeItem != null) {
                            treeItem.setBindClass(new Class[]{srcBean.loadClass()});
                        }
                        repositoryInst.addJavaBean(srcBean);
                    } else {
                        JavaSrcBean srcBean = repositoryInst.getJavaSrcByClassName(realClassName);
                        if (srcBean != null && srcBean.getJavaTempId() == null) {
                            srcBean.setJavaTempId(javatemp.getJavaTempId());
                        }
                        srcFiles.add(srcBean);
                    }
                }
            }
        }

        return srcFiles;
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome == null) {
            chrome = new LogSetpLog();
        }
        return chrome;
    }

}
