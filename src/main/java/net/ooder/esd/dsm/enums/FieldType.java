package net.ooder.esd.dsm.enums;


import net.ooder.annotation.Enumstype;

public enum FieldType implements Enumstype {
    custom("普通"), pk("主键"), fk("外键"), caption("显示名称"), otherCaption("外键关联"), ref("外部引用"),;


    private final String name;


    FieldType(String name) {
        this.name = name;
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
