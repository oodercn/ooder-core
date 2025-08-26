package net.ooder.esd.bean.bar;

import net.ooder.esd.annotation.event.EventPos;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.manager.editor.CustomMenuAction;
import net.ooder.esd.tool.component.APICallerComponent;

import java.util.List;

public interface PopDynBar<T extends PopDynBar, I extends TreeListItem> extends MenuDynBar<T, I> {


    public void addChild(T bars);

    public List<APICallerComponent> getApis();

    public Boolean isDyn();

    public EventPos getPos();


    public void setPos(EventPos pos);

    public void addTag(String key, Object object);

    public Class<? extends CustomMenuAction> getServiceClass();


}
