package net.ooder.esd.bean.data;

import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.view.DynLoadAnnotation;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.action.DYNAppendType;
import net.ooder.esd.annotation.event.MenuEventEnum;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.component.CustomDynLoadView;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.manager.plugins.api.APIFactory;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@CustomClass(clazz = CustomDynLoadView.class,
        viewType = CustomViewType.MODULE,
        moduleType = ModuleViewType.DYNCONFIG
)
@AnnotationType(clazz = DynLoadAnnotation.class)
public class CustomDynDataBean extends CustomDataBean {

    ModuleViewType moduleViewType = ModuleViewType.DYNCONFIG;

    String projectName;

    String refClassName;

    String saveUrl;

    String dataUrl;

    DYNAppendType append;

    Boolean cache;


    public CustomDynDataBean() {

    }

    public CustomDynDataBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        ESDClass esdClass = methodAPIBean.getViewClass();

        DynLoadAnnotation formAnnotation = AnnotationUtil.getMethodAnnotation(methodAPIBean.getMethod(), DynLoadAnnotation.class);
        if (formAnnotation == null) {
            AnnotationUtil.fillDefaultValue(DynLoadAnnotation.class, this);
        } else {
            AnnotationUtil.fillBean(formAnnotation, this);
        }


        if (formAnnotation != null) {
            if (dataUrl == null || dataUrl.equals("")) {
                dataUrl = formAnnotation.dataUrl();
            }
            if (saveUrl == null || saveUrl.equals("")) {
                saveUrl = formAnnotation.saveUrl();
            }
            if (refClassName == null || refClassName.equals("")) {
                refClassName = formAnnotation.refClassName();
            }
        } else {
            if (dataUrl == null || dataUrl.equals("")) {
                dataUrl = methodAPIBean.getUrl();
            }
        }
        try {
            if (esdClass != null) {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(esdClass.getCtClass().getName());
                List<CustomMethodInfo> esdMethods = new ArrayList<>();
                esdMethods.addAll(esdClass.getMethodsList());
                esdMethods.addAll(esdClass.getOtherMethodsList());
                for (CustomMethodInfo methodField : esdMethods) {
                    MethodConfig methodBean = apiClassConfig.getMethodByName(methodField.getInnerMethod().getName());
                    if (methodBean != null) {
                        APICallerComponent component = new APICallerComponent(methodAPIBean);
                        APICallerProperties properties = component.getProperties();
                        Set<CustomMenuItem> bindMenus = properties.getBindMenu();
                        if (bindMenus != null && bindMenus.size() > 0) {
                            for (CustomMenuItem bindMenu : bindMenus) {
                                if (bindMenu.getMenu() != null && bindMenu.getMenu().actions().length > 0) {
                                    Action customAction = new Action(bindMenu.getMenu().actions()[0], MenuEventEnum.onMenuSelected);
                                    component.setAlias(customAction.getTarget());
                                }
                                APIFactory.bindUrl(bindMenu.getMethodName(), this, methodBean.getRequestMethodBean());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public CustomDynDataBean(DynLoadAnnotation annotation) {
        fillData(annotation);
    }


    public CustomDynDataBean fillData(DynLoadAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getRefClassName() {
        return refClassName;
    }

    public void setRefClassName(String refClassName) {
        this.refClassName = refClassName;
    }

    public DYNAppendType getAppend() {
        return append;
    }

    public void setAppend(DYNAppendType append) {
        this.append = append;
    }


    @Override
    public Boolean getCache() {
        return cache;
    }

    public void setCache(Boolean cache) {
        this.cache = cache;
    }


    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
