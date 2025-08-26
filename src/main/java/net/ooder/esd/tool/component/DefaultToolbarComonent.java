package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.ui.UIButtonType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.tool.properties.ToolBarProperties;

public class DefaultToolbarComonent extends ToolBarComponent {
    public DefaultToolbarComonent(String alias) {
        super(alias, new ToolBarProperties(alias + "toolbar"));
        TreeListItem toolbar = new TreeListItem(alias + "toolbar", alias + "toolbar")
                .addChild(new TreeListItem(UIButtonType.add))
                .addChild(new TreeListItem(UIButtonType.delete))
                .addChild(new TreeListItem(UIButtonType.delete));
        this.getProperties().addChild(toolbar);
    }

}
