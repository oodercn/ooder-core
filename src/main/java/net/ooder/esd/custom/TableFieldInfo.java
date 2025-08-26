package net.ooder.esd.custom;

import net.ooder.common.database.metadata.ColInfo;
import net.ooder.common.database.util.TypeMapping;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.annotation.ContainerAnnotation;
import net.ooder.esd.annotation.Label;
import net.ooder.esd.annotation.Tips;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.bean.field.SearchFieldBean;
import net.ooder.esd.bean.field.LabelBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.bean.grid.GridColItemBean;
import net.ooder.esd.manager.editor.CustomMenuAction;
import net.ooder.esd.tool.XUITypeMapping;
import net.ooder.annotation.Disabled;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Type;

public class TableFieldInfo<M extends WidgetBean, N extends ComboBoxBean> implements ESDField<M, N> {


    ComboInputFieldBean comboBean;
    // ESDClass esdClass;
    String id;
    String name;
    String expression;
    ComboInputType type;
    String width;
    String height;
    Object value;
    String desc;
    String target;

    boolean dynLoad = false;

    String[] enums;

    Class<? extends Enum>  enumClass;

    String imageClsss;

    boolean serialize;
    boolean uid;
    boolean pid;
    boolean readonly;
    boolean isCaption = false;
    String projectVersionName;
    Class returnType;
    boolean hidden = false;
    Integer index = 1;
    Integer colSpan = 1;
    String caption;
    GridColItemBean gridColItemBean;
    FieldBean fieldBean = new FieldBean();
    ;
    CustomFieldBean customBean = new CustomFieldBean();
    M widgetConfig;
    N comboConfig;

    CustomLayoutItemBean layoutItemBean;
    DisabledBean disabledBean = AnnotationUtil.fillDefaultValue(Disabled.class, new DisabledBean());
    TipsBean tipsBean = AnnotationUtil.fillDefaultValue(Tips.class, new TipsBean());
    LabelBean labelBean = AnnotationUtil.fillDefaultValue(Label.class, new LabelBean());
    DockBean dockBean = new DockBean();
    ContainerBean containerBean = AnnotationUtil.fillDefaultValue(ContainerAnnotation.class, new ContainerBean());
    Class serviceClass = CustomMenuAction.class;


