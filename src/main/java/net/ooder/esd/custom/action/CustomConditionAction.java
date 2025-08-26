package net.ooder.esd.custom.action;


import net.ooder.common.EventKey;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;


public class CustomConditionAction extends Action {

    public CustomConditionAction(CustomAction type, CustomMenu toType, EventKey eventKey) throws JDSException {
        super(type,eventKey);
        String menuId = toType.type() + "_" + ComboInputType.button.name();
        Condition condition = new Condition("{args[1].id}", SymbolType.equal, menuId);
        this.addCondition(condition);
    }


}

