package net.ooder.esd.dsm.enums;


import net.ooder.annotation.IconEnumstype;

public enum RepositoryTempType implements IconEnumstype {
    statistics("默认模板", "fas fa-template"),
    mybatis("Mybatis模板", "fas fa-database"),
    Hibernate("Hibernate模板", "fas fa-cogs");

    private final String name;
    private final String imageClass;


    RepositoryTempType(String name, String imageClass) {
        this.name = name;
        this.imageClass = imageClass;

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
