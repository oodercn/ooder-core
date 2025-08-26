package net.ooder.esd.custom.component.nav;


import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.NavGalleryComboViewBean;
import net.ooder.esd.custom.properties.NavGalleryProperties;
import net.ooder.esd.tool.component.GalleryComponent;

public class NavGalleryComponent extends GalleryComponent {


    public NavGalleryComponent(NavGalleryComboViewBean navGalleryViewBean) {
        init(navGalleryViewBean);
    }

    public NavGalleryComponent(MethodConfig methodConfig) {
        super();
        NavGalleryComboViewBean navGalleryViewBean = (NavGalleryComboViewBean) methodConfig.getView();
        init(navGalleryViewBean);
    }

    void init(NavGalleryComboViewBean navGalleryViewBean) {
        this.setProperties(new NavGalleryProperties(navGalleryViewBean));
        this.setAlias(navGalleryViewBean.getName());
    }

}
