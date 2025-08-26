package net.ooder.esd.custom.component.grid;

import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomMGridViewBean;
import net.ooder.esd.engine.EUModule;

import java.util.Map;

public class FullMGridComponent extends CustomMGridComponent{

    public FullMGridComponent() {
        super();
    }


    public FullMGridComponent(EUModule module, MethodConfig methodConfig, Map<String, Object> valueMap) {
        super(module, methodConfig, valueMap);
        ClassMGridComponent currComponent = new ClassMGridComponent(methodConfig);
        this.addChildNav(currComponent);
        this.setCurrComponent(currComponent);
        CustomMGridViewBean viewBean = (CustomMGridViewBean) methodConfig.getView();
        //用戶扩展处理
        this.fillGridAction(viewBean, currComponent);
        this.fillViewAction(methodConfig);
        this.fillMenuAction(viewBean, currComponent);


    }


}
