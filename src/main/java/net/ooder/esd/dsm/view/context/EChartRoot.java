package net.ooder.esd.dsm.view.context;

import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.bean.view.CustomEChartViewBean;
import net.ooder.esd.dsm.aggregation.context.AggViewRoot;


public class EChartRoot  extends BaseViewRoot<CustomEChartViewBean>  {



    private static final Class[] customClass = new Class[]{};


    public EChartRoot(AggViewRoot viewRoot, CustomEChartViewBean viewBean,String moduleName, String className) {
        super(viewRoot, viewBean, moduleName, className);

    }



    public  Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public ViewType getViewType() {
        return ViewType.ECHARTS;
    }


}
