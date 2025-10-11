package net.ooder.esd.dsm.view.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.RightContextMenu;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.field.combo.ComboListBoxFieldBean;
import net.ooder.esd.bean.field.combo.ComboxFieldBean;
import net.ooder.esd.bean.grid.GridColItemBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.BaseFieldInfo;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.AggregationManager;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.tool.OODTypeMapping;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;
import net.ooder.esd.tool.properties.Header;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.util.*;

@AnnotationType(clazz = CustomAnnotation.class)
public class FieldGridConfig implements ESDFieldConfig {


    String id;

    String domainId;
    String fieldname;
    String viewClassName;
    String sourceClassName;
    String sourceMethodName;
    //  String serviceClassName;

    String entityClassName;
    String methodName;
    String simpleClassName;
    String className;
    String caption;

    Class<? extends Enum> enumClass;
    CustomFieldBean customBean;
    Boolean serialize;
    Boolean uid;
    Boolean pid;
    CustomRefBean refBean;
    Boolean colHidden;
    Boolean haslable;
    Boolean captionField;
    Boolean readonly;
    GridColItemBean gridColItemBean;
    RightContextMenuBean contextMenuBean;

    Set<GridEventBean> gridEvents = new HashSet<>();

    List<TreeListItem> items = new ArrayList<>();

    @JSONField(serialize = false)
    FieldAggConfig aggConfig;

    public FieldGridConfig() {

    }

    public void update(Header header) {
        this.caption = header.getCaption();
        this.id = header.getId();
        this.colHidden = header.getHidden();
        this.readonly = header.getReadonly();
        this.fieldname = header.getId();


        if (gridColItemBean == null) {
            gridColItemBean = new GridColItemBean();
        }
        gridColItemBean.update(header);

        ComboInputType inputType = header.getType();
        if (inputType.equals(ComboInputType.auto)) {
            inputType = ComboInputType.input;
        } else if (inputType.equals(ComboInputType.password)) {
            colHidden = true;
        }
        if (header.getEnumItem() != null) {
            enumClass = header.getEnumItem().getClass();
        }
        if (this.className == null && simpleClassName == null) {
            Class clazz = null;
            if (header.getEnumItem() != null) {
                clazz = header.getEnumItem().getClass();
            } else if (header.getEnumClass() != null) {
                clazz = header.getEnumClass();
            } else {
                clazz = OODTypeMapping.genType(inputType);
            }
            simpleClassName = clazz.getSimpleName();
            this.className = clazz.getName();
        }


        if (header.getEditorListItems() != null) {
            for (TreeListItem listItem : header.getEditorListItems()) {
                items.add(listItem);
            }
        }
    }

    public FieldGridConfig(Header header) {
        this.update(header);

    }


    public FieldGridConfig(FieldAggConfig aggConfig, String sourceClassName, String sourceMethodName) {
        this.sourceClassName = sourceClassName;
        this.sourceMethodName = sourceMethodName;
        this.simpleClassName = aggConfig.getSimpleClassName();
        if (aggConfig.getEsdField() != null) {
            init(aggConfig.getEsdField());
        }
        this.init(aggConfig);

    }

    void init(ESDField colInfo) {
        Class returnType = colInfo.getReturnType();
        this.viewClassName = colInfo.getESDClass().getClassName();
        contextMenuBean = colInfo.getContextMenuBean();
        this.customBean = colInfo.getCustomBean();
        this.serialize = colInfo.isSerialize();
        this.pid = colInfo.isPid();
        this.uid = colInfo.isUid();
        this.refBean = colInfo.getRefBean();
        this.fieldname = colInfo.getId();
        this.captionField = colInfo.isCaption();
        this.id = fieldname;
        this.gridColItemBean = colInfo.getGridColItemBean();
        if (gridColItemBean == null) {
            this.gridColItemBean = new GridColItemBean(colInfo);
        }

        ComboInputType inputType = gridColItemBean.getInputType();
        if (inputType == null) {
            inputType = ComboInputType.input;
        }

        if (inputType.equals(ComboInputType.password)) {
            colHidden = true;
        }
        if (colInfo.getComponentType().equals(ComponentType.COMBOINPUT)) {
            if (inputType.equals(ComboInputType.combobox)) {
                ComboxFieldBean comboListBoxFieldBean = (ComboxFieldBean) colInfo.getComboConfig();
                items = comboListBoxFieldBean.getListBean().getCustomListBean().getItems();
            } else if (inputType.equals(ComboInputType.listbox)) {
                ComboListBoxFieldBean comboListBoxFieldBean = (ComboListBoxFieldBean) colInfo.getComboConfig();
                items = comboListBoxFieldBean.getListBean().getCustomListBean().getItems();
            } else if (returnType.isEnum()) {
                items = ESDEnumsUtil.getItems(returnType, null);
            }
        }


        this.caption = colInfo.getCaption();
        this.colHidden = colInfo.isHidden();
        this.domainId = colInfo.getDomainId();
        if (colInfo.getGenericType() != null) {
            this.simpleClassName = AnnotationUtil.toType(colInfo.getGenericType()).toString();
        } else {
            this.simpleClassName = AnnotationUtil.toType(colInfo.getReturnType()).toString();
        }
        if (colInfo.getReturnType().isEnum()) {
            enumClass = colInfo.getReturnType();
        }

        if (colInfo instanceof BaseFieldInfo) {
            BaseFieldInfo baseFieldInfo = (BaseFieldInfo) colInfo;
            this.gridEvents = baseFieldInfo.getGridEvents();
            for (GridEventBean eventBean : gridEvents) {
                List<Action> actions = eventBean.getActions();
                for (Action action : actions) {
                    Condition condition = new Condition("{args[1].id}", SymbolType.equal, this.getFieldname());
                    action.addCondition(condition);
                }
            }
        }

    }

