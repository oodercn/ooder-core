package net.ooder.esd.dsm.enums;


import net.ooder.annotation.IconEnumstype;

public enum DSMType implements IconEnumstype {
    ALL("模版库", "fa-solid fa-layer-group", "fa-solid fa-code-branch", "java.agg.AggEditor"),
    REPOSITORY("仓储库", "fa-solid fa-database", "fa-solid fa-file-code", "java.repository.JavaEditor"),
    AGGREGATION("领域聚合", "fa-solid fa-project-diagram", "fa-solid fa-code-branch", "java.agg.AggEditor"),
    VIEW("视图工厂", "fa-solid fa-table", "fa-solid fa-eye", "java.view.JavaViewEditor"),
    CUSTOMDOMAIN("通用域", "fa-solid fa-cloud-upload-alt", "fa-solid fa-code-branch", "java.agg.AggEditor"),
    SUPPORTDOMAIN("支撑域", "fa-solid fa-sitemap", "fa-solid fa-code-branch", "java.agg.AggEditor"),
    USERDOMAIN("用户域", "fa-solid fa-layer-group", "fa-solid fa-code-branch", "java.agg.AggEditor");

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
