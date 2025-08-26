package net.ooder.esd.bean.data;


import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.NavGalleryViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.custom.component.nav.FullNavGalleryComponent;
import net.ooder.web.util.AnnotationUtil;
@CustomClass(clazz = FullNavGalleryComponent.class,
        viewType = CustomViewType.FRAME,
        moduleType = ModuleViewType.NAVGALLERYCONFIG
)
@AnnotationType(clazz = NavGalleryViewAnnotation.class)
public class NavGalleryDataBean extends CustomDataBean {

    ModuleViewType moduleViewType = ModuleViewType.NAVGALLERYCONFIG;

    String dataUrl;

    Boolean cache = false;

    public NavGalleryDataBean() {

    }

    public NavGalleryDataBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        ESDClass esdClass = methodAPIBean.getViewClass();
        if (esdClass != null) {
            NavGalleryViewAnnotation annotation = AnnotationUtil.getClassAnnotation(esdClass.getCtClass(), NavGalleryViewAnnotation.class);
            if (annotation != null) {
                if (cache == null) {
                    cache = annotation.cache();
                }
                if (dataUrl == null || dataUrl.equals("")) {
                    dataUrl = annotation.dataUrl();
                }

            }
        }

    }


    public NavGalleryDataBean fillData(NavGalleryViewAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String getDomainId() {
        return domainId;
    }

    @Override
    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    @Override
    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    @Override
    public Boolean getCache() {
        return cache;
    }

    public void setCache(Boolean cache) {
        this.cache = cache;
    }

    @Override
    public String getSourceClassName() {
        return sourceClassName;
    }

    @Override
    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

}
