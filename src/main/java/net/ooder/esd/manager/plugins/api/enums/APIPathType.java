package net.ooder.esd.manager.plugins.api.enums;

import net.ooder.esd.manager.plugins.api.enums.APIType;

import java.util.ArrayList;
import java.util.List;

public enum APIPathType {

    RAD("RAD", "/RAD/", "编辑器", APIType.system, "fa fa-cubes"),

    admin("admin", "/admin/", "管理控制台", APIType.system, "xui-uicmd-restore"),

    log("log", "/log/", "日志处理", APIType.system, "xui-icon-print"),

    vfs("vfs", "/api/vfs/", "分布式存储", APIType.system, "xui-icon-menu"),

    org("org", "/api/org/", "组织机构", APIType.system, "fa fa-cubes"),

    app("app", "/jds/iot/", "IOT应用", APIType.system, "fa fa-cubes"),

    bpm("bpm", "/jds/bpm/", "流程应用", APIType.system, "fa fa-cubes"),

    db("db", "/fdt/", "数据库接口", APIType.userdef, "xui-uicmd-max"),

    dsm("dsm", "/dsm/", "DSM建模", APIType.userdef, "spafont spa-icon-conf"),

    bpmclient("bpmclient", "/bpm/", "工作流接口", APIType.userdef, "fa fa-cubes"),

    orgclient("orgclient", "/system/org/", "组织机构接口", APIType.userdef, "fa fa-cubes"),

    fdt("fdt", "/jds/fdt/", "电子表单", APIType.system, "xui-uicmd-max");


    private final String pattern;
    private String systemCode;
    private APIType apiType;
    private String desc;
    private String imageClass;


    APIPathType(String systemCode, String pattern, String desc, APIType apiType, String imageClass) {
        this.systemCode = systemCode;
        this.desc = desc;
        this.pattern = pattern;
        this.apiType = apiType;
        this.imageClass = imageClass;


    }

    public APIType getApiType() {
        return apiType;
    }

    public void setApiType(APIType apiType) {
        this.apiType = apiType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    @Override
    public String toString() {
        return systemCode;
    }


    public String getPattern() {
        return pattern;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public static APIPathType fromSystemCode(String systemCode) {
        for (APIPathType type : APIPathType.values()) {
            if (type.getSystemCode().equals(systemCode)) {
                return type;
            }
        }
        return null;
    }

    public static APIPathType[] listType(APIType apiType) {
        List<APIPathType> apiPathTypes = new ArrayList<APIPathType>();
        for (APIPathType type : APIPathType.values()) {
            if (apiType == null || apiType.equals(APIType.all) || type.getApiType().equals(apiType)) {
                apiPathTypes.add(type);
            }
        }
        return apiPathTypes.toArray(new APIPathType[]{});
    }


}
