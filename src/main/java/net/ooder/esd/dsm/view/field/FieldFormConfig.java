package net.ooder.esd.dsm.view.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.ColType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.ClassUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.FieldAnnotation;
import net.ooder.esd.annotation.RightContextMenu;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.field.*;
import net.ooder.esd.bean.field.base.InputFieldBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.bean.field.combo.CustomModuleRefFieldBean;
import net.ooder.esd.bean.field.combo.SimpleComboBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.custom.*;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.FieldAggConfig;
import net.ooder.esd.dsm.aggregation.context.MethodRoot;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.OODTypeMapping;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.esd.util.OODUtil;
import net.ooder.esd.util.json.FormFieldComboDeserializer;
import net.ooder.esd.util.json.FormFieldDeserializer;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.ConstructorBean;
import net.ooder.web.util.AnnotationUtil;
import net.ooder.web.util.MethodUtil;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.*;

@AnnotationType(clazz = CustomAnnotation.class)
public class FieldFormConfig<M extends FieldComponentBean, N extends ComboBoxBean> implements ESDFieldConfig<M, N> {
    protected Log log = LogFactory.getLog(JDSConstants.CONFIG_KEY, FieldFormConfig.class);

    String id;

    String fieldname;

    String viewClassName;

    String sourceClassName;

    String entityClassName;

    String serviceClassName;

    String methodName;

    String sourceMethodName;

    String domainId;

    ComponentType componentType;

    FieldBean fieldBean;

    CustomFieldBean customBean;

    TipsBean tipsBean;

    LabelBean labelBean;

    ContainerBean containerBean;

    RightContextMenuBean contextMenuBean;

    SearchFieldBean searchFieldBean;

    @JSONField(serialize = false)
    FieldAggConfig aggConfig;


    @JSONField(deserializeUsing = FormFieldComboDeserializer.class)
    N comboConfig;

    @JSONField(deserializeUsing = FormFieldDeserializer.class)
    M widgetConfig;

    @JSONField(serialize = false)
    Component component;

    @JSONField(serialize = false)
    ESDField esdField;

    DBFieldBean dbFieldBean;


    public FieldFormConfig() {


    }

    public FieldFormConfig(Component component) {
        this.init(component);
    }


    public FieldFormConfig(ESDField info, String sourceClassName, String sourceMethodName) {
        this.sourceClassName = sourceClassName;
        this.sourceMethodName = sourceMethodName;
        this.fieldname = info.getFieldName();
        this.init(info);
        this.init(this.getAggConfig());

    }

    public FieldFormConfig(FieldAggConfig aggConfig, String sourceClassName, String sourceMethodName) {
        this.sourceClassName = sourceClassName;
        this.sourceMethodName = sourceMethodName;
        this.fieldname = aggConfig.getFieldname();

        if (aggConfig.getEsdField() != null) {
            this.init(aggConfig.getEsdField());
        }
        this.init(aggConfig);
    }

    void init(Component component) {
        String comName = component.getProperties().getName();
        if (comName == null || comName.equals("")) {
            comName = component.getAlias();
        }
        this.fieldname = OODUtil.formatJavaName(comName, false);
        this.id = fieldname;
        this.methodName = fieldname;
        this.componentType = ComponentType.fromType(component.getKey());
        if (customBean == null) {
            customBean = new CustomFieldBean(component);
        } else {
            customBean.update(component);
        }

        if (fieldBean == null) {
            fieldBean = new FieldBean(component);
        } else {
            fieldBean.update(component);
        }

        if (tipsBean == null) {
            tipsBean = new TipsBean(component);
        } else {
            tipsBean.update(component);
        }


        if (labelBean == null) {
            labelBean = new LabelBean(component.getProperties());
        } else {
            labelBean.update(component);
        }
        if (containerBean == null) {
            containerBean = new ContainerBean(component);
        } else {
            containerBean.update(component);
        }

        if (aggConfig == null) {
            aggConfig = this.getInnerFieldAgg();
        }
        Map valueMap = JSON.parseObject(JSON.toJSONString(component.getProperties()), Map.class);
        OgnlUtil.setProperties(valueMap, aggConfig, false, false);
    }


