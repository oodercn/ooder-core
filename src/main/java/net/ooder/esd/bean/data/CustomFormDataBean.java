package net.ooder.esd.bean.data;

import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.view.FormViewAnnotation;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.custom.component.form.FullClassFormComponent;
import net.ooder.web.util.AnnotationUtil;
@CustomClass(clazz = FullClassFormComponent.class,
        viewType = CustomViewType.MODULE,
        moduleType = ModuleViewType.FORMCONFIG
)
@AnnotationType(clazz = FormViewAnnotation.class)
public class CustomFormDataBean extends CustomDataBean {


    ModuleViewType moduleViewType = ModuleViewType.FORMCONFIG;

    String saveUrl;

    String searchUrl;

    String reSetUrl;

    String dataUrl;

    Boolean autoSave = false;

    Boolean cache = false;


    public CustomFormDataBean() {

    }


    public CustomFormDataBean(MethodConfig methodConfig) {
        super(methodConfig);
        FormViewAnnotation formAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), FormViewAnnotation.class);
        if (formAnnotation != null) {
            AnnotationUtil.fillBean(formAnnotation, this);
        }
    }

    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getReSetUrl() {
        return reSetUrl;
    }

    public void setReSetUrl(String reSetUrl) {
        this.reSetUrl = reSetUrl;
    }

    @Override
    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public Boolean getAutoSave() {
        return autoSave;
    }

    public void setAutoSave(Boolean autoSave) {
        this.autoSave = autoSave;
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
