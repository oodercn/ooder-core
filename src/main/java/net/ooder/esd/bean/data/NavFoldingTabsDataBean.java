package net.ooder.esd.bean.data;


import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.annotation.view.NavFoldingTabsViewAnnotation;
import net.ooder.esd.annotation.view.TabsViewAnnotation;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.view.ViewEntityConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

@AnnotationType(clazz = NavFoldingTabsViewAnnotation.class)
public class NavFoldingTabsDataBean implements CustomBean {

    String expression;

    String editorPath;

    String saveUrl;

    ResponsePathTypeEnum itemType;

    String className;


    public NavFoldingTabsDataBean() {

    }

    public NavFoldingTabsDataBean(ESDClass esdClass) {
        TabsViewAnnotation annotation = AnnotationUtil.getClassAnnotation(esdClass.getCtClass(), TabsViewAnnotation.class);
        if (annotation != null) {
            fillData(annotation);
        }
    }

    public NavFoldingTabsDataBean(TabsViewAnnotation annotation) {
        fillData(annotation);
    }

    public NavFoldingTabsDataBean fillData(TabsViewAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public NavFoldingTabsDataBean(ViewEntityConfig esdClassConfig) {
        this.className = esdClassConfig.getClassName();


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

}
