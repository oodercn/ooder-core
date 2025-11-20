package net.ooder.esd.dsm.gen.view;

import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.gen.GenJavaTask;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.ViewInst;
import net.ooder.esd.dsm.view.context.DicViewRoot;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.util.OODUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GenViewDicJava extends GenJavaTask {

    public ViewInst viewInst;
    public List<TabListItem> items;
    public String className;
    public String moduleName;
    public ChromeProxy chrome;

    public GenViewDicJava(DomainInst domainInst, String moduleName, List<TabListItem> items, String className, ChromeProxy chrome) {
        super();
        this.moduleName = moduleName;
        this.viewInst = domainInst.getViewInst();
        this.items = items;
        this.className = className;
        this.chrome = chrome;
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
                    genChildTree(viewInst, moduleName, (TreeListItem) listItem, className);
                }

            }
            DicViewRoot dicViewRoot = new DicViewRoot(viewInst, moduleName, items, className);
            GenJava javaGen = GenJava.getInstance(viewInst.getProjectVersionName());
            List<JavaTemp> viewTemps = BuildFactory.getInstance().getTempManager().getCustomViewTemps(ViewType.DIC);
            for (JavaTemp javatemp : viewTemps) {
                if (javatemp.getRangeType() != null && javatemp.getRangeType().equals(RangeType.MODULEVIEW)) {
                    String packageName = className.substring(0, className.lastIndexOf("."));
                    String simClass = className.substring(className.lastIndexOf(".") + 1);
                    if (javatemp.getPackagePostfix() != null && !javatemp.getPackagePostfix().equals("")) {
                        packageName = packageName + "." + javatemp.getPackagePostfix();
                    }

                    String className = StringUtility.replace(javatemp.getNamePostfix(), "**", simClass);
                    dicViewRoot.setClassName(className);
                    dicViewRoot.setPackageName(packageName);
                    File file = javaGen.createJava(javatemp, dicViewRoot, chrome);
                    JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, viewInst, javatemp.getJavaTempId());
                    javaSrcBeans.add(srcBean);
                    BuildFactory.getInstance().createSource(srcBean.getClassName(), dicViewRoot, javatemp, srcBean);
                    classList.add(srcBean.getClassName());
                }
            }
            for (JavaSrcBean srcBean : javaSrcBeans) {
                viewInst.updateView(srcBean);
            }
        }
        return javaSrcBeans;
    }

    private void genChildTree(ViewInst viewInst, String moduleName, TreeListItem item, String className) {
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
                } catch (ClassNotFoundException e) {
                    DSMFactory.getInstance().getViewManager().genDicJava(viewInst, treeListItems, moduleName, bindClassName, null);
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

    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome == null) {
            chrome = new LogSetpLog();
        }
        return chrome;
    }
}
