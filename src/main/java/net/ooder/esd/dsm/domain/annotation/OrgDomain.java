package net.ooder.esd.dsm.domain.annotation;

import net.ooder.esd.dsm.domain.enums.OrgDomainType;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
public @interface OrgDomain {

    String tempId() default "default";

    OrgDomainType type() default OrgDomainType.ALL;

    Class sourceClass() default Void.class;


}
