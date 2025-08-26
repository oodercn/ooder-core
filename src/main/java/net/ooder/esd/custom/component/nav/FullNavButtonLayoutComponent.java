package net.ooder.esd.custom.component.nav;

import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.NavButtonLayoutComboViewBean;
import net.ooder.esd.engine.EUModule;

import java.util.Map;

public class FullNavButtonLayoutComponent extends ButtonLayoutModuleComponent {

    public FullNavButtonLayoutComponent() {
        super();
    }

    public FullNavButtonLayoutComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super(module, methodConfig, valueMap);
        NavButtonLayoutComboViewBean navGalleryViewBean = (NavButtonLayoutComboViewBean) methodConfig.getView();
        NavButtonLayoutComponent galleryPanel = new NavButtonLayoutComponent(navGalleryViewBean);
        this.addChildNav(galleryPanel, navGalleryViewBean);
    }


}
