package net.ooder.esd.custom.component;

import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.view.CustomButtonLayoutViewBean;
import net.ooder.esd.bean.data.CustomDataBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ButtonLayoutComponent;
import net.ooder.esd.tool.properties.ButtonLayoutProperties;

import java.util.Map;

public class FullButtonLayoutComponent extends CustomButtonLayoutComponent {


    public FullButtonLayoutComponent() {
        super();
    }

    public FullButtonLayoutComponent(EUModule module, MethodConfig methodConfig, Map valueMap) throws ClassNotFoundException {
        super(module, methodConfig, valueMap);
        CustomButtonLayoutViewBean viewBean = (CustomButtonLayoutViewBean) methodConfig.getView();
        ButtonLayoutProperties properties = new ButtonLayoutProperties(viewBean);
        ButtonLayoutComponent currComponent = new ButtonLayoutComponent(module.getName() + ComponentType.BUTTONLAYOUT.name(), properties);
        CustomDataBean dataBean = methodConfig.getDataBean();
        if (dataBean != null && dataBean.getCs() != null) {
            currComponent.setCS(dataBean.getCs());
        }
        this.addChildNav(currComponent);
        this.setCurrComponent(currComponent);
        fillAction(viewBean);
        this.fillViewAction(methodConfig);

        //用戶扩展处理
        fillEventAction(viewBean, currComponent);

    }


}
