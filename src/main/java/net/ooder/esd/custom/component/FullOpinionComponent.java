package net.ooder.esd.custom.component;

import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.data.CustomDataBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomOpinionViewBean;
import net.ooder.esd.custom.properties.OpinionProperties;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.OpinionComponent;

import java.util.Map;

public class FullOpinionComponent extends CustomOpinionComponent {


    public FullOpinionComponent() {
        super();
    }

    public FullOpinionComponent(EUModule module, MethodConfig methodConfig, Map valueMap) throws ClassNotFoundException {
        super(module, methodConfig, valueMap);
        CustomOpinionViewBean viewBean = (CustomOpinionViewBean) methodConfig.getView();
        OpinionProperties properties = new OpinionProperties(viewBean);
        OpinionComponent currComponent =  new OpinionComponent(module.getName() + ComponentType.OPINION.name(), properties);
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
        fillOpinionAction(viewBean, currComponent);
        this.fillViewAction(methodConfig);


    }


}
