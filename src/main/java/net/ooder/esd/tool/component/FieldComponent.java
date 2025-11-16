package net.ooder.esd.tool.component;

import net.ooder.common.EventKey;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esb.util.EsbFactory;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.action.CustomFieldAction;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.event.CustomHotKeyEvent;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.RequestPathTypeEnum;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.bean.ComponentBean;
import net.ooder.esd.bean.FieldEventBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.RightContextMenuBean;
import net.ooder.esd.bean.field.CustomListBean;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.bean.field.combo.ComboxFieldBean;
import net.ooder.esd.bean.view.CustomFormViewBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.action.CustomAPICallAction;
import net.ooder.esd.custom.action.SetCustomQueryDataAction;
import net.ooder.esd.custom.action.ShowPageAction;
import net.ooder.esd.custom.component.CustomContextBar;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.manager.editor.PluginsFactory;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;
import net.ooder.esd.tool.properties.FieldProperties;
import net.ooder.esd.tool.properties.UrlPathData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FieldComponent<T extends FieldProperties, K extends EventKey> extends Component<T, K> {
    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, FieldComponent.class);

    public FieldComponent(ComponentType componentType, String alias) {
        super(componentType, alias);
    }

    public FieldComponent(ComponentType componentType) {
        super(componentType);
    }

    public FieldComponent(String alias) {
        super(ComponentType.UI, alias);
    }


    public void initEditor(EUModule euModule, FieldFormConfig field, K eventKey) {
        try {

            FieldBean fieldBean = field.getFieldBean();
            Class service = fieldBean.getServiceClass();
            if (checkExpression(euModule, field)) {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(service.getName());
                if (apiClassConfig != null) {
                    this.getProperties().setExpression(fieldBean.getExpression());
                    MethodConfig methodBean = apiClassConfig.getMethodByName(eventKey.getEvent());
                    String alias = apiClassConfig.getSimpleName() + "_" + methodBean.getMethodName();
                    APICallerComponent component = (APICallerComponent) euModule.getComponent().findComponentByAlias(alias);
                    if (component == null) {
                        component = new APICallerComponent(methodBean);
                        component.setAlias(alias);
                        if (euModule.getComponent().getTopComponentBox() != null) {
                            UrlPathData treepathData = new UrlPathData(euModule.getComponent().getTopComponentBox().getAlias(), RequestPathTypeEnum.FORM, "");
                            component.getProperties().addRequestData(treepathData);
                        }
                        euModule.getComponent().addChildren(component);
                    }
                    SetCustomQueryDataAction setParamsAction = new SetCustomQueryDataAction(component.getAlias(), "fieldName", field.getFieldname(), eventKey);
                    this.addAction(setParamsAction);
                    Action callAction = new CustomAPICallAction(component, eventKey);
                    this.addAction(callAction);


                } else {
                    logger.warn(" input editor err in  class[" + field.getViewClassName() + "][" + field.getFieldname() + "]");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initFilter(EUModule euModule, FieldFormConfig field, CustomListBean customListBean) {
        try {
            Class clazz = field.getFieldBean().getServiceClass();
            if ((customListBean.getItemsExpression() != null && !customListBean.getItemsExpression().equals(""))
                    || (customListBean.getFilter() != null && !customListBean.getFilter().equals(""))) {
                if (clazz != null && !clazz.equals(Void.class)) {
                    ApiClassConfig config = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(clazz.getName());
                    if (config != null) {
                        MethodConfig methodBean = config.getMethodByEvent(CustomFieldEvent.DYNRELOAD);
                        String apiAlias = methodBean.getSourceClass().getCtClass().getSimpleName() + "_" + CustomFieldEvent.DYNRELOAD.getType();
                        APICallerComponent apiCallerComponent = (APICallerComponent) euModule.getComponent().findComponentByAlias(apiAlias);
                        if (apiCallerComponent == null) {
                            apiCallerComponent = new APICallerComponent(methodBean);
                            if (euModule.getComponent().getTopComponentBox() != null) {
                                UrlPathData treepathData = new UrlPathData(euModule.getComponent().getTopComponentBox().getAlias(), RequestPathTypeEnum.FORM, "");
                                apiCallerComponent.getProperties().addRequestData(treepathData);
                            }
                            apiCallerComponent.setAlias(apiAlias);
                            euModule.getComponent().addChildren(apiCallerComponent);

                        }
                        SetCustomQueryDataAction setParamsAction = new SetCustomQueryDataAction(apiCallerComponent.getAlias(), "fieldName", this.getAlias(), FieldEventEnum.onClick);
                        this.addAction(setParamsAction);
                        Action callAction = new CustomAPICallAction(apiCallerComponent, false, FieldEventEnum.onClick);

                        this.addAction(callAction);

                    } else {
                        logger.warn(" input editor err in  class[" + field.getViewClassName() + "][" + field.getFieldname() + "]");
                    }
                }
            }
            //   }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initEvent(EUModule euModule, FieldFormConfig field) {
        FieldBean fieldBean = field.getFieldBean();


        if (fieldBean != null && fieldBean.getServiceClass() != null) {

            for (FieldEventBean fieldEvent : fieldBean.getExtFieldEvent()) {
                List<Action> actionList = fieldEvent.getActions();
                for (Action fieldAction : actionList) {
                    this.addAction(fieldAction);
                }
            }
            try {
                ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(fieldBean.getServiceClass().getName());
                ModuleComponent moduleComponent = euModule.getComponent();
                for (CustomFieldEvent fieldEvent : fieldBean.getCustomFieldEvent()) {
                    for (CustomAction actionType : fieldEvent.getActions(false)) {
                        String target = actionType.target();
                        if (!target.startsWith(this.getAlias() + "_")) {
                            target = this.getAlias() + "_" + actionType.target();
                        }
                        Action action = new Action(actionType, fieldEvent.getEventEnum(), target);
                        if (actionType instanceof CustomFieldAction) {
                            CustomFieldAction fieldAction = (CustomFieldAction) actionType;
                            fieldAction.updateTagter(target);
                            action = new Action(fieldAction, fieldEvent.getEventEnum(), target);
                        }
                        MethodConfig methodConfig = apiClassConfig.getFieldEvent(fieldEvent);
                        if (methodConfig != null) {
                            if (!methodConfig.isModule()) {
                                if (moduleComponent.findComponentByAlias(target) == null) {
                                    APICallerComponent apiCallerComponent = new APICallerComponent(methodConfig);
                                    apiCallerComponent.setAlias(target);
                                    moduleComponent.addChildren(apiCallerComponent);
                                }
                            } else {
                                action = new ShowPageAction(methodConfig, fieldEvent.getEventEnum());
                                action.updateArgs("{args[2]}", 6);
                                action.updateArgs("{args[3]}", 5);
                            }
                        }
                        this.addAction(action);
                    }
                }


                for (CustomHotKeyEvent hotKeyEvent : fieldBean.getCustomHotKeyEvent()) {
                    for (CustomAction actionType : hotKeyEvent.getActions(false)) {
                        String target = actionType.target();
                        if (!target.startsWith(this.getAlias() + "_")) {
                            target = this.getAlias() + "_" + actionType.target();
                        }
                        Action action = new Action(actionType, hotKeyEvent.getEventEnum(), target);
                        if (actionType instanceof CustomFieldAction) {
                            CustomFieldAction fieldAction = (CustomFieldAction) actionType;
                            fieldAction.updateTagter(target);
                            action = new Action(fieldAction, hotKeyEvent.getEventEnum(), target);
                        }
                        Condition condition = new Condition("{args[1]}", SymbolType.equal, hotKeyEvent.getHotKey());
                        action.addCondition(condition);
                        MethodConfig methodConfig = apiClassConfig.getHotKeyEvent(hotKeyEvent);
                        if (methodConfig != null) {
                            if (!methodConfig.isModule()) {
                                if (moduleComponent.findComponentByAlias(target) == null) {
                                    APICallerComponent apiCallerComponent = new APICallerComponent(methodConfig);
                                    apiCallerComponent.setAlias(target);
                                    moduleComponent.addChildren(apiCallerComponent);
                                }
                            } else {
                                action = new ShowPageAction(methodConfig, hotKeyEvent.getEventEnum());
                                action.updateArgs("{args[1]}", 6);
                                action.updateArgs("{args[2]}", 5);
                            }
                        }
                        this.addAction(action);
                    }
                }


            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
    }


    public void initContextMenu(EUModule euModule, FieldFormConfig field, EventKey eventKey) {
        CustomContextBar contextBar = null;
        try {
            RightContextMenuBean contextMenuBean = field.getContextMenuBean();
            if (contextMenuBean != null && contextMenuBean.getFristMenuClass() != null) {
                if (!contextMenuBean.getFristMenuClass().equals(Void.class)) {
                    try {
                        Class contextMenu = contextMenuBean.getFristMenuClass();
                        contextBar = PluginsFactory.getInstance().initMenuClass(contextMenu, CustomContextBar.class);
                        contextBar.bindFieldAction(this, euModule.getComponent(), field, eventKey);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private boolean checkExpression(EUModule euModule, FieldFormConfig field) {
        boolean canAddEvent = false;
        MethodConfig methodConfig = euModule.getComponent().getMethodAPIBean();
        if (methodConfig.getView() instanceof CustomFormViewBean) {
            CustomFormViewBean viewBean = (CustomFormViewBean) methodConfig.getView();
            List<FieldFormConfig> fieldFormConfigs = viewBean.getCustomFields();

            Set<String> names = new HashSet<>();
            for (FieldFormConfig fieldFormConfig : fieldFormConfigs) {
                names.add(fieldFormConfig.getFieldname());
                if (fieldFormConfig.getCustomBean() != null && fieldFormConfig.getCustomBean().getTarget() != null) {
                    names.add(fieldFormConfig.getCustomBean().getTarget());
                }
            }

            for (FieldFormConfig fieldFormConfig : fieldFormConfigs) {
                FieldBean fieldBean = fieldFormConfig.getFieldBean();
                Class service = fieldBean.getServiceClass();
                ComponentBean widgetBean = fieldFormConfig.getWidgetConfig();
                CustomListBean customListBean = null;
                if (widgetBean instanceof ComboxFieldBean) {
                    customListBean = ((ComboxFieldBean) widgetBean).getListBean().getCustomListBean();
                }


                if (customListBean != null) {
                    if (customListBean.getItemsExpression() != null && !customListBean.getItemsExpression().equals("") && !canAddEvent) {
                        String expression = EsbFactory.macrosExpression(customListBean.getItemsExpression(), names);
                        if (expression.contains("$Custom['" + field.getFieldname() + "']") || expression.contains("$Custom['" + field.getCustomBean().getTarget() + "']")) {
                            canAddEvent = true;
                        }
                    }
                    if (customListBean.getFilter() != null && !customListBean.getFilter().equals("") && !canAddEvent) {
                        String filter = EsbFactory.macrosExpression(customListBean.getFilter(), names);
                        if (filter.contains("$Custom['" + field.getFieldname() + "']") || filter.contains("$Custom['" + field.getCustomBean().getTarget() + "']")) {
                            canAddEvent = true;
                        }
                    }
                }

                if (service != null && !service.equals(Void.class)
                        && fieldBean != null
                        && !canAddEvent
                        && fieldBean.getExpression() != null
                        && !fieldBean.getExpression().equals("")) {


                    String expression = EsbFactory.macrosExpression(fieldBean.getExpression(), names);
                    if (expression.contains("$Custom['" + field.getFieldname() + "']") || expression.contains("$Custom['" + field.getCustomBean().getTarget() + "']")) {
                        canAddEvent = true;
                    }
                }
            }
        }

        return canAddEvent;
    }

}
