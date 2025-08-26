package net.ooder.esd.bean.data;

import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.view.DivViewAnnotation;
import net.ooder.esd.annotation.view.FormViewAnnotation;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.custom.component.FullDivComponent;
import net.ooder.web.util.AnnotationUtil;
@CustomClass(
        clazz = FullDivComponent.class,
        viewType = CustomViewType.MODULE,
        moduleType = ModuleViewType.DIVCONFIG
)
@AnnotationType(clazz = DivViewAnnotation.class)
public class CustomDivDataBean extends CustomDataBean {


    ModuleViewType moduleViewType = ModuleViewType.DIVCONFIG;

    String saveUrl;

    String searchUrl;

    String reSetUrl;

    String dataUrl;

    Boolean autoSave = false;

    Boolean cache = false;


    public CustomDivDataBean() {

    }


    public CustomDivDataBean(MethodConfig methodConfig) {
        super(methodConfig);
        FormViewAnnotation formAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), FormViewAnnotation.class);
        if (formAnnotation != null) {
            if (dataUrl == null || dataUrl.equals("")) {
                dataUrl = formAnnotation.dataUrl();
            }

            if (saveUrl == null || saveUrl.equals("")) {
                saveUrl = formAnnotation.saveUrl();
            }

            if (searchUrl == null || searchUrl.equals("")) {
                searchUrl = formAnnotation.searchUrl();
            }

            if (reSetUrl == null || reSetUrl.equals("")) {
                reSetUrl = formAnnotation.reSetUrl();
            }
            if (expression == null || expression.equals("")) {
                expression = formAnnotation.expression();
            }
            autoSave = formAnnotation.autoSave();
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
