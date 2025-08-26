package net.ooder.esd.custom.action;

import net.ooder.common.EventKey;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.tool.properties.Action;

import java.util.Arrays;

public class CustomExpressionAction<K extends EventKey> extends Action<K> {


    public CustomExpressionAction(String action,K eventKey) {
        super(eventKey);
        this.setArgs(Arrays.asList(new String[]{"{page." + action + ".invoke()()}"}));
        this.setType(ActionTypeEnum.control);
        this.setTarget(action);
        this.setDesc("设定参数");
        this.setMethod("setQueryData");
        this.setRedirection("other:callback:call");
        this.set_return(true);
    }


}