    void init(ESDField<M, N> info) {
        this.viewClassName = info.getESDClass().getClassName();
        this.componentType = info.getComponentType();


        if (contextMenuBean == null) {
            contextMenuBean = info.getContextMenuBean();
        }
        if (this.customBean == null) {
            customBean = info.getCustomBean();
        }
        if (customBean == null) {
            customBean = new CustomFieldBean(info.getFieldName());
        }

        if (info.getTarget() != null) {
            customBean.setTarget(info.getTarget());
        }


        if (this.fieldBean == null) {
            this.fieldBean = info.getFieldBean();
            if (componentType == null && !fieldBean.getComponentType().equals(ComponentType.INPUT)) {
                this.componentType = fieldBean.getComponentType();
            }
        }
        if (this.tipsBean == null) {
            this.tipsBean = info.getTipsBean();
        }
        if (this.labelBean == null) {
            labelBean = info.getLabelBean();
        }
        if (this.containerBean == null) {
            this.containerBean = info.getContainerBean();
        }

        if (containerBean.getDisabledBean() == null) {
            containerBean.setDisabledBean(info.getDisabledBean());
        }
        if (containerBean.getDockBean() == null) {
            containerBean.setDockBean(info.getDockBean());
        }


    }


    void init(FieldAggConfig aggConfig) {
        this.domainId = aggConfig.getDomainId();
        this.fieldname = aggConfig.getFieldname();
        this.methodName = aggConfig.getMethodName();
        this.componentType = aggConfig.getComponentType();
        if (this.id == null) {
            this.id = aggConfig.getId();
        }
        M widget = getWidgetConfig();
        if (widget != null && (widget instanceof ComboBoxBean)) {
            componentType = ComponentType.COMBOINPUT;
        } else if (aggConfig.getColHidden() != null && aggConfig.getColHidden()) {
            componentType = ComponentType.HIDDENINPUT;
        }
    }


    public void update(ModuleComponent parentModuleComponent, Component component) {
        this.component = component;
        this.init(component);
        CustomViewBean viewConfig = null;
        if (this.widgetConfig == null || !widgetConfig.getComponentType().equals(componentType)) {
            this.widgetConfig = this.createDefaultWidget(componentType, parentModuleComponent, component);
        }
        if (widgetConfig != null) {
            if (widgetConfig instanceof WidgetBean) {
                WidgetBean widgetBean = (WidgetBean) widgetConfig;
                viewConfig = widgetBean.getViewBean();
                if (viewConfig != null) {
                    this.setServiceClassName(viewConfig.getSourceClassName());
                    this.setMethodName(viewConfig.getSourceMethodName());
                }

            }
            if (componentType.equals(ComponentType.COMBOINPUT)) {
                ComboInputProperties comboInputProperties = (ComboInputProperties) component.getProperties();
                if (comboConfig == null) {
                    this.comboConfig = this.createDefaultCombo(comboInputProperties.getType(), component);
                }
            }
            widgetConfig.update(parentModuleComponent, component);
        }
    }

    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classSet = new HashSet<>();
        try {
            M widget = getWidgetConfig();
            if (widget != null && widget instanceof CustomModuleRefFieldBean) {
                CustomModuleRefFieldBean fieldBean = (CustomModuleRefFieldBean) widget;
                ESDField esdField = this.getEsdField();
                if (esdField != null) {
                    if (esdField.getReturnType() != null) {
                        classSet.add(esdField.getReturnType());
                    }
                    if (esdField.getEnumClass() != null) {
                        classSet.add(esdField.getEnumClass());
                    }
                }
                if (fieldBean.getBindClass() != null && !fieldBean.getBindClass().equals(Void.class) && !fieldBean.getBindClass().equals(Enum.class)) {
                    ApiClassConfig apiClassConfig = null;
                    try {
                        apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(fieldBean.getBindClass().getName());
                        MethodConfig methodConfig = apiClassConfig.getMethodByEvent(CustomFieldEvent.POPEDITOR);
                        if (methodConfig == null) {
                            methodConfig = apiClassConfig.getMethodByItem(CustomMenuItem.INDEX);
                        }
                        if (methodConfig != null) {
                            classSet.add(methodConfig.getViewClass().getCtClass());
                            classSet.addAll(methodConfig.getView().getOtherClass());
                        }
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }
                    classSet.add(fieldBean.getBindClass());
                }

            }
            if (this.getContextMenuBean() != null) {
                for (Class clazz : getContextMenuBean().getMenuClass()) {
                    if (clazz != null && !clazz.equals(Void.class) && !clazz.equals(Enum.class)) {
                        classSet.add(clazz);
                    }

                }
            }
            if (fieldBean != null && fieldBean.getServiceClass() != null) {
                classSet.add(fieldBean.getServiceClass());

            }

            if (customBean != null && customBean.getEnumClass() != null) {
                classSet.add(customBean.getEnumClass());
            }


            if (this.getContextMenuBean() != null && getContextMenuBean().getMenuClass() != null) {
                ComboBoxBean comboConfig = this.getComboConfig();
                classSet.addAll(comboConfig.getOtherClass());
            }

            if (widget != null) {
                if (widget instanceof ComboBoxBean) {
                    ComboBoxBean comboBoxBean = this.getComboConfig();
                    if (comboBoxBean != null) {
                        classSet.addAll(comboBoxBean.getOtherClass());
                    } else {
                        classSet.addAll(widget.getOtherClass());
                    }
                } else {
                    classSet.addAll(widget.getOtherClass());
                }
            }


            if (this.getMethodRoot() != null && this.getMethodRoot().getModuleBean().getBindService() != null) {
                classSet.add(this.getMethodRoot().getModuleBean().getBindService());
            }

        } catch (Throwable e) {

        }