    void init(FieldAggConfig aggConfig) {
        this.methodName = aggConfig.getMethodName();
        this.domainId = aggConfig.getDomainId();
        this.fieldname = aggConfig.getFieldname();
        this.serialize = aggConfig.getSerialize();
        this.colHidden = aggConfig.getColHidden();
        this.pid = aggConfig.getPid();
        this.uid = aggConfig.getUid();
        this.refBean = aggConfig.getRefBean();
        this.fieldname = aggConfig.getId();
        this.captionField = aggConfig.getCaptionField();
        this.id = aggConfig.getId();
        this.readonly = aggConfig.getReadonly();
        this.caption = aggConfig.getCaption();
        this.domainId = aggConfig.getDomainId();

        if (this.simpleClassName == null || this.simpleClassName.equals("")) {
            simpleClassName = aggConfig.getSimpleClassName();
        }
    }


    public FieldGridConfig(ESDField colInfo, String sourceClassName, String sourceMethodName) {
        this.sourceClassName = sourceClassName;
        this.sourceMethodName = sourceMethodName;
        init(colInfo);
        this.init(this.getAggConfig());


    }

    public String getSourceMethodName() {
        return sourceMethodName;
    }

    public void setSourceMethodName(String sourceMethodName) {
        this.sourceMethodName = sourceMethodName;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans = new ArrayList<>();
        FieldAggConfig aggConfig = this.getAggConfig();
        if (aggConfig != null) {
            if (colHidden != null && colHidden) {
                aggConfig.setColHidden(colHidden);
            }
            annotationBeans.addAll(aggConfig.getAnnotationBeans());
        } else {
            annotationBeans.add(this);
            if (refBean != null) {
                annotationBeans.add(refBean);
            }
        }

        if (gridColItemBean != null && !AnnotationUtil.getAnnotationMap(gridColItemBean).isEmpty()) {
            annotationBeans.add(gridColItemBean);
        }

        if (contextMenuBean != null && !AnnotationUtil.getAnnotationMap(contextMenuBean).isEmpty()) {
            annotationBeans.add(contextMenuBean);
        }


        annotationBeans.addAll(this.getGridEvents());

        return annotationBeans;
    }


    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();

        if (this.getEnumClass() != null) {
            classSet.add(this.getEnumClass());
        }

        if (items != null) {
            for (TreeListItem item : items) {
                if (item != null && item.getEnumItem() != null) {
                    classSet.add(item.getEnumItem().getClass());
                }
            }
        }

        if (this.getContextMenuBean() != null) {
            for (Class clazz : getContextMenuBean().getMenuClass()) {
                if (clazz != null && !clazz.equals(Void.class) && !clazz.equals(Enum.class)) {
                    classSet.add(clazz);
                }

            }
        }

