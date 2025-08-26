package net.ooder.esd.manager.plugins.img.node;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;

import java.util.ArrayList;
import java.util.List;


public class ImgConfig {
    String id;
    String caption;
    String name;
    String imageClass;
    Config config;
    List<ImgConfig> sub;
    List<ALImg> imgs;
    @JSONField(serialize = false)
    ALImgFolder defaultConfig;
    @JSONField(serialize = false)
    List<ALImgFolder> subItems = new ArrayList<ALImgFolder>();
    String projectName;

    Integer imageWidth = 64;

    Integer imageHeight = 64;

    String pattern = ".*\\.(gif|jpg|jpeg|bmp|png)$";


    public ImgConfig(ALImgFolder imgFolder) {
        this.id = imgFolder.getId();
        this.projectName=imgFolder.getProjectName();
        this.config = imgFolder.getConfig();
        this.imageWidth = config.getImageWidth();
        this.imageHeight = config.getImageHeight();
        this.pattern = config.getPattern();
        this.caption = imgFolder.getCaption();
        this.name = imgFolder.getCaption();
        this.subItems = imgFolder.getSubItems();
        this.defaultConfig = imgFolder.getDefaultConfig();
        this.imgs = imgFolder.getImgs();
        this.imageClass = imgFolder.getImageClass();
    }

    public ImgConfig(String id, String caption, String name, String imageClass) {
        this.id = id;
        this.caption = caption;
        this.name = name;
        this.imageClass = imageClass;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setSub(List<ImgConfig> sub) {
        this.sub = sub;
    }

    public void setDefaultConfig(ALImgFolder defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public List<ALImg> getImgs() {
        if (imgs == null || imgs.isEmpty()) {
            ImgConfig defaultConfig = this.getDefaultConfig();
            if (defaultConfig != null) {
                imgs = new ArrayList<ALImg>();
                for (ALImg font : defaultConfig.getImgs()) {
                    imgs.add(font.clone());
                }
            }
        }
        return imgs;
    }

    public List<ALImgFolder> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<ALImgFolder> subItems) {
        this.subItems = subItems;
    }

    public List<ImgConfig> getSub() {
        if (this.subItems != null && !subItems.isEmpty()) {
            sub = new ArrayList<ImgConfig>();
            for (ALImgFolder configMenu : subItems) {
                ImgConfig config = null;
                try {
                    config = getClient().getImgConfig(projectName,configMenu.getId());
                } catch (JDSException e) {
                    e.printStackTrace();
                }

                if (config != null) {
                    config.setImageClass(configMenu.getImageClass());
                    sub.add(config);

                } else {
                    sub.add(new ImgConfig(configMenu));
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


    public void setImgs(List<ALImg> imgs) {
        this.imgs = imgs;
    }

    @JSONField(serialize = false)
    public ImgConfig getDefaultConfig() {
        ImgConfig defaultConfig = null;
        try {
            if (defaultConfig != null) {
                defaultConfig = this.getClient().getImgConfig(projectName,defaultConfig.getId());
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return defaultConfig;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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