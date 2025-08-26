package net.ooder.esd.bean;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.dsm.view.field.ESDFieldConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.tool.properties.item.UIItem;

import java.util.List;
import java.util.Set;

public interface CustomView<T extends ESDFieldConfig,U extends UIItem> extends CustomBean {

    public String getDomainId();

    public String getMethodName();

    public String getSourceClassName();

    public String getViewClassName();

    public Set<ComponentType> getBindTypes();

    public ModuleViewType getModuleViewType();

    public List<T> getAllFields();

    public List<FieldModuleConfig> getNavItems();

    public List<U> getTabItems();

    public T getFieldByName(String name);

    public FieldModuleConfig getItemByName(String name);


}
