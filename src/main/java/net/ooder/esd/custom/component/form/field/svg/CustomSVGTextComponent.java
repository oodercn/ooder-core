package net.ooder.esd.custom.component.form.field.svg;

import net.ooder.esd.bean.svg.SVGTextBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.SVGTextComponent;
import net.ooder.esd.tool.properties.svg.text.SVGTextProperties;

import java.util.Map;

public class CustomSVGTextComponent extends SVGTextComponent {


    public CustomSVGTextComponent(EUModule euModule, FieldFormConfig<SVGTextBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getId());
        SVGTextBean svgPathBean = field.getWidgetConfig().clone();

        SVGTextProperties properties = new SVGTextProperties(svgPathBean);
        properties.setId(field.getId());
        properties.setName(field.getFieldname());
        properties.setDesc(field.getAggConfig().getCaption());

        this.setProperties(properties);
        this.setTarget(target);

    }


}
