package net.ooder.esd.bean.bar;

import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.tool.component.APICallerComponent;

import java.util.List;

public interface MenuDynBar<T extends MenuDynBar, I extends TreeListItem> extends DynBar<T, I> {

    public void setAlias(String alias);

    public String getCaption();

    public String getImageClass();

    public Integer getIndex();

    public void addChild(T bars);

    public List<APICallerComponent> getApis();

    public CustomMenuType getMenuType();

}
