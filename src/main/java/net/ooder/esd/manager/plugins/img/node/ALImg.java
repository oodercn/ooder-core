package net.ooder.esd.manager.plugins.img.node;

import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.ProjectCacheManager;
import net.ooder.esd.engine.inner.INProjectVersion;
import net.ooder.vfs.FileInfo;

public class ALImg {
    String name;
    String imgId;
    String path;
    String location;


    public ALImg(String projectVersionName, FileInfo fileInfo) {
        this.name = fileInfo.getName();
        this.imgId = fileInfo.getID();
        this.path = fileInfo.getPath();

        try {
            if (projectVersionName != null) {
                ProjectCacheManager projectCacheManager = ESDFacrory.getDefalutProjectManager();
                INProjectVersion projectVersion = projectCacheManager.getProjectVersionByName(projectVersionName);
                String rootPath = projectVersion.getPath();
                if (path.startsWith(rootPath)) {
                    path = StringUtility.replace(path, rootPath, "");
                }
            }

        } catch (JDSException e) {
            e.printStackTrace();
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

    protected ALImg clone() {
        ALImg ialFont = new ALImg();
        ialFont.setImgId(this.imgId);
        ialFont.setPath(this.path);
        ialFont.setName(this.name);
        return ialFont;
    }


    public ALImg() {

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof ALImg) {
            return ((ALImg) obj).getPath().equals(this.getPath());
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