        return classSet;
    }


    public Boolean getReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public List<TreeListItem> getItems() {
        return items;
    }

    public void setItems(List<TreeListItem> items) {
        this.items = items;
    }

    @JSONField(serialize = false)
    public ESDField getEsdField() {
        ESDField esdField = null;
        try {
            if (getViewClassName() != null) {
                esdField = BuildFactory.getInstance().getClassManager().getAggEntityByName(getViewClassName(), false).getField(fieldname);
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }
        return esdField;
    }

    public GridColItemBean getGridColItemBean() {
        if (gridColItemBean == null) {
            if (getEsdField() != null && getEsdField().getGridColItemBean() != null) {
                this.gridColItemBean = getEsdField().getGridColItemBean();
            } else {
                gridColItemBean = new GridColItemBean(this.getEsdField());
            }
        }
        return gridColItemBean;
    }


    @Override
    public String getViewClassName() {
        if (viewClassName == null && this.getSourceMethodConfig() != null) {
            viewClassName = this.getSourceMethodConfig().getViewClassName();
        }
        if (viewClassName == null && entityClassName != null) {
            viewClassName = entityClassName;
        }
        return viewClassName;
    }

    public Set<GridEventBean> getGridEvents() {
        return gridEvents;
    }

    public void setGridEvents(Set<GridEventBean> gridEvents) {
        this.gridEvents = gridEvents;
    }

    @Override
    public CustomFieldBean getCustomBean() {
        return customBean;
    }

    @Override
    public void setCustomBean(CustomFieldBean customBean) {
        this.customBean = customBean;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setAggConfig(FieldAggConfig aggConfig) {
        this.aggConfig = aggConfig;
    }

    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }

    public RightContextMenuBean genContextMenuBean() {
        if (contextMenuBean == null) {
            contextMenuBean = new RightContextMenuBean(fieldname);
            AnnotationUtil.fillDefaultValue(RightContextMenu.class, contextMenuBean);
        }
        return contextMenuBean;
    }

    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }

    public void setGridColItemBean(GridColItemBean gridColItemBean) {
        this.gridColItemBean = gridColItemBean;
    }


    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }


    @JSONField(serialize = false)
    public FieldAggConfig getAggConfig() {
        if (aggConfig == null) {
            try {
                AggregationManager aggregationManager = DSMFactory.getInstance().getAggregationManager();
                if (entityClassName == null) {
                    if (sourceMethodName != null && !sourceMethodName.equals("") && getMethodConfig() != null) {
                        String topSourceClsss = getMethodConfig().getEsdClass().getTopSourceClass().getClassName();
                        AggEntityConfig sourceEntityConfig = aggregationManager.getAggEntityConfig(topSourceClsss, false);
                        if (sourceEntityConfig != null && sourceEntityConfig.getMethodByName(sourceMethodName) != null) {
                            MethodConfig entityMethod = sourceEntityConfig.getMethodByName(sourceMethodName);
                            AggEntityConfig aggEntityConfig = entityMethod.getAggEntityConfig();
                            this.entityClassName = aggEntityConfig.getCurrClassName();
                        }
                    } else {
                        entityClassName = this.getViewClassName();
                    }
                }

                AggEntityConfig aggEntityConfig = aggregationManager.getAggEntityConfig(entityClassName, false);
                if (entityClassName != null && aggEntityConfig != null) {
                    aggConfig = aggEntityConfig.getFieldByName(this.fieldname);
                }


                if (aggConfig == null) {
                    AggEntityConfig viewEntityConfig = aggregationManager.getAggEntityConfig(getViewClassName(), false);
                    if (viewEntityConfig != null) {
                        aggConfig = viewEntityConfig.getFieldByName(this.fieldname);
                    }
                }

                if (aggConfig == null && this.getEsdField() != null) {
                    aggConfig = new FieldAggConfig(this.getEsdField(), domainId);
                    DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(getViewClassName(), false).getAllFieldMap().put(fieldname, aggConfig);
                }

            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return aggConfig;
    }


    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    @Override
    public String getSourceClassName() {
        return sourceClassName;
    }


    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public void setSimpleClassName(String simpleClassName) {
        this.simpleClassName = simpleClassName;
    }

    public Class<? extends Enum> getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(Class<? extends Enum> enumClass) {
        this.enumClass = enumClass;
    }

    @JSONField(serialize = false)
    public MethodConfig getMethodConfig() {
        MethodConfig methodConfig = null;
        try {
            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.getViewClassName());
            methodConfig = apiClassConfig.getMethodByName(methodName);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return methodConfig;
    }

    @JSONField(serialize = false)
    public MethodConfig getSourceMethodConfig() {
        MethodConfig methodConfig = null;
        try {
            if (sourceMethodName != null) {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceClassName);
                methodConfig = apiClassConfig.getMethodByName(sourceMethodName);
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }
        return methodConfig;
    }

    @Override
    public Boolean getSerialize() {
        return serialize;
    }

    public void setSerialize(Boolean serialize) {
        this.serialize = serialize;
    }

    @Override
    public Boolean getUid() {
        return uid;
    }

    public void setUid(Boolean uid) {
        this.uid = uid;
    }

    @Override
    public Boolean getPid() {
        return pid;
    }

    public void setPid(Boolean pid) {
        this.pid = pid;
    }

    @Override
    public Boolean getColHidden() {
        if (colHidden == null) {
            colHidden = false;
        }
        return colHidden;
    }

    public void setColHidden(Boolean colHidden) {
        this.colHidden = colHidden;
    }

    public Boolean getHaslable() {
        return haslable;
    }

    public void setHaslable(Boolean haslable) {
        this.haslable = haslable;
    }

    @Override
    public Boolean getCaptionField() {
        return captionField;
    }

    public void setCaptionField(Boolean captionField) {
        this.captionField = captionField;
    }

    public String toAnnotationStr() {
        CustomFieldBean fieldBean = new CustomFieldBean(fieldname);
        fieldBean.setHidden(this.getColHidden());
        Map valueMap = JSON.parseObject(JSON.toJSONString(this), Map.class);
        OgnlUtil.setProperties(valueMap, fieldBean, false, false);
        return AnnotationUtil.toAnnotationStr(fieldBean);
    }

    @Override
    public Class<? extends ESDFieldConfig> getClazz() {
        return this.getClass();
    }

    @Override
    public CustomRefBean getRefBean() {
        return refBean;
    }

    public void setRefBean(CustomRefBean refBean) {
        this.refBean = refBean;
    }

}
