package net.ooder.esd.custom.component;


import net.ooder.esd.bean.data.CustomDataBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomGalleryViewBean;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.GalleryComponent;
import net.ooder.esd.tool.properties.GalleryProperties;

import java.util.Map;

public class FullGalleryComponent extends CustomGalleryComponent {


    public FullGalleryComponent() {
        super();
    }

    public FullGalleryComponent(EUModule module, MethodConfig methodConfig, Map valueMap) throws ClassNotFoundException {
        super(module, methodConfig, valueMap);
        CustomGalleryViewBean viewBean = (CustomGalleryViewBean) methodConfig.getView();
        GalleryProperties properties = new GalleryProperties(viewBean);
        GalleryComponent currComponent = new GalleryComponent(viewBean.getName(), properties);
        if (viewBean.getComponent() != null) {
            currComponent.setCS(viewBean.getComponent().getCS());
            currComponent.setEvents(viewBean.getComponent().getEvents());
        }

        CustomDataBean dataBean = methodConfig.getDataBean();
        if (dataBean != null && dataBean.getCs() != null) {
            currComponent.setCS(dataBean.getCs());
        }
      //  this.getMainComponent().addChildren(currComponent);
        this.addChildNav(currComponent);
        this.setCurrComponent(currComponent);
        fillAction(viewBean);
        this.fillViewAction(methodConfig);
        this.addChildNav(currComponent);
        //用戶扩展处理
        fillGalleryAction(viewBean, currComponent);
        this.fillViewAction(methodConfig);


    }


}
