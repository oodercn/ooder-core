package net.ooder.esd.custom.component.form.field;


import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.field.StatusButtonsFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.StatusButtonsComponent;
import net.ooder.esd.tool.properties.form.StatusButtonsProperties;
import net.ooder.esd.tool.properties.item.CmdItem;

import java.util.List;
import java.util.Map;

public class CustomStatusButtonsComponent extends StatusButtonsComponent {


    public CustomStatusButtonsComponent(EUModule euModule, FieldFormConfig<StatusButtonsFieldBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getId());
        StatusButtonsProperties properties = new StatusButtonsProperties();
        StatusButtonsFieldBean fieldBean = field.getWidgetConfig();
        properties.setId(field.getId());
        properties.setHeight(fieldBean.getBarHeight());
        properties.setName(field.getFieldname());
        properties.setDesc(field.getAggConfig().getCaption());
        properties.setCaption(field.getAggConfig().getCaption());
        properties.setValue(value);
        properties.setDock(Dock.middle);
        properties.setReadonly(field.getAggConfig().getReadonly());
        properties.setDisabled(field.getAggConfig().getDisabled());
        List<TreeListItem> itemList = fieldBean.getCustomListBean().getItems();
        for (TreeListItem item : itemList) {
            CmdItem cmdItem=new CmdItem(item);
            properties.addItem(cmdItem);
        }
        this.setProperties(properties);
        this.setTarget(target);
        initEditor(euModule, field, FieldEventEnum.onChange);
    }
}
