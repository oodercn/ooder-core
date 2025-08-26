package net.ooder.esd.custom.component.index;

import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.tool.component.DivComponent;
import net.ooder.esd.tool.component.ImageComponent;
import net.ooder.esd.tool.component.LabelComponent;
import net.ooder.esd.tool.properties.CS;
import net.ooder.esd.tool.properties.DivProperties;
import net.ooder.esd.tool.properties.form.ImageProperties;
import net.ooder.esd.tool.properties.form.LabelProperties;

import java.util.HashMap;

public class IndexLeftTopComponent extends DivComponent {


    public IndexLeftTopComponent() {
        super("leftTree");
        DivProperties divProperties = (DivProperties) this.getProperties();
        divProperties.setHeight("4em");
        divProperties.setDock(Dock.top);
        divProperties.setzIndex(10);
        divProperties.setPanelBgClr("#3498DB");
        this.addChildren(getLogoText());
        this.addChildren(getLogoImage());

    }

    LabelComponent getLogoText() {
        LabelComponent logoLabelCom = new LabelComponent();
        logoLabelCom.setAlias("logoText");
        LabelProperties logoProperties = logoLabelCom.getProperties();
        logoProperties.setLeft("4.5em");
        logoProperties.setWidth("3.85em");
        logoProperties.setHeight("1.2em");
        logoProperties.setFontSize("18px");
        logoProperties.setFontColor("#FFFFFF");
        logoProperties.setCaption("CodeBee");
        logoProperties.sethAlign(HAlignType.center);
        return logoLabelCom;
    }


    ImageComponent getLogoImage() {
        ImageComponent logoImgeCom = new ImageComponent();
        logoImgeCom.setAlias("logo");
        ImageProperties logoProperties = logoImgeCom.getProperties();
        logoProperties.setLeft("0.75em");
        logoProperties.setWidth("3.6em");
        logoProperties.setHeight("3.6em");
        logoProperties.setSrc("/RAD/img/staticjds.gif");
        net.ooder.esd.tool.properties.CS cs = new  net.ooder.esd.tool.properties.CS ();
        HashMap csMap = new HashMap();
        csMap.put("opacity", 0.75);
        cs.setKEY(csMap);
        logoImgeCom.setCS(cs);
        return logoImgeCom;
    }


}
