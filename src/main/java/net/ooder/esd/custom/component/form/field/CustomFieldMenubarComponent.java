package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.NavMenuBarViewBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.MenuBarComponent;
import net.ooder.esd.tool.properties.MenuBarProperties;

import java.util.HashMap;
import java.util.Map;

public class CustomFieldMenubarComponent extends MenuBarComponent {
    @JSONField(serialize = false)
    EUModule euModule;


    public CustomFieldMenubarComponent(EUModule euModule, FieldFormConfig<NavMenuBarViewBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        NavMenuBarViewBean navTabsViewBean = (NavMenuBarViewBean) field.getMethodConfig().getView();
        this.setAlias(field.getFieldname());

        Component component = field.getWidgetConfig().getComponent();
        if (component != null) {
            if (component.getCS() != null) {
                this.CS = component.getCS();
            }
            this.events = component.getEvents();
        }

        init(euModule, navTabsViewBean, valueMap);
        this.setTarget(target);
    }

    public CustomFieldMenubarComponent(EUModule euModule, MethodConfig methodConfig, Map dbMap) {
        NavMenuBarViewBean customComponentViewBean = (NavMenuBarViewBean) methodConfig.getView();
        this.setAlias(methodConfig.getFieldName());
        init(euModule, customComponentViewBean, dbMap);
    }


    void init(EUModule euModule, NavMenuBarViewBean navTabsViewBean, Map dbMap) {
        MenuBarProperties properties = new MenuBarProperties(navTabsViewBean);
        this.setProperties(properties);
        net.ooder.esd.tool.properties.CS cs = new   net.ooder.esd.tool.properties.CS();
        HashMap borderMap = new HashMap();
        borderMap.put("background-color", "#3498DB");
        borderMap.put("border", "0px");
        cs.setBORDER(borderMap);

        HashMap listMap = new HashMap();
        listMap.put("padding", "0px");
        cs.setLIST(listMap);

        HashMap itemMap = new HashMap();
        itemMap.put("margin", "0.26em 1em 0em 1em");
        // itemMap.put("margin", "0 1em -0.125em 1em");
        itemMap.put("padding-left", "1em");
        itemMap.put("padding-right", "1em");
        cs.setITEM(itemMap);

        HashMap itemsMap = new HashMap();
        itemsMap.put("color", "#f6f6f6");
        itemsMap.put("background-color", "#3498DB");
        cs.setITEMS(itemsMap);
        this.setCS(cs);


    }


}
