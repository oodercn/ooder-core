package net.ooder.esd.custom.component.form.field;

import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.bean.field.base.ElementFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ElementComponent;
import net.ooder.esd.tool.properties.ElementProperties;

import java.util.Map;

public class CustomElementComponent extends ElementComponent {
    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomElementComponent.class);


    public CustomElementComponent(EUModule euModule, FieldFormConfig<ElementFieldBean,?> field, String target, Object value, Map valueMap) {
        super(field.getFieldname());
        ElementFieldBean comboInputFieldBean = field.getWidgetConfig();
        ElementProperties elementProperties = new ElementProperties(comboInputFieldBean,field.getContainerBean());
        this.setProperties( elementProperties);
        ElementComponent elementComponent = new ElementComponent(field.getFieldname(), elementProperties);
        initEditor(euModule, field,  FieldEventEnum.onChange);
        this.initEvent(euModule, field);
        this.addChildren(elementComponent);
        this.setTarget(target);
    }




}
