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
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.gen.GenJavaTask;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.ViewInst;
import net.ooder.esd.dsm.view.context.DicViewRoot;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.util.OODUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenViewDicJava extends GenJavaTask {

    public ViewInst viewInst;
    public List<TabListItem> items;
    public String className;
    public String moduleName;
    public Class dicClass;
    public ChromeProxy chrome;
    DicViewRoot dicViewRoot;

    public GenViewDicJava(ViewInst viewInst, String moduleName, List<TabListItem> items, String className, ChromeProxy chrome) {
        super();
        this.moduleName = moduleName;
        this.viewInst = viewInst;
        this.items = items;
        this.className = className;
        this.chrome = chrome;
        dicViewRoot = new DicViewRoot(viewInst, moduleName, items, className);
        try {
            viewTemps = BuildFactory.getInstance().getTempManager().getCustomModuleViewTemps(ViewType.DIC);
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<JavaSrcBean> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        Class clazz = null;
        try {
            clazz = ClassUtility.loadClass(className);
        } catch (ClassNotFoundException e) {
        }
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        if (clazz == null) {
            for (TabListItem listItem : items) {
                if (listItem instanceof TreeListItem) {
                    GenViewDicJava task = genChildTree(viewInst, moduleName, (TreeListItem) listItem, className);
                    if (task != null && task.getJavaSrcBeanList() != null) {
                        javaSrcBeans.addAll(task.getJavaSrcBeanList());
                        classList.addAll(task.getClassList());
                    }
                }
            }
            GenJava javaGen = GenJava.getInstance(viewInst.getProjectVersionName());
            for (JavaTemp javatemp : viewTemps) {
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
                try {
                    dicClass = javaGen.dynCompile(srcBean.getClassName(), srcBean.getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                javaSrcBeans.add(srcBean);
                BuildFactory.getInstance().createSource(srcBean.getClassName(), dicViewRoot, javatemp, srcBean);
                classList.add(srcBean.getClassName());
            }
            for (JavaSrcBean srcBean : javaSrcBeans) {
                viewInst.updateView(srcBean);
            }
        }
        return javaSrcBeans;
    }


    private GenViewDicJava genChildTree(ViewInst viewInst, String moduleName, TreeListItem item, String className) {

        GenViewDicJava task = null;
        List<TabListItem> treeListItems = item.getSub();
        if (treeListItems != null && treeListItems.size() > 0) {
            String packageName = className.substring(0, className.lastIndexOf("."));
            String name = className.substring(className.lastIndexOf(".") + 1);
            List<Class> bindClass = new ArrayList<>();
            Class clazz = null;
            Class[] clazzList = item.getBindClass();
            String bindClassName = null;
            if (clazzList != null && clazzList.length > 0) {
                bindClassName = clazzList[0].getName();
            }
            if (bindClassName == null) {
                bindClassName = packageName + "." + OODUtil.formatJavaName(name + item.getId(), true);
            }
            try {
                try {
                    clazz = ClassUtility.loadClass(bindClassName);
                } catch (Throwable e) {
                    task = DSMFactory.getInstance().getViewManager().genDicJava(viewInst, treeListItems, moduleName, bindClassName, null);
                }
                try {
                    clazz = ClassUtility.loadClass(bindClassName);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }

            if (clazz != null) {
                bindClass.add(clazz);
            }
            item.setBindClass(bindClass.toArray(new Class[]{}));
        }

        return task;

    }


    public ViewInst getViewInst() {
        return viewInst;
    }

    public void setViewInst(ViewInst viewInst) {
        this.viewInst = viewInst;
    }

    public List<TabListItem> getItems() {
        return items;
    }

    public void setItems(List<TabListItem> items) {
        this.items = items;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Class getDicClass() {
        return dicClass;
    }

    public void setDicClass(Class dicClass) {
        this.dicClass = dicClass;
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
