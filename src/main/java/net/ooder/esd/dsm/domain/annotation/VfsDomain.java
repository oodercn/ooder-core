package net.ooder.esd.dsm.domain.annotation;

import net.ooder.esd.dsm.domain.enums.VfsDomainType;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
public @interface VfsDomain {

    String tempId() default "default";

    VfsDomainType type() default VfsDomainType.all;

    Class sourceClass() default Void.class;

}
