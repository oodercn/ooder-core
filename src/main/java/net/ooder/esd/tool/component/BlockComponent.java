package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.UIEventEnum;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.BlockProperties;

public class BlockComponent extends WidgetComponent<BlockProperties, UIEventEnum> {


    public BlockComponent(String alias, BlockProperties properties) {
        super(ComponentType.BLOCK, alias, properties);
        this.setProperties(properties);
        this.getProperties().setBorderType(BorderType.none);

    }


    public BlockComponent addAction(UIEventEnum eventKey, Action<UIEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public BlockComponent(String alias) {
        super(ComponentType.BLOCK);
        this.setAlias(alias);
        this.setProperties(new BlockProperties(Dock.none));
        this.getProperties().setBorderType(BorderType.none);
    }

    public BlockComponent() {
        super(ComponentType.BLOCK);
        this.setProperties(new BlockProperties(Dock.fill));
        this.getProperties().setBorderType(BorderType.none);
    }

    public BlockComponent(Dock dock, String alias) {
        super(ComponentType.BLOCK, alias, dock);
        this.setProperties(new BlockProperties(dock));
        this.getProperties().setBorderType(BorderType.none);
    }


    public BlockComponent(Component child, Dock dock) {
        super(ComponentType.BLOCK, child.getAlias() + "Block", dock);
        this.setProperties(new BlockProperties(dock));
        this.getProperties().setBorderType(BorderType.none);
        this.addChildren(child);
    }

}
