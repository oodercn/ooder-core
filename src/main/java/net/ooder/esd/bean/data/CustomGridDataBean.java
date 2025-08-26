package net.ooder.esd.bean.data;


import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.view.GridViewAnnotation;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.component.grid.CustomGridComponent;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.custom.component.grid.FullGridComponent;
import net.ooder.web.util.AnnotationUtil;

import java.util.HashSet;
import java.util.Set;
@CustomClass(clazz = FullGridComponent.class,
        viewType = CustomViewType.LISTMODULE,
        moduleType = ModuleViewType.GRIDCONFIG
)
@AnnotationType(clazz = GridViewAnnotation.class)
public class CustomGridDataBean extends CustomDataBean {

    ModuleViewType moduleViewType = ModuleViewType.GRIDCONFIG;
    public Boolean cache;
    public String editorPath;
    public String addPath;
    public String sortPath;
    public String saveRowPath;
    public String saveAllRowPath;
    public String delPath;
    public String dataUrl;


    public CustomGridDataBean() {

    }


    public CustomGridDataBean fillData(GridViewAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public CustomGridDataBean(CustomGridComponent gridComponent) {
        this.editorPath = gridComponent.getEditorPath();
        this.addPath = gridComponent.getAddPath();
        this.dataUrl = gridComponent.getDataUrl();
        this.sortPath = gridComponent.getSortPath();
        this.delPath = gridComponent.getDelPath();
        this.saveRowPath = gridComponent.getSaveRowPath();
        this.saveAllRowPath = gridComponent.getSaveRowPath();

    }


    public CustomGridDataBean(MethodConfig methodConfig) {
        super(methodConfig);
        GridViewAnnotation gridAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), GridViewAnnotation.class);

        if (gridAnnotation != null) {
            AnnotationUtil.fillBean(gridAnnotation, this);
//            if (dataUrl == null || dataUrl.equals("")) {
//                dataUrl = gridAnnotation.dataUrl();
//            }
//            if (addPath == null || addPath.equals("")) {
//                addPath = gridAnnotation.addPath();
//            }
//            if (editorPath == null || editorPath.equals("")) {
//                editorPath = gridAnnotation.editorPath();
//            }
//            if (saveRowPath == null || editorPath.equals("")) {
//                saveRowPath = gridAnnotation.saveRowPath();
//            }
//            if (saveAllRowPath == null || saveAllRowPath.equals("")) {
//                saveAllRowPath = gridAnnotation.saveAllRowPath();
//            }
//            if (sortPath == null || sortPath.equals("")) {
//                sortPath = gridAnnotation.sortPath();
//            }
//            if (delPath == null || delPath.equals("")) {
//                delPath = gridAnnotation.delPath();
//            }
//            if (expression == null || expression.equals("")) {
//                expression = gridAnnotation.expression();
//            }

        }


    }


    public Set<Class> getCustomService() {
        customService = new HashSet<>();
        for (String className : this.getCustomServiceClass()) {
            try {
                if (className.endsWith(".class")) {
                    className.substring(0, className.length() - ".class".length());
                }
                customService.add(ClassUtility.loadClass(className));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return customService;
    }

    public void setCustomService(Set<Class> customService) {
        if (customService == null || customService.isEmpty()) {
            this.getCustomServiceClass().clear();
        } else {
            for (Class clazz : customService) {
                this.getCustomServiceClass().add(clazz.getName());
            }
        }
    }

    public Set<String> getCustomServiceClass() {
        return customServiceClass;
    }

    public void setCustomServiceClass(Set<String> customServiceClass) {
        this.customServiceClass = customServiceClass;
    }


    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getSaveRowPath() {
        return saveRowPath;
    }

    public void setSaveRowPath(String saveRowPath) {
        this.saveRowPath = saveRowPath;
    }

    public String getSaveAllRowPath() {
        return saveAllRowPath;
    }

    public void setSaveAllRowPath(String saveAllRowPath) {
        this.saveAllRowPath = saveAllRowPath;
    }

    @Override
    public Boolean getCache() {
        return cache;
    }

    public void setCache(Boolean cache) {
        this.cache = cache;
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

    @Override
    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
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
}
