package net.ooder.esd.custom.component.form.field.combo;

import com.alibaba.fastjson.JSON;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomComboInputComponent extends ComboInputComponent {

    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomComboInputComponent.class);

    public CustomComboInputComponent(EUModule euModule, FieldFormConfig<ComboInputFieldBean, ? extends ComboBoxBean> field, String target, Object value, Map<String, Object> valueMap) {
        super(field);
        ComboInputProperties inputProperties = this.getProperties();
        ComboInputFieldBean inputFieldBean = field.getWidgetConfig();
        if (value != null) {
            if (value instanceof ComboInputFieldBean) {
                String json = JSON.toJSONString(value);
                String obj = (String) TemplateRuntime.eval(json, value);
                OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), inputFieldBean, false, false);
            } else if (value instanceof String && !value.equals("")) {
                inputProperties.setValue(value);
            }
        }
        if (value != null && (value instanceof String) && !value.equals("")) {
            inputProperties.setValue(value);
        } else if (valueMap != null) {
            value = valueMap.get(field.getFieldname());
            if (value != null && !value.equals("")) {
                inputProperties.setValue(value);
            }
        }

        inputProperties.setId(field.getId());
        inputProperties.setName(field.getFieldname());
        initEvent(euModule, field);
        initContextMenu(euModule, field, FieldEventEnum.onContextmenu);
        initEditor(euModule, field, FieldEventEnum.onChange);

        this.setTarget(target);
    }


}
