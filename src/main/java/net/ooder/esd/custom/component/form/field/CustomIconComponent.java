package net.ooder.esd.custom.component.form.field;


import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.event.IconEventEnum;
import net.ooder.esd.bean.field.IconFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.IconComponent;
import net.ooder.esd.tool.properties.IconProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomIconComponent<T extends IconProperties, K extends IconEventEnum> extends IconComponent<T, K> {


    public CustomIconComponent(EUModule euModule, FieldFormConfig<IconFieldBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getFieldname());
        IconFieldBean iconFieldBean = field.getWidgetConfig();

        if (value != null) {
            if (value instanceof IconFieldBean) {
                String json = JSON.toJSONString(value);
                String obj = (String) TemplateRuntime.eval(json, value);
                OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), iconFieldBean, false, false);
            } else if (value instanceof String && !value.equals("")) {
                this.getProperties().setImageClass((String) value);
            }
        }


        IconProperties properties = new IconProperties(iconFieldBean,field.getContainerBean());
        properties.setId(field.getId());
        properties.setName(field.getFieldname());
        this.setProperties((T) properties);
        this.setTarget(target);

    }
}
