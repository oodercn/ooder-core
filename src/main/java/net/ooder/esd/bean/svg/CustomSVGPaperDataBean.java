package net.ooder.esd.bean.svg;

import net.ooder.annotation.AnnotationType;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.SVGPagerViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.data.CustomDataBean;
import net.ooder.esd.custom.component.FullSVGPaperComponent;
import net.ooder.web.util.AnnotationUtil;

@CustomClass(clazz = FullSVGPaperComponent.class,
        viewType = CustomViewType.MODULE,
        moduleType = ModuleViewType.SVGPAPERCONFIG
)
@AnnotationType(clazz = SVGPagerViewAnnotation.class)
public class CustomSVGPaperDataBean extends CustomDataBean {


    ModuleViewType moduleViewType = ModuleViewType.SVGPAPERCONFIG;

    String saveUrl;

    String searchUrl;

    String reSetUrl;

    String dataUrl;

    Boolean autoSave = false;

    Boolean cache = false;


    public CustomSVGPaperDataBean() {

    }


    public CustomSVGPaperDataBean(MethodConfig methodConfig) {
        super(methodConfig);
        SVGPagerViewAnnotation formAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), SVGPagerViewAnnotation.class);
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
