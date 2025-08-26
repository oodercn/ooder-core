package net.ooder.esd.engine.event;

import net.ooder.annotation.EventEnums;

public enum PackageEventEnums implements EventEnums {

    create("创建文件", "create"),

    lock("文件夹锁定", "lock"),

    beforReName("名称开始修改", "beforReName"),

    reNameEnd("名称开始修改", "reNameEnd"),

    beforDelete("开始删除", "beforDelete"),

    deleteing("删除进行中", "deleteing"),

    deleteEnd("删除完成", "deleteEnd"),

    save("保存完成", "save"),

    beforCopy("Copy开始", "beforCopy"),

    copying("正在COPY", "copying"),

    copyEnd("Copy完成", "copyEnd"),

    beforMove("Move开始", "beforMove"),

    moving("正在Move", "moving"),

    moveEnd("Move完成", "moveEnd"),

    beforClean("开始清空", "beforClean"),

    cleanEnd("清空完成", "cleanEnd"),

    restore("还原完成", "restore");

    private String name;


    private String method;


    public String getMethod() {
	return method;
    }

    public void setMethod(String method) {
	this.method = method;
    }

    public void setName(String name) {
	this.name = name;
    }


    public String getName() {
	return name;
    }

    PackageEventEnums(String name, String method) {

	this.name = name;
	this.method = method;


    }

    @Override
    public String toString() {
	return method.toString();
    }


    public static PackageEventEnums fromMethod(String method) {
	for (PackageEventEnums type : PackageEventEnums.values()) {
	    if (type.getMethod().equals(method)) {
		return type;
	    }
	}
	return null;
    }

    public static PackageEventEnums fromType(String method) {
	for (PackageEventEnums type : PackageEventEnums.values()) {
	    if (type.getMethod().equals(method)) {
		return type;
	    }
	}
	return null;
    }

    @Override
    public String getType() {
	return method.toString();
    }

}
