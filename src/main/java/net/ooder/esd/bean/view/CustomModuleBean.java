package net.ooder.esd.bean.view;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.annotation.DynLoad;
import net.ooder.annotation.SimpleCustomBean;
import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.event.*;
import net.ooder.esd.annotation.field.BlockFieldAnnotation;
import net.ooder.esd.annotation.field.DialogAnnotation;
import net.ooder.esd.annotation.field.PanelFieldAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.*;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.DialogComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.ModuleProperties;
import net.ooder.esd.util.OODUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

@AnnotationType(clazz = ModuleAnnotation.class)
public class CustomModuleBean implements CustomBean, Comparable<CustomModuleBean> {

    ModuleViewType moduleViewType;

    String methodName;

    String packageName;

    String sourceMethodName;

    String sourceClassName;

    String domainId;

    String alias;

    String id;

    String name;

    String caption;

    String imageClass;

    Integer rotate;

    String target;

    String childname;

    Class bindService;

    Boolean dynLoad;

    Integer index = 1;

    Boolean cache;

    Boolean initMethod;

    BorderType borderType;

    DialogBean dialogBean;

    CustomPanelBean panelBean;

    CustomBlockBean blockBean;

    CustomDivBean divBean;

    ModuleViewBean viewConfig;

    String euClassName;

    String cssStyle;

    public PanelType panelType = PanelType.block;

    public LinkedHashSet<Action> onRenderAction = new LinkedHashSet();

    public LinkedHashSet<Action> onReadyAction = new LinkedHashSet();

    public LinkedHashSet<Action> onFragmentChangedAction = new LinkedHashSet();

    public LinkedHashSet<Action> onMessageAction = new LinkedHashSet();

    public LinkedHashSet<Action> beforeCreatedAction = new LinkedHashSet();

    public LinkedHashSet<Action> beforeDestroyAction = new LinkedHashSet();

    public LinkedHashSet<Action> onDestroyAction = new LinkedHashSet();

    public LinkedHashSet<Action> afterShowAction = new LinkedHashSet();

    public LinkedHashSet<Action> onLoadBaseClassAction = new LinkedHashSet();

    public LinkedHashSet<Action> onLoadRequiredClassAction = new LinkedHashSet();

    public LinkedHashSet<Action> onLoadRequiredClassErrAction = new LinkedHashSet();


    public LinkedHashSet<Action> onIniResourceAction = new LinkedHashSet();

    public LinkedHashSet<Action> beforeIniComponentsAction = new LinkedHashSet();

    public LinkedHashSet<Action> afterIniComponentsAction = new LinkedHashSet();

    public LinkedHashSet<Action> onModulePropChangeAction = new LinkedHashSet();

    public LinkedHashSet<CustomModuleEventEnum> onRender = new LinkedHashSet();

    public LinkedHashSet<ModuleOnReadyEventEnum> onReady = new LinkedHashSet();

    public LinkedHashSet<CustomModuleEventEnum> onFragmentChanged = new LinkedHashSet();

    public LinkedHashSet<ModuleOnMessageEventEnum> onMessage = new LinkedHashSet();

    public LinkedHashSet<CustomModuleEventEnum> beforeCreated = new LinkedHashSet();

    public LinkedHashSet<CustomOnDestroyEventEnum> beforeDestroy = new LinkedHashSet();

    public LinkedHashSet<CustomOnDestroyEventEnum> onDestroy = new LinkedHashSet();

    public LinkedHashSet<CustomModuleEventEnum> afterShow = new LinkedHashSet();

    public LinkedHashSet<CustomModuleEventEnum> onLoadBaseClass = new LinkedHashSet();

    public LinkedHashSet<CustomModuleEventEnum> onLoadRequiredClass = new LinkedHashSet();

    public LinkedHashSet<CustomModuleEventEnum> onLoadRequiredClassErr = new LinkedHashSet();

    public LinkedHashSet<CustomModuleEventEnum> onIniResource = new LinkedHashSet();

    public LinkedHashSet<CustomModuleEventEnum> beforeIniComponents = new LinkedHashSet();

    public LinkedHashSet<CustomModuleEventEnum> afterIniComponents = new LinkedHashSet();

    public LinkedHashSet<ModuleOnPropChangeEventEnum> onModulePropChange = new LinkedHashSet();

    ContainerBean containerBean;

    @JSONField(serialize = false)
    ModuleComponent moduleComponent;

    @JSONField(serialize = false)
    List<JavaSrcBean> javaSrcBeans;

    @JSONField(serialize = false)
    MethodConfig methodConfig;


    public CustomModuleBean() {

    }


    public CustomModuleBean(MethodConfig methodConfig) {
        AnnotationUtil.fillDefaultValue(ModuleAnnotation.class, this);
        reBindMethod(methodConfig);
    }

    public CustomModuleBean(CustomMethodInfo methodInfo) {
        AnnotationUtil.fillDefaultValue(ModuleAnnotation.class, this);
        this.initMethod(methodInfo.getInnerMethod());
        this.sourceClassName = methodInfo.getInnerMethod().getDeclaringClass().getName();
        this.domainId = methodInfo.getDomainId();
        this.sourceMethodName = methodInfo.getMethodName();
        this.methodName = methodInfo.getMethodName();
        this.alias = OODUtil.formatJavaName(methodName, true);
        this.id = OODUtil.formatJavaName(methodName, true);
        ;
    }

    public CustomModuleBean(ModuleComponent moduleComponent) {
        this.update(moduleComponent);
    }

    public CustomModuleBean(ModuleProperties moduleProperties) {
        this.init(moduleProperties);
    }

    public CustomModuleBean(Component component) {
        this.updateComponent(component);
    }


