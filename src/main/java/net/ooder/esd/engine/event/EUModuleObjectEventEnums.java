package net.ooder.esd.engine.event;

import net.ooder.annotation.EventEnums;

public enum EUModuleObjectEventEnums implements EventEnums {

    befaultUpLoad("开始上传文件", "befaultUpLoad"),

    upLoading("上传中", "upLoading"),

    upLoadEnd("上传完毕", "upLoadEnd"),

    upLoadError("上传错误", "upLoadError"),

    append("追加成功", "append"),

    share("文件分享", "share"),

    beforDownLaod("文件下载", "beforDownLoad"),

    downLaoding("文件下载中", "downLoading"),

    downLaodEnd("文件下载结束", "downLoadEnd");


    private String name;

    private Integer code;

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

    EUModuleObjectEventEnums(String name, String method) {

	this.name = name;
	this.method = method;
	this.code = code;

    }

    @Override
    public String toString() {
	return method.toString();
    }



    public static EUModuleObjectEventEnums fromMethod(String method) {
	for (EUModuleObjectEventEnums type : EUModuleObjectEventEnums.values()) {
	    if (type.getMethod().equals(method)) {
		return type;
	    }
	}
	return null;
    }

    public static EUModuleObjectEventEnums fromType(String method) {
	for (EUModuleObjectEventEnums type : EUModuleObjectEventEnums.values()) {
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