    public TableFieldInfo(ColInfo dbcol, int index, String projectVersionName) {
        this.index = index;
        this.projectVersionName = projectVersionName;
        this.name = StringUtility.formatJavaName(dbcol.getName().toLowerCase(), false);
        this.id = name;

        if (dbcol.getPk()) {
            uid = true;
        }


        desc = dbcol.getCnname().replaceAll("\n", "");
        if (desc.length() > 12) {
            desc = desc.substring(0, 12);
        }

        this.enums = dbcol.getEnums();
        this.enumClass = dbcol.getEnumClass();
        ComponentType componentType = XUITypeMapping.getComponentType(dbcol);
        try {
            this.returnType = Class.forName(TypeMapping.getMappedType(dbcol));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        if (hidden || uid) {
            fieldBean.setComponentType(ComponentType.HIDDENINPUT);
        } else {
            this.type = XUITypeMapping.getType(dbcol);
            fieldBean.setRequired(!dbcol.isCanNull());
            this.caption = desc;
            this.height = "30";
            fieldBean.setColSpan(1);
            fieldBean.setHaslabel(true);
            this.serialize = true;
            this.pid = false;
            this.readonly = false;
            this.isCaption = false;
            if (componentType.equals(ComponentType.RICHEDITOR) || componentType.equals(ComponentType.MULTILINES) || dbcol.getLength() > 200) {
                fieldBean.setManualHeight(200);
                colSpan = -1;
                type = ComboInputType.auto;
            }
            if (type == null) {
                type = ComboInputType.auto;
            }
        }
        if (uid) {
            hidden = true;
        }
        boolean haslabel = true;
        if (componentType.equals(ComponentType.CODEEDITOR)
                || componentType.equals(ComponentType.JAVAEDITOR)
                || componentType.equals(ComponentType.MODULE)
                || componentType.equals(ComponentType.MODLUEPLACEHOLDER)
                ) {
            colSpan = -1;
            haslabel = false;
        } else if (
                componentType.equals(ComponentType.RICHEDITOR)
                        || componentType.equals(ComponentType.MULTILINES)
                        || componentType.equals(ComponentType.RADIOBOX)) {
            colSpan = -1;
        }

        if (componentType.equals(ComponentType.COMBOINPUT)) {
            this.comboBean = new ComboInputFieldBean<>();
            comboBean.setInputType(type);
        }

        fieldBean.setHaslabel(haslabel);
        fieldBean.setColSpan(colSpan);
        fieldBean.setComponentType(componentType);
    }

    @Override
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public CustomLayoutItemBean getLayoutItemBean() {
        return layoutItemBean;
    }

    public void setLayoutItemBean(CustomLayoutItemBean layoutItemBean) {
        this.layoutItemBean = layoutItemBean;
    }

    public ComboInputFieldBean getComboBean() {
        return comboBean;
    }

    public void setComboBean(ComboInputFieldBean comboBean) {
        this.comboBean = comboBean;
    }

    public void setWidgetConfig(M widgetConfig) {
        this.widgetConfig = widgetConfig;
    }

    public Class<? extends Enum>  getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public GridColItemBean getGridColItemBean() {
        return gridColItemBean;
    }

    @Override
    public RightContextMenuBean getContextMenuBean() {
        return null;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public void setComboConfig(N comboConfig) {
        this.comboConfig = comboConfig;
    }

    @Override
    public FieldBean getFieldBean() {
        return fieldBean;
    }

    @Override
    public CustomFieldBean getCustomBean() {
        return customBean;
    }

    @Override
    public ComponentType getComponentType() {
        return fieldBean.getComponentType();
    }

    public void setGridColItemBean(GridColItemBean gridColItemBean) {
        this.gridColItemBean = gridColItemBean;
    }

    @Override
    public ESDClass getESDClass() {
        return null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getFieldName() {
        return name;
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
    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }


    public ComboInputType getType() {
        return type;
    }

    public void setType(ComboInputType type) {
        this.type = type;
    }

    @Override
    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
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

    public void setDesc(String desc) {
        this.desc = desc;
    }


    @Override
    public String getImageClass() {
        return null;
    }


    public void setSerialize(boolean serialize) {
        this.serialize = serialize;
    }


    @Override
    public boolean isUid() {
        return uid;
    }

    public void setUid(boolean uid) {
        this.uid = uid;
    }

    @Override
    public boolean isPid() {
        return pid;
    }

    public void setPid(boolean pid) {
        this.pid = pid;
    }

    @Override
    public boolean isReadonly() {
        return readonly;
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public boolean isSerialize() {
        return fieldBean.getSerialize();
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }


    @Override
    public boolean isCaption() {
        return isCaption;
    }

    public void setCaption(boolean caption) {
        isCaption = caption;
    }

    @Override
    public String getProjectVersionName() {
        return projectVersionName;
    }

    @Override
    public String getDomainId() {
        return null;
    }

    public void setProjectVersionName(String projectVersionName) {
        this.projectVersionName = projectVersionName;
    }

    @Override
    public Class getReturnType() {
        return returnType;
    }

    @Override
    public Type getGenericType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public CustomRefBean getRefBean() {
        return null;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


    @Override
    public M getWidgetConfig() {
        return widgetConfig;
    }

    @Override
    public N getComboConfig() {
        return null;
    }

    public String[] getEnums() {
        return enums;
    }

    public void setEnums(String[] enums) {
        this.enums = enums;
    }

    public boolean isDynLoad() {
        return dynLoad;
    }

    public void setDynLoad(boolean dynLoad) {
        this.dynLoad = dynLoad;
    }

    public String getImageClsss() {
        return imageClsss;
    }

    public void setImageClsss(String imageClsss) {
        this.imageClsss = imageClsss;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setFieldBean(FieldBean fieldBean) {
        this.fieldBean = fieldBean;
    }

    public void setCustomBean(CustomFieldBean customBean) {
        this.customBean = customBean;
    }

    @Override
    public ContainerBean getContainerBean() {
        return containerBean;
    }

    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
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

    @Override
    public SearchFieldBean getSearchFieldBean() {
        return new SearchFieldBean();
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

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    @Override
    public DockBean getDockBean() {
        return dockBean;
    }

    public void setDockBean(DockBean dockBean) {
        this.dockBean = dockBean;
    }

    @Override
    public boolean isSplit() {
        return false;
    }


}
