package net.ooder.esd.custom.component;


import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.data.CustomDataBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomTitleBlockViewBean;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.TitleBlockComponent;
import net.ooder.esd.tool.properties.TitleBlockProperties;

import java.util.Map;

public class FullTitleBlockComponent extends CustomTitleBlockComponent {


    public FullTitleBlockComponent() {
        super();
    }

    public FullTitleBlockComponent(EUModule module, MethodConfig methodConfig, Map valueMap) {
        super(module, methodConfig, valueMap);
        CustomTitleBlockViewBean viewBean = (CustomTitleBlockViewBean) methodConfig.getView();
        TitleBlockProperties properties = new TitleBlockProperties(viewBean);
        TitleBlockComponent currComponent =  new TitleBlockComponent(module.getName() + ComponentType.TITLEBLOCK.name(), properties);
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
        fillTitleBlockAction(viewBean, currComponent);
        this.fillViewAction(methodConfig);


    }


}
