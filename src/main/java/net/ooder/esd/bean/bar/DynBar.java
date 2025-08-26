package net.ooder.esd.bean.bar;

import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.properties.item.UIItem;

import java.util.List;

public interface DynBar<T extends DynBar, I extends UIItem> extends Comparable<T> {
    public String getId();

    public List<I> filter(Object obj);

    public void addSplit(String id);

    public void addMenu(CustomMenu... types);

    public boolean initMenuClass(Class clazz);

    public void addMenu(String className, APICallerComponent component);

    public void addMenu(APICallerComponent component);

}
