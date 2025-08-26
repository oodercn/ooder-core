package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.CustomGroupFieldBean;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.bean.nav.GroupItemBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.util.json.DefaultTabItem;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;


public class GroupProperties extends PanelProperties {

    Boolean lazyAppend;

    Boolean autoReload;

    Boolean dynDestory;

    Boolean lazyLoad;

    Class bindService;

    String groupName;

    String euClassName;

    Boolean displayBar;

    DefaultTabItem tabItem;


    public GroupProperties() {
        super();
    }

    public GroupProperties(Dock dock) {
        super(dock);
    }

    public GroupProperties(GroupItemBean itemBean, ContainerBean containerBean) {
        if (containerBean != null) {
            this.init(containerBean);
        }
        init(itemBean);
    }

    public GroupProperties(ContainerBean containerBean) {
        if (containerBean != null) {
            this.init(containerBean);
        }
    }

    public GroupProperties(FieldFormConfig<CustomGroupFieldBean, ?> fieldFormConfig) {
        CustomGroupFieldBean groupFieldBean = fieldFormConfig.getWidgetConfig();
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(groupFieldBean), Map.class), this, false, false);
        FieldBean fieldBean = fieldFormConfig.getFieldBean();
        if (fieldBean != null) {
            if (fieldBean.getManualHeight() != null && fieldBean.getManualHeight() != -1) {
                this.height = fieldBean.getManualHeight() + "px";
            }
            if (fieldBean.getManualWidth() != null && fieldBean.getManualWidth() != -1) {
                this.width = fieldBean.getManualWidth() + "px";
            }

        }

        this.groupName = groupFieldBean.getGroupName();
        this.euClassName = groupFieldBean.getEuClassName();

    }


    void init(GroupItemBean itemBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(itemBean), Map.class), this, false, false);
        this.groupName = itemBean.getGroupName();
        this.euClassName = itemBean.getEuClassName();
        this.name = itemBean.getName();
        this.id = itemBean.getId();
        this.caption = itemBean.getCaption();
    }

    public String getEuClassName() {
        return euClassName;
    }

    public void setEuClassName(String euClassName) {
        this.euClassName = euClassName;
    }

    public DefaultTabItem getTabItem() {
        return tabItem;
    }

    public void setTabItem(DefaultTabItem tabItem) {
        this.tabItem = tabItem;
    }

    public Boolean getLazyAppend() {
        return lazyAppend;
    }

    public void setLazyAppend(Boolean lazyAppend) {
        this.lazyAppend = lazyAppend;
    }

    public Boolean getAutoReload() {
        return autoReload;
    }

    public void setAutoReload(Boolean autoReload) {
        this.autoReload = autoReload;
    }

    public Boolean getDynDestory() {
        return dynDestory;
    }

    public void setDynDestory(Boolean dynDestory) {
        this.dynDestory = dynDestory;
    }

    public Boolean getLazyLoad() {
        return lazyLoad;
    }

    public void setLazyLoad(Boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }

    public Class getBindService() {
        return bindService;
    }

    public void setBindService(Class bindService) {
        this.bindService = bindService;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean getDisplayBar() {
        return displayBar;
    }

    public void setDisplayBar(Boolean displayBar) {
        this.displayBar = displayBar;
    }


}


