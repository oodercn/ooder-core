package net.ooder.esd.custom.component;


import net.ooder.common.JDSException;
import net.ooder.esd.annotation.action.CustomLoadClassAction;
import net.ooder.esd.annotation.action.CustomPageAction;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.event.TabsEventEnum;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.field.CustomButtonViewsFieldBean;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.bean.view.CustomButtonViewsViewBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ButtonViewsComponent;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.ButtonViewsProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CustomButtonViewsComponent extends ButtonViewsComponent {


    public CustomButtonViewsComponent(EUModule euModule, FieldFormConfig<CustomButtonViewsFieldBean, ?> field, String target, Object value, Map<String, Object> valueMap) {
        super();
        try {
            CustomButtonViewsViewBean tabsViewBean = (CustomButtonViewsViewBean) field.getMethodConfig().getView();
            this.init(tabsViewBean, valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public CustomButtonViewsComponent(EUModule module, MethodConfig methodConfig, Map<String, ?> valueMap) {
        super();
        try {
            CustomButtonViewsViewBean tabsViewBean = (CustomButtonViewsViewBean) methodConfig.getView();
            this.init(tabsViewBean, valueMap);
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }


    void init(CustomButtonViewsViewBean tabsViewBean, Map<String, ?> valueMap) throws JDSException {
        ButtonViewsProperties viewsProperties = new ButtonViewsProperties(tabsViewBean, valueMap);
        this.setProperties(viewsProperties);
        if (tabsViewBean.getAutoSave() != null && tabsViewBean.getAutoSave()) {
            Action saveAction = new Action(CustomPageAction.AUTOSAVE, TabsEventEnum.beforePageClose);
            this.addAction(saveAction, false);
        }
        this.setAlias(tabsViewBean.getMethodName());
        List<TabItemBean> childTabViewBeans = tabsViewBean.getItemBeans();

        this.fillComponent(childTabViewBeans, valueMap);

        if (tabsViewBean.getLazyAppend() == null || tabsViewBean.getLazyAppend()) {
            Action showAction = new Action(CustomLoadClassAction.tabShow, TabsEventEnum.onItemSelected);
            showAction.updateArgs(this.getAlias(), 4);
            if (tabsViewBean != null && tabsViewBean.getAutoReload() != null && tabsViewBean.getAutoReload()) {
                this.addAction(showAction);
            } else {
                showAction.setEventKey(TabsEventEnum.onIniPanelView);
                this.addAction(showAction);
            }
            if (viewsProperties.getItems().size() > 0) {
                Action clickItemAction = new Action(TabsEventEnum.onRender);
                clickItemAction.setType(ActionTypeEnum.control);
                clickItemAction.setTarget(this.getAlias());
                clickItemAction.setDesc("初始化");
                clickItemAction.setMethod("fireItemClickEvent");
                clickItemAction.setArgs(Arrays.asList(new String[]{viewsProperties.getFristId()}));
                this.addAction(clickItemAction);
            }
        }

    }


}
