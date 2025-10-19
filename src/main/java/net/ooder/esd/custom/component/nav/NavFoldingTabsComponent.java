package net.ooder.esd.custom.component.nav;


import net.ooder.common.JDSException;
import net.ooder.config.ResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.action.CustomPageAction;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomFoldingTabsFieldBean;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.bean.view.NavFoldingTabsViewBean;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.custom.properties.NavFoldingTabsProperties;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.FoldingTabsComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.RequestParamBean;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NavFoldingTabsComponent extends FoldingTabsComponent {
    public NavFoldingTabsComponent(EUModule euModule, FieldFormConfig<CustomFoldingTabsFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super();
        try {
            this.setAlias(field.getMethodName());
            this.init(field.getMethodConfig(), valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public NavFoldingTabsComponent(EUModule module, MethodConfig methodConfig, Map<String, ?> valueMap) {
        super();
        try {
            this.setAlias(methodConfig.getMethodName());
            this.init(methodConfig, valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }


    public NavFoldingTabsComponent(NavFoldingTabsViewBean tabsViewBean, Map<String, Object> valueMap) {
        super();
        try {
            this.setAlias(tabsViewBean.getMethodName());
            init(tabsViewBean, valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public void init(MethodConfig methodConfig, Map<String, ?> valueMap) throws JDSException {
        NavFoldingTabsViewBean tabsViewBean = (NavFoldingTabsViewBean) methodConfig.getView();
        this.init(tabsViewBean, valueMap);

    }


    void init(NavFoldingTabsViewBean tabsViewBean, Map<String, ?> valueMap) throws JDSException {
        this.setProperties(new NavFoldingTabsProperties(tabsViewBean, valueMap));

        if (tabsViewBean.getAutoSave() != null && tabsViewBean.getAutoSave()) {
            Action saveAction = new Action(CustomPageAction.AUTOSAVE, TabsEventEnum.beforePageClose);
            this.addAction(saveAction, false);
        }
        List<TabItemBean> childTabViewBeans = tabsViewBean.getItemBeans();

        this.fillComponent(childTabViewBeans, valueMap);

        if (tabsViewBean.getLazyAppend() == null || tabsViewBean.getLazyAppend()) {
            Action showAction = new Action(CustomLoadClassAction.tabShow, TabsEventEnum.onItemSelected);
            showAction.updateArgs(this.getAlias(), 4);
            if (tabsViewBean != null && tabsViewBean.getAutoReload()) {
                if (!this.getEvents().containsKey(TabsEventEnum.onItemSelected)) {
                    this.addAction(showAction);
                }
            } else {
                if (!this.getEvents().containsKey(TabsEventEnum.onIniPanelView)) {
                    showAction.setEventKey(TabsEventEnum.onIniPanelView);
                    this.addAction(showAction);
                }
            }

            NavFoldingTabsProperties viewsProperties = this.getProperties();

            if (viewsProperties.getItems().size() > 0) {
                Action clickItemAction = new Action(TabsEventEnum.onRender);
                clickItemAction.setType(ActionTypeEnum.control);
                clickItemAction.setTarget(this.getAlias());
                clickItemAction.setDesc("初始化");
                clickItemAction.setMethod("fireItemClickEvent");
                clickItemAction.setArgs(Arrays.asList(new String[]{viewsProperties.getFristId()}));
                if (!this.getEvents().containsKey(TabsEventEnum.onRender)) {
                    this.addAction(clickItemAction);
                }
            }
        }


    }

    private void fillComponent(List<TabItemBean> childTabViewBeans, Map<String, ?> valueMap) throws JDSException {
        for (TabItemBean childTabViewBean : childTabViewBeans) {
            MethodConfig childMethodConfig = childTabViewBean.getMethodConfig();
            if (childMethodConfig != null) {
                ModuleViewType moduleViewType = childMethodConfig.getView().getModuleViewType();
                EUModule childModule = childMethodConfig.getModule(valueMap, DSMFactory.getInstance().getDefaultProjectName());
                if (childTabViewBean.getLazyAppend() != null && !childTabViewBean.getLazyAppend()) {
                    Component dataComponent = childModule.getComponent().getCurrComponent().clone();
                    dataComponent.setAlias(childTabViewBean.getGroupName() + "_" + ComponentType.FOLDINGTABS.name());
                    dataComponent.setTarget(childTabViewBean.getId());
                    if (dataComponent != null && dataComponent instanceof DataComponent) {
                        if (childMethodConfig.getApi().getAutoRun() != null && childMethodConfig.getApi().getAutoRun()) {
                            ResultModel resultModel = null;
                            try {
                                Map contextMap = JDSActionContext.getActionContext().getContext();
                                contextMap.putAll(childMethodConfig.getTagVar());
                                if (childTabViewBean.getConstructorBean() != null && childTabViewBean.getTabItem() != null) {
                                    Object object = childTabViewBean.getConstructorBean().invok(childTabViewBean.getTabItem());
                                    for (RequestParamBean requestParamBean : childMethodConfig.getRequestMethodBean().getParamSet()) {
                                        Object value = OgnlUtil.getValue(requestParamBean.getParamName(), contextMap, object);
                                        contextMap.put(requestParamBean.getParamName(), value);
                                    }
                                }
                                ;
                                JDSActionContext.getActionContext().getContext().put(CustomViewFactory.MethodBeanKey, childMethodConfig);
                                resultModel = (ResultModel) childMethodConfig.getRequestMethodBean().invok(JDSActionContext.getActionContext().getOgnlContext(), contextMap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ((DataComponent) dataComponent).setData(resultModel.get());
                        }
                    }
                    this.addChildren(dataComponent);

                } else if (moduleViewType.equals(ModuleViewType.DYNCONFIG)) {
                    Component component = childModule.getComponent().getTopComponentBox();
                    if (this.getModuleComponent().findComponentByAlias(component.getAlias()) == null) {
                        this.addChildren(component);
                    }
                    List<Component> apiComponents = childModule.getComponent().findComponents(ComponentType.APICALLER, null);
                    for (Component apiComponent : apiComponents) {
                        if (this.getModuleComponent().findComponentByAlias(component.getAlias()) == null) {
                            childModule.getComponent().addChildren(apiComponent);
                        }
                    }

                }
            }
        }
    }


}
