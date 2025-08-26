package net.ooder.esd.manager.plugins.img.node;

import net.ooder.common.JDSException;
import net.ooder.esd.engine.Project;

import java.util.List;

public class ImgNode {

    String name;
    String id;
    String imageClass;
    String projectName;
    String caption;
    List<ImgConfig> sub;
    public Boolean iniFold = false;


    public ImgNode(Project project) {
        this.projectName = project.getProjectName();
        this.name = projectName;
        this.caption = project.getDesc() + "(" + projectName + ")";
        this.imageClass = imageClass;
        this.id = project.getId();
        try {
            this.sub = project.getImgs();
        } catch (JDSException e) {
            e.printStackTrace();
        }


    }

    public List<ImgConfig> getSub() {
        return sub;
    }

    public void setSub(List<ImgConfig> sub) {
        this.sub = sub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }
}
