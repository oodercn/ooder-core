package net.ooder.esd.custom.properties;


import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.NavFoldingComboViewBean;
import net.ooder.esd.bean.view.NavFoldingTabsViewBean;
import net.ooder.esd.bean.view.TabsViewBean;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.web.RequestParamBean;

import java.util.*;


public class NavFoldingTabsProperties extends NavTabsProperties<NavFoldingTabsListItem> {

    public NavFoldingTabsProperties() {

    }

    public NavFoldingTabsProperties(NavFoldingComboViewBean navTabsViewBean, Map<String, ?> valueMap) {
        init(navTabsViewBean, valueMap);
    }
    public NavFoldingTabsProperties(NavFoldingTabsViewBean navTabsViewBean, Map<String, ?> valueMap) {
        init(navTabsViewBean, valueMap);
    }

    void init(NavFoldingTabsViewBean navTabsViewBean, Map<String, ?> valueMap) {
        List<FieldModuleConfig> moduleList = navTabsViewBean.getNavItems();
        this.setBorderType(BorderType.none);
        for (FieldModuleConfig itemInfo : moduleList) {
            NavFoldingTabsListItem navItemProperties = new NavFoldingTabsListItem(itemInfo);
            Map<String, Object> tagMap = new HashMap<>();
            Set<RequestParamBean> paramBeans = navTabsViewBean.getParamSet();
            for (RequestParamBean paramBean : paramBeans) {
                if (!Arrays.asList(DSMFactory.SkipParams).contains(paramBean.getParamName())) {
                    Object obj = valueMap.get(paramBean.getParamName());
                    if (obj != null && !obj.equals("")) {
                        tagMap.put(paramBean.getParamName(), obj);
                    }
                }
            }

            navItemProperties.getTagVar().putAll(tagMap);
            navItemProperties.getTagVar().putAll(itemInfo.getTagVar());
            this.addItem(navItemProperties);

        }
        if (this.getItems() != null && this.getItems().size() > 0) {
            this.setValue(this.getItems().get(0).getId());
        }
        if (navTabsViewBean.getCaption() != null && !navTabsViewBean.getCaption().equals("")) {
            this.caption = navTabsViewBean.getCaption();
        }
        if (navTabsViewBean.getImageClass() != null && !navTabsViewBean.getImageClass().equals("")) {
            this.imageClass = navTabsViewBean.getImageClass();
        }

    }



    void init(NavFoldingComboViewBean navTabsViewBean, Map<String, ?> valueMap) {
        List<FieldModuleConfig> moduleList = navTabsViewBean.getNavItems();
        this.setBorderType(BorderType.none);
        for (FieldModuleConfig itemInfo : moduleList) {
            NavFoldingTabsListItem navItemProperties = new NavFoldingTabsListItem(itemInfo);
            Map<String, Object> tagMap = new HashMap<>();
            Set<RequestParamBean> paramBeans = navTabsViewBean.getParamSet();

            for (RequestParamBean paramBean : paramBeans) {
                if (!Arrays.asList(DSMFactory.SkipParams).contains(paramBean.getParamName())) {
                    Object obj = valueMap.get(paramBean.getParamName());
                    if (obj != null && !obj.equals("")) {
                        tagMap.put(paramBean.getParamName(), obj);
                    }
                }
            }

            navItemProperties.getTagVar().putAll(tagMap);
            navItemProperties.getTagVar().putAll(itemInfo.getTagVar());
            this.addItem(navItemProperties);

        }
        if (this.getItems() != null && this.getItems().size() > 0) {
            this.setValue(this.getItems().get(0).getId());
        }
        if (navTabsViewBean.getCaption() != null && !navTabsViewBean.getCaption().equals("")) {
            this.caption = navTabsViewBean.getCaption();
        }
        if (navTabsViewBean.getImageClass() != null && !navTabsViewBean.getImageClass().equals("")) {
            this.imageClass = navTabsViewBean.getImageClass();
        }
        TabsViewBean tabsViewBean = navTabsViewBean.getTabsViewBean();

        initTab(tabsViewBean);


    }

    void initTab(TabsViewBean tabsViewBean) {
        this.activeLast = tabsViewBean.getActiveLast();
        this.closeBtn = tabsViewBean.getCloseBtn();
        this.setMessage(tabsViewBean.getMessage());
        this.selMode = tabsViewBean.getSelMode();
        this.lazyAppend = tabsViewBean.getLazyAppend();
        if (tabsViewBean.getOptBtn() != null && !tabsViewBean.getOptBtn().equals("")) {
            this.optBtn = tabsViewBean.getOptBtn();
        }
        if (tabsViewBean.getMaxHeight() != null && tabsViewBean.getMaxHeight() != -1) {
            this.maxHeight = tabsViewBean.getMaxHeight();
        }

    }


    public NavFoldingTabsProperties(MethodConfig methodConfig, Map<String, ?> valueMap) {
        this.name = methodConfig.getName();
        NavFoldingComboViewBean tabsViewBean = (NavFoldingComboViewBean) methodConfig.getView();
        init(tabsViewBean, valueMap);
    }


}
