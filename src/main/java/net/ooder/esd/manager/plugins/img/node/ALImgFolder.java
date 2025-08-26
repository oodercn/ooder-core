package net.ooder.esd.manager.plugins.img.node;

import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ALImgFolder {
    String name;
    String id;
    String path;
    String caption;
    Config config = new Config();
    String imageClass;
    String projectName;
    List<ALImg> imgs;
    ALImgFolder defaultConfig;
    List<ALImgFolder> subItems = new ArrayList<ALImgFolder>();

    public ALImgFolder(String projectName, Folder folder) {
        this.caption = folder.getDescrition();
        this.projectName = projectName;
        this.id = folder.getID();
        this.path = folder.getPath();
        this.name = folder.getName();
        this.build(folder, projectName, false);

    }


    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public ALImgFolder(String id, String caption, String name, String imageClass) {
        this.id = id;
        this.caption = caption;
        this.name = name;
        this.imageClass = imageClass;
    }

    public void build(Folder folder, String projectName, boolean hasChild) {
        for (FileInfo fileInfo : folder.getFileList()) {
            Pattern p = Pattern.compile(config.getPattern(), Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(fileInfo.getName());
            if (matcher.find()) {
                ALImg img = new ALImg(projectName, fileInfo);
                if (imgs == null) {
                    imgs = new ArrayList<ALImg>();
                }
                if (!imgs.contains(img)) {
                    imgs.add(img);
                }


            }
        }
        if (hasChild) {
            for (Folder cfoolder : folder.getChildrenList()) {
                ALImgFolder cALImgFolder = new ALImgFolder(projectName, cfoolder);
                this.subItems.add(cALImgFolder);
            }
        }

    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public ALImgFolder getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(ALImgFolder defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public List<ALImgFolder> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<ALImgFolder> subItems) {
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


    public List<ALImg> getImgs() {
        return imgs;
    }

    public void setImgs(List<ALImg> imgs) {
        this.imgs = imgs;
    }

    public void setPath(String path) {
        this.path = path;

    }

    protected ALImgFolder clone() {
        ALImgFolder ialFont = new ALImgFolder();
        ialFont.setId(this.id);
        ialFont.setProjectName(this.projectName);
        ialFont.setCaption(this.caption);
        ialFont.setImageClass(this.getImageClass());
        ialFont.setPath(this.path);
        ialFont.setSubItems(this.subItems);
        ialFont.setName(this.name);
        ialFont.setConfig(this.config);
        ialFont.setImgs(this.imgs);
        return ialFont;
    }


    public ALImgFolder() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}