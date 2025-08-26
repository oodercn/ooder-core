package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.bean.view.CustomLayoutViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomLayoutFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.properties.LayoutProperties;
import net.ooder.esd.tool.properties.item.LayoutListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomFieldLayoutComponent extends LayoutComponent {

    @JSONField(serialize = false)
    EUModule euModule;


    public CustomFieldLayoutComponent(EUModule euModule, FieldFormConfig<CustomLayoutFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        this.setAlias(field.getFieldname());
        this.euModule = euModule;
        CustomLayoutViewBean layoutViewBean = null;
        CustomLayoutFieldBean layoutFieldBean = field.getWidgetConfig();
        if (layoutFieldBean != null && layoutFieldBean.getViewBean() != null) {
            layoutViewBean = (CustomLayoutViewBean) layoutFieldBean.getViewBean();
        } else {
            layoutViewBean = (CustomLayoutViewBean) field.getMethodConfig().getView();
        }
        Component component = layoutFieldBean.getComponent();
        if (component != null) {
            if (component.getCS() != null) {
                this.CS = component.getCS();
            }
            this.events = component.getEvents();
        }

        init(layoutViewBean, valueMap);
        this.setTarget(target);

    }

    public CustomFieldLayoutComponent(EUModule euModule, MethodConfig methodConfig, Map dbMap) {
        this.euModule = euModule;
        CustomLayoutViewBean layoutViewBean = (CustomLayoutViewBean) methodConfig.getView();
        this.setAlias(methodConfig.getFieldName());
        init(layoutViewBean, dbMap);
    }


    void init(CustomLayoutViewBean layoutViewBean, Map dbMap) {
        LayoutProperties layoutProperties = new LayoutProperties(layoutViewBean);
        String projectName = this.euModule.getProjectVersion().getProjectName();
        List<EUModule> childModules = new ArrayList<>();
        List<LayoutListItem> layoutListItems = layoutViewBean.getTabItems();
        for (LayoutListItem layoutListItem : layoutListItems) {
            if (layoutListItem.getEuClassName() != null) {
                try {
                    EUModule newmodule = ESDFacrory.getAdminESDClient().getModule(layoutListItem.getEuClassName(), projectName);
                    if (newmodule == null) {
                        newmodule = CustomViewFactory.getInstance().getView(layoutListItem.getEuClassName(), projectName);
                    }
                    if (newmodule == null) {
                        MethodConfig methodConfig = CustomViewFactory.getInstance().getMethodAPIBean(layoutListItem.getEuClassName(), projectName);
                        newmodule = CustomViewFactory.getInstance().buildView(methodConfig, projectName, dbMap, true);
                        childModules.add(newmodule);
                    } else {
                        childModules.add(newmodule);
                    }
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }


        List<FieldModuleConfig> moduleList = layoutViewBean.getNavItems();
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
        this.setProperties(layoutProperties);
    }


}
