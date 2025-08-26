package net.ooder.esd.custom.action;

import net.ooder.common.EventKey;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.tool.properties.Action;
import java.util.Arrays;

public class SetTagVarQueryDataAction extends Action {


    public SetTagVarQueryDataAction(String action,EventKey eventKey) {
        super(eventKey);
        this.setArgs(Arrays.asList(new String[]{"{page." + action + ".setQueryData()}", null, null, "{args[3].tagVar}", "tagVar"}));
        this.setType(ActionTypeEnum.control);
        this.setTarget(action);
        this.setDesc("设定参数");
        this.setMethod("setQueryData");
        this.setRedirection("other:callback:call");
        this.set_return( true);
    }

    public SetTagVarQueryDataAction(String action,String tagVarStr,EventKey eventKey) {
        super(eventKey);
        this.setArgs(Arrays.asList(new String[]{"{page." + action + ".setQueryData()}", null, null, tagVarStr, "tagVar"}));
        this.setType(ActionTypeEnum.control);
        this.setTarget(action);
        this.setDesc("设定参数");
        this.setMethod("setQueryData");
        this.setRedirection("other:callback:call");
        this.set_return( true);
    }

}
