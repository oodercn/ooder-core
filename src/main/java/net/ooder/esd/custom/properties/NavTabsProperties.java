package net.ooder.esd.custom.properties;


import com.alibaba.fastjson.JSON;
import net.ooder.common.util.ClassUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.bean.view.TabsViewBean;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.tool.properties.AbsUIProperties;
import net.ooder.esd.tool.properties.TabsProperties;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.RequestParamBean;

import java.util.*;


public class NavTabsProperties<T extends NavTabListItem> extends TabsProperties<T> {

    public NavTabsProperties() {

    }

    public NavTabsProperties(TabsViewBean navTabsViewBean) {
        init(navTabsViewBean, JDSActionContext.getActionContext().getContext());
    }


    public NavTabsProperties(TabsViewBean tabsViewBean, Map<String, ?> valueMap) {
        if (tabsViewBean != null) {
            init(tabsViewBean, valueMap);
        }
    }

    public NavTabsProperties(MethodConfig methodConfig, Map valueMap) {
        TabsViewBean tabsViewBean = (TabsViewBean) methodConfig.getView();
        List<FieldModuleConfig> moduleList = tabsViewBean.getNavItems();
        Map<String, Object> tagMap = new HashMap<>();
        Set<RequestParamBean> paramBeans = methodConfig.getParamSet();
        for (RequestParamBean paramBean : paramBeans) {
            Object obj = valueMap.get(paramBean.getParamName());
            if (!Arrays.asList(DSMFactory.SkipParams).contains(paramBean.getParamName())) {
                if (obj != null && !obj.equals("")) {
                    tagMap.put(paramBean.getParamName(), obj);
                }
            }
        }

        for (FieldModuleConfig itemInfo : moduleList) {
            NavTabListItem navItemProperties = new NavTabListItem(itemInfo);
            navItemProperties.getTagVar().putAll(itemInfo.getTagVar());
            navItemProperties.getTagVar().putAll(tagMap);
            this.addItem((T) navItemProperties);
        }

        if (tabsViewBean != null) {
            this.init(tabsViewBean, valueMap);
        }
    }

    void init(TabsViewBean tabsViewBean, Map<String, ?> valueMap) {
        List<NavTabListItem> childTabViewBeans = tabsViewBean.getTabItems();
        for (NavTabListItem childTabViewBean : childTabViewBeans) {
            this.addItem((T) childTabViewBean);
        }
        if (tabsViewBean.getItemBeans() != null && !tabsViewBean.getItemBeans().isEmpty()) {
            List<T> navTabItemBeans = new ArrayList<>();
            List<TabItemBean> tabItemBeans = tabsViewBean.getItemBeans();
            for (TabItemBean item : tabItemBeans) {
                navTabItemBeans.add((T) new NavTabListItem(item, valueMap));
            }
            this.setItems(navTabItemBeans);
        }


        if (this.getItems() == null || this.getItems().isEmpty()) {
            Class<? extends Enum> enumClass = tabsViewBean.getEnumClass();
            Class<? extends Enum> viewClass = null;
            String viewClassName = tabsViewBean.getViewClassName();
            if (viewClassName != null) {
                Class clazz = null;
                try {
                    clazz = ClassUtility.loadClass(viewClassName);
                    if (clazz.isEnum()) {
                        viewClass = clazz;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            List<NavTabListItem> items = (List<NavTabListItem>) this.getItems();
            if (viewClass != null) {
                items = ESDEnumsUtil.getEnumItems(viewClass, NavTabListItem.class);
            } else if (enumClass != null) {
                items = ESDEnumsUtil.getEnumItems(enumClass, NavTabListItem.class);
            }
            this.setItems((List<T>) items);
        }
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(tabsViewBean), Map.class), this, false, false);
        if (this.getItems() != null && this.getItems().size() > 0) {
            AbsUIProperties uiProperties = this.getItems().get(0);
            this.setValue(uiProperties.getId());
        }

        if (tabsViewBean.getSingleOpen()) {
            this.setNoHandler(true);
        }
    }


}
