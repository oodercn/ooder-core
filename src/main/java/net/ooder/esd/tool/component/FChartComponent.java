package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.fchart.FChartProperties;

public class FChartComponent extends Component<FChartProperties, FieldEventEnum> {


    public FChartComponent addAction(Action<FieldEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public FChartComponent(String alias, FChartProperties properties) {
        super(ComponentType.FCHART, alias);
        this.setProperties(properties);

    }

    public FChartComponent(String alias) {
        super(ComponentType.FCHART, alias);
        this.setProperties( new FChartProperties());
    }

    public FChartComponent() {
        super(ComponentType.FCHART);
        this.setProperties(new FChartProperties());
    }


}
