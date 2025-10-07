package net.ooder.esd.bean.data;

import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.view.PopTreeViewAnnotation;
import net.ooder.esd.annotation.view.TreeViewAnnotation;
import net.ooder.esd.annotation.ui.ResponsePathTypeEnum;
import net.ooder.esd.annotation.view.NavFoldingTreeViewAnnotation;
import net.ooder.esd.annotation.view.NavTreeViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ESDField;
import net.ooder.web.util.AnnotationUtil;

public abstract class TreeDataBaseBean extends CustomDataBean {


    Boolean cache = true;

    Boolean autoSave = false;

    String editorPath;

    String saveUrl;

    String dataUrl;

    String loadChildUrl;

    String fieldCaption;

    String fieldId;

    String rootId;

    ResponsePathTypeEnum itemType = ResponsePathTypeEnum.TREEVIEW;


    public TreeDataBaseBean() {

    }

    public TreeDataBaseBean(MethodConfig methodConfig) {
        super(methodConfig);
        NavFoldingTreeViewAnnotation navFoldingTreeViewAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), NavFoldingTreeViewAnnotation.class);
        PopTreeViewAnnotation popTreeViewAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), PopTreeViewAnnotation.class);
        NavTreeViewAnnotation navTreeAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), NavTreeViewAnnotation.class);
        TreeViewAnnotation treeAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), TreeViewAnnotation.class);
        if (navFoldingTreeViewAnnotation != null) {
            if (dataUrl == null || dataUrl.equals("")) {
                dataUrl = navFoldingTreeViewAnnotation.dataUrl();
            }

            if (saveUrl == null || saveUrl.equals("")) {
                saveUrl = navFoldingTreeViewAnnotation.saveUrl();
            }

            if (expression == null || expression.equals("")) {
                expression = navFoldingTreeViewAnnotation.expression();
            }
            if (itemType == null) {
                itemType = navFoldingTreeViewAnnotation.itemType();
            }
            if (autoSave == null) {
                autoSave = navFoldingTreeViewAnnotation.autoSave();
            }
        } else {
            AnnotationUtil.fillDefaultValue(NavFoldingTreeViewAnnotation.class, this);
        }


        if (navTreeAnnotation != null) {
            if (dataUrl == null || dataUrl.equals("")) {
                dataUrl = navTreeAnnotation.dataUrl();
            }
            if (loadChildUrl == null || loadChildUrl.equals("")) {
                loadChildUrl = navTreeAnnotation.loadChildUrl();
            }
            if (saveUrl == null || saveUrl.equals("")) {
                saveUrl = navTreeAnnotation.saveUrl();
            }

            if (autoSave == null) {
                autoSave = navTreeAnnotation.autoSave();
            }
            if (editorPath == null || editorPath.equals("")) {
                editorPath = navTreeAnnotation.editorPath();
            }
            if (expression == null || expression.equals("")) {
                expression = navTreeAnnotation.expression();
            }
            if (itemType == null) {
                itemType = navTreeAnnotation.itemType();
            }
            if (rootId == null) {
                rootId = navTreeAnnotation.rootId();
            }
        } else {
            AnnotationUtil.fillDefaultValue(NavTreeViewAnnotation.class, this);
        }

        if (treeAnnotation != null) {
            if (dataUrl == null || dataUrl.equals("")) {
                dataUrl = treeAnnotation.dataUrl();
            }
            if (loadChildUrl == null || loadChildUrl.equals("")) {
                loadChildUrl = treeAnnotation.loadChildUrl();
            }
            if (saveUrl == null || saveUrl.equals("")) {
                saveUrl = treeAnnotation.saveUrl();
            }
            if (editorPath == null || editorPath.equals("")) {
                editorPath = treeAnnotation.editorPath();
            }
            if (expression == null || expression.equals("")) {
                expression = treeAnnotation.expression();
            }
            if (itemType == null) {
                itemType = treeAnnotation.itemType();
            }
            if (rootId == null) {
                rootId = treeAnnotation.rootId();
            }
            if (autoSave == null) {
                autoSave = treeAnnotation.autoSave();
            }
        } else {
            AnnotationUtil.fillDefaultValue(TreeViewAnnotation.class, this);
        }

        if (popTreeViewAnnotation != null) {
            if (dataUrl == null || dataUrl.equals("")) {
                dataUrl = popTreeViewAnnotation.dataUrl();
            }
            if (loadChildUrl == null || loadChildUrl.equals("")) {
                loadChildUrl = popTreeViewAnnotation.loadChildUrl();
            }
            if (saveUrl == null || saveUrl.equals("")) {
                saveUrl = popTreeViewAnnotation.saveUrl();
            }
            if (editorPath == null || editorPath.equals("")) {
                editorPath = popTreeViewAnnotation.editorPath();
            }
            if (expression == null || expression.equals("")) {
                expression = popTreeViewAnnotation.expression();
            }
            if (fieldCaption == null || fieldCaption.equals("")) {
                fieldCaption = popTreeViewAnnotation.fieldCaption();
            }
            if (fieldCaption == null || fieldCaption.equals("")) {
                ESDField captionField = methodConfig.getTopSourceClass().getEntityClass().getCaptionField();
                if (captionField != null) {
                    fieldCaption = captionField.getName();
                }

            }


            if (fieldId == null || fieldId.equals("")) {
                fieldId = popTreeViewAnnotation.fieldId();
            }

            if (fieldId == null || fieldId.equals("")) {
                fieldId = methodConfig.getTopSourceClass().getEntityClass().getUid();
            }

            if (itemType == null) {
                itemType = popTreeViewAnnotation.itemType();
            }
            if (rootId == null) {
                rootId = popTreeViewAnnotation.rootId();
            }
        } else {
            AnnotationUtil.fillDefaultValue(PopTreeViewAnnotation.class, this);
        }

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

    @Override
    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getLoadChildUrl() {
        return loadChildUrl;
    }

    public void setLoadChildUrl(String loadChildUrl) {
        this.loadChildUrl = loadChildUrl;
    }

    public String getFieldCaption() {
        return fieldCaption;
    }

    public void setFieldCaption(String fieldCaption) {
        this.fieldCaption = fieldCaption;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public ResponsePathTypeEnum getItemType() {
        return itemType;
    }

    public void setItemType(ResponsePathTypeEnum itemType) {
        this.itemType = itemType;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public abstract ModuleViewType getModuleViewType();


}
