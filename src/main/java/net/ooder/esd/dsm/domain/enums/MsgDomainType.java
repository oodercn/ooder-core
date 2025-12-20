package net.ooder.esd.dsm.domain.enums;


import net.ooder.annotation.IconEnumstype;

public enum MsgDomainType implements IconEnumstype {
    MQTT("Mqtt消息", "ri-radio-tower-line"),
    APICALL("接口", "ri-plug-line"),
    COMMAND("命令", "ri-terminal-box-line");


    private final String name;
    private final String imageClass;


    MsgDomainType(String name, String imageClass) {
        this.name = name;
        this.imageClass = imageClass;
    }

    public static MsgDomainType fromType(String type) {
        MsgDomainType defaultViewType = MQTT;
        if (type != null) {
            for (MsgDomainType viewType : MsgDomainType.values()) {
                if (viewType.getType().equals(type)) {
                    defaultViewType = viewType;
                }
            }
        }

        return defaultViewType;
    }

    public String getImageClass() {
        return imageClass;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public String getType() {
        return name();
    }

    @Override
    public String getName() {
        return name;
    }
}
