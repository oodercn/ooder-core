package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.MessageEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.MessageProperties;

public class MessageComponent<T extends MessageProperties, K extends MessageEventEnum> extends Component<T, K> {
    public MessageProperties properties;


    public MessageComponent(String alias,MessageProperties properties) {
        super(ComponentType.MESSAGESERVICE, alias);
        this.properties = properties;
    }

    public MessageComponent addAction(Action<K> action) {
        super.addAction( action);
        return this;
    }

    public MessageComponent() {
        super(ComponentType.MESSAGESERVICE);
        this.properties = new MessageProperties();
    }

    @Override
    public T getProperties() {
        return (T) properties;
    }

    public void setProperties(MessageProperties properties) {
        this.properties = properties;
    }

}
