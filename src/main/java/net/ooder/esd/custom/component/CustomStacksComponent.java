package net.ooder.esd.custom.component;


import net.ooder.common.JDSException;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.action.CustomPageAction;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomStacksFieldBean;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.bean.view.StacksViewBean;
import net.ooder.esd.custom.properties.StacksProperties;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.StacksComponent;
import net.ooder.esd.tool.properties.Action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CustomStacksComponent extends StacksComponent {

    public CustomStacksComponent(EUModule euModule, FieldFormConfig<CustomStacksFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super();
        try {
            this.setAlias(field.getMethodName());
            this.init(field.getMethodConfig(), valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public CustomStacksComponent(EUModule module, MethodConfig methodConfig, Map<String, ?> valueMap) {
        super();
        try {
            this.setAlias(methodConfig.getMethodName());
            this.init(methodConfig, valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }


    public CustomStacksComponent(StacksViewBean tabsViewBean, Map<String, ?> valueMap) {
        super();
        try {
            this.setAlias(tabsViewBean.getMethodName());
            init(tabsViewBean, valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public void init(MethodConfig methodConfig, Map<String, ?> valueMap) throws JDSException {
        StacksViewBean tabsViewBean = (StacksViewBean) methodConfig.getView();
        this.init(tabsViewBean, valueMap);

    }


    void init(StacksViewBean tabsViewBean, Map<String, ?> valueMap) throws JDSException {
        this.setProperties(new StacksProperties(tabsViewBean, valueMap));

        if (tabsViewBean.getAutoSave() != null && tabsViewBean.getAutoSave()) {
            Action saveAction = new Action(CustomPageAction.AUTOSAVE, TabsEventEnum.beforePageClose);
            this.addAction(saveAction, false);
        }

        List<TabItemBean> childTabViewBeans = tabsViewBean.getItemBeans();

        this.fillComponent(childTabViewBeans, valueMap);

        if (tabsViewBean.getLazyAppend() == null || tabsViewBean.getLazyAppend()) {
            Action showAction = new Action(CustomLoadClassAction.tabShow, TabsEventEnum.onItemSelected);
            showAction.updateArgs(this.getAlias(), 4);
            if (tabsViewBean != null && tabsViewBean.getAutoReload()) {
                if (!this.getEvents().containsKey(TabsEventEnum.onItemSelected)) {
                    this.addAction(showAction);
                }
            } else {
                if (!this.getEvents().containsKey(TabsEventEnum.onIniPanelView)) {
                    showAction.setEventKey(TabsEventEnum.onIniPanelView);
                    this.addAction(showAction);
                }
            }

            StacksProperties viewsProperties = this.getProperties();
            if (viewsProperties.getItems().size() > 0) {
                Action clickItemAction = new Action(TabsEventEnum.onRender);
                clickItemAction.setType(ActionTypeEnum.control);
                clickItemAction.setTarget(this.getAlias());
                clickItemAction.setDesc("初始化");
                clickItemAction.setMethod("fireItemClickEvent");
                clickItemAction.setArgs(Arrays.asList(new String[]{viewsProperties.getFristId()}));
                if (!this.getEvents().containsKey(TabsEventEnum.onRender)) {
                    this.addAction(clickItemAction);
                }
            }

        }


    }
}
