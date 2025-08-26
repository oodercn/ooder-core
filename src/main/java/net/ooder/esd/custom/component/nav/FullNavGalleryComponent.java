package net.ooder.esd.custom.component.nav;


import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.engine.EUModule;

import java.util.Map;

public class FullNavGalleryComponent extends CustomNavGalleryComponent {

    public FullNavGalleryComponent() {
        super();

    }

    public FullNavGalleryComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws ClassNotFoundException {
        super(module, methodConfig, valueMap);
    }


}
