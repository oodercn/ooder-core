package net.ooder.esd.dsm.temp;


import net.ooder.esd.dsm.enums.DSMTempType;

import java.util.HashSet;
import java.util.Set;

public class DSMBean {

    public String dsmTempId;

    public String dsmId;

    public String space = "dsm";

    public String name;

    public String desc;

    public String thumbnailFileId;

    public String image;

    public DSMTempType type = DSMTempType.custom;

    public Set<String> javaTempIds = new HashSet<>();

    public DSMBean() {

    }

    public String getDsmTempId() {
        if (dsmTempId == null) {
            dsmTempId = dsmId;
        }
        return dsmTempId;
    }

    public void setDsmTempId(String dsmTempId) {
        this.dsmTempId = dsmTempId;
    }

    public String getThumbnailFileId() {
        return thumbnailFileId;
    }

    public void setThumbnailFileId(String thumbnailFileId) {
        this.thumbnailFileId = thumbnailFileId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<String> getJavaTempIds() {
        return javaTempIds;
    }

    public void setJavaTempIds(Set<String> javaTempIds) {
        this.javaTempIds = javaTempIds;
    }


    public DSMTempType getType() {
        return type;
    }

    public void setType(DSMTempType type) {
        this.type = type;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDsmId() {
        return dsmId;
    }

    public void setDsmId(String dsmId) {
        this.dsmId = dsmId;
    }

}
