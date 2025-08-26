package net.ooder.esd.dsm.domain.annotation;

import net.ooder.esd.dsm.domain.enums.MsgDomainType;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
public @interface MsgDomain {

    String tempId() default "default";

    MsgDomainType type() default MsgDomainType.MQTT;

    Class sourceClass() default Void.class;

}