    public void reBindMethod(MethodConfig methodConfig) {
        if (methodConfig != null) {
            if (methodConfig.getMethod() != null) {
                this.initMethod(methodConfig.getMethod());
            }
            this.methodConfig = methodConfig;
            this.sourceClassName = methodConfig.getSourceClassName();
            this.domainId = methodConfig.getDomainId();
            this.sourceMethodName = methodConfig.getMethodName();
            this.methodName = methodConfig.getMethodName();
            this.euClassName = methodConfig.getViewClassName();
            if (euClassName == null) {
                euClassName = methodConfig.getEUClassName();
            }
            this.alias = OODUtil.formatJavaName(methodName, true);
            this.id = OODUtil.formatJavaName(methodName, true);
        }
    }


    public void updateComponent(Component component) {
        this.id = OODUtil.formatJavaName(component.getAlias(), true);
        this.name = id;
        this.methodName = id;
        this.target = component.getTarget();
        this.alias = component.getAlias();
        this.index = component.getProperties().getTabindex() == null ? 1 : component.getProperties().getTabindex();
        ModuleComponent parentModuleComponent = component.getModuleComponent();
        this.packageName = parentModuleComponent.getClassName().substring(0, parentModuleComponent.getClassName().lastIndexOf("."));
        if (euClassName == null) {
            this.euClassName = packageName + "." + OODUtil.formatJavaName(component.getAlias(), true);
        }
        if (caption == null) {
            this.caption = component.getProperties().getDesc() == null ? component.getAlias() : component.getProperties().getDesc();
        }
        ModuleComponent simModuleComponent = new ModuleComponent(component);
        ModuleProperties moduleProperties = new ModuleProperties();
        DSMProperties dsmProperties = new DSMProperties();
        dsmProperties.setSourceClassName(parentModuleComponent.getClassName());
        ModuleProperties parentModuleProperties = parentModuleComponent.getProperties();
        if (parentModuleProperties.getDsmProperties() != null) {
            dsmProperties.setDomainId(parentModuleProperties.getDsmProperties().getDomainId());
        }
        dsmProperties.setRealPath(component.getPath());
        moduleProperties.setMethodName(this.methodName);
        moduleProperties.setDsmProperties(dsmProperties);
        simModuleComponent.setProperties(moduleProperties);
        this.update(simModuleComponent);
    }


    public void update(ModuleComponent moduleComponent) {
        this.reBindMethod(moduleComponent.getMethodAPIBean());
        this.moduleComponent = moduleComponent;
        moduleComponent.setModuleBean(this);
        this.euClassName = moduleComponent.getClassName();
        this.moduleViewType = moduleComponent.getModuleViewType();
        if (moduleViewType.equals(ModuleViewType.LAYOUTCONFIG)) {
            moduleViewType = moduleComponent.guessComponentType();
        }
        this.alias = moduleComponent.getAlias();
        Component currComponent = moduleComponent.getCurrComponent();
        if (currComponent != null) {
            this.alias = currComponent.getAlias();
        }
        this.init(moduleComponent.getProperties());
        if (moduleComponent.getMethodAPIBean() != null) {
            this.methodConfig = moduleComponent.getMethodAPIBean();
            this.methodName = methodConfig.getMethodName();
            this.sourceMethodName = methodConfig.getMethodName();
            this.sourceClassName = methodConfig.getSourceClassName();
            this.domainId = methodConfig.getDomainId();
            this.alias = methodName;
        }

        if (alias != null) {
            this.id = OODUtil.formatJavaName(alias, true);
            this.name = id;
            this.methodName = id;
        }

        Component mainComponent = moduleComponent.getMainBoxComponent();
        if (mainComponent == null) {
            mainComponent = currComponent;
        }

        PanelType userPanelType = moduleComponent.getProperties().getPanelType();

        if (userPanelType != null) {
            panelType = userPanelType;
        }

        if (panelType == null) {
            if (mainComponent != null) {
                ComponentType componentType = ComponentType.fromType(mainComponent.getKey());
                switch (componentType) {
                    case PANEL:
                        panelType = PanelType.panel;
                        break;
                    case DIALOG:
                        panelType = PanelType.dialog;
                        break;
                    case DIV:
                        panelType = PanelType.div;
                        break;
                    default:
                        panelType = PanelType.block;
                }
            } else {
                panelType = PanelType.block;
            }
        }

        switch (panelType) {
            case dialog:
                if (dialogBean == null) {
                    dialogBean = new DialogBean((DialogComponent) mainComponent);
                } else {
                    dialogBean.update((DialogComponent) mainComponent);
                }
                break;
            case block:
                if (blockBean == null) {
                    blockBean = new CustomBlockBean(mainComponent);
                } else {
                    blockBean.update(mainComponent);
                }
                break;
            case div:

                if (divBean == null) {
                    divBean = new CustomDivBean(mainComponent);
                } else {
                    divBean.update(mainComponent);
                }

                break;
            case panel:

                if (panelBean == null) {
                    panelBean = new CustomPanelBean(mainComponent);
                } else {
                    panelBean.update(mainComponent);
                }

                break;
        }

        if (viewConfig == null) {
            viewConfig = new ModuleViewBean(moduleComponent.getViewConfig());
        } else {
            viewConfig.update(moduleComponent.getViewConfig());
        }
    }


    void init(ModuleProperties moduleProperties) {
        DSMProperties dsmProperties = moduleProperties.getDsmProperties();
        if (dsmProperties != null) {
            sourceClassName = dsmProperties.getSourceClassName();
            sourceMethodName = dsmProperties.getSourceMethodName();
            domainId = dsmProperties.getDomainId();
        }
        if (moduleProperties.getId() != null) {
            id = moduleProperties.getId();
        }
        if (moduleProperties.getName() != null) {
            name = moduleProperties.getName();
        }

        if (moduleProperties.getCaption() != null) {
            caption = moduleProperties.getCaption();
        }

        if (moduleProperties.getTarget() != null) {
            target = moduleProperties.getTarget();
        }

        if (methodName == null) {
            methodName = moduleProperties.getMethodName() == null ? alias : moduleProperties.getMethodName();
        }

        imageClass = moduleProperties.getImageClass();
        rotate = moduleProperties.getRotate();
        childname = moduleProperties.getChildname();
        bindService = moduleProperties.getBindService();
        dynLoad = moduleProperties.getDynLoad();
        borderType = moduleProperties.getBorderType();
    }

