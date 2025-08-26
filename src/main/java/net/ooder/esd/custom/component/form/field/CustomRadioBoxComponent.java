package net.ooder.esd.custom.component.form.field;

import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.field.CustomListBean;
import net.ooder.esd.bean.field.base.RadioBoxFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.RadioBoxComponent;
import net.ooder.esd.tool.properties.form.RadioBoxProperties;
import net.sf.cglib.beans.BeanMap;

import java.util.List;
import java.util.Map;

public class CustomRadioBoxComponent extends RadioBoxComponent {

    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomRadioBoxComponent.class);


    public CustomRadioBoxComponent(EUModule euModule, FieldFormConfig<RadioBoxFieldBean, ?> field, String target, Object value, Map valueMap) {
        // super(ComponentType.Div, field.getId() + "div", Dock.fill);
        super(field.getId());
        this.setTarget(target);
        RadioBoxProperties radioBoxProperties = new RadioBoxProperties();
        RadioBoxFieldBean fieldBean = field.getWidgetConfig();
        CustomListBean listFieldBean = fieldBean.getListFieldBean();

        if (listFieldBean != null) {
            BeanMap.create(radioBoxProperties).putAll(BeanMap.create(field));
        }
        BeanMap.create(radioBoxProperties).putAll(BeanMap.create(field.getAggConfig()));
        BeanMap.create(radioBoxProperties).putAll(BeanMap.create(fieldBean));
        BeanMap.create(radioBoxProperties).putAll(BeanMap.create(field));
        radioBoxProperties.setId(field.getId());
        radioBoxProperties.setName(field.getFieldname());
        radioBoxProperties.setDesc(field.getAggConfig().getCaption());
        radioBoxProperties.setCaption(field.getAggConfig().getCaption());
        radioBoxProperties.setValue(value);
        radioBoxProperties.setDock(fieldBean.getDock());
        radioBoxProperties.setReadonly(field.getAggConfig().getReadonly());
        radioBoxProperties.setDisabled(field.getAggConfig().getDisabled());

        if (listFieldBean != null) {
            radioBoxProperties.setDynLoad(listFieldBean.getDynLoad());
            List<TreeListItem> itemList = listFieldBean.getItems();
            for (TreeListItem item : itemList) {
                radioBoxProperties.addItem(item);
            }
        }
        this.setProperties(radioBoxProperties);
        initEditor(euModule, field, FieldEventEnum.onChange);

        this.setTarget(target);


    }


}
