package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.bean.field.base.InputFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.InputComponent;
import net.ooder.esd.tool.properties.form.InputProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomFieldInputComponent extends InputComponent {


    public CustomFieldInputComponent(EUModule euModule, FieldFormConfig<InputFieldBean, ?> field, String target, Object value, Map valueMap) {

        super(field.getFieldname());

        InputFieldBean inputFieldBean = field.getWidgetConfig();

        if (value != null && value instanceof InputFieldBean) {
            String json = JSON.toJSONString(value);
            String obj = (String) TemplateRuntime.eval(json, value);
            OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), inputFieldBean, false, false);
        }


        InputProperties inputProperties = new InputProperties(field);

        if (value != null && value instanceof String && !value.equals("")) {
            inputProperties.setValue(value);
        }

        this.setProperties( inputProperties);
        this.setTarget(target);
        initContextMenu(euModule, field, FieldEventEnum.onContextmenu);
        initEditor(euModule, field,  FieldEventEnum.onChange);
        this.initEvent(euModule, field);

    }


}
