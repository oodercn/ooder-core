package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.field.ImageFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ImageComponent;
import net.ooder.esd.tool.properties.form.ImageProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomImageComponent extends ImageComponent {


    public CustomImageComponent(EUModule euModule, FieldFormConfig<ImageFieldBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getFieldname());
        ImageFieldBean fieldBean = field.getWidgetConfig();
        ImageProperties properties = new ImageProperties(fieldBean,field.getContainerBean());

        if (value != null) {
            if (value instanceof ImageFieldBean) {
                String json = JSON.toJSONString(value);
                String obj = (String) TemplateRuntime.eval(json, value);
                OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), fieldBean, false, false);
            } else if (value instanceof String && !value.equals("")) {
                properties.setSrc((String) value);
            }
        }
        properties.setId(field.getId());
        properties.setName(field.getFieldname());
        this.setProperties(properties);
        this.setTarget(target);
    }


}
