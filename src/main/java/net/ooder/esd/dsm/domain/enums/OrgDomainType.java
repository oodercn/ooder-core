package net.ooder.esd.dsm.domain.enums;


import net.ooder.annotation.IconEnumstype;

public enum OrgDomainType implements IconEnumstype {
    DEPARMENT("组织机构", "ri-building-line"),
    ROLE("角色", "ri-user-tag-line"),
    PERSON("成员", "ri-user-line"),
    RIGHT("权限", "ri-key-line"),
    ALL("全部", "ri-stack-line");

    private final String name;
    private final String imageClass;


    OrgDomainType(String name, String imageClass) {
        this.name = name;
        this.imageClass = imageClass;
    }

    public static OrgDomainType fromType(String type) {
        OrgDomainType defaultViewType = ALL;
        if (type != null) {
            for (OrgDomainType viewType : OrgDomainType.values()) {
                if (viewType.getType().equals(type)) {
                    defaultViewType = viewType;
                }
            }
        }

        return defaultViewType;
    }

    public String getImageClass() {
        return imageClass;
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
