package net.ooder.esd.bean.data;


import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.StacksViewAnnotation;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.annotation.view.TabsViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.component.nav.FullStacksComponent;
import net.ooder.esd.dsm.view.ViewEntityConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
@CustomClass(clazz = FullStacksComponent.class,
        viewType = CustomViewType.NAV,
        moduleType = ModuleViewType.NAVSTACKSCONFIG
)
@AnnotationType(clazz = StacksViewAnnotation.class)
public class StacksDataBean extends CustomDataBean {


    ModuleViewType moduleViewType = ModuleViewType.NAVSTACKSCONFIG;

    ResponsePathTypeEnum itemType = ResponsePathTypeEnum.TABS;

    Boolean cache = false;

    Boolean autoSave = false;

    String dataUrl;

    String saveUrl;

    String reSetUrl;

    String expression;

    String editorPath;


    String domainId;

    String className;


    public StacksDataBean(MethodConfig methodConfig) {
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


    public StacksDataBean() {

    }

    public StacksDataBean(ESDClass esdClass) {
        StacksViewAnnotation annotation = AnnotationUtil.getClassAnnotation(esdClass.getCtClass(), StacksViewAnnotation.class);
        if (annotation != null) {
            fillData(annotation);
        }
    }

    public StacksDataBean(StacksViewAnnotation annotation) {
        fillData(annotation);
    }

    public StacksDataBean fillData(StacksViewAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public StacksDataBean(ViewEntityConfig esdClassConfig) {
        this.className = esdClassConfig.getClassName();
        this.domainId = esdClassConfig.getDomainId();

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


    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getEditorPath() {
        return editorPath;
    }

    public void setEditorPath(String editorPath) {
        this.editorPath = editorPath;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
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

    public String getReSetUrl() {
        return reSetUrl;
    }

    public void setReSetUrl(String reSetUrl) {
        this.reSetUrl = reSetUrl;
    }
}
