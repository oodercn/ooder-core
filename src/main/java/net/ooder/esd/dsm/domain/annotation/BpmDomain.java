package net.ooder.esd.dsm.domain.annotation;

import net.ooder.esd.dsm.domain.enums.BpmDomainType;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
public @interface BpmDomain {

    String tempId() default "default";

    BpmDomainType type() default BpmDomainType.ADMIN;

    Class sourceClass() default Void.class;

}
