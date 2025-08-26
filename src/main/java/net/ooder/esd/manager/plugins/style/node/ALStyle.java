package net.ooder.esd.manager.plugins.style.node;

import net.ooder.vfs.FileInfo;

public class ALStyle {
    String name;
    String imgId;
    String path;
    String location;


    public ALStyle(FileInfo fileInfo) {
        this.name = fileInfo.getName();
        this.imgId = fileInfo.getID();
        this.path = fileInfo.getPath();
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.startsWith("/root")) {
            path = "/root" + path;
        }
        this.location = path;

    }


    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    protected ALStyle clone() {
        ALStyle ialFont = new ALStyle();
        ialFont.setImgId(this.imgId);
        ialFont.setPath(this.path);
        ialFont.setName(this.name);
        return ialFont;
    }


    public ALStyle() {

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ALStyle) {
            return ((ALStyle) obj).getPath().equals(this.getPath());
        }
        return super.equals(obj);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}