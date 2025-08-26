package net.ooder.esd.custom.component.form.field.svg.comb;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.svg.SVGRectAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.bean.svg.SVGRectBean;
import net.ooder.esd.bean.svg.comb.SVGRectCombBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.SVGRectCombComponent;
import net.ooder.esd.tool.properties.svg.comb.Key;
import net.ooder.esd.tool.properties.svg.comb.rect.RectProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomSVGRectCombComponent extends SVGRectCombComponent{
    public CustomSVGRectCombComponent(EUModule euModule, FieldFormConfig<SVGRectCombBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getId());
        SVGRectCombBean svgPathBean = field.getWidgetConfig().clone();
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
        RectProperties properties =  new RectProperties(svgPathBean);
        this.setProperties(properties);
        this.setTarget(target);

    }


}
