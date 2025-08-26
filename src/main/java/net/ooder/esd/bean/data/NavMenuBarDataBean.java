package net.ooder.esd.bean.data;


import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.NavMenuBarViewAnnotation;
import net.ooder.esd.annotation.view.TabsViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.custom.component.nav.FullNavMenuBarComponent;
import net.ooder.web.util.AnnotationUtil;
@CustomClass(clazz = FullNavMenuBarComponent.class,
        viewType = CustomViewType.NAV,
        moduleType = ModuleViewType.NAVMENUBARCONFIG
)
@AnnotationType(clazz = NavMenuBarViewAnnotation.class)
public class NavMenuBarDataBean extends CustomDataBean {

    ModuleViewType moduleViewType = ModuleViewType.NAVMENUBARCONFIG;


    String className;

    String dataUrl;

    Boolean cache = false;


    public NavMenuBarDataBean() {

    }


    public NavMenuBarDataBean(MethodConfig methodAPIBean) {
        ESDClass esdClass = methodAPIBean.getViewClass();
        this.methodName = methodAPIBean.getMethodName();
        this.domainId = methodAPIBean.getDomainId();
        if (esdClass != null) {
            NavMenuBarViewAnnotation formAnnotation = AnnotationUtil.getClassAnnotation(esdClass.getCtClass(), NavMenuBarViewAnnotation.class);
            if (formAnnotation == null) {
                AnnotationUtil.fillDefaultValue(NavMenuBarViewAnnotation.class, this);
            } else {
                AnnotationUtil.fillBean(formAnnotation, this);
            }

            if (formAnnotation != null) {
                if (dataUrl == null || dataUrl.equals("")) {
                    dataUrl = formAnnotation.dataUrl();
                }

                if (expression == null || expression.equals("")) {
                    expression = formAnnotation.expression();
                }

            }
        }


    }

    public NavMenuBarDataBean fillData(TabsViewAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getDataUrl() {
        return dataUrl;
    }

    @Override
    public Boolean getCache() {
        return cache;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    public void setCache(Boolean cache) {
        this.cache = cache;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

}
