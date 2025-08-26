package net.ooder.esd.engine.enums;


import net.ooder.esd.annotation.ui.ComponentBaseType;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RADPlugin {

    PluginsPostationType[] postation();

    PluginsContextType[] context() default {PluginsContextType.all};

    ComponentBaseType[] baseType() default {};
}
