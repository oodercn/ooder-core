package net.ooder.esd.custom.component;


import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.view.CustomContentBlockViewBean;
import net.ooder.esd.bean.data.CustomDataBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ContentBlockComponent;
import net.ooder.esd.tool.properties.ContentBlockProperties;

import java.util.Map;

public class FullContentBlockComponent extends CustomContentBlockComponent {


    public FullContentBlockComponent() {
        super();
    }

    public FullContentBlockComponent(EUModule module, MethodConfig methodConfig, Map valueMap) {
        super(module, methodConfig, valueMap);
        CustomContentBlockViewBean viewBean = (CustomContentBlockViewBean) methodConfig.getView();
        ContentBlockProperties properties = new ContentBlockProperties(viewBean);
        ContentBlockComponent currComponent = new ContentBlockComponent(module.getName() + ComponentType.CONTENTBLOCK.name(), properties);
        CustomDataBean dataBean = methodConfig.getDataBean();
        if (dataBean != null && dataBean.getCs() != null) {
            currComponent.setCS(dataBean.getCs());
        }
        this.addChildNav(currComponent);
        this.setCurrComponent(currComponent);
        fillAction(viewBean);
        this.fillViewAction(methodConfig);
        this.addChildNav(currComponent);
        //用戶扩展处理
        fillContentBlockAction(viewBean, currComponent);
        this.fillViewAction(methodConfig);


    }


}
