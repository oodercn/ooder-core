package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.echarts.EChartProperties;

public class EChartComponent extends Component<EChartProperties, FieldEventEnum> {

    public EChartProperties properties;

    @Override
    public EChartProperties getProperties() {
        return properties;
    }

    public void setProperties(EChartProperties properties) {
        this.properties = properties;
    }

    public EChartComponent addAction( Action<FieldEventEnum> action) {
        super.addAction( action);
        return this;
    }




    public EChartComponent(String alias, EChartProperties properties) {
        super(ComponentType.ECHARTS, alias);
        this.properties = properties;

    }

    public EChartComponent(String alias) {
        super(ComponentType.ECHARTS, alias);
        this.setProperties( new EChartProperties());
    }

    public EChartComponent() {
        super(ComponentType.ECHARTS);
        this.setProperties(new EChartProperties());
    }



}
