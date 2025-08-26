package net.ooder.esd.dsm.domain.annotation;

import net.ooder.esd.dsm.domain.enums.NavDomainType;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
public @interface NavDomain {

    String tempId() default "default";

    NavDomainType type() default NavDomainType.MODULE;

    Class sourceClass() default Void.class;

}
