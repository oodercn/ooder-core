package net.ooder.esd.custom.component.form.field;

import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.bean.field.CustomDivFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.DivComponent;
import net.ooder.esd.tool.properties.DivProperties;

import java.util.Map;

public class CustomDivComponent extends DivComponent {
    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomDivComponent.class);


    public CustomDivComponent(EUModule euModule, FieldFormConfig<CustomDivFieldBean, ?> field, String target, Object value, Map valueMap) {
        super();
        this.setAlias(field.getFieldname());
        CustomDivFieldBean divFieldBean = field.getWidgetConfig();
        DivProperties divPropertie = new DivProperties(divFieldBean);
        this.setProperties(divPropertie);
        initContextMenu(euModule, field, FieldEventEnum.onContextmenu);
        this.setTarget(target);
    }


}
