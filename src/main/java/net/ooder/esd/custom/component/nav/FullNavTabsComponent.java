package net.ooder.esd.custom.component.nav;

import net.ooder.common.JDSException;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.data.TabsDataBean;
import net.ooder.esd.bean.view.TabsViewBean;
import net.ooder.esd.custom.component.CustomTabsComponent;
import net.ooder.esd.engine.EUModule;

import java.util.Map;

public class FullNavTabsComponent extends BaseNavTabsComponent {

    public FullNavTabsComponent() {
        super();
    }


    public FullNavTabsComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) throws JDSException {
        super(module, methodConfig, valueMap);
        TabsDataBean dataBean = (TabsDataBean) methodConfig.getDataBean();
        TabsViewBean viewBean = (TabsViewBean) methodConfig.getView();
        CustomTabsComponent currComponent = new CustomTabsComponent(euModule, methodConfig, valueMap);
        this.addChildNav(currComponent);
        this.setCurrComponent(currComponent);
        this.fillTabsAction(viewBean, currComponent);
        this.fillViewAction(dataBean);
        this.addChildren(this.genAPIComponent(methodConfig));
        this.fillToolBar(viewBean, currComponent);

    }


}
