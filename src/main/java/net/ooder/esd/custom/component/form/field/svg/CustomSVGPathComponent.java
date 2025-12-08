package net.ooder.esd.custom.component.form.field.svg;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.svg.SVGPathBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.SVGPathComponent;
import net.ooder.esd.tool.properties.svg.comb.Key;
import net.ooder.esd.tool.properties.svg.comb.path.PathProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomSVGPathComponent extends SVGPathComponent {


    public CustomSVGPathComponent(EUModule euModule, FieldFormConfig<SVGPathBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getId());
        SVGPathBean svgPathBean = field.getWidgetConfig().clone();
        if (value != null && value instanceof Key) {
            String json = JSON.toJSONString(value);
            String obj = (String) TemplateRuntime.eval(json, value);
            Key svgKey = (Key) value;
            OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), value, false, false);
            this.setAlias(svgKey.getId());
        }
        PathProperties properties = new PathProperties(svgPathBean);
        properties.setId(field.getId());
        properties.setName(field.getFieldname());
        properties.setDesc(field.getAggConfig().getCaption());
        this.setProperties(properties);
        this.setTarget(target);

    }


}
