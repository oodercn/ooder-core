package net.ooder.esd.dsm.view.context;

import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.bean.view.CustomPanelFormViewBean;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import java.util.ArrayList;
import java.util.List;


public class PanelViewRoot  extends BaseFormViewRoot<CustomPanelFormViewBean> {

    public AggViewRoot aggViewRoot;


    private List<FieldFormConfig> allFields = new ArrayList<>();

    private static final Class[] customClass = new Class[]{};



    public PanelViewRoot(AggViewRoot aggViewRoot, CustomPanelFormViewBean viewBean, String moduleName, String className) {
        super(aggViewRoot, viewBean, moduleName, className);
    }

    public Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.PANEL;
    }



}
