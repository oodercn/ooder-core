package net.ooder.esd.bean;


import com.alibaba.fastjson.annotation.JSONField;

import net.ooder.annotation.IconEnumstype;
import net.ooder.esd.annotation.TreeItem;
import net.ooder.esd.util.json.BindClassArrDeserializer;


public enum TreeNode3Enums implements TreeItem, IconEnumstype {

    node3("node3", "fa-solid fa-file-alt"),
    node4("node4", "fa-solid fa-file-code");

    private String id;
    private String type;
    private String name;
    private String caption;
    private String imageClass;
    private boolean iniFold;
    private boolean dynDestory;
    private boolean lazyLoad;
    @JSONField(deserializeUsing = BindClassArrDeserializer.class)
    public Class[] bindClass;


    TreeNode3Enums(String caption, String imageClass) {
        this.id = name();
        this.type = name();
        this.name = name();
        this.caption = caption;
        this.imageClass = imageClass;
    }

    TreeNode3Enums(String caption, String imageClass, Class... bindClass) {
        this.id = name();
        this.type = name();
        this.name = name();
        this.bindClass = bindClass;
        this.caption = caption;
        this.imageClass = imageClass;
    }


    TreeNode3Enums(String caption, String imageClass, boolean iniFold, boolean dynDestory, boolean lazyLoad, Class... bindClass) {
        this.id = name();
        this.type = name();
        this.name = name();
        this.bindClass = bindClass;
        this.caption = caption;
        this.imageClass = imageClass;
        this.iniFold = iniFold;
        this.dynDestory = dynDestory;
        this.lazyLoad = lazyLoad;
    }


    public String getId() {
        return id;
    }

    public String getCaption() {
        return caption;
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

    @Override
    public Class[] getBindClass() {
        return bindClass;
    }

    @Override
    public boolean isIniFold() {
        return iniFold;
    }

    @Override
    public boolean isDynDestory() {
        return dynDestory;
    }

    @Override
    public boolean isLazyLoad() {
        return lazyLoad;
    }
}
