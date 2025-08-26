package net.ooder.esd.bean;


import com.alibaba.fastjson.annotation.JSONField;

import net.ooder.annotation.IconEnumstype;
import net.ooder.esd.annotation.TreeItem;
import net.ooder.esd.util.json.BindClassArrDeserializer;


public enum TreeEnums implements TreeItem, IconEnumstype {

    node1("node1", ""),
    node2("node2", "", TreeNode3Enums.class);

    private String id;
    private String type;
    private String name;
    private String caption;
    private String imageClass;
    private boolean iniFold;
    private boolean dynDestory;
    private boolean lazyLoad;
    @JSONField(deserializeUsing = BindClassArrDeserializer.class)
    private Class[] bindClass;


    TreeEnums(String caption, String imageClass, Class... bindClass) {
        this.id = name();
        this.type = name();
        this.name = name();
        this.bindClass = bindClass;
        this.caption = caption;
        this.imageClass = imageClass;
    }

    TreeEnums(String caption, String imageClass) {
        this.id = name();
        this.type = name();
        this.name = name();
        this.caption = caption;
        this.imageClass = imageClass;
    }

    TreeEnums(String caption, String imageClass, boolean iniFold, boolean dynDestory, boolean lazyLoad, Class... bindClass) {
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
