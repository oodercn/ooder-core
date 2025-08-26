package net.ooder.esd.bean.data;


import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.TitleBlockViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.custom.component.FullTitleBlockComponent;
import net.ooder.web.util.AnnotationUtil;
@CustomClass(clazz = FullTitleBlockComponent.class,
        viewType = CustomViewType.LISTMODULE,
        moduleType = ModuleViewType.TITLEBLOCKCONFIG
)
@AnnotationType(clazz = TitleBlockViewAnnotation.class)
public class CustomTitleBlockDataBean extends CustomDataBean {

    ModuleViewType moduleViewType = ModuleViewType.TITLEBLOCKCONFIG;
    String itemCaption;
    String itemId;
    Boolean cache = false;
    Boolean autoSave = false;
    String editorPath;
    String clickFlagPath;
    String addPath;
    String sortPath;
    String delPath;
    String dataUrl;


    public CustomTitleBlockDataBean() {

    }


    public CustomTitleBlockDataBean fillData(TitleBlockViewAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public CustomTitleBlockDataBean(MethodConfig methodConfig) {
        super(methodConfig);
        TitleBlockViewAnnotation gridAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), TitleBlockViewAnnotation.class);
        this.dataUrl = methodConfig.getUrl();
        if (gridAnnotation != null) {
            if (dataUrl == null || dataUrl.equals("")) {
                dataUrl = gridAnnotation.dataUrl();
            }

            if (itemCaption == null || itemCaption.equals("")) {
                itemCaption = gridAnnotation.itemCaption();
            }
            if (itemId == null || itemId.equals("")) {
                itemId = gridAnnotation.itemId();
            }
            if (addPath == null || addPath.equals("")) {
                addPath = gridAnnotation.addPath();
            }
            if (editorPath == null || editorPath.equals("")) {
                editorPath = gridAnnotation.editorPath();
            }
            if (sortPath == null || sortPath.equals("")) {
                sortPath = gridAnnotation.sortPath();
            }
            if (clickFlagPath == null || clickFlagPath.equals("")) {
                clickFlagPath = gridAnnotation.clickFlagPath();
            }
            if (delPath == null || delPath.equals("")) {
                delPath = gridAnnotation.delPath();
            }
            if (expression == null || expression.equals("")) {
                expression = gridAnnotation.expression();
            }
            this.autoSave = gridAnnotation.autoSave();
        }
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

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getItemCaption() {
        return itemCaption;
    }

    public void setItemCaption(String itemCaption) {
        this.itemCaption = itemCaption;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


    public String getSortPath() {
        return sortPath;
    }

    public void setSortPath(String sortPath) {
        this.sortPath = sortPath;
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

    public String getEditorPath() {
        return editorPath;
    }

    public void setEditorPath(String editorPath) {
        this.editorPath = editorPath;
    }

    public String getAddPath() {
        return addPath;
    }

    public void setAddPath(String addPath) {
        this.addPath = addPath;
    }

    public String getDelPath() {
        return delPath;
    }

    public void setDelPath(String delPath) {
        this.delPath = delPath;
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
