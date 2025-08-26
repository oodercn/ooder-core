package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.JSON;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.bean.field.base.CheckBoxFieldBean;
import net.ooder.esd.bean.field.base.RichEditorFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.CheckBoxComponent;
import net.ooder.esd.tool.properties.form.CheckBoxProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomCheckBoxComponent extends CheckBoxComponent {

    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomCheckBoxComponent.class);


    public CustomCheckBoxComponent(EUModule euModule, FieldFormConfig<CheckBoxFieldBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getId());
        CheckBoxFieldBean checkBoxFieldBean = field.getWidgetConfig();
        if (value != null && value instanceof RichEditorFieldBean) {
            String json = JSON.toJSONString(value);
            String obj = (String) TemplateRuntime.eval(json, value);
            OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), checkBoxFieldBean, false, false);
        }
        CheckBoxProperties properties = new CheckBoxProperties(checkBoxFieldBean,field.getContainerBean());
        if (value != null) {
            if (value instanceof String && !value.equals("")) {
                properties.setValue(value);
            }
        } else if (valueMap != null) {
            value = valueMap.get(field.getFieldname());
            if (value != null && !value.equals("")) {
                properties.setValue(value);
            }
        }

        properties.setId(field.getId());
        properties.setName(field.getFieldname());
        properties.setValue(value);
        properties.setCaption("");
        if (field.getCustomBean() != null) {
            properties.setDesc(field.getCustomBean().getCaption());
            properties.setReadonly(field.getCustomBean().getReadonly());
            properties.setDisabled(field.getCustomBean().getDisabled());
        } else {
            properties.setDesc(field.getAggConfig().getCaption());
            properties.setReadonly(field.getAggConfig().getReadonly());
            properties.setDisabled(field.getAggConfig().getDisabled());
        }

        this.initEvent(euModule, field);
        this.setProperties( properties);
        initEditor(euModule, field, FieldEventEnum.onChange);
        this.setTarget(target);

    }


}
