package net.ooder.esd.dsm.enums;


import net.ooder.annotation.Enumstype;

public enum ThumbnailType implements Enumstype {
    javaTemp("java模板", "javaTempId"), dsmTemp("dsm模板", "dsmTempId"), sysTemp("sys模板", "sysId"), fileTemp("默认模板", "vfsFileId");


    private final String name;
    private final String pkName;


    ThumbnailType(String name, String pkName) {
        this.name = name;
        this.pkName = pkName;
    }

    public static ThumbnailType formType(String type) {
        for (ThumbnailType thumbnailType : ThumbnailType.values()) {
            if (thumbnailType.getType().equals(type)) {
                return thumbnailType;
            }
        }
        return fileTemp;
    }

    public static ThumbnailType formParamName(String pkName) {
        for (ThumbnailType thumbnailType : ThumbnailType.values()) {
            if (thumbnailType.getPkName().equals(pkName)) {
                return thumbnailType;
            }
        }
        return fileTemp;
    }


    public String getPkName() {
        return pkName;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public String getType() {
        return name();
    }

    @Override
    public String getName() {
        return name;
    }
}
