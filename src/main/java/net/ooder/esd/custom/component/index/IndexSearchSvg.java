package net.ooder.esd.custom.component.index;


import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.ComboInputComponent;
import net.ooder.esd.tool.component.SVGPaperComponent;
import net.ooder.esd.tool.component.SVGPathComponent;
import net.ooder.esd.tool.properties.BlockProperties;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.esd.tool.properties.svg.SVGPaperProperties;
import net.ooder.esd.tool.properties.svg.comb.path.PathAttr;
import net.ooder.esd.tool.properties.svg.comb.path.PathProperties;

public class IndexSearchSvg extends SVGPaperComponent {

    SVGPathComponent svgPathComponent;

    BlockComponent blockComponent;

    ComboInputComponent searchInput;

    public IndexSearchSvg() {

        super("searchSvgMenu");
        SVGPaperProperties logoProperties = new SVGPaperProperties();//svgPaperComponent.getProperties();
        logoProperties.setRight("6em");
        logoProperties.setWidth("17em");
        logoProperties.setTop("3.5em");
        logoProperties.setHeight("4.5em");
        logoProperties.setzIndex(1002);
        logoProperties.setGraphicZIndex(2);
        logoProperties.setOverflow(OverflowType.visible);
        this.setProperties(logoProperties);

        this.blockComponent = getBlockComponent();
        this.addChildren(blockComponent);
        searchInput=this.getSearchInput();
        this.addChildren(searchInput);
        svgPathComponent = this.getSvgPathComponent();
        this.addChildren(svgPathComponent);

    }

    public ComboInputComponent getSearchInput() {

        ComboInputComponent comboInputComponent = new ComboInputComponent();
        comboInputComponent.setAlias("search");
        ComboInputProperties inputProperties = comboInputComponent.getProperties();
        inputProperties.setDirtyMark(false);
        inputProperties.setLeft("0.625em");
        inputProperties.setTop("1.875em");
        inputProperties.setWidth("14.875em");
        inputProperties.setzIndex(1002);
        inputProperties.setLabelSize("8em");
        inputProperties.setLabelPos(LabelPos.none);
        inputProperties.setLabelCaption("查询");
        inputProperties.setType(ComboInputType.none);
        inputProperties.setCommandBtn("ood-icon-search");

        return comboInputComponent;
    }

    public BlockComponent getBlockComponent() {
        BlockComponent blockComponent = new BlockComponent();
        BlockProperties blockProperties = blockComponent.getProperties();
        blockProperties.setLeft("0em");
        blockProperties.setDock(Dock.none);
        blockProperties.setTop("1.25em");
        blockProperties.setWidth("16.9em");
        blockProperties.setHeight("3.1em");
        blockProperties.setzIndex(0);
        blockProperties.setShadow(true);
        blockProperties.setBorderType(BorderType.flat);
        blockProperties.setBackground("#FFFFFF");
        return blockComponent;
    }

    public SVGPathComponent getSvgPathComponent() {
        SVGPathComponent pathComponent = new SVGPathComponent();
        PathProperties pathProperties = pathComponent.getProperties();
        pathProperties.setSvgTag("Shapes:Triangle");
        PathAttr pathAttr = new PathAttr();
        pathAttr.setPath("M,140,21L,133,1L,110,21");
        pathAttr.setFill("#ffffff");
        pathAttr.setStroke("#B6B6B6");
        pathProperties.setAttr(pathAttr);
        return pathComponent;

    }


}
