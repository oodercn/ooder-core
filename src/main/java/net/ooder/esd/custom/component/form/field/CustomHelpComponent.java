package net.ooder.esd.custom.component.form.field;

import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.action.CustomFormAction;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.field.combo.ComboInputFieldBean;
import net.ooder.esd.bean.field.combo.ComboNumberFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.ComboInputProperties;

import java.util.Map;

public class CustomHelpComponent extends ComboInputComponent {

    public CustomHelpComponent(EUModule euModule, FieldFormConfig<ComboInputFieldBean, ComboNumberFieldBean> field, String target, Object value, Map<String, Object> valueMap) {
        super(field);
        ComboInputProperties comboInputProperties = this.getProperties();
        comboInputProperties.setDock(Dock.top);
        comboInputProperties.setName(TreeListItem.ESDSearchPattern);
        comboInputProperties.setLabelCaption("查询:");
        comboInputProperties.setType(ComboInputType.helpinput);
        comboInputProperties.setLeft("3.5em");
        comboInputProperties.setTop("2em");
        comboInputProperties.setLabelSize("4em");
        comboInputProperties.setImageClass("ri-code-box-line");
        this.setProperties(comboInputProperties);
        initEvent(euModule, field);
        this.addAction( new Action(CustomFormAction.RELOAD,FieldEventEnum.onChange));

    }
}
