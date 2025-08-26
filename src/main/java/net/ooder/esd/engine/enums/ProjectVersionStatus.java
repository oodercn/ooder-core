package net.ooder.esd.engine.enums;


import net.ooder.annotation.Enumstype;
import net.ooder.common.FolderState;

public enum ProjectVersionStatus implements Enumstype {

    UNDERREVISION("UNDERREVISION", FolderState.locked, "冻结"),

    RELEASED("RELEASED", FolderState.normal, "激活"),

    DELETEED("DELETEED", FolderState.deleted, "删除"),

    CLEAR("CLEAR", FolderState.deleted, "清空"),

    UNDERTEST("UNDERTEST", FolderState.tested, "测试中");


    private FolderState folderState;
    private String type;

    private String name;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    ProjectVersionStatus(String type, FolderState folderState, String name) {
        this.type = type;
        this.name = name;
        this.folderState = folderState;


    }

    public FolderState getFolderState() {
        return folderState;
    }

    public void setFolderState(FolderState folderState) {
        this.folderState = folderState;
    }

    @Override
    public String toString() {
        return type;
    }

    public static ProjectVersionStatus fromType(String typeName) {
        for (ProjectVersionStatus type : ProjectVersionStatus.values()) {
            if (type.getType().equals(typeName)) {
                return type;
            }
        }
        return UNDERTEST;
    }

    public static ProjectVersionStatus fromFolderType(String typeName) {
        for (ProjectVersionStatus type : ProjectVersionStatus.values()) {
            if (type.getFolderState().equals(typeName)) {
                return type;
            }
        }
        return UNDERTEST;
    }

}
