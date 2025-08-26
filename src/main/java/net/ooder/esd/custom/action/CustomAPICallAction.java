package net.ooder.esd.custom.action;

import net.ooder.common.EventKey;
import net.ooder.esd.annotation.action.CustomAPIMethod;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.properties.Action;



import java.util.Arrays;

public class CustomAPICallAction<K extends EventKey> extends Action<K> {


    public CustomAPICallAction(APICallerComponent callerComponent, K eventKey) {
        super(eventKey);
        init(callerComponent,  false,  eventKey);
    }

    void init(APICallerComponent callerComponent, boolean _return, K eventKey) {
        this.setEventKey(eventKey);
        this.setArgs(Arrays.asList(new String[]{"{page." + callerComponent.getAlias() + ".invoke()}"}));
        this.setType(ActionTypeEnum.control);
        this.setTarget(callerComponent.getAlias());
        this.setDesc("调用" + callerComponent.getProperties().getDesc());
        this.setMethod(CustomAPIMethod.invoke.getType());
        this.setRedirection("other:callback:call");
        this.set_return(_return);
    }

    public CustomAPICallAction(APICallerComponent callerComponent, boolean _return, K eventKey) {
        super(eventKey);
        init(callerComponent, _return,eventKey);
    }

}
