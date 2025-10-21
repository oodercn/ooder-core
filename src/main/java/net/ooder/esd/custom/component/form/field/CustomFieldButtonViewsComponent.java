package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomButtonViewsFieldBean;
import net.ooder.esd.bean.view.CustomButtonViewsViewBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.component.CustomButtonViewsComponent;
import net.ooder.esd.custom.properties.ButtonViewsListItem;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.ButtonViewsProperties;

import java.util.List;
import java.util.Map;

public class CustomFieldButtonViewsComponent extends CustomButtonViewsComponent {
    @JSONField(serialize = false)
    EUModule euModule;


    public CustomFieldButtonViewsComponent(EUModule euModule, FieldFormConfig<CustomButtonViewsFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super(euModule, field, target, value, valueMap);
        this.setAlias(field.getFieldname());
        this.euModule = euModule;
        CustomButtonViewsViewBean buttonViewsViewBean = null;
        CustomButtonViewsFieldBean buttonViewsFieldBean = field.getWidgetConfig();
        if (buttonViewsFieldBean != null && buttonViewsFieldBean.getViewBean() != null) {
            buttonViewsViewBean = buttonViewsFieldBean.getViewBean();
        } else {
            buttonViewsViewBean = (CustomButtonViewsViewBean) field.getMethodConfig().getView();
        }

        Component component = buttonViewsFieldBean.getComponent();
        if (component != null) {
            if (component.getCS() != null) {
                this.CS = component.getCS();
            }
            this.events = component.getEvents();
        }


        init(buttonViewsViewBean, valueMap);
        this.setTarget(target);

    }

    public CustomFieldButtonViewsComponent(EUModule euModule, MethodConfig methodConfig, Map dbMap) {
        super(euModule, methodConfig, dbMap);
        this.euModule = euModule;
        CustomButtonViewsViewBean buttonViewsViewBean = (CustomButtonViewsViewBean) methodConfig.getView();
        this.setAlias(methodConfig.getFieldName());
        init(buttonViewsViewBean, dbMap);
    }


    void init(CustomButtonViewsViewBean buttonViewsViewBean, Map contextMap) {
        ButtonViewsProperties blockProperties = new ButtonViewsProperties(buttonViewsViewBean, contextMap);
        String projectName = this.euModule.getProjectVersion().getProjectName();
        List<ButtonViewsListItem> buttonViewsListItems = buttonViewsViewBean.getTabItems();
        List<CustomModuleBean> moduleBeans = buttonViewsViewBean.getModuleBeans();
        if (moduleBeans.size() > 0) {
            for (CustomModuleBean moduleBean : moduleBeans) {
                this.addChildren(moduleBean.getModuleComponent());
            }
        } else if (buttonViewsListItems.size() > 0) {
            for (ButtonViewsListItem buttonViewsListItem : buttonViewsListItems) {
                try {
                    EUModule newmodule = null;
                    if (buttonViewsListItem.getEuClassName() != null) {
                        newmodule = ESDFacrory.getAdminESDClient().getModule(buttonViewsListItem.getEuClassName(), projectName);
                        if (newmodule == null) {
                            if (newmodule == null) {
                                newmodule = CustomViewFactory.getInstance().getView(buttonViewsListItem.getEuClassName(), projectName);
                            }
                            if (newmodule == null) {
                                MethodConfig methodConfig = CustomViewFactory.getInstance().getMethodAPIBean(buttonViewsListItem.getEuClassName(), projectName);
                                newmodule = CustomViewFactory.getInstance().buildView(methodConfig, projectName, contextMap, true);
                            }

                            if (newmodule != null && newmodule.getComponent() != null && newmodule.getComponent().getCurrComponent() != null) {
                                newmodule.getComponent().getCurrComponent().setTarget(buttonViewsListItem.getId());
                                newmodule.getComponent().getCurrComponent().setAlias(newmodule.getComponent().getAlias());
                                this.addChildren(newmodule.getComponent().getCurrComponent());
                            }
                        }
                    } else if (buttonViewsListItem.getBindClass() != null) {
                        for(Class clazz:buttonViewsListItem.getBindClass()){
                            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(clazz.getName());
                            MethodConfig methodConfig = apiClassConfig.findEditorMethod();
                            if (methodConfig != null) {
                                newmodule = ESDFacrory.getAdminESDClient().getCustomModule(methodConfig, projectName, contextMap);
                                if (newmodule == null) {
                                    newmodule = CustomViewFactory.getInstance().getView(buttonViewsListItem.getEuClassName(), projectName);
                                }
                                if (newmodule == null) {
                                    newmodule = CustomViewFactory.getInstance().buildView(methodConfig, projectName, contextMap, true);
                                }
                            }
                            if (newmodule != null && newmodule.getComponent() != null && newmodule.getComponent().getCurrComponent() != null) {
                                newmodule.getComponent().getCurrComponent().setTarget(buttonViewsListItem.getId());
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