        return ClassUtility.checkBase(classSet);
    }


    @JSONField(serialize = false)
    public List<CustomBean> getDbAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.add(customBean);
        annotationBeans.add(getDBFieldBean());
        annotationBeans.add(fieldBean);
        return annotationBeans;
    }

    @JSONField(serialize = false)
    public DBFieldBean getDBFieldBean() {
        if (dbFieldBean == null) {
            dbFieldBean = new DBFieldBean();
            dbFieldBean.setDbFieldName(this.getFieldname().toLowerCase());
            ColType colType = ColType.VARCHAR2;
            if (this.componentType.equals(ComponentType.COMBOINPUT)) {
                colType = OODTypeMapping.guessInputDBType(this.getComboConfig().getInputType());
            } else {
                colType = OODTypeMapping.guessComponentDBType(this.getComponentType());
            }
            dbFieldBean.setDbType(colType);
            if (this.getFieldBean().getRequired() != null && this.getFieldBean().getRequired()) {
                dbFieldBean.setNull(true);
            }
            dbFieldBean.setCnName(this.getFieldBean().getCaption());
            if (this.getWidgetConfig() != null && this.getWidgetConfig() instanceof InputFieldBean) {
                InputFieldBean inputFieldBean = (InputFieldBean) this.getWidgetConfig();
                dbFieldBean.setLength(inputFieldBean.getMaxlength());
            }
        }

        return dbFieldBean;

    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        FieldAggConfig aggConfig = this.getInnerFieldAgg();
        if (fieldBean != null) {
            annotationBeans.addAll(fieldBean.getAnnotationBeans());
        }
        if (labelBean != null) {
            annotationBeans.add(labelBean);
        }
        if (tipsBean != null) {
            for (CustomBean customBean : tipsBean.getAnnotationBeans()) {
                if (!annotationBeans.contains(customBean)) {
                    annotationBeans.add(customBean);
                }
            }
        }

        if (containerBean != null) {
            List<CustomBean> customBeans = containerBean.getAnnotationBeans();
            for (CustomBean customBean : customBeans) {
                if (!annotationBeans.contains(customBean)) {
                    annotationBeans.add(customBean);
                }
            }
        }

        if (contextMenuBean != null) {
            annotationBeans.add(contextMenuBean);
        }

        List<CustomBean> checkAnns = new ArrayList<>();
        for (CustomBean customBean : annotationBeans) {
            if (!AnnotationUtil.getAnnotationMap(customBean).isEmpty()) {
                checkAnns.add(customBean);
            }
        }

        if (this.customBean != null) {
            if (aggConfig != null) {
                Map valueMap = JSON.parseObject(JSON.toJSONString(aggConfig), Map.class);
                OgnlUtil.setProperties(valueMap, customBean, false, false);
            }
            checkAnns.add(this.customBean);
        } else if (aggConfig != null) {
            if (customBean != null) {
                aggConfig.setColHidden(customBean.getHidden());
                aggConfig.setTarget(customBean.getTarget());
                if (customBean.getIndex() != null && customBean.getIndex() != 0) {
                    aggConfig.setIndex(customBean.getIndex());
                }
            }
            checkAnns.addAll(aggConfig.getAnnotationBeans());
        } else {
            checkAnns.add(this);
        }

        M widget = getWidgetConfig();
        if (widget != null) {
            ComponentBean widgetBean = getWidgetConfig();
            if (widgetBean instanceof ComboBoxBean) {
                if (widget != null) {
                    List<CustomBean> customBeans = widget.getAnnotationBeans();
                    for (CustomBean customBean : customBeans) {
                        if (customBean instanceof ComboInputFieldBean) {
                            ComboInputType comboInputType = ((ComboInputFieldBean) customBean).getInputType();
                            if (comboInputType != null && comboInputType.equals(ComboInputType.input)) {
                                if (!classEqual(customBean, checkAnns)) {
                                    checkAnns.add(customBean);
                                }
                            }
                        } else {
                            if (!classEqual(customBean, checkAnns)) {
                                checkAnns.add(customBean);
                            }
                        }
                    }
                } else {
                    List<CustomBean> customBeans = widget.getAnnotationBeans();
                    for (CustomBean customBean : customBeans) {
                        if (!classEqual(customBean, checkAnns)) {
                            checkAnns.add(customBean);
                        }
                    }
                }
            } else {
                List<CustomBean> customBeanList = widgetBean.getAnnotationBeans();
                if (widgetBean instanceof WidgetBean) {
                    customBeanList = ((WidgetBean) widgetBean).getFieldAnnotationBeans();
                }
                for (CustomBean customBean : customBeanList) {
                    if (!classEqual(customBean, checkAnns)) {
                        checkAnns.add(customBean);
                    }
                }
            }

            if (comboConfig != null && !comboConfig.getInputType().equals(ComboInputType.input)) {
                List<CustomBean> customBeans = comboConfig.getAnnotationBeans();
                for (CustomBean customBean : customBeans) {
                    if (!classEqual(customBean, checkAnns)) {
                        checkAnns.add(customBean);
                    }
                }
            }
        }
        return checkAnns;
    }

    private boolean classEqual(CustomBean customBean, List<CustomBean> checkAnns) {
        for (CustomBean bean : checkAnns) {
            if (customBean.getClass().equals(bean.getClass())) {
                return true;
            }
        }
        return false;

    }

    ;


    public String getWidgetConfigClass() {
        String widgetConfigClass = null;
        if (this.getWidgetConfig() != null) {
            widgetConfigClass = this.getWidgetConfig().getClass().getName();
        }
        return widgetConfigClass;
    }


    @JSONField(serialize = false)
    public ESDField getEsdField() {
        if (esdField == null) {
            try {
                if (getViewClassName() != null) {
                    ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(getViewClassName(), false);
                    if (esdClass != null) {
                        esdField = esdClass.getField(fieldname);
                        if (esdField == null) {
                            esdField = esdClass.getField(id);
                        }
                    }
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return esdField;
    }

    public String getEntityClassName() {
        if (entityClassName == null) {
            if (sourceMethodName != null
                    && !sourceMethodName.equals("")
                    && getMethodConfig() != null
                    && getMethodConfig().getEsdClass() != null
                    && getMethodConfig().getEsdClass().getTopSourceClass() != null) {

                String topSourceClsss = getMethodConfig().getEsdClass().getTopSourceClass().getClassName();
                try {
                    AggEntityConfig sourceEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(topSourceClsss, false);
                    MethodConfig entityMethod = sourceEntityConfig.getMethodByName(sourceMethodName);
                    if (entityMethod != null && entityMethod.getInnerReturnType() != null) {
                        if (MethodUtil.checkType(entityMethod.getInnerReturnType().getName())) {
                            AggEntityConfig aggEntityConfig = entityMethod.getAggEntityConfig();
                            if (aggEntityConfig != null) {
                                this.entityClassName = aggEntityConfig.getCurrClassName();
                            }

                        } else {
                            this.entityClassName = entityMethod.getInnerReturnType().getName();
                        }
                    } else {
                        entityClassName = this.getViewClassName();
                    }
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            } else {
                entityClassName = this.getViewClassName();
            }
        }
        return entityClassName;
    }

    @JSONField(serialize = false)
    public FieldAggConfig getAggConfig() {
        if (aggConfig == null) {
            aggConfig = getInnerFieldAgg();
        }
        return aggConfig;

    }

    @JSONField(serialize = false)
    private FieldAggConfig getInnerFieldAgg() {
        FieldAggConfig aggInnerConfig = null;
        try {
            String entityClassName = getEntityClassName();
            if (entityClassName != null) {
                AggEntityConfig aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(entityClassName, false);
                if (aggEntityConfig != null) {
                    aggInnerConfig = aggEntityConfig.getFieldByName(fieldname);
                    if (aggInnerConfig == null && getViewClassName() != null) {
                        aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(getViewClassName(), false);
                        if (aggEntityConfig != null) {
                            aggInnerConfig = aggEntityConfig.getFieldByName(fieldname);
                            if (aggInnerConfig == null && this.getEsdField() != null) {
                                aggInnerConfig = new FieldAggConfig(this.getEsdField(), domainId);
                                DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(getViewClassName(), false).getAllFieldMap().put(fieldname, aggInnerConfig);
                            }
                        }
                    }

                    Class clazz = OODTypeMapping.guessResultType(componentType);
                    if (aggInnerConfig == null) {
                        aggInnerConfig = aggEntityConfig.createField(fieldname, clazz, componentType);
                    }
                }
            }

            if (aggInnerConfig == null) {
                aggInnerConfig = new FieldAggConfig(fieldname, OODTypeMapping.guessResultType(componentType), componentType, domainId, entityClassName, 0);
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }


        return aggInnerConfig;

    }


    public N getComboConfig() {
        if (this.comboConfig == null) {
            ESDField esdField = getEsdField();
            if (esdField != null) {
                comboConfig = (N) getEsdField().getComboConfig();
            }
        }
        if (comboConfig != null && comboConfig instanceof SimpleComboBean) {
            SimpleComboBean fieldBean = (SimpleComboBean) comboConfig;
            ESDField esdField = getEsdField();
            if (esdField instanceof BaseFieldInfo) {
                BaseFieldInfo fieldInfo = (BaseFieldInfo) getEsdField();
                fieldInfo.setComponentType(ComponentType.COMBOINPUT);
                fieldInfo.initCombo(fieldBean.getInputType());
                this.comboConfig = (N) fieldInfo.getComboConfig();
            } else if (!(esdField instanceof TableFieldInfo)) {
                this.comboConfig = this.createDefaultCombo(fieldBean.getInputType(), null);
            }
        }

        if (this.comboConfig == null) {
            this.createDefaultCombo(ComboInputType.input, null);
        }
        return comboConfig;
    }

    @JSONField(serialize = false)
    private ConstructorBean getComponentConstructor(Class clazz, Class... sourceClass) {
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            if (constructor.getParameterTypes().length == sourceClass.length) {
                boolean hasClass = false;
                for (int k = 0; k < sourceClass.length; k++) {
                    Class soruceParamterClass = sourceClass[k];
                    Class paramterClass = constructor.getParameterTypes()[k];
                    if (soruceParamterClass.isAssignableFrom(paramterClass)) {
                        hasClass = true;
                    } else {
                        hasClass = false;
                    }
                }
                if (hasClass) {
                    return new ConstructorBean(constructor);
                }
            }
        }
        return null;
    }

    public CustomFieldBean createCustomBean() {
        if (this.customBean == null) {
            customBean = new CustomFieldBean(fieldname);
        }
        return customBean;
    }

    ;

    public FieldBean createFieldBean() {
        if (this.fieldBean == null) {
            fieldBean = new FieldBean();
        }
        return fieldBean;
    }

    public M createDefaultWidget(ComponentType componentType, ModuleComponent moduleComponent, Component component) {
        Properties properties = component.getProperties();
        if (component.getBuildLock() == null || !component.getBuildLock()) {
            component.setBuildLock(true);
            Class clazz = CustomViewConfigFactory.getInstance().getDefaultWidgetClass(componentType);
            MethodConfig methodConfig = this.getMethodConfig();
            try {
                OgnlContext ognlContext = JDSActionContext.getActionContext().getOgnlContext();
                if (clazz != null && ognlContext != null) {
                    if (component != null) {
                        ConstructorBean methodBean = getComponentConstructor(clazz, MethodConfig.class);
                        ConstructorBean componentConstructor = getComponentConstructor(clazz, MethodConfig.class, Component.class);
                        ConstructorBean constructorBean = getComponentConstructor(clazz, ModuleComponent.class, Component.class);
                        ConstructorBean simBean = getComponentConstructor(clazz, Component.class);
                        if (constructorBean != null) {
                            widgetConfig = (M) OgnlRuntime.callConstructor(ognlContext, clazz.getName(), new Object[]{moduleComponent, component});
                        } else if (componentConstructor != null) {
                            widgetConfig = (M) OgnlRuntime.callConstructor(ognlContext, clazz.getName(), new Object[]{methodConfig, component});
                        } else if (methodConfig != null && methodBean != null) {
                            widgetConfig = (M) OgnlRuntime.callConstructor(ognlContext, clazz.getName(), new Object[]{methodConfig});
                        } else if (simBean != null) {
                            widgetConfig = (M) OgnlRuntime.callConstructor(ognlContext, clazz.getName(), new Object[]{component});
                        } else {
                            properties = component.getProperties();
                            widgetConfig = (M) OgnlRuntime.callConstructor(ognlContext, clazz.getName(), new Object[]{properties});
                        }
                    } else {
                        widgetConfig = (M) OgnlRuntime.callConstructor(ognlContext, clazz.getName(), new Object[]{});
                        Class<Annotation> annotationClass = CustomViewConfigFactory.getInstance().getWidgetAnnMap().get(componentType);
                        if (annotationClass != null) {
                            AnnotationUtil.fillDefaultValue(annotationClass, widgetConfig);
                        }
                    }
                } else {
                    widgetConfig = (M) new HiddenInputFieldBean();
                }

                if (widgetConfig instanceof WidgetBean) {
                    WidgetBean widgetViewBean = (WidgetBean) widgetConfig;
                    CustomViewBean viewConfig = widgetViewBean.getViewBean();
                    if (viewConfig != null) {
                        CustomModuleBean moduleBean = viewConfig.getModuleBean();
                        if (moduleBean == null) {
                            moduleBean = new CustomModuleBean(component);
                            moduleBean.reBindMethod(this.getMethodConfig());
                        }
                        if (viewConfig.getBindService() != null) {
                            moduleBean.setBindService(viewConfig.getBindService());
                        }
                        MethodRoot methodRoot = this.getMethodRoot();
                        if (methodRoot != null) {
                            methodRoot.update(moduleBean, component);
                        }
                        this.setMethodName(viewConfig.getSourceMethodName());
                        if (viewConfig.getBindService() != null) {
                            this.setServiceClassName(viewConfig.getBindService().getName());
                        }
                    }
                }
            } catch (OgnlException e) {
                e.printStackTrace();
            }
            component.setBuildLock(false);
        }

        return widgetConfig;
    }

    public N createDefaultCombo(ComboInputType inputType, Component component) {
        N comboBox = null;
        OgnlContext ognlContext = JDSActionContext.getActionContext().getOgnlContext();
        Class clazz = CustomViewConfigFactory.getInstance().getDefaultComboBoxClass(inputType);
        if (clazz != null && ognlContext != null) {
            try {
                if (component != null) {
                    comboBox = (N) OgnlRuntime.callConstructor(ognlContext, clazz.getName(), new Object[]{component});
                } else {
                    comboBox = (N) OgnlRuntime.callConstructor(ognlContext, clazz.getName(), new Object[]{inputType});
                }
            } catch (OgnlException e) {
                e.printStackTrace();
            }
        }
        if (comboBox == null) {
            comboBox = (N) new SimpleComboBean(inputType);
        }
        this.comboConfig = comboBox;
        return comboBox;
    }

    public void setComboConfig(N comboConfig) {
        this.comboConfig = comboConfig;
    }

    @JSONField(serialize = false)
    public MethodConfig getMethodConfig() {
        MethodConfig methodConfig = null;
        try {
            String serviceClassName = this.getServiceClassName();
            if (serviceClassName == null) {
                serviceClassName = this.getViewClassName();
            }
            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(serviceClassName);
            if (apiClassConfig != null) {
                methodConfig = apiClassConfig.getMethodByName(methodName == null ? fieldname : OODUtil.getGetMethodName(methodName));
                if (methodConfig == null) {
                    methodConfig = apiClassConfig.findEditorMethod();
                }
            }

            if (methodConfig == null) {
                AggEntityConfig aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(serviceClassName, false);
                if (aggEntityConfig != null) {
                    methodConfig = aggEntityConfig.getMethodByName(methodName == null ? fieldname : OODUtil.getGetMethodName(methodName));
                }
            }


            if (methodConfig == null) {
                ApiClassConfig sourceApi = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.getViewClassName());
                if (sourceApi != null) {
                    methodConfig = sourceApi.getMethodByName(methodName == null ? fieldname : OODUtil.getGetMethodName(methodName));
                }
            }

            if (methodConfig == null) {
                AggEntityConfig aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(this.getViewClassName(), false);
                if (aggEntityConfig != null) {
                    methodConfig = aggEntityConfig.getMethodByName(methodName == null ? fieldname : OODUtil.getGetMethodName(methodName));
                }
            }


        } catch (JDSException e) {
            log.error(e);
            // e.printStackTrace();
        }

//        if (methodConfig==null && this.getMethodRoot()!=null){
//            methodConfig=new MethodConfig(methodRoot,this.getDomainId(),this.getDomainId());
//        }

        return methodConfig;
    }

    @JSONField(serialize = false)
    public MethodConfig getServiceMethodConfig() {
        MethodConfig methodConfig = null;
        try {

            if (this.getWidgetConfig() instanceof CustomModuleRefFieldBean) {
                CustomModuleRefFieldBean moduleRefFieldBean = (CustomModuleRefFieldBean) this.getWidgetConfig();
                if (moduleRefFieldBean.getBindClass() != null) {
                    serviceClassName = moduleRefFieldBean.getBindClass().getName();
                }
            }

            if (serviceClassName != null && !serviceClassName.equals("")) {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(serviceClassName);
                if (apiClassConfig != null) {
                    methodConfig = apiClassConfig.getMethodByEvent(CustomFieldEvent.POPEDITOR);
                    if (methodConfig == null) {
                        methodConfig = apiClassConfig.getMethodByEvent(CustomTabsEvent.TABEDITOR);
                    }
                    if (methodConfig == null) {
                        methodConfig = apiClassConfig.getMethodByItem(CustomMenuItem.INDEX);
                    }

                }
                if (methodConfig == null) {
                    AggEntityConfig aggEntityConfig = DSMFactory.getInstance().getAggregationManager().getAggEntityConfig(this.getServiceClassName(), false);
                    if (aggEntityConfig != null) {
                        methodConfig = apiClassConfig.getMethodByEvent(CustomFieldEvent.POPEDITOR);
                        if (methodConfig == null) {
                            methodConfig = apiClassConfig.getMethodByEvent(CustomTabsEvent.TABEDITOR);
                        }
                        if (methodConfig == null) {
                            methodConfig = apiClassConfig.getMethodByItem(CustomMenuItem.INDEX);
                        }
                    }
                }
            }
        } catch (JDSException e) {
            log.error(e);
            // e.printStackTrace();
        }
        return methodConfig;
    }

    @JSONField(serialize = false)
    public String getMethodInfo() {
        String metaInfo = this.getAggConfig().getSimpleClassName() + " " + OODUtil.getGetMethodName(this.getAggConfig().getFieldname()) + "()";
        if (getServiceMethodConfig() != null) {
            metaInfo = this.getServiceMethodConfig().getSourceMetaInfo();
        } else if (this.getMethodConfig() != null && this.getMethodConfig().isModule()) {
            metaInfo = this.getMethodConfig().getSourceMetaInfo();
        }
        return metaInfo;
    }

    @JSONField(serialize = false)
    public MethodConfig getSourceMethodConfig() {
        MethodConfig methodConfig = null;
        try {
            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceClassName);
            if (apiClassConfig != null) {
                methodConfig = apiClassConfig.getMethodByName(sourceMethodName);
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }
        return methodConfig;
    }


    public M getWidgetConfig() {
        if (this.widgetConfig == null || !widgetConfig.getComponentType().equals(this.getComponentType())) {
            ESDField esdField = getEsdField();
            if (getEsdField() != null) {
                if (esdField instanceof BaseFieldInfo) {
                    BaseFieldInfo baseFieldInfo = (BaseFieldInfo) getEsdField();
                    baseFieldInfo.setComponentType(this.getComponentType());
                    baseFieldInfo.initWidget();
                    widgetConfig = (M) getEsdField().getWidgetConfig();
                    if (widgetConfig != null && widgetConfig.getComponentType() != null) {
                        this.setComponentType(widgetConfig.getComponentType());
                    }

                } else if (!(esdField instanceof TableFieldInfo)) {
                    widgetConfig = this.createDefaultWidget(componentType, null, component);
                }
            }
        }
        if (widgetConfig != null && this.getMethodConfig() != null && this.getMethodConfig().getView() != null && (widgetConfig instanceof WidgetBean)) {
            ((WidgetBean) widgetConfig).setViewBean(this.getMethodConfig().getView());
        }
        return widgetConfig;
    }


    @Override
    public Boolean getSerialize() {
        if (this.getAggConfig() != null && this.getAggConfig().getSerialize() != null) {
            return this.getAggConfig().getSerialize();
        }
        return true;
    }

    @Override
    public Boolean getColHidden() {
        return false;
    }

    @Override
    public CustomRefBean getRefBean() {
        CustomRefBean refBean = null;
        if (this.getAggConfig() != null) {
            refBean = this.getAggConfig().getRefBean();
        }
        return refBean;
    }

    @Override
    public Boolean getPid() {
        if (this.getAggConfig() != null && this.getAggConfig().getPid() != null) {
            return this.getAggConfig().getPid();
        }
        return false;
    }

    @Override
    public Boolean getUid() {
        if (this.getAggConfig() != null && this.getAggConfig().getUid() != null) {
            return this.getAggConfig().getUid();
        }
        return false;
    }

    @Override
    public Boolean getCaptionField() {
        if (this.getAggConfig() != null && this.getAggConfig().getCaptionField() != null) {
            return this.getAggConfig().getCaptionField();
        }
        return false;
    }

    public String getViewClassName() {
        if (viewClassName == null && entityClassName != null) {
            viewClassName = entityClassName;
        }

        if (viewClassName == null && this.getSourceMethodConfig() != null) {
            viewClassName = this.getSourceMethodConfig().getViewClassName();
        }
        return viewClassName;
    }


    public FieldBean getFieldBean() {
        if (fieldBean == null) {
            if (this.getEsdField() != null) {
                fieldBean = this.getEsdField().getFieldBean();
            } else {
                this.fieldBean = new FieldBean();
                AnnotationUtil.fillDefaultValue(FieldAnnotation.class, fieldBean);
            }
        }
        return fieldBean;
    }

    public MethodRoot getMethodRoot() {
        MethodRoot methodRoot = null;
        if (this.getWidgetConfig() != null) {
            CustomModuleBean moduleBean = null;
            if (this.getWidgetConfig() instanceof WidgetBean) {
                WidgetBean widgetBean = (WidgetBean) this.getWidgetConfig();
                if (widgetBean.getViewBean() != null && widgetBean.getViewBean().getModuleBean() != null) {
                    moduleBean = widgetBean.getViewBean().getModuleBean();
                    moduleBean.reBindMethod(widgetBean.getViewBean().getMethodConfig());
                }
            } else if (this.getWidgetConfig() instanceof CustomModuleRefFieldBean) {
                CustomModuleRefFieldBean refFieldBean = (CustomModuleRefFieldBean) this.getWidgetConfig();
                if (getServiceMethodConfig() != null) {
                    MethodConfig methodConfig = getServiceMethodConfig();
                    if (methodConfig != null) {
                        moduleBean = methodConfig.getModuleBean();
                    }
                } else if (refFieldBean.getModuleBean() != null) {
                    moduleBean = refFieldBean.getModuleBean();
                }
            }
            if (moduleBean != null && moduleBean.getAlias() != null) {
                if (this.getCustomBean() != null) {
                    moduleBean.setIndex(this.getCustomBean().getIndex());
                }
                methodRoot = new MethodRoot(moduleBean, component);
            }
        }
        return methodRoot;
    }

    @JSONField(serialize = false)
    public List<JavaSrcBean> getAllJavaSrcBeans() {
        List<JavaSrcBean> allJavaSrc = new ArrayList<>();

        if (widgetConfig != null && widgetConfig.getJavaSrcBeans() != null) {
            List<JavaSrcBean> allWidgetJavaSrc = widgetConfig.getJavaSrcBeans();
            for (JavaSrcBean widgetSrc : allWidgetJavaSrc) {
                if (widgetSrc != null && !allJavaSrc.contains(widgetSrc)) {
                    allJavaSrc.add(widgetSrc);
                }
            }
        }

        if (comboConfig != null && comboConfig.getJavaSrcBeans() != null) {
            List<JavaSrcBean> allComboJavaSrc = comboConfig.getJavaSrcBeans();
            for (JavaSrcBean widgetSrc : allComboJavaSrc) {
                if (widgetSrc != null && !allJavaSrc.contains(widgetSrc)) {
                    allJavaSrc.add(widgetSrc);
                }
            }
        }
        return allJavaSrc;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public void setEsdField(ESDField esdField) {
        this.esdField = esdField;
    }

    public DBFieldBean getDbFieldBean() {
        return dbFieldBean;
    }

    public void setDbFieldBean(DBFieldBean dbFieldBean) {
        this.dbFieldBean = dbFieldBean;
    }

    public SearchFieldBean getSearchFieldBean() {
        return searchFieldBean;
    }

    public void setSearchFieldBean(SearchFieldBean searchFieldBean) {
        this.searchFieldBean = searchFieldBean;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public String getSourceMethodName() {
        return sourceMethodName;
    }

    public void setSourceMethodName(String sourceMethodName) {
        this.sourceMethodName = sourceMethodName;
    }

    public void setAggConfig(FieldAggConfig aggConfig) {
        this.aggConfig = aggConfig;
    }


    public CustomFieldBean getCustomBean() {
        return customBean;
    }

    public void setCustomBean(CustomFieldBean customBean) {
        this.customBean = customBean;
    }

    public ContainerBean getContainerBean() {
        return containerBean;
    }

    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }


    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    @Override
    public String getDomainId() {
        return domainId;
    }

    @Override
    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    //@Override
    public String getServiceClassName() {
        return serviceClassName;
    }

    public void setServiceClassName(String serviceClassName) {
        this.serviceClassName = serviceClassName;
    }

    @Override
    public String getId() {
        return id == null ? this.fieldname : id;
    }

    @Override
    public Class<? extends ESDFieldConfig> getClazz() {
        return this.getClass();
    }

    @Override
    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
    }


    @Override
    public String getFieldname() {
        return fieldname;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public RightContextMenuBean genContextMenuBean() {
        if (contextMenuBean == null) {
            contextMenuBean = new RightContextMenuBean(fieldname);
            AnnotationUtil.fillDefaultValue(RightContextMenu.class, contextMenuBean);
        }
        return contextMenuBean;
    }

    public RightContextMenuBean getContextMenuBean() {
        return contextMenuBean;
    }


    public void setContextMenuBean(RightContextMenuBean contextMenuBean) {
        this.contextMenuBean = contextMenuBean;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }


    public TipsBean getTipsBean() {
        return tipsBean;
    }

    public void setTipsBean(TipsBean tipsBean) {
        this.tipsBean = tipsBean;
    }

    public LabelBean getLabelBean() {
        return labelBean;
    }

    public void setLabelBean(LabelBean labelBean) {
        this.labelBean = labelBean;
    }


    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public void setWidgetConfig(M widgetConfig) {
        this.widgetConfig = widgetConfig;
    }

    public void setFieldBean(FieldBean fieldBean) {
        this.fieldBean = fieldBean;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof FieldFormConfig) {
            return ((FieldFormConfig) obj).getId().equals(getId());
        }
        return super.equals(obj);
    }
}
