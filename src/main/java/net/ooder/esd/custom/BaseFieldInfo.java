package net.ooder.esd.custom;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.*;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.event.FieldEvent;
import net.ooder.esd.annotation.event.GridEvent;
import net.ooder.esd.annotation.field.*;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.bean.field.LabelBean;
import net.ooder.esd.bean.field.SearchFieldBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.bean.grid.GridColItemBean;
import net.ooder.esd.tool.OODTypeMapping;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseFieldInfo<M extends ComponentBean, N extends ComboBoxBean> implements ESDField<M, N> {


    String id;
    String domainId;
    String projectVersionName;
    String name;
    String expression;

    String width;
    String height = "30";
    Object value;
    String desc;
    String imageClass;
    String fieldName;

    String target;

    String caption = "";

    boolean split = false;

    boolean serialize = true;
    boolean isCaption = false;
    boolean uid = false;
    boolean readonly = false;
    boolean disabled = false;
    boolean pid = false;
    boolean hidden = false;

    Integer index = 0;

    ComponentType componentType;

    String simpleClassName;

    Dock dock;

    RefType refType;

    ComponentType[] bindTypes = new ComponentType[]{};

    Set<GridEventBean> gridEvents = new HashSet<>();

    @JSONField(serialize = false)
    ESDClass esdClass;

    CustomRefBean refBean;

    GridColItemBean gridColItemBean;

    CustomLayoutItemBean layoutItemBean;

    FieldBean fieldBean;

    CustomFieldBean customBean;

    DisabledBean disabledBean;

    TipsBean tipsBean;

    LabelBean labelBean;

    DockBean dockBean;

    ContainerBean containerBean;

    CustomUIBean uiBean;

    SearchFieldBean searchFieldBean;

    RightContextMenuBean contextMenuBean;

    Class<? extends Enum> enumClass;

    Set<String> enums;

    M widgetConfig;

    N comboConfig;

    abstract Annotation getWidgetAnnotation(ComboInputType inputType);

    abstract <T extends Annotation> T getAnnotation(Class<T> annotationClass);

    abstract Set<Annotation> getAllAnnotation();

    @Override
    public abstract Class getReturnType();

    @Override
    public abstract Type getGenericType();

    public N initCombo(ComboInputType inputType) {
        Class<?> clazz = null;
        try {
            if (inputType == null) {
                ComboInputAnnotation inputAnnotation = getAnnotation(ComboInputAnnotation.class);
                if (inputAnnotation != null) {
                    inputType = inputAnnotation.inputType();
                } else {
                    inputType = OODTypeMapping.getType(getReturnType());
                }
                for (Annotation annotation : this.getAllAnnotation()) {
                    if (!(annotation.annotationType().equals(ComboInputAnnotation.class))) {
                        BeanClass beanClass = annotation.annotationType().getAnnotation(BeanClass.class);
                        CustomClass customClass = annotation.annotationType().getAnnotation(CustomClass.class);
                        if (beanClass != null && customClass != null
                                && ComboInputFieldBean.class.isAssignableFrom(beanClass.clazz())
                                ) {
                            inputType = customClass.inputType()[0];
                        }
                    }
                }
                if ((enumClass != null && !enumClass.equals(Enum.class) && !enumClass.equals(Void.class))
                        || (enums != null && enums.size() > 0)) {
                    inputType = ComboInputType.listbox;
                }
            }
//
            clazz = CustomViewConfigFactory.getInstance().getDefaultComboBoxClass(inputType);
            Constructor constructor = null;
            try {
                constructor = clazz.getConstructor(new Class[]{ESDField.class, Set.class, ComboInputType.class});
                comboConfig = (N) constructor.newInstance(new Object[]{this, this.getAllAnnotation(), inputType});
            } catch (NoSuchMethodException e) {
                try {
                    constructor = clazz.getConstructor(new Class[]{ESDField.class, Set.class});
                    comboConfig = (N) constructor.newInstance(new Object[]{this, this.getAllAnnotation()});
                } catch (NoSuchMethodException ee) {
                    constructor = clazz.getConstructor(new Class[]{Set.class});
                    comboConfig = (N) constructor.newInstance(new Object[]{this.getAllAnnotation()});
                }
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return comboConfig;
    }


    public void initWidget() {
        Class<?> clazz = null;
        ComponentType[] skipComs = new ComponentType[]{ComponentType.TOOLBAR, ComponentType.APICALLER, ComponentType.COMBOINPUT, ComponentType.INPUT, ComponentType.LIST};
        Class<? extends Annotation>[] skipAnnotations = new Class[]{ComboInputAnnotation.class, ToolBarMenu.class, ListAnnotation.class, InputAnnotation.class, APIEventAnnotation.class};
        if (componentType == null || Arrays.asList(skipComs).contains(componentType)) {

            CustomClass customClass = CustomViewConfigFactory.getInstance().getWidgetCustomAnnotation(this.getAllAnnotation().toArray(new Annotation[]{}));
            if (customClass != null) {
                componentType = customClass.componentType();
            }


            if (componentType == null || componentType.equals(ComponentType.INPUT)) {
                //独立组件
                if (this.getAnnotation(InputAnnotation.class) != null) {
                    componentType = ComponentType.INPUT;
                } else if (this.getAnnotation(ListAnnotation.class) != null) {
                    componentType = ComponentType.LIST;
                } else if (this.getAnnotation(ToolBarMenu.class) != null) {
                    componentType = ComponentType.TOOLBAR;
                } else if (this.getAnnotation(APIEventAnnotation.class) != null) {
                    componentType = ComponentType.APICALLER;
                }
            }

        }
//
//        if (componentType.equals(ComponentType.Input)) {
//            for (Annotation annotation : this.getAllAnnotation()) {
//                if (!(annotation.annotationType().equals(InputAnnotation.class))) {
//                    BeanClass beanClass = annotation.annotationType().getAnnotation(BeanClass.class);
//                    CustomClass customClass = annotation.annotationType().getAnnotation(CustomClass.class);
//                    if (beanClass != null && customClass != null) {
//                        componentType = customClass.componentType();
//                    }
//                }
//            }
//        }
//
//        if (componentType.equals(ComponentType.List)) {
//            for (Annotation annotation : this.getAllAnnotation()) {
//                if (!(annotation.annotationType().equals(ListAnnotation.class))) {
//                    BeanClass beanClass = annotation.annotationType().getAnnotation(BeanClass.class);
//                    CustomClass customClass = annotation.annotationType().getAnnotation(CustomClass.class);
//                    if (beanClass != null && customClass != null) {
//                        componentType = customClass.componentType();
//                    }
//                }
//            }
//        }
        try {
            clazz = CustomViewConfigFactory.getInstance().getDefaultWidgetClass(componentType);
            Constructor constructor = null;
            try {
                constructor = clazz.getConstructor(new Class[]{ESDField.class, Set.class});
                widgetConfig = (M) constructor.newInstance(new Object[]{this, this.getAllAnnotation()});
            } catch (NoSuchMethodException e) {
                constructor = clazz.getConstructor(new Class[]{Set.class});
                widgetConfig = (M) constructor.newInstance(new Object[]{this.getAllAnnotation()});
            }

//
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }


    void init(Integer index, ESDClass esdClass) {
        JSONField jsonField = getAnnotation(JSONField.class);
        CustomAnnotation customAnnotation = getAnnotation(CustomAnnotation.class);
        MethodChinaName methodChinaName = getAnnotation(MethodChinaName.class);
        Split splitClass = getAnnotation(Split.class);
        Uid uidClass = getAnnotation(Uid.class);
        Pid pidClass = getAnnotation(Pid.class);
        Caption captionClass = getAnnotation(Caption.class);
        Ref refAnnotation = getAnnotation(Ref.class);
        Readonly readonlyClass = getAnnotation(Readonly.class);
        Required requiredClass = getAnnotation(Required.class);
        Disabled disabledClass = getAnnotation(Disabled.class);
        DynCheck dynCheckClass = getAnnotation(DynCheck.class);
        DirtyMask dirtyMarkClass = getAnnotation(DirtyMask.class);
        GridColItemAnnotation gridItemAnnotation = getAnnotation(GridColItemAnnotation.class);
        LayoutItemAnnotation layoutItemAnnotation = getAnnotation(LayoutItemAnnotation.class);
        FieldAnnotation fieldAnnotation = getAnnotation(FieldAnnotation.class);
        Tips tipsAnnotation = getAnnotation(Tips.class);
        Label labelAnnotation = getAnnotation(Label.class);
        SearchAnnotation searchAnnotation = getAnnotation(SearchAnnotation.class);
        DockAnnotation dockAnnotation = getAnnotation(DockAnnotation.class);
        UIAnnotation uiAnnotation = getAnnotation(UIAnnotation.class);
        ContainerAnnotation containerAnnotation = getAnnotation(ContainerAnnotation.class);
        GridEvent gridEvent = getAnnotation(GridEvent.class);
        if (gridEvent != null) {
            GridEventBean gridEventBean = new GridEventBean(gridEvent);
            gridEvents.add(gridEventBean);
        }


        this.index = index;
        this.esdClass = esdClass;
        this.domainId = esdClass.getDomainId();
        Type genType = null;
        try {
            genType = this.getGenericType();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (genType != null) {
            this.simpleClassName = AnnotationUtil.toType(this.getGenericType()).toString();
            this.componentType = OODTypeMapping.getComponentType(this.getReturnType(), this.getGenericType());
        } else {
            this.simpleClassName = AnnotationUtil.toType(this.getReturnType()).toString();
            this.componentType = OODTypeMapping.getComponentType(this.getReturnType(), this.getReturnType());
        }


        if (fieldAnnotation != null) {
            this.expression = fieldAnnotation.expression();
            this.bindTypes = fieldAnnotation.bindTypes();
            this.height = Integer.toString(fieldAnnotation.manualHeight());
            this.width = Integer.toString(fieldAnnotation.manualWidth());
            this.serialize = fieldAnnotation.serialize();
        }

        this.fieldBean = new FieldBean(this.getAllAnnotation());

        if (gridItemAnnotation != null) {
            this.width = gridItemAnnotation.width();
            this.gridColItemBean = new GridColItemBean(gridItemAnnotation);
        }

        if (layoutItemAnnotation != null) {
            this.layoutItemBean = new CustomLayoutItemBean(layoutItemAnnotation);

        }

        //jsonField serialize 是否序列化 输出
        if (customAnnotation != null) {
            this.customBean = new CustomFieldBean(customAnnotation);
            this.index = customBean.getIndex();
            this.target = customBean.getTarget();
            if (customAnnotation.hidden()) {
                componentType = ComponentType.HIDDENINPUT;
                this.hidden = true;
            }
            this.isCaption = customAnnotation.captionField();
            this.uid = customAnnotation.uid();
            this.enumClass = customAnnotation.enumClass();
            this.enums = new HashSet<>(Arrays.asList(customAnnotation.enums()));
            if ((enumClass != null && !enumClass.equals(Enum.class) && !enumClass.equals(Void.class))
                    || (enums != null && enums.size() > 0)) {
                componentType = ComponentType.COMBOINPUT;
            }

            if (customAnnotation.disabled()) {
                this.disabled = customAnnotation.disabled();
            }

            this.pid = customAnnotation.pid();
            if (customAnnotation.pid()) {
                componentType = ComponentType.HIDDENINPUT;
                this.hidden = true;
            }


            this.imageClass = customAnnotation.imageClass();
            this.readonly = customAnnotation.readonly();
            if (!customAnnotation.caption().equals("")) {
                this.caption = customAnnotation.caption();
            }
            if (!customAnnotation.id().equals("")) {
                id = customAnnotation.id();
            }
        }

        if (refAnnotation != null) {
            refBean = new CustomRefBean(refAnnotation);
            componentType = ComponentType.COMBOINPUT;
            if (refAnnotation.fk().equals("")) {
                refBean.setFk(this.getName());
            }
            if (refAnnotation.pk().equals("")) {
                refBean.setPk(getESDClass().getUid());
            }
        }

        if (uidClass != null) {
            uid = true;
            this.hidden = true;
            componentType = ComponentType.HIDDENINPUT;
        }
        if (pidClass != null) {
            pid = true;
            this.hidden = true;
            componentType = ComponentType.HIDDENINPUT;
        }
        if (splitClass != null) {
            split = true;
        }

        if (readonlyClass != null) {
            readonly = true;
        }
        if (requiredClass != null) {
            if (fieldBean == null) {
                this.fieldBean = new FieldBean();
            }
            this.fieldBean.setRequired(true);
        }
        if (disabledClass != null) {
            disabledBean = new DisabledBean(disabledClass);
            disabled = disabledBean.getDisabled();
        }

        if (dockAnnotation != null) {
            dockBean = new DockBean(dockAnnotation);
        }

        if (tipsAnnotation != null) {
            tipsBean = new TipsBean(tipsAnnotation);
        }

        if (labelAnnotation != null) {
            labelBean = new LabelBean(labelAnnotation);
        }

        if (searchAnnotation != null) {
            searchFieldBean = new SearchFieldBean(searchAnnotation);
        }

        containerBean = new ContainerBean(getAllAnnotation());

        if (uiAnnotation != null) {
            this.uiBean = new CustomUIBean(uiAnnotation);
        }

        if (dynCheckClass != null) {
            this.fieldBean.setDynCheck(true);
        }

        if (dirtyMarkClass != null) {
            this.fieldBean.setDirtyMark(true);
        }
        if (captionClass != null) {
            isCaption = true;
        }

        if (fieldBean.getComponentType() == null) {
            this.fieldBean.setComponentType(componentType);
        } else {
            componentType = fieldBean.getComponentType();
        }
        //   initWidget();

        //  initWidget();

//        if (componentType.equals(ComponentType.ComboInput)) {
//            this.initCombo(null);
//        }
//

        //json 覆盖所有属性
        if (jsonField != null) {
            this.serialize = jsonField.serialize();
            if (!jsonField.name().equals("")) {
                this.fieldName = jsonField.name();
                this.name = jsonField.name();
            }
            if (!jsonField.name().equals("")) {
                id = jsonField.name();
            }
        }

        //中文注解最后覆盖
        if (caption == null || caption.equals("")) {
            if (methodChinaName != null) {
                caption = methodChinaName.cname();
            } else {
                if (caption.equals("")) {
                    this.caption = name;
                }

            }
        }
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }


    @Override
    public RightContextMenuBean getContextMenuBean() {
        if (contextMenuBean == null) {
            RightContextMenu rightMenu = getAnnotation(RightContextMenu.class);
            if (rightMenu != null) {
                if (rightMenu != null) {
                    contextMenuBean = new RightContextMenuBean(this.getFieldName(), rightMenu);
                }
            }
        }
        return contextMenuBean;
    }


    public Set<GridEventBean> getGridEvents() {
        return gridEvents;
    }

    public void setGridEvents(Set<GridEventBean> gridEvents) {
        this.gridEvents = gridEvents;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }

    @Override
    public CustomLayoutItemBean getLayoutItemBean() {
        return layoutItemBean;
    }

    public void setLayoutItemBean(CustomLayoutItemBean layoutItemBean) {
        this.layoutItemBean = layoutItemBean;
    }

    @Override
    public DockBean getDockBean() {
        return dockBean;
    }

    public void setDockBean(DockBean dockBean) {
        this.dockBean = dockBean;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public ComponentType[] getBindTypes() {
        return bindTypes;
    }


    @Override
    public DisabledBean getDisabledBean() {
        return disabledBean;
    }

    public void setDisabledBean(DisabledBean disabledBean) {
        this.disabledBean = disabledBean;
    }

    @Override
    public TipsBean getTipsBean() {
        return tipsBean;
    }

    public void setTipsBean(TipsBean tipsBean) {
        this.tipsBean = tipsBean;
    }

    @Override
    public LabelBean getLabelBean() {
        return labelBean;
    }

    public void setLabelBean(LabelBean labelBean) {
        this.labelBean = labelBean;
    }

    public CustomRefBean getRefBean() {
        return refBean;
    }

    public void setRefBean(CustomRefBean refBean) {
        this.refBean = refBean;
    }


    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    @Override
    public String getProjectVersionName() {
        return projectVersionName;
    }

    public void setProjectVersionName(String projectVersionName) {
        this.projectVersionName = projectVersionName;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    @Override
    public M getWidgetConfig() {
        if (widgetConfig == null) {
            this.initWidget();
        }
        return widgetConfig;
    }

    public void setWidgetConfig(M widgetConfig) {
        this.widgetConfig = widgetConfig;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public void setSimpleClassName(String simpleClassName) {
        this.simpleClassName = simpleClassName;
    }

    @Override
    public boolean isSplit() {
        return split;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }


    public RefType getRefType() {
        return refType;
    }


    public void setBindTypes(ComponentType[] bindTypes) {
        this.bindTypes = bindTypes;
    }


    public ESDClass getEsdClass() {
        return esdClass;
    }

    public void setEsdClass(ESDClass esdClass) {
        this.esdClass = esdClass;
    }


    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }


    @Override
    public boolean isSerialize() {
        return serialize;
    }

    public void setSerialize(boolean serialize) {
        this.serialize = serialize;
    }


    @Override
    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public ESDClass getESDClass() {
        return this.esdClass;
    }

    @Override
    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String getDesc() {
        return desc;
    }


    @Override
    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }


    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCaption() {
        return caption;
    }


    public void setRefType(RefType refType) {
        this.refType = refType;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    public CustomUIBean getUiBean() {
        return uiBean;
    }

    public void setUiBean(CustomUIBean uiBean) {
        this.uiBean = uiBean;
    }

    @Override
    public Set<String> getEnums() {
        return enums;
    }

    public void setEnums(Set<String> enums) {
        this.enums = enums;
    }

    @Override
    public ComponentType getComponentType() {

        return componentType;
    }

    @Override
    public ContainerBean getContainerBean() {
        return containerBean;
    }

    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public boolean isUid() {
        return uid;
    }

    @Override
    public boolean isCaption() {
        return isCaption;
    }

    public void setCaption(boolean caption) {
        isCaption = caption;
    }


    @Override
    public boolean isPid() {
        return pid;
    }

    public void setUid(boolean uid) {
        this.uid = uid;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }


    public void setPid(boolean pid) {
        this.pid = pid;
    }

    @Override
    public String getImageClass() {
        return imageClass;
    }


    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    @Override
    public boolean isDisabled() {
        return disabled;
    }

    @Override
    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    @Override
    public CustomFieldBean getCustomBean() {
        return customBean;
    }

    public void setCustomBean(CustomFieldBean customBean) {
        this.customBean = customBean;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public SearchFieldBean getSearchFieldBean() {
        return searchFieldBean;
    }

    public void setSearchFieldBean(SearchFieldBean searchFieldBean) {
        this.searchFieldBean = searchFieldBean;
    }

    public FieldBean getFieldBean() {
        return fieldBean;
    }

    public void setFieldBean(FieldBean fieldBean) {
        this.fieldBean = fieldBean;
    }

    public GridColItemBean getGridColItemBean() {
        return gridColItemBean;
    }

    public void setGridColItemBean(GridColItemBean gridColItemBean) {
        this.gridColItemBean = gridColItemBean;
    }

    @Override
    public N getComboConfig() {
        if (comboConfig == null) {
            this.initCombo(null);
        }
        return comboConfig;
    }

    public void setComboConfig(N comboConfig) {
        this.comboConfig = comboConfig;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof BaseFieldInfo) {
            BaseFieldInfo baseFieldInfo = (BaseFieldInfo) obj;
            if (this.getFieldName() != null && baseFieldInfo.getFieldName() != null) {
                return this.getFieldName().equals(baseFieldInfo.getFieldName());
            }
        }
        return super.equals(obj);
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
