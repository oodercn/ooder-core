package net.ooder.esd.bean.data;


import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.NavGroupViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.custom.component.nav.FullNavGroupComponent;
import net.ooder.web.util.AnnotationUtil;
@CustomClass(clazz = FullNavGroupComponent.class,
        viewType = CustomViewType.MODULE,
        moduleType = ModuleViewType.NAVGROUPCONFIG
)
@AnnotationType(clazz = NavGroupViewAnnotation.class)
public class NavGroupDataBean extends CustomDataBean {


    ModuleViewType moduleViewType = ModuleViewType.NAVGROUPCONFIG;

    Boolean cache = false;

    Boolean autoSave = false;

    String saveUrl;

    String reSetUrl;

    String dataUrl;

    String searchUrl;

    String caption;


    public NavGroupDataBean() {

    }


    public NavGroupDataBean(MethodConfig methodConfig) {
        super(methodConfig);
        NavGroupViewAnnotation groupViewAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), NavGroupViewAnnotation.class);
        if (groupViewAnnotation != null) {
            if (dataUrl == null || dataUrl.equals("")) {
                dataUrl = groupViewAnnotation.dataUrl();
            }
            if (saveUrl == null || saveUrl.equals("")) {
                saveUrl = groupViewAnnotation.saveUrl();
            }
            if (reSetUrl == null || reSetUrl.equals("")) {
                reSetUrl = groupViewAnnotation.reSetUrl();
            }


            if (searchUrl == null || searchUrl.equals("")) {
                searchUrl = groupViewAnnotation.searchUrl();
            }

            autoSave = groupViewAnnotation.autoSave();
        }


    }


    public NavGroupDataBean fillData(NavGroupViewAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
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

    @Override
    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
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
