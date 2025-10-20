package net.ooder.esd.custom.properties;


import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.nav.TabItemBean;
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

    public NavFoldingTabsProperties(NavFoldingTabsViewBean navTabsViewBean, Map<String, ?> valueMap) {
        init(navTabsViewBean, valueMap);
    }

    public NavFoldingTabsProperties(NavFoldingComboViewBean navTabsViewBean, Map<String, ?> valueMap) {
        init(navTabsViewBean, valueMap);
    }


    public NavFoldingTabsProperties(MethodConfig methodConfig, Map<String, ?> valueMap) {
        this.name = methodConfig.getName();
        NavFoldingComboViewBean tabsViewBean = (NavFoldingComboViewBean) methodConfig.getView();
        init(tabsViewBean, valueMap);
    }


    void init(NavFoldingTabsViewBean navTabsViewBean, Map<String, ?> valueMap) {
        this.getItems().clear();
        List<FieldModuleConfig> moduleList = navTabsViewBean.getNavItems();
        if (moduleList.size() > 0) {
            fillNavItem(moduleList, navTabsViewBean, valueMap);
        } else {
            List<TabItemBean> childTabViewBeans = navTabsViewBean.getItemBeans();
            for (TabItemBean itemInfo : childTabViewBeans) {
                NavFoldingTabsListItem navItemProperties = new NavFoldingTabsListItem(itemInfo, valueMap);
                Set<RequestParamBean> paramBeans = navTabsViewBean.getParamSet();
                Map<String, Object> tagMap = getParamMap(valueMap, paramBeans);
                navItemProperties.getTagVar().putAll(tagMap);
                this.addItem(navItemProperties);
            }
        }
        fillDefaultValue(navTabsViewBean);

    }


    void init(NavFoldingComboViewBean navTabsViewBean, Map<String, ?> valueMap) {
        this.getItems().clear();
        List<FieldModuleConfig> moduleList = navTabsViewBean.getNavItems();
        if (moduleList.size() > 0) {
            fillNavItem(moduleList, navTabsViewBean, valueMap);
        } else {
            List<NavTabListItem> childTabViewBeans = navTabsViewBean.getTabItems();
            this.setBorderType(BorderType.none);
            for (NavTabListItem itemInfo : childTabViewBeans) {
                NavFoldingTabsListItem navItemProperties = new NavFoldingTabsListItem(itemInfo);
                Set<RequestParamBean> paramBeans = navTabsViewBean.getParamSet();
                Map<String, Object> tagMap = getParamMap(valueMap, paramBeans);
                navItemProperties.getTagVar().putAll(tagMap);
                navItemProperties.getTagVar().putAll(itemInfo.getTagVar());
                this.addItem(navItemProperties);
            }
        }

        fillDefaultValue(navTabsViewBean);
        TabsViewBean tabsViewBean = navTabsViewBean.getTabsViewBean();
        initTab(tabsViewBean);

    }


    void fillNavItem(List<FieldModuleConfig> moduleList, CustomViewBean navTabsViewBean, Map<String, ?> valueMap) {
        for (FieldModuleConfig itemInfo : moduleList) {
            NavFoldingTabsListItem navItemProperties = new NavFoldingTabsListItem(itemInfo);
            Set<RequestParamBean> paramBeans = navTabsViewBean.getParamSet();
            Map<String, Object> tagMap = getParamMap(valueMap, paramBeans);
            navItemProperties.getTagVar().putAll(tagMap);
            navItemProperties.getTagVar().putAll(itemInfo.getTagVar());
            this.addItem(navItemProperties);
        }
    }

    private Map<String, Object> getParamMap(Map<String, ?> valueMap, Set<RequestParamBean> paramBeans) {
        Map<String, Object> tagMap = new HashMap<>();
        for (RequestParamBean paramBean : paramBeans) {
            if (!Arrays.asList(DSMFactory.SkipParams).contains(paramBean.getParamName())) {
                Object obj = valueMap.get(paramBean.getParamName());
                if (obj != null && !obj.equals("")) {
                    tagMap.put(paramBean.getParamName(), obj);
                }
            }
        }
        return tagMap;
    }

    ;


    void fillDefaultValue(CustomViewBean navTabsViewBean) {
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


}
