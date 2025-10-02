package net.ooder.esd.bean.data;


import net.ooder.annotation.AnnotationType;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.annotation.view.NavFoldingTabsViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.component.nav.FullNavFoldingTabsComponent;
import net.ooder.web.util.AnnotationUtil;

@CustomClass(clazz = FullNavFoldingTabsComponent.class,
        viewType = CustomViewType.FRAME,
        moduleType = ModuleViewType.NAVFOLDINGTABSCONFIG
)
@AnnotationType(clazz = NavFoldingTabsViewAnnotation.class)
public class NavFoldingTabsDataBean extends CustomDataBean {
    ModuleViewType moduleViewType = ModuleViewType.NAVBUTTONLAYOUTCONFIG;

    String expression;

    String editorPath;

    String saveUrl;

    ResponsePathTypeEnum itemType = ResponsePathTypeEnum.TABS;

    String className;

    String reSetUrl;

    String dataUrl;

    Boolean autoSave = false;

    Boolean cache = false;

    public NavFoldingTabsDataBean() {

    }

    public NavFoldingTabsDataBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        NavFoldingTabsViewAnnotation annotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), NavFoldingTabsViewAnnotation.class);
        if (annotation == null) {
            AnnotationUtil.fillDefaultValue(NavFoldingTabsViewAnnotation.class, this);
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


    public NavFoldingTabsDataBean fillData(NavFoldingTabsViewAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public Boolean getAutoSave() {
        return autoSave;
    }

    public void setAutoSave(Boolean autoSave) {
        this.autoSave = autoSave;
    }

    public String getReSetUrl() {
        return reSetUrl;
    }

    public void setReSetUrl(String reSetUrl) {
        this.reSetUrl = reSetUrl;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    public String getEditorPath() {
        return editorPath;
    }

    public void setEditorPath(String editorPath) {
        this.editorPath = editorPath;
    }

    public String getSaveUrl() {
        return saveUrl;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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
