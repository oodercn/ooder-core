package net.ooder.esd.bean.grid;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.GridItemAnnotation;
import net.ooder.esd.annotation.RightContextMenu;
import net.ooder.esd.annotation.TreeItem;
import net.ooder.esd.bean.view.CustomGridViewBean;
import net.ooder.esd.bean.view.CustomMGridViewBean;
import net.ooder.esd.bean.bar.ContextMenuBar;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.bean.RightContextMenuBean;
import net.ooder.esd.dsm.view.field.FieldTreeConfig;
import net.ooder.web.ConstructorBean;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.NotNull;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@AnnotationType(clazz = GridItemAnnotation.class)
public class ChildGridViewBean<T extends FieldTreeConfig> implements ContextMenuBar {

    Boolean iniFold;

    int index;

    Boolean animCollapse;

    Boolean dynDestory;
    Boolean dynLoad;


    @NotNull
    Boolean lazyLoad;

    @NotNull
    Boolean autoReload;

    String imageClass;

    String valueSeparator;

    String groupName;

    Class bindService;

    Class<? extends TreeItem> customItems;

    String caption;

    SelModeType selMode;

    Set<ComponentType> bindTypes;


    String className;

    RightContextMenuBean contextMenuBean;

    Set<CustomTreeEvent> event = new LinkedHashSet<>();

    ConstructorBean constructorBean;

    @JSONField(serialize = false)
    Class fristClass;

    public String domainId;

    public String parentId;


    public ChildGridViewBean() {

    }


    public Class getFristClass() {
        return fristClass;
    }

    public void setFristClass(Class fristClass) {
        this.fristClass = fristClass;
    }


    public ChildGridViewBean(Constructor constructor, CustomGridViewBean viewBean) {
        constructorBean = new ConstructorBean(constructor);
        this.domainId = viewBean.getDomainId();
        this.parentId = viewBean.getId();

        Class[] paramClass = constructor.getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];

        }

        GridItemAnnotation treeViewAnnotation = AnnotationUtil.getConstructorAnnotation(constructor, GridItemAnnotation.class);
        if (treeViewAnnotation != null) {
            fillData(treeViewAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(GridItemAnnotation.class, this);
        }

        RightContextMenu annotation = AnnotationUtil.getConstructorAnnotation(constructor, RightContextMenu.class);
        if (annotation != null) {
            contextMenuBean = new RightContextMenuBean(this.getGroupName(),annotation);
        }


        String childCaption = null;
        if (fristClass != null) {
            childCaption = fristClass.getSimpleName();
        } else {
            childCaption = this.getGroupName();
        }
        if (viewBean.getCaption() != null) {
            caption = viewBean.getCaption() + "（" + childCaption + ")";
        } else {
            caption = viewBean.getName() + "（" + childCaption + ")";
        }


    }

    public ChildGridViewBean(Constructor constructor, CustomMGridViewBean viewBean) {
        constructorBean = new ConstructorBean(constructor);
        this.domainId = viewBean.getDomainId();
        this.parentId = viewBean.getId();

        Class[] paramClass = constructor.getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];

        }

        GridItemAnnotation treeViewAnnotation = AnnotationUtil.getConstructorAnnotation(constructor, GridItemAnnotation.class);
        if (treeViewAnnotation != null) {
            fillData(treeViewAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(GridItemAnnotation.class, this);
        }

        RightContextMenu annotation = AnnotationUtil.getConstructorAnnotation(constructor, RightContextMenu.class);
        if (annotation != null) {
            contextMenuBean = new RightContextMenuBean(this.getGroupName(),annotation);
        }


        String childCaption = null;
        if (fristClass != null) {
            childCaption = fristClass.getSimpleName();
        } else {
            childCaption = this.getGroupName();
        }
        if (viewBean.getCaption() != null) {
            caption = viewBean.getCaption() + "（" + childCaption + ")";
        } else {
            caption = viewBean.getName() + "（" + childCaption + ")";
        }


    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ChildGridViewBean) {
            return ((ChildGridViewBean) obj).getGroupName().equals(this.getGroupName());
        }
        return super.equals(obj);
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        if (contextMenuBean != null && contextMenuBean.getMenuClass().length > 0) {
            annotationBeans.add(contextMenuBean);
        }


        return annotationBeans;
    }


    public ChildGridViewBean fillData(GridItemAnnotation annotation) {
        if (annotation == null) {
            AnnotationUtil.fillDefaultValue(GridItemAnnotation.class, this);
        } else {
            AnnotationUtil.fillBean(annotation, this);
        }

        return this;
    }

    public String getGroupName() {
        if (groupName == null || groupName.equals("")) {
            if (fristClass != null) {
                groupName = fristClass.getSimpleName();
            } else {
                groupName = getClassName();
            }
        }
        return groupName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }


    public Set<CustomTreeEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomTreeEvent> event) {
        this.event = event;
    }

    public SelModeType getSelMode() {
        return selMode;
    }

    public void setSelMode(SelModeType selMode) {
        this.selMode = selMode;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    public ConstructorBean getConstructorBean() {
        return constructorBean;
    }

    public void setConstructorBean(ConstructorBean constructorBean) {
        this.constructorBean = constructorBean;
    }

    public void setBindTypes(Set<ComponentType> bindTypes) {
        this.bindTypes = bindTypes;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }


    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Boolean getAnimCollapse() {
        return animCollapse;
    }

    public void setAnimCollapse(Boolean animCollapse) {
        this.animCollapse = animCollapse;
    }

    public Boolean getDynDestory() {
        return dynDestory;
    }

    public void setDynDestory(Boolean dynDestory) {
        this.dynDestory = dynDestory;
    }

    public Boolean getDynLoad() {
        return dynLoad;
    }

    public void setDynLoad(Boolean dynLoad) {
        this.dynLoad = dynLoad;
    }

    public Boolean getLazyLoad() {
        return lazyLoad;
    }

    public void setLazyLoad(Boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }

    public Boolean getAutoReload() {
        return autoReload;
    }

    public void setAutoReload(Boolean autoReload) {
        this.autoReload = autoReload;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getValueSeparator() {
        return valueSeparator;
    }

    public void setValueSeparator(String valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    public Class getBindService() {
        return bindService;
    }

    public void setBindService(Class bindService) {
        this.bindService = bindService;
    }

    public Class<? extends TreeItem> getCustomItems() {
        return customItems;
    }

    public void setCustomItems(Class<? extends TreeItem> customItems) {
        this.customItems = customItems;
    }

    @Override
    public Set<ComponentType> getBindTypes() {
        return bindTypes;
    }
}
