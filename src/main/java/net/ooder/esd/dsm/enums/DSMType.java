package net.ooder.esd.dsm.enums;


import net.ooder.annotation.IconEnumstype;

public enum DSMType implements IconEnumstype {
    ALL("模版库", "ri-stack-line", "ri-git-branch-line", "java.agg.AggEditor"),
    REPOSITORY("仓储库", "ri-database-line", "ri-file-code-line", "java.repository.JavaEditor"),
    AGGREGATION("领域聚合", "ri-node-tree", "ri-git-branch-line", "java.agg.AggEditor"),
    VIEW("视图工厂", "ri-table-line", "ri-eye-line", "java.view.JavaViewEditor"),
    CUSTOMDOMAIN("通用域", "ri-upload-cloud-line", "ri-git-branch-line", "java.agg.AggEditor"),
    SUPPORTDOMAIN("支撑域", "ri-node-tree", "ri-git-branch-line", "java.agg.AggEditor"),
    USERDOMAIN("用户域", "ri-stack-line", "ri-git-branch-line", "java.agg.AggEditor");

    private final String name;
    private final String imageClass;
    private final String fileImgClass;
    private final String editor;

    DSMType(String name, String imageClass, String fileImgClass, String editor) {
        this.name = name;
        this.imageClass = imageClass;
        this.fileImgClass = fileImgClass;
        this.editor = editor;
    }

    public static DSMType[] getCustomTypes() {
        return new DSMType[]{REPOSITORY, AGGREGATION, VIEW};
    }

    public static DSMType[] getDomainTypes() {
        return new DSMType[]{SUPPORTDOMAIN, CUSTOMDOMAIN, USERDOMAIN};
    }

    public String getFileImgClass() {
        return fileImgClass;
    }

    public String getEditor() {
        return editor;
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

    public String getImageClass() {
        return imageClass;
    }
}
