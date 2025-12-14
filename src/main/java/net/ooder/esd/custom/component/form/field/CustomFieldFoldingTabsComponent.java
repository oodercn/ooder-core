package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomFoldingTabsFieldBean;
import net.ooder.esd.bean.view.NavFoldingTabsViewBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.component.CustomFoldingTabsComponent;
import net.ooder.esd.custom.properties.NavFoldingTabsListItem;
import net.ooder.esd.custom.properties.NavFoldingTabsProperties;
import net.ooder.esd.custom.properties.NavTabListItem;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.item.TabListItem;

import java.util.List;
import java.util.Map;

public class CustomFieldFoldingTabsComponent extends CustomFoldingTabsComponent {
    @JSONField(serialize = false)
    EUModule euModule;


    public CustomFieldFoldingTabsComponent(EUModule euModule, FieldFormConfig<CustomFoldingTabsFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super(euModule, field, target, value, valueMap);
        this.setAlias(field.getFieldname());
        this.euModule = euModule;
        NavFoldingTabsViewBean tabsViewBean = null;
        CustomFoldingTabsFieldBean tabsFieldBean = field.getWidgetConfig();
        if (tabsFieldBean != null && tabsFieldBean.getViewBean() != null) {
            tabsViewBean = tabsFieldBean.getViewBean();
        } else {
            tabsViewBean = (NavFoldingTabsViewBean) field.getMethodConfig().getView();
        }

        Component component = tabsFieldBean.getComponent();
        if (component != null) {
            if (component.getCS() != null) {
                this.CS = component.getCS();
            }
            this.events = component.getEvents();
        }

        init(tabsViewBean, valueMap);
        this.setTarget(target);

    }

    public CustomFieldFoldingTabsComponent(EUModule euModule, MethodConfig methodConfig, Map dbMap) {
        super(euModule, methodConfig, dbMap);
        this.euModule = euModule;
        NavFoldingTabsViewBean tabsViewBean = (NavFoldingTabsViewBean) methodConfig.getView();
        this.setAlias(methodConfig.getFieldName());
        init(tabsViewBean, dbMap);
    }


    void init(NavFoldingTabsViewBean tabsViewBean, Map contextMap) {
        NavFoldingTabsProperties blockProperties = new NavFoldingTabsProperties(tabsViewBean, contextMap);
        String projectName = this.euModule.getProjectVersion().getProjectName();
        List<NavTabListItem> tabListItems = tabsViewBean.getTabItems();
        List<CustomModuleBean> moduleBeans = tabsViewBean.getModuleBeans();
        if (moduleBeans.size() > 0) {
            for (CustomModuleBean moduleBean : moduleBeans) {
                this.addChildren(moduleBean.getModuleComponent());
            }
        } else if (tabListItems.size() > 0) {
            for (TabListItem tabListItem : tabListItems) {
                try {
                    EUModule newmodule = null;
                    if (tabListItem.getEuClassName() != null) {
                        newmodule = ESDFacrory.getAdminESDClient().getModule(tabListItem.getEuClassName(), projectName);
                        if (newmodule == null) {
                            newmodule = CustomViewFactory.getInstance().getView(tabListItem.getEuClassName(), projectName);
                        }

                        if (newmodule == null) {
                            MethodConfig methodConfig = CustomViewFactory.getInstance().getMethodAPIBean(tabListItem.getEuClassName(), projectName);
                            newmodule = CustomViewFactory.getInstance().buildView(methodConfig, projectName, contextMap, true);
                        }
                        if (newmodule != null && newmodule.getComponent() != null && newmodule.getComponent().getCurrComponent() != null) {
                            newmodule.getComponent().getCurrComponent().setTarget(tabListItem.getId());
                            newmodule.getComponent().getCurrComponent().setAlias(newmodule.getComponent().getAlias());
                            this.addChildren(newmodule.getComponent().getCurrComponent());
                        }
                    } else if (tabListItem.getBindClass() != null) {
                        for(Class clazz:tabListItem.getBindClass()){
                            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(clazz.getName());
                            MethodConfig methodConfig = apiClassConfig.findEditorMethod();
                            if (methodConfig != null) {
                                newmodule = ESDFacrory.getAdminESDClient().getCustomModule(methodConfig, projectName, contextMap);
                                if (newmodule == null) {
                                    newmodule = CustomViewFactory.getInstance().getView(tabListItem.getEuClassName(), projectName);
                                }
                                if (newmodule == null) {
                                    newmodule = CustomViewFactory.getInstance().buildView(methodConfig, projectName, contextMap, true);
                                }
                            }
                            if (newmodule != null && newmodule.getComponent() != null && newmodule.getComponent().getCurrComponent() != null) {
                                newmodule.getComponent().getCurrComponent().setTarget(tabListItem.getId());
                                newmodule.getComponent().getCurrComponent().setAlias(newmodule.getComponent().getAlias());
                                this.addChildren(newmodule.getComponent().getCurrComponent());
                            }
                        }
                    }

                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }
        this.setProperties(blockProperties);
    }


    @Override
    public void append(EUModule parentModule, ModuleComponent component, String target) {
        super.append(parentModule, component, target);
    }

}
