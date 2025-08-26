package net.ooder.esd.custom.component.form;


import net.ooder.common.EventKey;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.ui.SymbolType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.Condition;


public class CustomFormCondition extends Action {

    public CustomFormCondition(CustomAction type, EventKey toType) throws JDSException {
        super(type,toType);
        Condition condition = new Condition("{args[1].id}", SymbolType.equal, toType.getEvent());
        this.addCondition(condition);
    }


}

