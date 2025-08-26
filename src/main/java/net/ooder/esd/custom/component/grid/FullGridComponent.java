package net.ooder.esd.custom.component.grid;

import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomGridViewBean;
import net.ooder.esd.engine.EUModule;

import java.util.Map;

public class FullGridComponent extends CustomGridComponent {

    public FullGridComponent() {
        super();
    }


    public FullGridComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super(module, methodConfig, valueMap);
        CustomGridViewBean viewBean = (CustomGridViewBean) methodConfig.getView();
        ClassGridComponent currComponent = new ClassGridComponent(viewBean);
        this.addChildNav(currComponent);
        this.setCurrComponent(currComponent);
        //用戶扩展处理
        this.fillGridAction(viewBean, currComponent);

        this.fillMenuAction(viewBean, currComponent);


    }


}
