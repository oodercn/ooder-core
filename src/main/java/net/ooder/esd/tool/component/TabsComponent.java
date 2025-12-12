package net.ooder.esd.tool.component;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.TypeUtils;
import net.ooder.common.JDSException;
import net.ooder.config.ResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.DataComponent;
import net.ooder.esd.custom.properties.NavTabListItem;
import net.ooder.esd.custom.properties.NavTabsProperties;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.properties.AbsUIProperties;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.PanelProperties;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.RequestParamBean;

import java.util.*;

public class TabsComponent<T extends NavTabsProperties> extends Component<T, TabsEventEnum> implements DataComponent<List<NavTabListItem>> {

    public TabsComponent addAction(Action<TabsEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public TabsComponent(ComponentType type, String alias) {
        super(type, alias);
    }

    public TabsComponent(String alias, T properties) {
        super(ComponentType.TABS, alias);
        this.setProperties(properties);
    }

    public TabsComponent(String alias) {
        super(ComponentType.TABS, alias);
        this.setProperties((T) new NavTabsProperties());
    }

    public void addChildComponet(Component... components) {
        for (Component component : components) {
            NavTabListItem item = null;
            if (component instanceof ModuleComponent) {
                ModuleComponent moduleComponent = (ModuleComponent) component;
                item = new NavTabListItem(component.getAlias() + "Item", moduleComponent.getTitle(), moduleComponent.getImageClass());
            } else if (component instanceof PanelComponent) {
                PanelComponent<PanelProperties> panelComponent = (PanelComponent) component;
                item = new NavTabListItem(component.getAlias() + "Item", panelComponent.getProperties().getCaption(), panelComponent.getProperties().getImageClass());
            } else {
                AbsUIProperties uiProperties = (AbsUIProperties) component.getProperties();
                item = new NavTabListItem(component.getAlias() + "Item", uiProperties.getCaption(), null);
            }

            this.getProperties().addItem(item);
            this.getProperties().setValue(((TabListItem) this.getProperties().getItems().get(0)).getId());
            component.setTarget(item.getId());
            this.addChildren(component);
        }
    }


    public TabsComponent(ComponentType type) {
        super(type);
    }

    public TabsComponent() {
        super(ComponentType.TABS);
        this.setProperties((T) new NavTabsProperties());
    }


    protected void fillComponent(List<TabItemBean> childTabViewBeans, Map<String, ?> valueMap) throws JDSException {
        if (this.getChildren() != null) {
            Set<String> moduleNameSet = new HashSet<>();
            for (Component component : this.getChildren()) {
                moduleNameSet.add(component.getAlias());
            }
            this.getChildren().clear();
            if (this.getModuleComponent() != null) {
                for (String name : moduleNameSet) {
                    this.getModuleComponent().getComponents().remove(name);
                }
            }
        }


        for (TabItemBean childTabViewBean : childTabViewBeans) {
            MethodConfig childMethodConfig = childTabViewBean.getMethodConfig();
            if (childMethodConfig != null) {
                ModuleViewType moduleViewType = childMethodConfig.getView().getModuleViewType();
                if (childTabViewBean.getLazyAppend() != null && !childTabViewBean.getLazyAppend()) {
                    EUModule childModule = childMethodConfig.getModule(valueMap, DSMFactory.getInstance().getDefaultProjectName());
                    Component dataComponent = childModule.getComponent().getCurrComponent().clone();
                    String euClassname = childModule.getComponent().getClassName();
                    String simClassName = euClassname.substring(euClassname.lastIndexOf(".") + 1);
                    dataComponent.setAlias(simClassName);
                    dataComponent.setTarget(childTabViewBean.getId());
                    if (dataComponent != null && dataComponent instanceof DataComponent) {
                        ResultModel resultModel = null;
                        try {
                            Map contextMap = JDSActionContext.getActionContext().getContext();
                            contextMap.putAll(childMethodConfig.getTagVar());
                            if (childTabViewBean.getConstructorBean() != null && childTabViewBean.getTabItem() != null) {
                                Object object = childTabViewBean.getConstructorBean().invok(childTabViewBean.getTabItem());
                                Set<RequestParamBean> paramBeanSet = childMethodConfig.getParamSet();
                                for (RequestParamBean paramBean : paramBeanSet) {
                                    if (paramBean.getParamClass().isEnum()) {
                                        Object value = OgnlUtil.getValue(paramBean.getParamName(), contextMap, object);
                                        if (value != null) {
                                            contextMap.put(paramBean.getParamName(), value);
                                            break;
                                        }
                                    }
                                }
                            } else {
                                String euClassName = childTabViewBean.getClassName();
                                String value = null;
                                if (euClassName != null && euClassName.indexOf(CustomViewFactory.INMODULE__) > -1) {
                                    String[] nameArr = euClassName.split(CustomViewFactory.INMODULE__);
                                    value = nameArr[1];
                                }

                                if (childMethodConfig != null && value != null && childMethodConfig.getRequestMethodBean().getParamSet().size() > 0) {
                                    Set<RequestParamBean> paramBeanSet = childMethodConfig.getParamSet();
                                    for (RequestParamBean paramBean : paramBeanSet) {
                                        if (paramBean.getParamClass().isEnum()) {
                                            Object obj = TypeUtils.castToJavaBean(value, paramBean.getParamClass());
                                            if (obj != null) {
                                                JDSActionContext.getActionContext().getContext().put(paramBean.getParamName(), obj);
                                                break;
                                            }
                                        }
                                    }

                                }
                            }
                            JDSActionContext.getActionContext().getContext().put(CustomViewFactory.MethodBeanKey, childMethodConfig);
                            resultModel = (ResultModel) childMethodConfig.getRequestMethodBean().invok(JDSActionContext.getActionContext().getOgnlContext(), contextMap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (resultModel != null) {
                            ((DataComponent) dataComponent).setData(resultModel.get());
                        }
                    }

                    this.addChildren(dataComponent);

                } else if (moduleViewType.equals(ModuleViewType.DYNCONFIG)) {
                    EUModule childModule = childMethodConfig.getModule(valueMap, DSMFactory.getInstance().getDefaultProjectName());
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

                } else {
                    NavTabListItem tabListItem = new NavTabListItem(childTabViewBean, valueMap);
                    String euClassname = tabListItem.getEuClassName();
                    String projectName = DSMFactory.getInstance().getDefaultProjectName();
                    EUModule realModule = ESDFacrory.getAdminESDClient().getModule(euClassname, projectName);
                    ModuleComponent moduleComponent = new ModuleComponent();
                    if (realModule == null) {
                        realModule = ESDFacrory.getAdminESDClient().createModule(euClassname, projectName);
                        moduleComponent = realModule.getComponent();
                    } else {
                        moduleComponent.setClassName(euClassname);
                    }
                    String simClassName = euClassname.substring(euClassname.lastIndexOf(".") + 1);
                    moduleComponent.setAlias(simClassName);
                    moduleComponent.setTarget(tabListItem.getId());
                    moduleComponent.getModuleVar().putAll(tabListItem.getTagVar());
                    this.addChildren(moduleComponent);
                }
            }
        }
    }

    @Override
    public void setData(List<NavTabListItem> data) {
        this.getProperties().setItems(data);
        List<TabItemBean> childTabViewBeans = new ArrayList<>();
        for (NavTabListItem navTabListItem : data) {
            TabItemBean tabItemBean = new TabItemBean(navTabListItem);
            childTabViewBeans.add(tabItemBean);
        }
        try {
            this.fillComponent(childTabViewBeans, JDSActionContext.getActionContext().getContext());
        } catch (JDSException e) {
            e.printStackTrace();
        }
        if (childTabViewBeans.size() > 0) {
            this.getProperties().setValue(childTabViewBeans.get(0).getId());
        }

    }

    @Override
    @JSONField(serialize = false)
    public List<NavTabListItem> getData() {
        return this.getProperties().getItems();
    }

}