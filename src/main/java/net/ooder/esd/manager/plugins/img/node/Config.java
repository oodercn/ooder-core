package net.ooder.esd.manager.plugins.img.node;

import java.util.List;

public class Config {

    Integer imageWidth = 64;

    Integer imageHeight = 64;

    String pattern = ".*\\.(gif|jpg|jpeg|bmp|png)$";


    List<String> list;

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
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

}
