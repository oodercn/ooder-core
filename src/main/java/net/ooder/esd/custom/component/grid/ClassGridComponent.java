package net.ooder.esd.custom.component.grid;

import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomGridViewBean;
import net.ooder.esd.tool.component.TreeGridComponent;
import net.ooder.esd.tool.properties.ClassGridProperties;

public class ClassGridComponent extends TreeGridComponent {
    public ClassGridComponent(MethodConfig methodConfig) {
        super();
        CustomGridViewBean viewBean = (CustomGridViewBean) methodConfig.getView();
        init(viewBean);
    }

    public ClassGridComponent(CustomGridViewBean viewBean) {
        super();
        init(viewBean);
    }

    public void init(CustomGridViewBean viewBean) {
        this.setProperties(new ClassGridProperties(viewBean));
        this.setAlias(viewBean.getName());
    }
}
