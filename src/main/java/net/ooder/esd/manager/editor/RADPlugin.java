package net.ooder.esd.manager.editor;


import net.ooder.esd.annotation.ui.ComponentBaseType;
import net.ooder.esd.engine.enums.PluginsContextType;
import net.ooder.esd.engine.enums.PluginsPostationType;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RADPlugin {

    PluginsPostationType[] postation();

    PluginsContextType[] context() default {PluginsContextType.all};

    ComponentBaseType[] baseType() default {};
}
