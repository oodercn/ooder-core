package net.ooder.esd.dsm.domain.enums;


import net.ooder.annotation.IconEnumstype;

public enum OrgDomainType implements IconEnumstype {
    DEPARMENT("组织机构", "fa-solid fa-building"),
    ROLE("角色", "fa-solid fa-user-tag"),
    PERSON("成员", "fa-solid fa-user"),
    RIGHT("权限", "fa-solid fa-key"),
    ALL("所有", "fa-solid fa-layer-group");

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
