package net.ooder.esd.manager.plugins.style.node;

import net.ooder.esd.manager.plugins.img.node.ALImg;
import net.ooder.esd.manager.plugins.img.node.Config;
import net.ooder.esd.manager.plugins.style.node.ALStyle;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ALStyleFolder {
    String name;
    String id;
    String path;
    String caption;
    Config config = new Config();
    String imageClass;
    List<ALStyle> styles;
    ALStyleFolder defaultConfig;
    List<ALStyleFolder> subItems = new ArrayList<ALStyleFolder>();

    public ALStyleFolder(Folder folder) {
        this.caption = folder.getDescrition();
        this.id = folder.getID();
        this.path = folder.getPath();
        this.name = folder.getName();
        this.build(folder, false);

    }


    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public ALStyleFolder(String id, String caption, String name, String imageClass) {
        this.id = id;
        this.caption = caption;
        this.name = name;
        this.imageClass = imageClass;
    }

    public void build(Folder folder, boolean hasChild) {
        for (FileInfo fileInfo : folder.getFileList()) {
            Pattern p = Pattern.compile(config.getPattern(), Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(fileInfo.getName());
            if (matcher.find()) {
                ALStyle style = new ALStyle(fileInfo);
                if (styles == null) {
                    styles = new ArrayList<ALStyle>();
                }
                if (!styles.contains(style)) {
                    styles.add(style);
                }


            }
        }
        if (hasChild) {
            for (Folder cfoolder : folder.getChildrenList()) {
                ALStyleFolder cALImgFolder = new ALStyleFolder(cfoolder);
                this.subItems.add(cALImgFolder);
            }
        }

    }

    public ALStyleFolder getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(ALStyleFolder defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public List<ALStyleFolder> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<ALStyleFolder> subItems) {
        this.subItems = subItems;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }


    public String getPath() {
        return path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public List<ALStyle> getStyles() {
        return styles;
    }

    public void setStyle(List<ALStyle> styles) {
        this.styles = styles;
    }

    public void setPath(String path) {
        this.path = path;

    }

    protected ALStyleFolder clone() {
        ALStyleFolder ialFont = new ALStyleFolder();
        ialFont.setId(this.id);
        ialFont.setCaption(this.caption);
        ialFont.setImageClass(this.getImageClass());
        ialFont.setPath(this.path);
        ialFont.setSubItems(this.subItems);
        ialFont.setName(this.name);
        ialFont.setConfig(this.config);
        ialFont.setStyle(this.styles);
        return ialFont;
    }


    public ALStyleFolder() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}