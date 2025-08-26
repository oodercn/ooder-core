package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.bean.field.CustomButtonLayoutFieldBean;
import net.ooder.esd.bean.view.CustomButtonLayoutViewBean;
import net.ooder.esd.custom.component.nav.NavButtonLayoutComponent;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.ButtonLayoutProperties;

import java.util.Map;

public class CustomFieldButtonLayoutComponent extends NavButtonLayoutComponent {
    @JSONField(serialize = false)
    EUModule euModule;


    public CustomFieldButtonLayoutComponent(EUModule euModule, FieldFormConfig<CustomButtonLayoutFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super(euModule, field, target, value, valueMap);
        this.setAlias(field.getFieldname());
        this.euModule = euModule;
        CustomButtonLayoutViewBean buttonLayoutViewBean = null;
        CustomButtonLayoutFieldBean buttonViewsFieldBean = field.getWidgetConfig();
        if (buttonViewsFieldBean != null && buttonViewsFieldBean.getViewBean() != null) {
            buttonLayoutViewBean = buttonViewsFieldBean.getViewBean();
        } else {
            buttonLayoutViewBean = (CustomButtonLayoutViewBean) field.getMethodConfig().getView();
        }

        Component component = buttonViewsFieldBean.getComponent();
        if (component != null) {
            if (component.getCS() != null) {
                this.CS = component.getCS();
            }
            this.events = component.getEvents();
        }

        init(buttonLayoutViewBean, valueMap);
        this.setTarget(target);

    }


    void init(CustomButtonLayoutViewBean buttonViewsViewBean, Map contextMap) {
        ButtonLayoutProperties blockProperties = new ButtonLayoutProperties(buttonViewsViewBean);
        this.setProperties(blockProperties);
    }


    @Override
    public void append(EUModule parentModule, ModuleComponent component, String target) {
        super.append(parentModule, component, target);
    }

}
