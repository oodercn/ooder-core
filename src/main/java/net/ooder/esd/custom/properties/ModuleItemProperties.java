package net.ooder.esd.custom.properties;

import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.PanelProperties;
import net.ooder.esd.tool.properties.item.GalleryNavItem;

import java.util.HashMap;

public class ModuleItemProperties extends GalleryNavItem {

    String defaultImageClass = "fa-solid fa-hashtag";


    public ModuleItemProperties(Enum enumType) {
        super(enumType);
    }


    public ModuleItemProperties(EUModule euModule, Integer k) {
        this.caption = euModule.getComponent().getTitle();
        this.id = euModule.getName() + "Item";

        imageClass = euModule.getComponent().getImageClass();
        this.setEuClassName(euModule.getClassName());
        if (imageClass == null || imageClass.equals("")) {
            Component fristComponent = euModule.getComponent().getTopComponentBox();
            if (fristComponent != null && fristComponent.getProperties() instanceof PanelProperties) {
                PanelProperties properties = (PanelProperties) fristComponent.getProperties();
                if (properties.getImageClass() != null && !properties.getImageClass().equals("")) {
                    imageClass = properties.getImageClass();
                }
            }
        }
        if (tagVar == null) {
            tagVar = new HashMap<>();
        }
        if (imageClass == null || imageClass.equals("")) {
            imageClass = defaultImageClass + k;
        }
        //this.setIniFold(false);

    }


}
