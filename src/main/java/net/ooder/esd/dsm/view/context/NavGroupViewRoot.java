package net.ooder.esd.dsm.view.context;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.annotation.GridAnnotation;
import net.ooder.esd.annotation.GroupItemAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.bean.view.NavGroupViewBean;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.aggregation.context.MethodRoot;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.web.util.MethodUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class NavGroupViewRoot extends BaseViewRoot<NavGroupViewBean> {

    private List<FieldFormConfig> allFields = new ArrayList<>();

    private List<FieldModuleConfig> items = new ArrayList<>();

    private List<MethodRoot> methodRoots = new ArrayList<>();


    @JSONField(serialize = false)
    List<CustomModuleBean> moduleBeans = new ArrayList<>();

    private static final Class[] customClass = new Class[]{
            ResultModel.class,
            ListResultModel.class,
            TreeListResultModel.class,
            GroupItemAnnotation.class,
            GridAnnotation.class,
            MethodChinaName.class,
            Controller.class,
            ModuleAnnotation.class,
            ToolBarMenu.class,
            ResponseBody.class};


    public NavGroupViewRoot(AggViewRoot viewRoot, NavGroupViewBean viewBean, String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);
        this.allFields = viewBean.getAllFields();
        this.items = viewBean.getNavItems();
        this.moduleBeans = viewBean.getModuleBeans();
        this.methodRoots = initMethodRoots(moduleBeans);
        this.imports.addAll(getMethodsImpls(methodRoots));

    }


    protected List<MethodRoot> initMethodRoots(List<CustomModuleBean> moduleBeans) {
        List<MethodRoot> methodRoots = new ArrayList<>();
        for (CustomModuleBean customModuleBean : moduleBeans) {
            MethodRoot methodRoot = new MethodRoot(customModuleBean);
            methodRoots.add(methodRoot);
        }
        return methodRoots;
    }

    protected Set<String> getMethodsImpls(List<MethodRoot> methodRoots) {
        Set<String> imports = new HashSet<>();
        for (MethodRoot methodRoot : methodRoots) {
            List<CustomBean> customBeans = methodRoot.getAnnotationBeans();
            for (CustomBean customBean : customBeans) {
                imports.add(customBean.getClass().getName());
                try {
                    imports = MethodUtil.getAllImports(customBean.getClass(), imports);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            imports.add(methodRoot.getModuleBean().getEuClassName());
        }
        return imports;
    }

    public List<FieldFormConfig> getAllFields() {
        return allFields;
    }

    public void setAllFields(List<FieldFormConfig> allFields) {
        this.allFields = allFields;
    }

    public List<FieldModuleConfig> getItems() {
        return items;
    }

    public void setItems(List<FieldModuleConfig> items) {
        this.items = items;
    }

    public List<MethodRoot> getMethodRoots() {
        return methodRoots;
    }

    public void setMethodRoots(List<MethodRoot> methodRoots) {
        this.methodRoots = methodRoots;
    }

    public List<CustomModuleBean> getModuleBeans() {
        return moduleBeans;
    }

    public void setModuleBeans(List<CustomModuleBean> moduleBeans) {
        this.moduleBeans = moduleBeans;
    }

    @Override
    public Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.NAVGROUP;
    }
}