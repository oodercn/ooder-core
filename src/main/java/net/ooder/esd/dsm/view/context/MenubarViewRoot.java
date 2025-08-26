package net.ooder.esd.dsm.view.context;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.util.StringUtility;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.annotation.GridAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.TabsAnnotation;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.bean.view.NavMenuBarViewBean;
import net.ooder.esd.dsm.DSMInst;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.aggregation.context.MethodRoot;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.web.util.MethodUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class MenubarViewRoot implements JavaRoot {

    private final String moduleName;
    public String packageName;

    public String className;

    public String cnName;

    public DSMInst dsmBean;

    public String space;

    public String baseUrl;

    public NavMenuBarViewBean viewBean;

    public Set<String> imports = new LinkedHashSet<>();

    private List<FieldFormConfig> allFields = new ArrayList<>();

    private List<TreeListItem> items = new ArrayList<>();

    private List<MethodRoot> methodRoots = new ArrayList<>();


    @JSONField(serialize = false)
    List<CustomModuleBean> moduleBeans = new ArrayList<>();

    private static final Class[] customClass = new Class[]{
            ResultModel.class,
            ListResultModel.class,
            TreeListResultModel.class,
            TabsAnnotation.class,
            GridAnnotation.class,
            MethodChinaName.class,
            Controller.class,
            ModuleAnnotation.class,
            ToolBarMenu.class,
            ResponseBody.class};


    public MenubarViewRoot(AggViewRoot viewRoot, NavMenuBarViewBean viewBean, String moduleName, String className) {

        this.dsmBean = viewRoot.getDsmBean();
        this.moduleName = moduleName;
        if (className.indexOf(".") == -1) {
            className = dsmBean.getRootPackage().getPackageName() + "." + className;
        }
        this.className = className.substring(className.lastIndexOf(".") + 1);
        this.packageName = className.substring(0, className.lastIndexOf("."));
        this.baseUrl = StringUtility.replace(packageName, ".", "/");
        this.cnName = dsmBean.getDesc();
        this.space = dsmBean.getSpace();
        this.space = dsmBean.getSpace();
        this.viewBean = viewBean;
        this.allFields = viewBean.getAllFields();
        this.items = viewBean.getTabItems();
        List<CustomModuleBean> moduleBeans = viewBean.getModuleBeans();

        for (CustomModuleBean customModuleBean : moduleBeans) {
            MethodRoot methodRoot = new MethodRoot(customModuleBean);
            methodRoots.add(methodRoot);
            List<CustomBean> customBeans = methodRoot.getAnnotationBeans();
            for (CustomBean customBean : customBeans) {
                imports.add(customBean.getClass().getName());
                try {
                    imports = MethodUtil.getAllImports(customBean.getClass(), imports);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            imports.add(customModuleBean.getModuleComponent().getClassName());
        }

        try {
            for (Class clazz : customClass) {
                imports.add(clazz.getName());
                if (clazz.isAnnotation()) {
                    imports = MethodUtil.getAllImports(clazz, imports);
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

    public String getModuleName() {
        return moduleName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Set<String> getImports() {
        return imports;
    }

    public List<MethodRoot> getMethodRoots() {
        return methodRoots;
    }

    public void setMethodRoots(List<MethodRoot> methodRoots) {
        this.methodRoots = methodRoots;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }

    public NavMenuBarViewBean getViewBean() {
        return viewBean;
    }

    public void setViewBean(NavMenuBarViewBean viewBean) {
        this.viewBean = viewBean;
    }

    public List<CustomModuleBean> getModuleBeans() {
        return moduleBeans;
    }

    public void setModuleBeans(List<CustomModuleBean> moduleBeans) {
        this.moduleBeans = moduleBeans;
    }

    public List<FieldFormConfig> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<FieldFormConfig> allFields) {
        this.allFields = allFields;
    }

    public List<TreeListItem> getItems() {
        return items;
    }

    public void setItems(List<TreeListItem> items) {
        this.items = items;
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

    public DSMInst getDsmBean() {
        return dsmBean;
    }

    public void setDsmBean(DSMInst dsmBean) {
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
