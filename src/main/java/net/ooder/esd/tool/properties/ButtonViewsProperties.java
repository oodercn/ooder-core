package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.VAlignType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomButtonViewsViewBean;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.custom.properties.ButtonViewsListItem;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.util.ESDEnumsUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.RequestParamBean;

import java.util.*;

public class ButtonViewsProperties extends TabsProperties<ButtonViewsListItem> {

    public HAlignType barHAlign;
    public VAlignType barVAlign;
    public Boolean noFoldBar;

    public ButtonViewsProperties() {

    }


    public ButtonViewsProperties(CustomButtonViewsViewBean tabsViewBean, Map valueMap) {
        List<TabItemBean> childTabViewBeans = tabsViewBean.getItemBeans();
        for (TabItemBean childTabViewBean : childTabViewBeans) {
            ButtonViewsListItem navItemProperties = new ButtonViewsListItem(childTabViewBean, valueMap);
            this.addItem(navItemProperties);
        }
        this.init(tabsViewBean);
    }

    public ButtonViewsProperties(MethodConfig methodConfig, Map valueMap) {
        CustomButtonViewsViewBean tabsViewBean = (CustomButtonViewsViewBean) methodConfig.getView();
        this.setBorderType(BorderType.none);
        this.name = methodConfig.getName();
        this.caption = methodConfig.getCaption();
        this.imageClass = methodConfig.getImageClass();
        Map<String, Object> tagMap = new HashMap<>();
        Set<RequestParamBean> paramBeans = methodConfig.getParamSet();
        for (RequestParamBean paramBean : paramBeans) {
           if (!Arrays.asList(DSMFactory.SkipParams).contains(paramBean.getParamName())){
               Object obj = valueMap.get(paramBean.getParamName());
               if (obj != null && !obj.equals("")) {
                   tagMap.put(paramBean.getParamName(), obj);
               }
            }
        }

        if (tabsViewBean != null) {
            this.init(tabsViewBean);
        }
    }

    void init(CustomButtonViewsViewBean tabsViewBean) {

        List<FieldModuleConfig> moduleList = tabsViewBean.getNavItems();

        if (moduleList!=null && moduleList.size()>0){
            for (FieldModuleConfig itemInfo : moduleList) {
                ButtonViewsListItem navItemProperties = new ButtonViewsListItem(itemInfo);
                navItemProperties.getTagVar().putAll(itemInfo.getTagVar());
                this.addItem(navItemProperties);
            }
        }else{

            for (ButtonViewsListItem item : tabsViewBean.getTabItems()) {
                this.addItem(item);
            }

            if (tabsViewBean.getItemBeans() != null && !tabsViewBean.getItemBeans().isEmpty()) {
                List<ButtonViewsListItem> tabItemBeans = new ArrayList<>();
                for (TabItemBean item : tabsViewBean.getItemBeans()) {
                    tabItemBeans.add(new ButtonViewsListItem(item));
                }
                this.setItems(tabItemBeans);
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
                List<ButtonViewsListItem> items = this.getItems();
                if (viewClass != null) {
                    items = ESDEnumsUtil.getEnumItems(viewClass, ButtonViewsListItem.class);
                } else if (enumClass != null) {
                    items = ESDEnumsUtil.getEnumItems(enumClass, ButtonViewsListItem.class);
                }
                this.setItems(items);
            }

        }


        if (this.getItems().size() > 0) {
            this.setValue(this.getItems().get(0).getId());
        }
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(tabsViewBean), Map.class), this, false, false);
    }


    public Boolean getNoFoldBar() {
        return noFoldBar;
    }

    public void setNoFoldBar(Boolean noFoldBar) {
        this.noFoldBar = noFoldBar;
    }

    public HAlignType getBarHAlign() {
        return barHAlign;
    }

    public void setBarHAlign(HAlignType barHAlign) {
        this.barHAlign = barHAlign;
    }

    public VAlignType getBarVAlign() {
        return barVAlign;
    }

    public void setBarVAlign(VAlignType barVAlign) {
        this.barVAlign = barVAlign;
    }


}
