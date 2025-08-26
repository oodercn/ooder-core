package net.ooder.esd.custom.component.form.field.combo;

import com.alibaba.fastjson.JSON;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.bean.field.combo.ComboNumberFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomComboNumberComponent extends ComboInputComponent {

    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomComboNumberComponent.class);

    public CustomComboNumberComponent(EUModule euModule, FieldFormConfig<ComboInputFieldBean, ComboNumberFieldBean> field, String target, Object value, Map<String, Object> valueMap) {
        super(field);
        ComboInputProperties comboInputProperties = this.getProperties();
        ComboNumberFieldBean numberFieldBean = field.getComboConfig();
        ComboInputFieldBean inputFieldBean = field.getWidgetConfig();

        if (value != null) {
            if (value instanceof ComboInputFieldBean) {
                String json = JSON.toJSONString(value);
                String obj = (String) TemplateRuntime.eval(json, value);
                OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), inputFieldBean, false, false);
            } else if (value instanceof ComboNumberFieldBean) {
                String json = JSON.toJSONString(value);
                String obj = (String) TemplateRuntime.eval(json, value);
                OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), numberFieldBean, false, false);
            }
        }
        if (value != null && (value instanceof String) && !value.equals("")) {
            comboInputProperties.setValue(value);
        } else if (valueMap != null) {
            value = valueMap.get(field.getFieldname());
            if (value != null && !value.equals("")) {
                comboInputProperties.setValue(value);
            }
        }

        initEvent(euModule, field);
        initContextMenu(euModule, field, FieldEventEnum.onContextmenu);
        initEditor(euModule, field, FieldEventEnum.onChange);


        this.setTarget(target);
    }


}
