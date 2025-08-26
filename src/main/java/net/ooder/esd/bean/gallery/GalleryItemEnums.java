package net.ooder.esd.bean.gallery;


import net.ooder.annotation.IconEnumstype;

public enum GalleryItemEnums implements IGalleryItem, IconEnumstype {

    a("item1", "fa fa-lg fa-close"),

    b("item2", "xui-icon-number1"),

    c("item3", "xui-icon-number2"),

    d("item4", "xui-icon-number3");


    private String type;
    private String name;
    private String caption;
    private String imageClass;
    public String comment;
    public String renderer;
    public String imagePos;
    public String imageBgSize;
    public String imageRepeat;
    public String iconFontSize;
    public String iconFontCode;
    public String iconStyle;
    public String flagText;
    public String flagClass;
    public String flagStyle;
    public String valueSeparator;
    public String bindClassName;
    public Class[] bindClass;

    GalleryItemEnums(String caption, String imageClass) {
        this.type = name();
        this.caption = caption;
        this.imageClass = imageClass;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String getRenderer() {
        return renderer;
    }

    @Override
    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    @Override
    public String getImagePos() {
        return imagePos;
    }

    @Override
    public void setImagePos(String imagePos) {
        this.imagePos = imagePos;
    }

    @Override
    public String getImageBgSize() {
        return imageBgSize;
    }

    @Override
    public void setImageBgSize(String imageBgSize) {
        this.imageBgSize = imageBgSize;
    }

    @Override
    public String getImageRepeat() {
        return imageRepeat;
    }

    @Override
    public void setImageRepeat(String imageRepeat) {
        this.imageRepeat = imageRepeat;
    }

    @Override
    public String getIconFontSize() {
        return iconFontSize;
    }

    @Override
    public void setIconFontSize(String iconFontSize) {
        this.iconFontSize = iconFontSize;
    }

    @Override
    public String getIconFontCode() {
        return iconFontCode;
    }

    @Override
    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
    }

    @Override
    public String getIconStyle() {
        return iconStyle;
    }

    @Override
    public void setIconStyle(String iconStyle) {
        this.iconStyle = iconStyle;
    }

    @Override
    public String getFlagText() {
        return flagText;
    }

    @Override
    public void setFlagText(String flagText) {
        this.flagText = flagText;
    }

    @Override
    public String getFlagClass() {
        return flagClass;
    }

    @Override
    public void setFlagClass(String flagClass) {
        this.flagClass = flagClass;
    }

    @Override
    public String getFlagStyle() {
        return flagStyle;
    }

    @Override
    public void setFlagStyle(String flagStyle) {
        this.flagStyle = flagStyle;
    }

    @Override
    public String getValueSeparator() {
        return valueSeparator;
    }

    @Override
    public void setValueSeparator(String valueSeparator) {
        this.valueSeparator = valueSeparator;
    }

    @Override
    public String getBindClassName() {
        return bindClassName;
    }

    @Override
    public void setBindClassName(String bindClassName) {
        this.bindClassName = bindClassName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    @Override
    public Class[] getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class[] bindClass) {
        this.bindClass = bindClass;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getImageClass() {
        return imageClass;
    }
}

