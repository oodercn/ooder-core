package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomStacksFieldBean;
import net.ooder.esd.bean.view.StacksViewBean;
import net.ooder.esd.custom.properties.StacksListItem;
import net.ooder.esd.custom.properties.StacksProperties;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.StacksComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomFieldStacksComponent extends StacksComponent {

    @JSONField(serialize = false)
    EUModule euModule;


    public CustomFieldStacksComponent(EUModule euModule, FieldFormConfig<CustomStacksFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        this.setAlias(field.getFieldname());
        this.euModule = euModule;
        StacksViewBean stacksViewBean = null;
        CustomStacksFieldBean stacksFieldBean = field.getWidgetConfig();
        if (stacksFieldBean != null && stacksFieldBean.getViewBean() != null) {
            stacksViewBean = stacksFieldBean.getViewBean();
        } else {
            stacksViewBean = (StacksViewBean) field.getMethodConfig().getView();
        }

        Component component = field.getWidgetConfig().getComponent();
        if (component != null) {
            if (component.getCS() != null) {
                this.CS = component.getCS();
            }
            this.events = component.getEvents();
        }

        init(stacksViewBean, valueMap);
        this.setTarget(target);

    }

    public CustomFieldStacksComponent(EUModule euModule, MethodConfig methodConfig, Map dbMap) {
        this.euModule = euModule;
        StacksViewBean stacksViewBean = (StacksViewBean) methodConfig.getView();
        this.setAlias(methodConfig.getFieldName());
        init(stacksViewBean, dbMap);
    }


    void init(StacksViewBean navStacksViewBean, Map dbMap) {
        StacksProperties blockProperties = new StacksProperties(navStacksViewBean, dbMap);
        String projectName = this.euModule.getProjectVersion().getProjectName();
        List<EUModule> childModules = new ArrayList<>();
        List<StacksListItem> stacksListItems = navStacksViewBean.getTabItems();
        for (StacksListItem stacksListItem : stacksListItems) {
            if (stacksListItem.getEuClassName() != null) {
                try {
                    EUModule newmodule = ESDFacrory.getAdminESDClient().getModule(stacksListItem.getEuClassName(), projectName);
                    if (newmodule == null) {
                        newmodule = CustomViewFactory.getInstance().getView(stacksListItem.getEuClassName(), projectName);
                    }
                    if (newmodule == null) {
                        MethodConfig methodConfig = CustomViewFactory.getInstance().getMethodAPIBean(stacksListItem.getEuClassName(), projectName);
                        newmodule = CustomViewFactory.getInstance().buildView(methodConfig, projectName, dbMap, true);
                        childModules.add(newmodule);
                    }else{
                        childModules.add(newmodule);
                    }
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }


        List<FieldModuleConfig> moduleList = navStacksViewBean.getNavItems();
        for (FieldModuleConfig itemInfo : moduleList) {
            try {
                EUModule newmodule = ESDFacrory.getAdminESDClient().getCustomModule(itemInfo.getMethodConfig(), projectName, dbMap);
                if (newmodule != null) {
                    childModules.add(newmodule);
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }

        }

        for (EUModule cmodule : childModules) {
            if (cmodule != null && cmodule.getComponent() != null && cmodule.getComponent().getCurrComponent() != null) {
                this.addChildren(cmodule.getComponent().getCurrComponent());
            }

        }
        this.setProperties(blockProperties);
    }


    @Override
    public void append(EUModule parentModule, ModuleComponent component, String target) {
        super.append(parentModule, component, target);
    }
}
