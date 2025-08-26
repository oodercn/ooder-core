package net.ooder.esd.dsm.gen.view;

import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.util.ClassUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.event.CustomEvent;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.gen.BaseAggCallabel;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.ViewInst;
import net.ooder.esd.dsm.view.context.ActionViewRoot;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.util.XUIUtil;
import net.ooder.web.RequestParamBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenActionJava extends BaseAggCallabel {

    public ViewInst viewInst;
    public List<TreeListItem> items;
    public String className;
    public String moduleName;
    public MenuBarBean menuBarBean;
    public List<RequestParamBean> paramBeans;
    public  List<CustomEvent> events;
    public ChromeProxy chrome;

    public GenActionJava(ViewInst viewInst, List<TreeListItem> items, MenuBarBean menuBarBean, List<RequestParamBean> paramBeans, List<CustomEvent> events, String className, ChromeProxy chrome) {
       super();
        this.menuBarBean = menuBarBean;
        this.paramBeans = paramBeans;
        this.events = events;
        this.viewInst = viewInst;
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
            for (TreeListItem listItem : items) {
                genChildAction(viewInst, listItem, menuBarBean, paramBeans, events, className);
            }
            ActionViewRoot actionViewRoot = new ActionViewRoot(viewInst, items, menuBarBean, paramBeans, events, className);
            GenJava javaGen = GenJava.getInstance(viewInst.getProjectVersionName());
            List<JavaTemp> viewTemps = BuildFactory.getInstance().getTempManager().getCustomViewTemps( ViewType.NAVMENUBAR);
            for (JavaTemp javatemp : viewTemps) {
                if (javatemp.getRangeType() != null && javatemp.getRangeType().equals(RangeType.MODULEVIEW)) {
                    File file = javaGen.createJava(javatemp, actionViewRoot, chrome);
                    JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(file, viewInst, javatemp.getJavaTempId());
                    try {
                        clazz = javaGen.dynCompile(srcBean.getClassName(), srcBean.getContent());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    javaSrcBeans.add(srcBean);
                }
            }

        }
        return javaSrcBeans;
    }



        private List<JavaSrcBean> genChildAction(ViewInst viewInst, TreeListItem item, MenuBarBean menuBarBean, List<RequestParamBean> paramBeans, List<CustomEvent> events, String className) {
        List<TreeListItem> treeListItems = item.getSub();
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
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
                bindClassName = packageName + "." + XUIUtil.formatJavaName(name + item.getId(), true);
            }
            try {
                try {
                    clazz = ClassUtility.loadClass(bindClassName);
                } catch (ClassNotFoundException e) {
                    javaSrcBeans = DSMFactory.getInstance().getViewManager().genAction(viewInst, treeListItems, menuBarBean, paramBeans, events, bindClassName, null);
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
                //item.setBindClassName(clazz.getName());
            }
            item.setBindClass(bindClass.toArray(new Class[]{}));
        }

        return javaSrcBeans;

    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome==null){
            chrome=new LogSetpLog();
        }
        return chrome;
    }
}
