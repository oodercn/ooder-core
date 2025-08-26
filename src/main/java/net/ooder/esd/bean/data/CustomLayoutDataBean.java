package net.ooder.esd.bean.data;


import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.LayoutViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.custom.component.FullCustomLayoutComponent;
import net.ooder.web.util.AnnotationUtil;
@CustomClass(clazz = FullCustomLayoutComponent.class,
        viewType = CustomViewType.LISTMODULE,
        moduleType = ModuleViewType.LAYOUTCONFIG
)
@AnnotationType(clazz = LayoutViewAnnotation.class)
public class CustomLayoutDataBean extends CustomDataBean {

    ModuleViewType moduleViewType = ModuleViewType.LAYOUTCONFIG;
    Boolean cache = false;
    String dataUrl;


    public CustomLayoutDataBean() {

    }


    public CustomLayoutDataBean fillData(LayoutViewAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public CustomLayoutDataBean(MethodConfig methodConfig) {
        super(methodConfig);
        LayoutViewAnnotation gridAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), LayoutViewAnnotation.class);
        this.dataUrl = methodConfig.getUrl();
        if (gridAnnotation != null) {
            if (dataUrl == null || dataUrl.equals("")) {
                dataUrl = gridAnnotation.dataUrl();
            }
        }
    }

    @Override
    public Boolean getCache() {
        return cache;
    }

    public void setCache(Boolean cache) {
        this.cache = cache;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }


    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }
}
