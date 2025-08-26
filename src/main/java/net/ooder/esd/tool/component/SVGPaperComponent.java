package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.svg.SVGPaperProperties;

public class SVGPaperComponent extends Component<SVGPaperProperties, FieldEventEnum> {


    public SVGPaperComponent addAction(Action<FieldEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public SVGPaperComponent(String alias, SVGPaperProperties properties) {
        super(ComponentType.SVGPAPER, alias);
        this.setProperties(properties);

    }

    public SVGPaperComponent(String alias) {
        super(ComponentType.SVGPAPER, alias);
        this.setProperties( new SVGPaperProperties());

    }


    public SVGPaperComponent() {
        super(ComponentType.SVGPAPER);
        this.setProperties(new SVGPaperProperties());
    }


}
