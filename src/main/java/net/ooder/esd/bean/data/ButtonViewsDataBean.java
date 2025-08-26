package net.ooder.esd.bean.data;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.annotation.ButtonViewsAnnotation;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.annotation.view.ButtonViewsViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.component.nav.FullNavButtonViewsComponent;
import net.ooder.web.util.AnnotationUtil;

@CustomClass(
        clazz = FullNavButtonViewsComponent.class,
        viewType = CustomViewType.LISTMODULE,
        moduleType = ModuleViewType.NAVBUTTONVIEWSCONFIG
)
@AnnotationType(clazz = ButtonViewsViewAnnotation.class)
public class ButtonViewsDataBean extends CustomDataBean {

    ModuleViewType moduleViewType = ModuleViewType.NAVBUTTONVIEWSCONFIG;
    ResponsePathTypeEnum itemType = ResponsePathTypeEnum.TABS;

    String saveUrl;

    String dataUrl;

    String reSetUrl;

    Boolean cache;


    public ButtonViewsDataBean() {

    }

    public ButtonViewsDataBean(MethodConfig methodConfig) {
        super(methodConfig);
        ESDClass esdClass = methodConfig.getViewClass();

        if (esdClass != null) {
            ButtonViewsViewAnnotation annotation = AnnotationUtil.getClassAnnotation(esdClass.getCtClass(), ButtonViewsViewAnnotation.class);
            if (annotation == null) {
                AnnotationUtil.fillDefaultValue(ButtonViewsViewAnnotation.class, this);
            } else {
                AnnotationUtil.fillBean(annotation, this);
            }
            if (annotation != null) {
                if (saveUrl == null || saveUrl.equals("")) {
                    saveUrl = annotation.saveUrl();
                }
                if (dataUrl == null || dataUrl.equals("")) {
                    dataUrl = annotation.dataUrl();
                }
                if (reSetUrl == null || reSetUrl.equals("")) {
                    reSetUrl = annotation.reSetUrl();
                }
            }

        }


    }

    @JSONField(serialize = false)
    public String getReSetUrl() {
        return reSetUrl;
    }

    @JSONField(serialize = false)
    public String getSaveUrl() {
        return saveUrl;
    }

    @JSONField(serialize = false)
    public String getDataUrl() {

        return dataUrl;
    }


    public void setReSetUrl(String reSetUrl) {
        this.reSetUrl = reSetUrl;
    }

    public void setCache(Boolean cache) {
        this.cache = cache;
    }


    @Override
    public Boolean getCache() {
        return cache;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }


    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }


    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }


    public ResponsePathTypeEnum getItemType() {
        return itemType;
    }

    public void setItemType(ResponsePathTypeEnum itemType) {
        this.itemType = itemType;
    }


    public ButtonViewsDataBean fillData(ButtonViewsAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
