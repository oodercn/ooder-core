package net.ooder.esd.dsm.gen.view;

import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.gen.GenJavaTask;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.ViewInst;
import net.ooder.esd.dsm.view.context.DicViewRoot;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.util.OODUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenViewDicJava extends GenJavaTask {

    public ViewInst viewInst;
    public String moduleName;
    public Class dicClass;
    public ChromeProxy chrome;
    DicViewRoot dicViewRoot;

    public GenViewDicJava(ViewInst viewInst, String moduleName, List<TabListItem> items, String className, ChromeProxy chrome) {
        super();
        this.moduleName = moduleName;
        this.viewInst = viewInst;
        this.chrome = chrome;
        dicViewRoot = new DicViewRoot(viewInst, moduleName, items, className);
        try {
            viewTemps = BuildFactory.getInstance().getTempManager().getCustomModuleViewTemps(ViewType.DIC);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<JavaGenSource> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        return genDic(dicViewRoot);
    }

    List<JavaGenSource> genDic(DicViewRoot dicViewRoot) {
        List<TabListItem> items = dicViewRoot.getItems();
        String className = dicViewRoot.getFullClassName();
        Class clazz = null;
        try {
            clazz = ClassUtility.loadClass(className);
        } catch (Throwable e) {
            log.warn(e.getMessage());
        }
        List<JavaGenSource> javaGenSources = new ArrayList<>();
        if (clazz == null) {
            for (TabListItem listItem : items) {
                if (listItem instanceof TreeListItem) {
                    List<JavaGenSource> srcBeanList = genChildTree((TreeListItem) listItem, className);
                    if (srcBeanList != null) {
                        for (JavaGenSource srcBean : srcBeanList) {
                            if (srcBean != null && !javaGenSources.contains(srcBean)) {
                                javaGenSources.addAll(srcBeanList);
                                classList.add(srcBean.getClassName());
                            }
                        }
                    }
                }
            }
            GenJava javaGen = GenJava.getInstance(viewInst.getProjectVersionName());
            for (JavaTemp javatemp : viewTemps) {
                try {
                    String packageName = className.substring(0, className.lastIndexOf("."));
                    String simClass = className.substring(className.lastIndexOf(".") + 1);
                    if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("..") && !javatemp.getPackagePostfix().equals("")) {
                        packageName = packageName + "." + javatemp.getPackagePostfix();
                    }
                    className = StringUtility.replace(javatemp.getNamePostfix(), "**", simClass);
                    dicViewRoot.setClassName(className);
                    dicViewRoot.setPackageName(packageName);
                    File file = javaGen.createJava(javatemp, dicViewRoot, chrome);
                    JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, viewInst, javatemp.getJavaTempId());
                    dicClass = javaGen.dynCompile(srcBean.getClassName(), srcBean.getContent());
                    JavaGenSource javaGenSource = BuildFactory.getInstance().createSource(srcBean.getClassName(), dicViewRoot, javatemp, srcBean);
                    javaGenSources.add(javaGenSource);
                    classList.add(srcBean.getClassName());
                } catch (Exception e) {
                    log.warn(e.getMessage());
                }
            }

            for (JavaGenSource srcBean : javaGenSources) {
                viewInst.updateView(srcBean.getSrcBean());
            }
        }

        return javaGenSources;
    }


    public Class getDicClass() {
        return dicClass;
    }

    public void setDicClass(Class dicClass) {
        this.dicClass = dicClass;
    }

    List<JavaGenSource> genChildTree(TreeListItem item, String className) {
        List<JavaGenSource> srcBeanList = new ArrayList<>();
        List<TabListItem> treeListItems = item.getSub();
        Set<Class> bindClass = new HashSet<>();
        if (treeListItems != null && treeListItems.size() > 0) {
            String packageName = className.substring(0, className.lastIndexOf("."));
            String name = className.substring(className.lastIndexOf(".") + 1);
            Class[] clazzList = item.getBindClass();
            String bindClassName = null;
            if (clazzList != null && clazzList.length > 0) {
                bindClassName = clazzList[0].getName();
            }
            if (bindClassName == null) {
                bindClassName = packageName + "." + OODUtil.formatJavaName(name + item.getId(), true);
            }

            DicViewRoot childRoot = new DicViewRoot(viewInst, moduleName, treeListItems, bindClassName);
            srcBeanList = genDic(childRoot);
            for (JavaGenSource srcBean : srcBeanList) {
                Class clazz = srcBean.getSrcBean().getClass();
                if (clazz != null) {
                    bindClass.add(clazz);
                }
            }

            item.setBindClass(bindClass.toArray(new Class[]{}));
        }

        return srcBeanList;

    }


    public ViewInst getViewInst() {
        return viewInst;
    }

    public void setViewInst(ViewInst viewInst) {
        this.viewInst = viewInst;
    }


    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }


    @Override
    public ChromeProxy getChrome() {
        return chrome;
    }

    @Override
    public void setChrome(ChromeProxy chrome) {
        this.chrome = chrome;
    }

    public DicViewRoot getDicViewRoot() {
        return dicViewRoot;
    }

    public void setDicViewRoot(DicViewRoot dicViewRoot) {
        this.dicViewRoot = dicViewRoot;
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome == null) {
            chrome = new LogSetpLog();
        }
        return chrome;
    }
}
