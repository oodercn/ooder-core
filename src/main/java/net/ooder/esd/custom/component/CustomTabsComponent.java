package net.ooder.esd.custom.component;


import net.ooder.common.JDSException;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.action.CustomPageAction;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomTabsFieldBean;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.bean.view.TabsViewBean;
import net.ooder.esd.custom.properties.NavTabsProperties;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.TabsComponent;
import net.ooder.esd.tool.properties.Action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CustomTabsComponent extends TabsComponent<NavTabsProperties> {


    public CustomTabsComponent(EUModule euModule, FieldFormConfig<CustomTabsFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super(field.getMethodConfig().getFieldName());
        try {
            this.init(field.getMethodConfig(), valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public CustomTabsComponent(EUModule module, MethodConfig methodConfig, Map<String, ?> valueMap) {
        super(methodConfig.getFieldName());
        try {
            this.init(methodConfig, valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }


    public CustomTabsComponent(TabsViewBean tabsViewBean, Map<String, Object> valueMap) {
        super(tabsViewBean.getName());
        try {
            init(tabsViewBean, valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        this.setAlias(tabsViewBean.getMethodName());
    }


    public void init(MethodConfig methodConfig, Map<String, ?> valueMap) throws JDSException {
        TabsViewBean tabsViewBean = (TabsViewBean) methodConfig.getView();
        this.init(tabsViewBean, valueMap);
    }

    void init(TabsViewBean tabsViewBean, Map<String, ?> valueMap) throws JDSException {
        NavTabsProperties viewsProperties = new NavTabsProperties(tabsViewBean, valueMap);
        this.setProperties(new NavTabsProperties(tabsViewBean, valueMap));
        if (tabsViewBean.getAutoSave() != null && tabsViewBean.getAutoSave()) {
            Action saveAction = new Action(CustomPageAction.AUTOSAVE, TabsEventEnum.beforePageClose);
            this.addAction(saveAction, false);
        }

        List<TabItemBean> childTabViewBeans = tabsViewBean.getItemBeans();

        this.fillComponent(childTabViewBeans, valueMap);


        Action showAction = new Action(CustomLoadClassAction.tabShow, TabsEventEnum.onItemSelected);
        showAction.updateArgs(this.getAlias(), 4);
        if (tabsViewBean != null && tabsViewBean.getAutoReload()) {
            if (!this.getEvents().containsKey(TabsEventEnum.onItemSelected)) {
                this.addAction(showAction, false);
            }

        } else {
            if (!this.getEvents().containsKey(TabsEventEnum.onIniPanelView)) {
                this.addAction(showAction, false);
            }
        }


        if (viewsProperties.getItems().size() > 0) {
            Action clickItemAction = new Action(TabsEventEnum.onRender);
            clickItemAction.setType(ActionTypeEnum.control);
            clickItemAction.setTarget(this.getAlias());
            clickItemAction.setDesc("初始化");
            clickItemAction.setMethod("fireItemClickEvent");
            clickItemAction.setArgs(Arrays.asList(new String[]{viewsProperties.getFristId()}));
            if (!this.getEvents().containsKey(TabsEventEnum.onRender)) {
                this.addAction(clickItemAction, false);
            }
        }

    }


}
