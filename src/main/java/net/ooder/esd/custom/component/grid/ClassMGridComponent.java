package net.ooder.esd.custom.component.grid;


import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.CustomMGridViewBean;
import net.ooder.esd.tool.component.MTreeGridComponent;
import net.ooder.esd.tool.properties.GridProperties;

public class ClassMGridComponent extends MTreeGridComponent {
    public ClassMGridComponent(MethodConfig methodConfig) {
        super();

        CustomMGridViewBean viewBean = (CustomMGridViewBean) methodConfig.getView();
        this.setProperties(new GridProperties(viewBean));
        this.setAlias(methodConfig.getMethodName() );
    }
}
