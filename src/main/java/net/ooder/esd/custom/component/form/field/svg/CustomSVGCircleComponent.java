package net.ooder.esd.custom.component.form.field.svg;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.svg.SVGCircleBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.SVGCircleComponent;
import net.ooder.esd.tool.properties.svg.comb.Key;
import net.ooder.esd.tool.properties.svg.comb.circle.CircleProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomSVGCircleComponent extends SVGCircleComponent {


    public CustomSVGCircleComponent(EUModule euModule, FieldFormConfig<SVGCircleBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getId() + ComponentType.SVGCIRCLE.getType());
        SVGCircleBean svgPathBean = field.getWidgetConfig().clone();
        if (value != null && value instanceof Key) {
            String json = JSON.toJSONString(value);
            String obj = (String) TemplateRuntime.eval(json, value);
            OgnlUtil.setProperties(JSON.parseObject(obj,Map.class), svgPathBean.getSvgKey(), false, false);
            Key svgKey = (Key) value;
            if (svgKey.getTitle() != null && !svgKey.getTitle().equals("")) {
                svgPathBean.getSvgText().setTitle(svgKey.getTitle());
            }
            if (svgKey.getText() != null && !svgKey.getText().equals("")) {
                svgPathBean.getSvgText().setText(svgKey.getText());
            }
            this.setAlias(svgKey.getId());
        }


        CircleProperties properties = new CircleProperties(svgPathBean);
        properties.setId(field.getId());
        properties.setName(field.getFieldname());
        properties.setDesc(field.getAggConfig().getCaption());

        this.setProperties(properties);
        this.setTarget(target);

    }


}
