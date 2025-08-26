package net.ooder.esd.custom.component.form;

import net.ooder.common.util.CnToSpell;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.properties.BlockProperties;
import net.ooder.jds.core.esb.EsbUtil;


public class CustomFormBottomBar extends BlockComponent {

    public CustomFormBottomBar(CustomFormMenu[] types, String alias) {
        super(CnToSpell.getFullSpell(alias + "Hidden"),  new BlockProperties(ComponentType.STATUSBUTTONS));
        BlockProperties properties =  this.getProperties();
        properties.setDock(Dock.bottom);
        for (CustomFormMenu menuType : types) {
            if (EsbUtil.parExpression(menuType.getExpression(), Boolean.class)) {
                CustomFormButton component = new CustomFormButton(menuType);
                this.addChildren(component);
            }

        }
    }


}
