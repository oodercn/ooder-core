package net.ooder.esd.bean;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.BottomBarMenu;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.item.LayoutListItem;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.web.util.AnnotationUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class NavBaseViewBean<K extends LayoutListItem, U extends TabListItem,M extends Component> extends CustomViewBean<FieldFormConfig, U,M> {

    public SelModeType modeType;

    public Set<CustomFormEvent> event = new LinkedHashSet<>();

    public List<CustomFormMenu> bottombarMenu = new ArrayList<>();

    public List<CustomFormMenu> customMenu = new ArrayList<>();

    public List<CustomFormMenu> toolBarMenu = new ArrayList<>();



    public NavBaseViewBean() {

    }


    public NavBaseViewBean(MethodConfig methodAPIBean) {
        super(methodAPIBean);
        try {
            this.initHiddenField(this.viewClassName);
        } catch (JDSException e) {
            e.printStackTrace();
        }


        if (customMenu != null && customMenu.size() > 0) {
            if (this.menuBar == null) {
                this.menuBar = AnnotationUtil.fillDefaultValue(MenuBarMenu.class, new MenuBarBean());
            }
        }

        if (bottombarMenu != null && bottombarMenu.size() > 0) {
            if (this.bottomBar == null) {
                this.bottomBar = AnnotationUtil.fillDefaultValue(BottomBarMenu.class, new BottomBarMenuBean());
            }
        }

        if (toolBarMenu != null && toolBarMenu.size() > 0) {
            if (this.toolBar == null) {
                this.toolBar = AnnotationUtil.fillDefaultValue(ToolBarMenu.class, new ToolBarMenuBean());
            }
        }


    }


    @Override
    @JSONField(serialize = false)
    public List<FieldModuleConfig> getNavItems() {
        Set<String> fieldNames = this.getItemNames();
        List<FieldModuleConfig> fields = new ArrayList<>();
        for (String fieldName : fieldNames) {
            FieldModuleConfig fieldFormConfig = this.getItemConfigMap().get(fieldName);
            if (fieldFormConfig != null) {
                fieldFormConfig.setDomainId(domainId);
                fieldFormConfig.setIndex(fields.size());
                fields.add(fieldFormConfig);
            }
        }
        // Collections.sort(fields);
        return fields;
    }


    @Override
    public FieldModuleConfig getItemByName(String name) {
        return this.getItemConfigMap().get(name);
    }


    public abstract List<K> getItemBeans();

    public K getItemBean(String id) {
        List<K> childTabViewBeans = this.getItemBeans();
        for (K childTabViewBean : childTabViewBeans) {
            if (childTabViewBean.getId() != null && childTabViewBean.getId().equals(id)) {
                return childTabViewBean;

            }
        }
        return null;
    }



    public List<CustomFormMenu> getToolBarMenu() {
        return toolBarMenu;
    }

    public void setToolBarMenu(List<CustomFormMenu> toolBarMenu) {
        this.toolBarMenu = toolBarMenu;
    }

    public SelModeType getModeType() {
        return modeType;
    }

    public void setModeType(SelModeType modeType) {
        this.modeType = modeType;
    }

    public Set<CustomFormEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomFormEvent> event) {
        this.event = event;
    }

    public List<CustomFormMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    public void setBottombarMenu(List<CustomFormMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }

    public List<CustomFormMenu> getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(List<CustomFormMenu> customMenu) {
        this.customMenu = customMenu;
    }
}
