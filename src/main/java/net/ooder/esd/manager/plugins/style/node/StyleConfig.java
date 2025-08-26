package net.ooder.esd.manager.plugins.style.node;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.manager.plugins.img.node.Config;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;

import java.util.ArrayList;
import java.util.List;


public class StyleConfig {
    String id;
    String caption;
    String name;
    String imageClass;
    Config config;
    List<StyleConfig> sub;
    List<ALStyle> styles;
    @JSONField(serialize = false)
    ALStyleFolder defaultConfig;
    @JSONField(serialize = false)
    List<ALStyleFolder> subItems = new ArrayList<ALStyleFolder>();

    public StyleConfig(ALStyleFolder imgFolder) {
        this.id = imgFolder.getId();
        this.caption = imgFolder.getCaption();
        this.name = imgFolder.getCaption();
        this.subItems = imgFolder.getSubItems();
        this.defaultConfig = imgFolder.getDefaultConfig();
        this.styles = imgFolder.getStyles();
        this.imageClass = imgFolder.getImageClass();
    }

    public StyleConfig(String id, String caption, String name, String imageClass) {
        this.id = id;
        this.caption = caption;
        this.name = name;
        this.imageClass = imageClass;
    }

    public void setSub(List<StyleConfig> sub) {
        this.sub = sub;
    }

    public void setDefaultConfig(ALStyleFolder defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public List<ALStyle> getstyles() {
        if (styles == null || styles.isEmpty()) {
            StyleConfig defaultConfig = this.getDefaultConfig();
            if (defaultConfig != null) {
                styles = new ArrayList<ALStyle>();
                for (ALStyle font : defaultConfig.getstyles()) {
                    styles.add(font.clone());
                }
            }
        }
        return styles;
    }

    public List<ALStyleFolder> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<ALStyleFolder> subItems) {
        this.subItems = subItems;
    }

    public List<StyleConfig> getSub() {
        if (this.subItems != null && !subItems.isEmpty()) {
            sub = new ArrayList<StyleConfig>();
            for (ALStyleFolder configMenu : subItems) {
                StyleConfig config = null;
                try {
                    config = getClient().getStyleConfig(configMenu.getId());
                } catch (JDSException e) {
                    e.printStackTrace();
                }

                if (config != null) {
                    config.setImageClass(configMenu.getImageClass());
                    sub.add(config);

                } else {
                    sub.add(new StyleConfig(configMenu));
                }

            }
        }
        return sub;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }


    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }


    public void setstyles(List<ALStyle> styles) {
        this.styles = styles;
    }

    @JSONField(serialize = false)
    public StyleConfig getDefaultConfig() {
        StyleConfig defaultConfig = null;
        try {
            if (defaultConfig != null) {
                defaultConfig = this.getClient().getStyleConfig(defaultConfig.getId());
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return defaultConfig;
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

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JSONField(serialize = false)
    public ESDClient getClient() throws JDSException {
        return ESDFacrory.getAdminESDClient();
    }

}