package net.ooder.esd.manager.plugins.font.node;


import java.util.ArrayList;
import java.util.List;


public class IFontMenu {
    String id;
    String caption;
    String name;
    String imageClass;
    IFontMenu defaultFont;
    List<IFontMenu> subItems = new ArrayList<>();

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }


    public IFontMenu() {

    }

    public IFontMenu(FontConfig fontConfig) {
        this.id = fontConfig.getId();
        this.caption = fontConfig.getDescription();
        this.name = fontConfig.getName();
        this.imageClass = fontConfig.getImageClass();
    }

    public IFontMenu(String id, String caption, String name, String imageClass) {
        this.id = id;
        this.caption = caption;
        this.name = name;
        this.imageClass = imageClass;
    }

    public List<IFontMenu> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<IFontMenu> subItems) {
        this.subItems = subItems;
    }

    public IFontMenu getDefaultFont() {
        return defaultFont;
    }

    public void setDefaultFont(IFontMenu defaultFont) {
        this.defaultFont = defaultFont;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        if (caption == null || caption.equals("")) {
            caption = this.name;
        }
        return caption;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof IFontMenu) {
            return ((IFontMenu) obj).getId().equals(this.getId());
        }
        return super.equals(obj);
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}