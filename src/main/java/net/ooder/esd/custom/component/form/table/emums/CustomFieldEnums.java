package net.ooder.esd.custom.component.form.table.emums;


import net.ooder.annotation.Enumstype;

public enum CustomFieldEnums implements Enumstype {

    pkName("pkName", "主键"),
    configKey("configKey", "数据库标识"),
    tableName("tableName", "库表名称"),
    pkValue("pkValue", "主键值");

    private final String name;
    private final String type;

    CustomFieldEnums(String type, String name) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static CustomFieldEnums fromType(String typeName) {
        for (CustomFieldEnums type : CustomFieldEnums.values()) {
            if (type.getType().equals(typeName)) {
                return type;
            }
        }
        return null;
    }


    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }
}
