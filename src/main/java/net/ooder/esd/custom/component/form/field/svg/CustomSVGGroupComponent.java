package net.ooder.esd.custom.component.form.field.svg;


import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.svg.SVGGroupBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.SVGGroupComponent;
import net.ooder.esd.tool.properties.svg.SVGProperties;

import java.util.Map;

public class CustomSVGGroupComponent extends SVGGroupComponent {


    public CustomSVGGroupComponent(EUModule euModule, FieldFormConfig<SVGGroupBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getId() + ComponentType.SVGPATH.getType());
        SVGGroupBean svgPathBean = field.getWidgetConfig().clone();
        SVGProperties properties =  new SVGProperties(svgPathBean.getSvgBean());
        properties.setId(field.getId());
        properties.setName(field.getFieldname());
        properties.setDesc(field.getAggConfig().getCaption());
        
        this.setProperties(properties);
        this.setTarget(target);

    }


}
