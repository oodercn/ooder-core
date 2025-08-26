package net.ooder.esd.custom.component.form.field.svg;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.svg.SVGEllipseBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.SVGEllipseComponent;
import net.ooder.esd.tool.properties.svg.comb.Key;
import net.ooder.esd.tool.properties.svg.ellipse.EllipseProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomSVGEllipseComponent extends SVGEllipseComponent {


    public CustomSVGEllipseComponent(EUModule euModule, FieldFormConfig<SVGEllipseBean, ?> field, String target, Object value, Map valueMap)  {
        super(field.getId() + ComponentType.SVGELLIPSE.getType());
        SVGEllipseBean svgPathBean = field.getWidgetConfig().clone();
        if (value != null && value instanceof Key) {
            String json = JSON.toJSONString(value);
            String obj = (String) TemplateRuntime.eval(json, value);
            OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), svgPathBean.getSvgKey(), false, false);
            Key svgKey = (Key) value;
            if (svgKey.getTitle() != null && !svgKey.getTitle().equals("")) {
                svgPathBean.getSvgText().setTitle(svgKey.getTitle());
            }
            if (svgKey.getText() != null && !svgKey.getText().equals("")) {
                svgPathBean.getSvgText().setText(svgKey.getText());
            }
            this.setAlias(svgKey.getId());
        }
        EllipseProperties properties =  new EllipseProperties(svgPathBean);
        properties.setId(field.getId());
        properties.setName(field.getFieldname());
        properties.setDesc(field.getAggConfig().getCaption());

        this.setProperties(properties);
        this.setTarget(target);

    }


}
