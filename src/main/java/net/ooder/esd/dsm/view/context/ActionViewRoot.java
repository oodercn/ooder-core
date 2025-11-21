package net.ooder.esd.dsm.view.context;

import net.ooder.annotation.*;
import net.ooder.common.util.StringUtility;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;

import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.event.CustomCallBack;
import net.ooder.esd.annotation.event.CustomEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.ui.UIType;
import net.ooder.esd.annotation.view.*;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.view.ViewInst;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.properties.fchart.Categories;
import net.ooder.esd.tool.properties.fchart.LineData;

import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.web.RequestMappingBean;
import net.ooder.web.RequestParamBean;
import net.ooder.web.util.MethodUtil;
import net.ooder.web.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class ActionViewRoot implements JavaRoot {

    public String packageName;

    public String className;

    public String cnName;

    public ViewInst dsmBean;

    public String moduleName;

    public String space;

    public MenuBarBean menuBarBean;

    public RequestMappingBean requestMapping;

    public Set<String> imports = new LinkedHashSet<>();

    private List<ActionRoot> actionRoots = new ArrayList<>();

    private static final Class[] customClass = new Class[]{
            TreeItem.class,
            IconEnumstype.class,
            PageUtil.class,
            EsbUtil.class,
            ResultModel.class,
            ListResultModel.class,
            TreeListResultModel.class,
            CustomMenuItem.class,
            CustomCallBack.class,
            UIType.class,
            View.class,
            LineData.class,
            Categories.class,
            CustomMenuType.class,
            ModuleViewType.class,
            Aggregation.class,
            LayoutViewAnnotation.class,
            APIEventAnnotation.class,
            FormAnnotation.class,
            GridAnnotation.class,
            TreeAnnotation.class,
            TabsViewAnnotation.class,
            FormViewAnnotation.class,
            TreeViewAnnotation.class,
            GalleryViewAnnotation.class,
            TabsAnnotation.class,
            GridViewAnnotation.class,
            ModuleAnnotation.class,
            MethodChinaName.class,
            Controller.class,
            SVGPaperViewAnnotation.class,
            ToolBarMenu.class,
            RequestMapping.class,
            ResponseBody.class
    };


    public ActionViewRoot(ViewInst dsmBean, List<TreeListItem> treeListItems, MenuBarBean menuBarBean, List<RequestParamBean> paramBeans, List<CustomEvent> events, String className) {

        int k = 0;
        for (TreeListItem treeListItem : treeListItems) {
            ActionRoot actionRoot = new ActionRoot(treeListItem, k, paramBeans, events);
            k++;
            actionRoots.add(actionRoot);
        }
        this.menuBarBean = menuBarBean;
        this.dsmBean = dsmBean;
        this.packageName = className.substring(0, className.lastIndexOf("."));
        this.className = className.substring(className.lastIndexOf(".") + 1);
        this.cnName = dsmBean.getDesc();
        this.space = dsmBean.getSpace();
        String url = StringUtility.replace(packageName, ".", "/");
        requestMapping = new RequestMappingBean(this.className.toLowerCase(), url);
        try {
            for (Class clazz : customClass) {
                imports.add(clazz.getPackage().getName() + ".*");
                if (clazz.isAnnotation()) {
                    imports = MethodUtil.getAllImports(clazz, imports);
                }
            }

            for (TreeListItem listItem : treeListItems) {
                for (Class clazz : listItem.getBindClass()) {
                    imports.add(clazz.getPackage().getName() + ".*");
                }

                if (listItem.getEuClassName() != null) {
                    imports.add(listItem.getEuClassName());
                }
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String basePackage = dsmBean.getPackageName();// + "." + dsmBean.getSpace().toLowerCase();
        for (Package pack : Package.getPackages()) {
            for (String packName : innerPacks) {
                if (pack.getName().startsWith(basePackage + "." + packName)) {
                    this.imports.add(pack.getName() + ".*");
                }
            }
        }
    }

    public List<CustomBean> getAnnotationBeans() {
        ArrayList annotationBeans = new ArrayList<>();
        annotationBeans.add(menuBarBean);
        annotationBeans.add(requestMapping);

        annotationBeans.add(new SimpleCustomBean(Aggregation.class));
        annotationBeans.add(new SimpleCustomBean(Controller.class));
        return annotationBeans;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<ActionRoot> getActionRoots() {
        return actionRoots;
    }

    public void setActionRoots(List<ActionRoot> actionRoots) {
        this.actionRoots = actionRoots;
    }

    public RequestMappingBean getRequestMapping() {

        return requestMapping;
    }

    public void setRequestMapping(RequestMappingBean requestMapping) {
        this.requestMapping = requestMapping;
    }

    public MenuBarBean getMenuBarBean() {
        return menuBarBean;
    }

    public void setMenuBarBean(MenuBarBean menuBarBean) {
        this.menuBarBean = menuBarBean;
    }

    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }

    public static Class[] getCustomClass() {
        return customClass;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public ViewInst getDsmBean() {
        return dsmBean;
    }

    public void setDsmBean(ViewInst dsmBean) {
        this.dsmBean = dsmBean;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCnName() {
        return cnName;
    }

    @Override
    public String getBasepath() {
        return packageName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }


}
