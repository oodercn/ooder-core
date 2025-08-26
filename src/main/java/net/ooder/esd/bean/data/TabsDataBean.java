package net.ooder.esd.bean.data;

import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.annotation.view.TabsViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.custom.component.nav.FullNavTabsComponent;
import net.ooder.web.util.AnnotationUtil;
@CustomClass(clazz = FullNavTabsComponent.class,
        viewType = CustomViewType.NAV,
        moduleType = ModuleViewType.NAVTABSCONFIG
)
@AnnotationType(clazz = TabsViewAnnotation.class)
public class TabsDataBean extends CustomDataBean {

    ModuleViewType moduleViewType = ModuleViewType.NAVTABSCONFIG;

    ResponsePathTypeEnum itemType = ResponsePathTypeEnum.TABS;

    Boolean cache = false;

    Boolean autoSave = false;

    String dataUrl;

    String saveUrl;

    String reSetUrl;


    public TabsDataBean() {

    }


    public TabsDataBean fillData(TabsViewAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public TabsDataBean(MethodConfig methodConfig) {
        super(methodConfig);
        TabsViewAnnotation annotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), TabsViewAnnotation.class);
        if (annotation == null) {
            AnnotationUtil.fillDefaultValue(TabsViewAnnotation.class, this);
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
            autoSave = annotation.autoSave();
        }

    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    public ResponsePathTypeEnum getItemType() {
        return itemType;
    }

    public void setItemType(ResponsePathTypeEnum itemType) {
        this.itemType = itemType;
    }

    @Override
    public Boolean getCache() {
        return cache;
    }

    public void setCache(Boolean cache) {
        this.cache = cache;
    }

    public Boolean getAutoSave() {
        return autoSave;
    }

    public void setAutoSave(Boolean autoSave) {
        this.autoSave = autoSave;
    }

    @Override
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

    public String getReSetUrl() {

        return reSetUrl;
    }

    public void setReSetUrl(String reSetUrl) {
        this.reSetUrl = reSetUrl;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

}
