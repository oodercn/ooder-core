package net.ooder.esd.engine.event;

import net.ooder.annotation.EventTypeEnums;
import net.ooder.common.JDSEvent;
import net.ooder.vfs.engine.event.VFSEvent;


public enum ESDEventTypeEnums implements EventTypeEnums {

    ModuleEvent("ModuleEvent", EUModuleEvent.class),

    ModuleVersionEvent("ModuleVersionEvent",EUModuleVersionEvent.class),

    ModuleObjectEvent("ModuleObjectEvent", EUModuleObjectEvent.class),

    PackageEvent("PackageEvent",PackageEvent.class);


    private String eventName;

    private Class<? extends JDSEvent> eventClass;

    ESDEventTypeEnums(String eventName, Class<? extends JDSEvent> eventClass) {

        this.eventName = eventName;

        this.eventClass = eventClass;

    }

    public static ESDEventTypeEnums fromName(String eventName) {
        for (ESDEventTypeEnums type : ESDEventTypeEnums.values()) {
            if (type.getEventName().equals(eventName)) {
                return type;
            }
        }
        return null;
    }

    public static ESDEventTypeEnums fromEventClass(Class<? extends JDSEvent> eventClass) {
        for (ESDEventTypeEnums type : ESDEventTypeEnums.values()) {
            if (type.getEventClass().equals(eventClass)) {
                return type;
            }
        }
        return null;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Class<? extends JDSEvent> getEventClass() {
        return eventClass;
    }

    public void setEventClass(Class<? extends VFSEvent> eventClass) {
        this.eventClass = eventClass;
    }

}
