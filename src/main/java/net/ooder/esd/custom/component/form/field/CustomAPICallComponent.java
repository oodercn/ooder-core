package net.ooder.esd.custom.component.form.field;


import net.ooder.esd.bean.CustomAPICallBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.APICallerComponent;

import java.util.Map;

public class CustomAPICallComponent extends APICallerComponent {


    public CustomAPICallComponent(EUModule euModule, FieldFormConfig<CustomAPICallBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getMethodConfig());
    }
}