    public String getEuClassName() {
        return euClassName;
    }

    public void setEuClassName(String euClassName) {
        this.euClassName = euClassName;
    }


    public String getCssStyle() {
        return cssStyle;
    }

    public void setCssStyle(String cssStyle) {
        this.cssStyle = cssStyle;
    }


    @JSONField(serialize = false)
    public MethodConfig getMethodConfig() {
        if (methodConfig == null && sourceClassName != null && methodName != null) {
            ApiClassConfig apiClassConfig = null;
            try {
                apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.sourceClassName);
                if (apiClassConfig != null) {
                    methodConfig = apiClassConfig.getMethodByName(methodName);
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return methodConfig;
    }


    @JSONField(serialize = false)
    public Method getMethod() {
        if (sourceClassName != null && methodName != null) {
            try {
                Class clazz = ClassUtility.loadClass(sourceClassName);
                if (clazz != null) {
                    Method[] methods = clazz.getMethods();
                    for (Method cmethod : methods) {
                        if (cmethod.getName().equals(methodName)) {
                            return cmethod;
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    public void setRotate(int rotate) {
        this.rotate = rotate;
    }


    public DialogBean getDialogBean() {
        if (panelType != null && !panelType.equals(PanelType.dialog)) {
            return null;
        }

        if (dialogBean == null) {
//            if (dialogBean == null && moduleComponent != null && moduleComponent.getModulePanelComponent() != null) {
//                dialogBean = new DialogBean(moduleComponent.getDialogComponent());
//            } else {
//
//
//            }

            if (this.getMethod() != null) {
                Set<Annotation> annotations = AnnotationUtil.getAllAnnotations(this.getMethod(), true);
                dialogBean = new DialogBean(annotations);
            } else {
                dialogBean = new DialogBean();
                AnnotationUtil.fillDefaultValue(DialogAnnotation.class, dialogBean);
            }
        }


        ContainerBean containerBean = this.getContainerBean();
        if (containerBean != null) {
            dialogBean.setContainerBean(containerBean);
        }

        return dialogBean;
    }

    public void setDialogBean(DialogBean dialogBean) {
        this.dialogBean = dialogBean;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getUIAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        containerBean = this.getContainerBean();
        if (panelType != null) {
            switch (panelType) {
                case dialog:
                    if (this.getDialogBean() != null) {
                        annotationBeans.addAll(this.getDialogBean().getAllAnnotationBeans());
                    } else {
                        annotationBeans.add(new SimpleCustomBean(DialogAnnotation.class));
                        if (containerBean != null) {
                            annotationBeans.addAll(containerBean.getAnnotationBeans());
                        }
                    }
                    break;
                case panel:
                    if (this.getPanelBean() != null) {
                        annotationBeans.addAll(this.getPanelBean().getAllAnnotationBeans());
                    }
                    break;
                case div:
                    if (this.getDivBean() != null) {
                        annotationBeans.addAll(this.getDivBean().getAllAnnotationBeans());
                    }
                    break;
                case block:
                    if (this.getBlockBean() != null) {
                        annotationBeans.addAll(this.getBlockBean().getAllAnnotationBeans());
                    }
                    break;
            }
        } else if (containerBean != null) {
            annotationBeans.addAll(containerBean.getAnnotationBeans());
        }

        if (this.getViewConfig() != null) {
            annotationBeans.addAll(this.getViewConfig().getAnnotationBeans());
        }

        return annotationBeans;

    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.add(this);
        annotationBeans.addAll(getUIAnnotationBeans());
        List<ModuleEventBean> eventBeans = this.getAllEvent();
        for (ModuleEventBean eventBean : eventBeans) {
            ModuleEventEnum moduleEventEnum = eventBean.getEventName();
            List<Action> actions = eventBean.getActions();
            for (Action action : actions) {
                switch (moduleEventEnum) {
                    case onFragmentChanged:
                        onFragmentChanged.add(CustomModuleEventEnum.valueOf(action.getEventValue()));
                        break;
                    case onMessage:
                        onMessage.add(ModuleOnMessageEventEnum.valueOf(action.getEventValue()));
                        break;
                    case beforeCreated:
                        beforeCreated.add(CustomModuleEventEnum.valueOf(action.getEventValue()));
                        break;
                    case onLoadBaseClass:
                        onLoadBaseClass.add(CustomModuleEventEnum.valueOf(action.getEventValue()));
                        break;
                    case onLoadRequiredClass:
                        onLoadRequiredClass.add(CustomModuleEventEnum.valueOf(action.getEventValue()));
                        break;
                    case onLoadRequiredClassErr:
                        onLoadRequiredClassErr.add(CustomModuleEventEnum.valueOf(action.getEventValue()));
                        break;
                    case onIniResource:
                        onIniResource.add(CustomModuleEventEnum.valueOf(action.getEventValue()));
                        break;
                    case beforeIniComponents:
                        beforeIniComponents.add(CustomModuleEventEnum.valueOf(action.getEventValue()));
                        break;
                    case afterIniComponents:
                        onIniResource.add(CustomModuleEventEnum.valueOf(action.getEventValue()));
                        break;
                    case afterShow:
                        beforeIniComponents.add(CustomModuleEventEnum.valueOf(action.getEventValue()));
                        break;
                    case onModulePropChange:
                        onIniResource.add(CustomModuleEventEnum.valueOf(action.getEventValue()));
                        break;
                    case onReady:
                        beforeIniComponents.add(CustomModuleEventEnum.valueOf(action.getEventValue()));
                        break;
                    case onRender:
                        beforeIniComponents.add(CustomModuleEventEnum.valueOf(action.getEventValue()));
                        break;
                    case beforeDestroy:
                        beforeDestroy.add(CustomOnDestroyEventEnum.valueOf(action.getEventValue()));
                        break;
                    case onDestroy:
                        onDestroy.add(CustomOnDestroyEventEnum.valueOf(action.getEventValue()));
                        break;
                }
            }
        }

        annotationBeans.addAll(onRenderAction);
        annotationBeans.addAll(onReadyAction);
        annotationBeans.addAll(onFragmentChangedAction);
        annotationBeans.addAll(onMessageAction);
        annotationBeans.addAll(beforeCreatedAction);
        annotationBeans.addAll(beforeDestroyAction);
        annotationBeans.addAll(onDestroyAction);
        annotationBeans.addAll(afterShowAction);
        annotationBeans.addAll(onLoadBaseClassAction);
        annotationBeans.addAll(onLoadRequiredClassAction);
        annotationBeans.addAll(onLoadRequiredClassErrAction);
        annotationBeans.addAll(onIniResourceAction);
        annotationBeans.addAll(beforeIniComponentsAction);
        annotationBeans.addAll(afterIniComponentsAction);
        annotationBeans.addAll(onModulePropChangeAction);
        return annotationBeans;
    }

    public void initMethod(Method method) {
        this.bindService = method.getDeclaringClass();
        Set<Annotation> annotations = AnnotationUtil.getAllAnnotations(method, true);
        fillData(annotations);
    }

    public void fillData(Set<Annotation> annotations) {
        boolean hasAnn = false;
        boolean hasView = true;

        for (Annotation annotation : annotations) {
            if (annotation instanceof ModuleAnnotation) {
                fillData((ModuleAnnotation) annotation);
                hasAnn = true;
            }
        }

        if (!hasAnn) {
            AnnotationUtil.fillDefaultValue(ModuleAnnotation.class, this);
        }

        for (Annotation annotation : annotations) {
            if (annotation instanceof DynLoad) {
                this.dynLoad = true;
            } else if (annotation instanceof ViewStylesAnnotation) {
                hasView = true;
            } else if (annotation instanceof DesignViewAnnotation) {
                hasView = true;
            }

            if (annotation instanceof DialogAnnotation) {
                this.panelType = PanelType.dialog;
            }
            if (annotation instanceof MDialogAnnotation) {
                this.panelType = PanelType.dialog;
            }
            if (annotation instanceof PanelAnnotation) {
                this.panelType = PanelType.panel;
            }
            if (annotation instanceof PanelFieldAnnotation) {
                this.panelType = PanelType.panel;
            }
            if (annotation instanceof BlockAnnotation) {
                this.panelType = PanelType.block;
            }
            if (annotation instanceof BlockFieldAnnotation) {
                this.panelType = PanelType.block;
            }
        }


        if (hasView) {
            viewConfig = new ModuleViewBean(annotations);
        }

        if (panelType != null) {

            switch (panelType) {
                case panel:
                    if (panelBean == null) {
                        panelBean = new CustomPanelBean(annotations);
                    } else {
                        CustomPanelBean fieldPanelBean = new CustomPanelBean(annotations);
                        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldPanelBean), Map.class), panelBean, false, false);
                    }

                    break;
                case block:
                    if (blockBean == null) {
                        blockBean = new CustomBlockBean(annotations);
                    } else {
                        CustomBlockBean fieldBlockBean = new CustomBlockBean(annotations);
                        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldBlockBean), Map.class), blockBean, false, false);
                    }

                    break;

                case dialog:
                    if (dialogBean == null) {
                        dialogBean = new DialogBean(annotations);
                    } else {
                        DialogBean fieldBlockBean = new DialogBean(annotations);
                        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldBlockBean), Map.class), dialogBean, false, false);
                    }
                    break;
            }
        }
    }

    public ModuleViewBean getViewConfig() {
        if (viewConfig == null) {
            viewConfig = new ModuleViewBean();
            viewConfig.update(null);
        }
        return viewConfig;
    }

    public void setViewConfig(ModuleViewBean viewConfig) {
        this.viewConfig = viewConfig;
    }

    private void fillData(ModuleAnnotation moduleAnnotation) {
        AnnotationUtil.fillBean(moduleAnnotation, this);

        CustomAction[] customActions = moduleAnnotation.onRenderAction();
        if (customActions.length > 0) {
            onRenderAction = new LinkedHashSet();
            for (CustomAction customAction : customActions) {
                onRenderAction.add(new Action(customAction, ModuleEventEnum.onRender));
            }
        }

        customActions = moduleAnnotation.onReadyAction();
        if (customActions.length > 0) {
            onReadyAction = new LinkedHashSet();
            for (CustomAction customAction : customActions) {
                onReadyAction.add(new Action(customAction, ModuleEventEnum.onReady));
            }
        }
        customActions = moduleAnnotation.onFragmentChangedAction();
        if (customActions.length > 0) {
            onFragmentChangedAction = new LinkedHashSet();
            for (CustomAction customAction : customActions) {
                onFragmentChangedAction.add(new Action(customAction, ModuleEventEnum.onFragmentChanged));
            }
        }

        customActions = moduleAnnotation.onMessageAction();
        if (customActions.length > 0) {
            onMessageAction = new LinkedHashSet();
            for (CustomAction customAction : customActions) {
                onMessageAction.add(new Action(customAction, ModuleEventEnum.onMessage));
            }
        }

        customActions = moduleAnnotation.beforeCreatedAction();
        if (customActions.length > 0) {
            beforeCreatedAction = new LinkedHashSet();
            for (CustomAction customAction : customActions) {
                beforeCreatedAction.add(new Action(customAction, ModuleEventEnum.beforeCreated));
            }
        }

        customActions = moduleAnnotation.beforeDestroyAction();
        if (customActions.length > 0) {
            beforeDestroyAction = new LinkedHashSet();
            for (CustomAction customAction : customActions) {
                beforeDestroyAction.add(new Action(customAction, ModuleEventEnum.beforeDestroy));
            }
        }
        customActions = moduleAnnotation.onDestroyAction();
        if (customActions.length > 0) {
            onDestroyAction = new LinkedHashSet();
            for (CustomAction customAction : customActions) {
                onDestroyAction.add(new Action(customAction, ModuleEventEnum.onDestroy));
            }
        }


        customActions = moduleAnnotation.afterShowAction();

        if (customActions.length > 0) {
            afterShowAction = new LinkedHashSet();
            for (CustomAction customAction : customActions) {
                afterShowAction.add(new Action(customAction, ModuleEventEnum.afterShow));
            }
        }

        customActions = moduleAnnotation.onLoadBaseClassAction();

        if (customActions.length > 0) {
            onLoadBaseClassAction = new LinkedHashSet();
            for (CustomAction customAction : customActions) {
                onLoadBaseClassAction.add(new Action(customAction, ModuleEventEnum.onLoadBaseClass));
            }
        }


        customActions = moduleAnnotation.onLoadRequiredClassAction();

        if (customActions.length > 0) {
            onLoadRequiredClassAction = new LinkedHashSet();
            for (CustomAction customAction : customActions) {
                onLoadRequiredClassAction.add(new Action(customAction, ModuleEventEnum.onLoadRequiredClass));
            }
        }


        customActions = moduleAnnotation.onLoadRequiredClassErrAction();

        if (customActions.length > 0) {
            onLoadRequiredClassErrAction = new LinkedHashSet();
            for (CustomAction customAction : customActions) {
                onLoadRequiredClassErrAction.add(new Action(customAction, ModuleEventEnum.onLoadRequiredClass));
            }
        }

        customActions = moduleAnnotation.onIniResourceAction();

        if (customActions.length > 0) {
            onIniResourceAction = new LinkedHashSet();
            for (CustomAction customAction : customActions) {
                onIniResourceAction.add(new Action(customAction, ModuleEventEnum.onIniResource));
            }
        }

        customActions = moduleAnnotation.beforeIniComponentsAction();
        if (customActions.length > 0) {
            afterIniComponentsAction = new LinkedHashSet();
            for (CustomAction customAction : customActions) {
                afterIniComponentsAction.add(new Action(customAction, ModuleEventEnum.afterIniComponents));
            }
        }

        customActions = moduleAnnotation.afterIniComponentsAction();
        if (customActions.length > 0) {
            afterIniComponentsAction = new LinkedHashSet();
            for (CustomAction customAction : customActions) {
                afterIniComponentsAction.add(new Action(customAction, ModuleEventEnum.afterIniComponents));
            }
        }

        customActions = moduleAnnotation.onModulePropChangeAction();

        if (customActions.length > 0) {
            onModulePropChangeAction = new LinkedHashSet();
            for (CustomAction customAction : customActions) {
                onModulePropChangeAction.add(new Action(customAction, ModuleEventEnum.onModulePropChange));
            }
        }

    }

    public String getSourceMethodName() {
        return sourceMethodName;
    }

    public void setSourceMethodName(String sourceMethodName) {
        this.sourceMethodName = sourceMethodName;
    }

    public Action getActionById(String actionId) {
        Set<Action> actions = this.getAllActions();
        for (Action action : actions) {
            if (action.getId().equals(actionId)) {
                return action;
            }
        }
        return null;
    }

    public ModuleEventBean getEventById(String eventId) {
        List<ModuleEventBean> eventBeans = this.getAllEvent();
        for (ModuleEventBean eventBean : eventBeans) {
            if (eventBean.getEventId().equals(eventId)) {
                return eventBean;
            }
        }
        return null;
    }


    @JSONField(serialize = false)
    public Set<Action> getAllActions() {
        Set<Action> actions = new HashSet<>();
        List<ModuleEventBean> events = getAllEvent();
        for (ModuleEventBean event : events) {
            for (Action customAction : event.getActions()) {
                actions.add(customAction);
            }
        }
        return actions;
    }

    @JSONField(serialize = false)
    public List<ModuleEventBean> getAllEvent() {
        List<ModuleEventBean> apiEvents = new ArrayList<>();
        if (onFragmentChanged != null) {
            for (CustomModuleEventEnum beforDataEvent : onFragmentChanged) {
                apiEvents.add(new ModuleEventBean(beforDataEvent, ModuleEventEnum.onFragmentChanged));
            }
        }
        if (onMessage != null)

        {
            for (ModuleOnMessageEventEnum beforInvokeEvent : onMessage) {
                apiEvents.add(new ModuleEventBean(beforInvokeEvent, ModuleEventEnum.onMessage));
            }
        }
        if (beforeCreated != null)

        {
            for (CustomModuleEventEnum callBackEvent : beforeCreated) {
                apiEvents.add(new ModuleEventBean(callBackEvent, ModuleEventEnum.beforeCreated));
            }
        }
        if (onLoadBaseClass != null)

        {
            for (CustomModuleEventEnum onErrorEvent : onLoadBaseClass) {
                apiEvents.add(new ModuleEventBean(onErrorEvent, ModuleEventEnum.onLoadBaseClass));
            }
        }

        if (onLoadRequiredClass != null)

        {
            for (CustomModuleEventEnum onDataEvent : onLoadRequiredClass) {
                apiEvents.add(new ModuleEventBean(onDataEvent, ModuleEventEnum.onLoadRequiredClass));
            }
        }
        if (onLoadRequiredClassErr != null)

        {
            for (CustomModuleEventEnum onExecuteSuccessEvent : onLoadRequiredClassErr) {
                apiEvents.add(new ModuleEventBean(onExecuteSuccessEvent, ModuleEventEnum.onLoadRequiredClassErr));
            }
        }

        if (onIniResource != null)

        {
            for (CustomModuleEventEnum onExecueErrorEvent : onIniResource) {
                apiEvents.add(new ModuleEventBean(onExecueErrorEvent, ModuleEventEnum.onIniResource));
            }
        }


        if (beforeIniComponents != null) {
            for (CustomModuleEventEnum onExecuteSuccessEvent : beforeIniComponents) {
                apiEvents.add(new ModuleEventBean(onExecuteSuccessEvent, ModuleEventEnum.beforeIniComponents));
            }
        }

        if (afterIniComponents != null)

        {
            for (CustomModuleEventEnum onExecueErrorEvent : afterIniComponents) {
                apiEvents.add(new ModuleEventBean(onExecueErrorEvent, ModuleEventEnum.afterIniComponents));
            }
        }


        if (afterShow != null) {
            for (CustomModuleEventEnum onExecuteSuccessEvent : afterShow) {
                apiEvents.add(new ModuleEventBean(onExecuteSuccessEvent, ModuleEventEnum.afterShow));
            }
        }

        if (onModulePropChange != null)

        {
            for (ModuleOnPropChangeEventEnum onExecueErrorEvent : onModulePropChange) {
                apiEvents.add(new ModuleEventBean(onExecueErrorEvent, ModuleEventEnum.onModulePropChange));
            }
        }


        if (onReady != null) {
            for (ModuleOnReadyEventEnum onExecuteSuccessEvent : onReady) {
                apiEvents.add(new ModuleEventBean(onExecuteSuccessEvent, ModuleEventEnum.onReady));
            }
        }

        if (beforeDestroy != null)

        {
            for (CustomOnDestroyEventEnum onExecueErrorEvent : beforeDestroy) {
                apiEvents.add(new ModuleEventBean(onExecueErrorEvent, ModuleEventEnum.beforeDestroy));
            }
        }
        if (onDestroy != null)

        {
            for (CustomOnDestroyEventEnum onExecueErrorEvent : onDestroy) {
                apiEvents.add(new ModuleEventBean(onExecueErrorEvent, ModuleEventEnum.onDestroy));
            }
        }

        return apiEvents;

    }

    public CustomBlockBean getBlockBean() {
        if (blockBean == null) {
//            if (moduleComponent != null && moduleComponent.getCurrComponent() != null) {
//                Component component = moduleComponent.getCurrComponent();
//                blockBean = new CustomBlockBean(component);
//            } else {
//                Method method = this.getMethod();
//                if (method != null) {
//                    Set<Annotation> annotations = AnnotationUtil.getAllAnnotations(method, true);
//                    blockBean = new CustomBlockBean(annotations);
//                } else {
//                    blockBean = new CustomBlockBean();
//                    AnnotationUtil.fillDefaultValue(BlockAnnotation.class, blockBean);
//                }
//            }
            Method method = this.getMethod();
            if (method != null) {
                Set<Annotation> annotations = AnnotationUtil.getAllAnnotations(method, true);
                blockBean = new CustomBlockBean(annotations);
            } else {
                blockBean = new CustomBlockBean();
                AnnotationUtil.fillDefaultValue(BlockAnnotation.class, blockBean);
            }

            if (getContainerBean() != null) {
                blockBean.setContainerBean(getContainerBean());
            }
        }
        return blockBean;
    }


    public void setBlockBean(CustomBlockBean blockBean) {
        this.blockBean = blockBean;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public Integer getRotate() {
        return rotate;
    }

    public void setRotate(Integer rotate) {
        this.rotate = rotate;
    }


    public Class getBindService() {
        return bindService;
    }

    public String getChildname() {
        return childname;
    }

    public void setChildname(String childname) {
        this.childname = childname;
    }

    public void setBindService(Class bindService) {
        this.bindService = bindService;
    }


    public LinkedHashSet<Action> getBeforeDestroyAction() {
        return beforeDestroyAction;
    }

    public void setBeforeDestroyAction(LinkedHashSet<Action> beforeDestroyAction) {
        this.beforeDestroyAction = beforeDestroyAction;
    }

    public LinkedHashSet<CustomOnDestroyEventEnum> getBeforeDestroy() {
        return beforeDestroy;
    }

    public void setBeforeDestroy(LinkedHashSet<CustomOnDestroyEventEnum> beforeDestroy) {
        this.beforeDestroy = beforeDestroy;
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }


    public Boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }


    public Boolean isDynLoad() {
        return dynLoad;
    }

    public void setDynLoad(boolean dynLoad) {
        this.dynLoad = dynLoad;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }


    public Boolean getCache() {
        return cache;
    }

    public void setCache(Boolean cache) {
        this.cache = cache;
    }

    public Boolean getDynLoad() {
        return dynLoad;
    }

    public void setDynLoad(Boolean dynLoad) {
        this.dynLoad = dynLoad;
    }

    public Set<CustomModuleEventEnum> getOnRender() {
        return onRender;
    }

    public void setOnRender(LinkedHashSet<CustomModuleEventEnum> onRender) {
        this.onRender = onRender;
    }

    public Set<ModuleOnReadyEventEnum> getOnReady() {
        return onReady;
    }

    public void setOnReady(LinkedHashSet<ModuleOnReadyEventEnum> onReady) {
        this.onReady = onReady;
    }

    public LinkedHashSet<CustomModuleEventEnum> getOnFragmentChanged() {
        return onFragmentChanged;
    }

    public void setOnFragmentChanged(LinkedHashSet<CustomModuleEventEnum> onFragmentChanged) {
        this.onFragmentChanged = onFragmentChanged;
    }

    public LinkedHashSet<ModuleOnMessageEventEnum> getOnMessage() {
        return onMessage;
    }

    public void setOnMessage(LinkedHashSet<ModuleOnMessageEventEnum> onMessage) {
        this.onMessage = onMessage;
    }

    public LinkedHashSet<CustomModuleEventEnum> getBeforeCreated() {
        return beforeCreated;
    }

    public void setBeforeCreated(LinkedHashSet<CustomModuleEventEnum> beforeCreated) {
        this.beforeCreated = beforeCreated;
    }

    public LinkedHashSet<Action> getBeforeIniComponentsAction() {
        return beforeIniComponentsAction;
    }

    public void setBeforeIniComponentsAction(LinkedHashSet<Action> beforeIniComponentsAction) {
        this.beforeIniComponentsAction = beforeIniComponentsAction;
    }

    public LinkedHashSet<CustomModuleEventEnum> getBeforeIniComponents() {
        return beforeIniComponents;
    }

    public void setBeforeIniComponents(LinkedHashSet<CustomModuleEventEnum> beforeIniComponents) {
        this.beforeIniComponents = beforeIniComponents;
    }

    public Set<CustomOnDestroyEventEnum> getOnDestroy() {
        return onDestroy;
    }

    public void setOnDestroy(LinkedHashSet<CustomOnDestroyEventEnum> onDestroy) {
        this.onDestroy = onDestroy;
    }

    public LinkedHashSet<CustomModuleEventEnum> getAfterShow() {
        return afterShow;
    }

    public void setAfterShow(LinkedHashSet<CustomModuleEventEnum> afterShow) {
        this.afterShow = afterShow;
    }

    public Set<CustomModuleEventEnum> getOnLoadBaseClass() {
        return onLoadBaseClass;
    }

    public void setOnLoadBaseClass(LinkedHashSet<CustomModuleEventEnum> onLoadBaseClass) {
        this.onLoadBaseClass = onLoadBaseClass;
    }

    public LinkedHashSet<CustomModuleEventEnum> getOnLoadRequiredClass() {
        return onLoadRequiredClass;
    }

    public void setOnLoadRequiredClass(LinkedHashSet<CustomModuleEventEnum> onLoadRequiredClass) {
        this.onLoadRequiredClass = onLoadRequiredClass;
    }

    public LinkedHashSet<CustomModuleEventEnum> getOnIniResource() {
        return onIniResource;
    }

    public void setOnIniResource(LinkedHashSet<CustomModuleEventEnum> onIniResource) {
        this.onIniResource = onIniResource;
    }

    public LinkedHashSet<CustomModuleEventEnum> getAfterIniComponents() {
        return afterIniComponents;
    }

    public void setAfterIniComponents(LinkedHashSet<CustomModuleEventEnum> afterIniComponents) {
        this.afterIniComponents = afterIniComponents;
    }

    public LinkedHashSet<ModuleOnPropChangeEventEnum> getOnModulePropChange() {
        return onModulePropChange;
    }

    public void setOnModulePropChange(LinkedHashSet<ModuleOnPropChangeEventEnum> onModulePropChange) {
        this.onModulePropChange = onModulePropChange;
    }


    public LinkedHashSet<Action> getOnRenderAction() {
        return onRenderAction;
    }

    public void setOnRenderAction(LinkedHashSet<Action> onRenderAction) {
        this.onRenderAction = onRenderAction;
    }

    public LinkedHashSet<Action> getOnReadyAction() {
        return onReadyAction;
    }

    public void setOnReadyAction(LinkedHashSet<Action> onReadyAction) {
        onReadyAction = onReadyAction;
    }

    public LinkedHashSet<Action> getOnFragmentChangedAction() {
        return onFragmentChangedAction;
    }

    public void setOnFragmentChangedAction(LinkedHashSet<Action> onFragmentChangedAction) {
        this.onFragmentChangedAction = onFragmentChangedAction;
    }

    public LinkedHashSet<Action> getOnMessageAction() {
        return onMessageAction;
    }

    public void setOnMessageAction(LinkedHashSet<Action> onMessageAction) {
        this.onMessageAction = onMessageAction;
    }

    public LinkedHashSet<Action> getBeforeCreatedAction() {
        return beforeCreatedAction;
    }

    public void setBeforeCreatedAction(LinkedHashSet<Action> beforeCreatedAction) {
        this.beforeCreatedAction = beforeCreatedAction;
    }

    public LinkedHashSet<Action> getOnDestroyAction() {
        return onDestroyAction;
    }

    public void setOnDestroyAction(LinkedHashSet<Action> onDestroyAction) {
        this.onDestroyAction = onDestroyAction;
    }

    public LinkedHashSet<Action> getAfterShowAction() {
        return afterShowAction;
    }

    public void setAfterShowAction(LinkedHashSet<Action> afterShowAction) {
        this.afterShowAction = afterShowAction;
    }

    public LinkedHashSet<Action> getOnLoadBaseClassAction() {
        return onLoadBaseClassAction;
    }

    public void setOnLoadBaseClassAction(LinkedHashSet<Action> onLoadBaseClassAction) {
        this.onLoadBaseClassAction = onLoadBaseClassAction;
    }

    public LinkedHashSet<Action> getOnLoadRequiredClassAction() {
        return onLoadRequiredClassAction;
    }

    public void setOnLoadRequiredClassAction(LinkedHashSet<Action> onLoadRequiredClassAction) {
        this.onLoadRequiredClassAction = onLoadRequiredClassAction;
    }

    public LinkedHashSet<Action> getOnIniResourceAction() {
        return onIniResourceAction;
    }

    public void setOnIniResourceAction(LinkedHashSet<Action> onIniResourceAction) {
        this.onIniResourceAction = onIniResourceAction;
    }

    public Set<Action> getAfterIniComponentsAction() {
        return afterIniComponentsAction;
    }

    public void setAfterIniComponentsAction(LinkedHashSet<Action> afterIniComponentsAction) {
        this.afterIniComponentsAction = afterIniComponentsAction;
    }

    public LinkedHashSet<Action> getOnModulePropChangeAction() {
        return onModulePropChangeAction;
    }

    public void setOnModulePropChangeAction(LinkedHashSet<Action> onModulePropChangeAction) {
        this.onModulePropChangeAction = onModulePropChangeAction;
    }

    public LinkedHashSet<Action> getOnLoadRequiredClassErrAction() {
        return onLoadRequiredClassErrAction;
    }

    public void setOnLoadRequiredClassErrAction(LinkedHashSet<Action> onLoadRequiredClassErrAction) {
        this.onLoadRequiredClassErrAction = onLoadRequiredClassErrAction;
    }

    public LinkedHashSet<CustomModuleEventEnum> getOnLoadRequiredClassErr() {
        return onLoadRequiredClassErr;
    }

    public void setOnLoadRequiredClassErr(LinkedHashSet<CustomModuleEventEnum> onLoadRequiredClassErr) {
        this.onLoadRequiredClassErr = onLoadRequiredClassErr;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public CustomPanelBean getPanelBean() {
        if (panelType != null && !panelType.equals(PanelType.panel)) {
            return null;
        }
//        if (panelBean == null && moduleComponent != null && moduleComponent.getModulePanelComponent() != null) {
//            panelBean = new CustomPanelBean(moduleComponent.getModulePanelComponent());
//        } else {
//            Method method = getMethod();
//            if (method != null) {
//                Set<Annotation> annotations = AnnotationUtil.getAllAnnotations(method, true);
//                panelBean = new CustomPanelBean(annotations);
//            } else {
//                panelBean = new CustomPanelBean();
//                AnnotationUtil.fillDefaultValue(PanelAnnotation.class, panelBean);
//            }
//        }

        if (panelBean == null) {
            Method method = getMethod();
            if (method != null) {
                Set<Annotation> annotations = AnnotationUtil.getAllAnnotations(method, true);
                panelBean = new CustomPanelBean(annotations);
            } else {
                panelBean = new CustomPanelBean();
                AnnotationUtil.fillDefaultValue(PanelAnnotation.class, panelBean);
            }
        }


        ContainerBean containerBean = getContainerBean();
        if (containerBean != null && panelBean.getDivBean() != null) {
            panelBean.getDivBean().setContainerBean(containerBean);
        }
        return panelBean;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Boolean getInitMethod() {
        return initMethod;
    }

    public void setInitMethod(Boolean initMethod) {
        this.initMethod = initMethod;
    }

    public ModuleComponent getModuleComponent() {
        return moduleComponent;
    }

    public void setModuleComponent(ModuleComponent moduleComponent) {
        this.moduleComponent = moduleComponent;
    }

    public void setPanelBean(CustomPanelBean panelBean) {
        this.panelBean = panelBean;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getId() {
        if (id == null) {
            if (target != null) {
                this.id = target;
            } else if (methodName != null) {
                this.id = this.methodName;
            }
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public PanelType getPanelType() {
        if (panelType == null) {
            panelType = PanelType.block;
        }
        return panelType;
    }


    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    @JSONField(serialize = false)
    public String getPath() {
        String path = null;
        switch (panelType) {
            case block:
                path = this.getBlockBean().getXpath();
                break;
            case dialog:
                path = this.getDialogBean().getXpath();
                break;
            case panel:
                path = this.getPanelBean().getXpath();
                break;
            case div:
                path = this.getDivBean().getXpath();
                break;
        }
        return path;
    }

    @JSONField(serialize = false)
    public Dock getDock() {
        Dock dock = null;
        if (panelType != null) {
            switch (panelType) {
                case block:
                    dock = this.getBlockBean().getDock();
                    break;
                case dialog:
                    dock = this.getDialogBean().getDock();
                    break;
                case panel:
                    dock = this.getPanelBean().getDock();
                    break;
            }
        }

        return dock;
    }

    @JSONField(serialize = false)
    public void setDock(Dock dock) {
        if (panelType != null) {
            switch (panelType) {
                case block:
                    this.getBlockBean().setDock(dock);
                    break;
                case dialog:
                    this.getDialogBean().setDock(dock);
                    break;
                case panel:
                    this.getPanelBean().setDock(dock);
                    break;

            }
        }
    }

    @JSONField(serialize = false)
    public void setPath(String path) {
        if (panelType != null) {
            switch (panelType) {
                case block:
                    this.getBlockBean().setXpath(path);
                    break;
                case dialog:
                    this.getDialogBean().setXpath(path);
                    break;
                case panel:
                    this.getPanelBean().setXpath(path);
                    break;
                case div:
                    this.getDivBean().setXpath(path);
                    break;
            }
        }
    }

    public void setPanelType(PanelType panelType) {
        this.panelType = panelType;
    }

    public CustomDivBean getDivBean() {

        if (panelType != null && !panelType.equals(PanelType.div)) {
            return null;
        }

        if (divBean == null) {
            if (this.getMethod() != null) {
                Set<Annotation> annotations = AnnotationUtil.getAllAnnotations(this.getMethod(), true);
                divBean = new CustomDivBean(annotations);
            } else {
                divBean = new CustomDivBean();
                AnnotationUtil.fillDefaultValue(DivAnnotation.class, dialogBean);
            }
        }
        ContainerBean containerBean = this.getContainerBean();
        if (containerBean != null) {
            divBean.setContainerBean(containerBean);
        }
        return divBean;
    }

    public void setDivBean(CustomDivBean divBean) {
        this.divBean = divBean;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof CustomModuleBean) {
            return ((CustomModuleBean) obj).getId().equals(this.getId());
        }
        return super.equals(obj);
    }

    public List<JavaSrcBean> getJavaSrcBeans() {
        return javaSrcBeans;
    }

    public void setJavaSrcBeans(List<JavaSrcBean> javaSrcBeans) {
        this.javaSrcBeans = javaSrcBeans;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public ContainerBean getContainerBean() {
        if (containerBean == null && methodConfig != null) {
            containerBean = methodConfig.getView().getContainerBean();
        }
        return containerBean;
    }

    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }

    @Override
    public int compareTo(CustomModuleBean o) {
        if (index == null) {
            return -1;
        }

        if (index != null && o.getIndex() != null) {
            return this.index - o.getIndex();
        }

        return 1;
    }
}
