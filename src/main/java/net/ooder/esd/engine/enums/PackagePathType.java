package net.ooder.esd.engine.enums;


import net.ooder.annotation.IconEnumstype;
import net.ooder.esd.annotation.ui.CustomImageType;
import net.ooder.esd.manager.plugins.api.enums.APIType;

import java.util.ArrayList;
import java.util.List;

public enum PackagePathType implements IconEnumstype {
    App("App", "/App/", "用户工程", PackageType.sys, "fa-solid fa-project-diagram"),
    bpm("bpm", "/bpm/", "流程应用", PackageType.custom, "fa-solid fa-sitemap"),
    bpmadmin("bpmadmin", "/bpm/admin/", "流程配置", PackageType.admin, "fa-solid fa-cogs"),
    custom("bpmcustom", "/bpm/custom/", "流程示例", PackageType.custom, "fa-solid fa-stream"),
    bpmdisplay("bpmdisplay", "/bpm/custom/display/", "流转控制", PackageType.custom, "fa-solid fa-exchange-alt"),
    bpmlist("bpmlist", "/bpm/custom/list/", "工作箱", PackageType.custom, "fa-solid fa-tasks"),
    bpmform("bpmform", "/bpm/form/", "工作流表单", PackageType.custom, "fa-solid fa-file-alt"),
    formplugins("formplugins", "/fdtform/plugins", "表单插件", PackageType.custom, "fa-solid fa-puzzle-piece"),
    img("img", "/img/", "图片库", PackageType.userdef, "fa-solid fa-image"),
    css("css", "/css/", "样式库", PackageType.userdef, "fa-solid fa-css3-alt"),
    java("java", "/java/", "JAVA编译器", PackageType.esd, "fa-solid fa-code"),
    debugproject("debugproject", "/debugproject/", "预览", PackageType.esd, "fa-solid fa-code"),
    Module("Module", "/Module/", "嵌入模块", PackageType.userdef, "fa-solid fa-puzzle-piece"),
    preview("preview", "/preview/", "预览", PackageType.admin, "fa-solid fa-search"),
    Debug("Debug", "/Debug/", "调试运行", PackageType.admin, "fa-solid fa-bug"),
    RAD("RAD", "/RAD/", "编辑器", PackageType.admin, "fa-solid fa-cubes"),
    RADDB("RADDB", "/RAD/db/", "数据库插件", PackageType.esd, "fa-solid fa-database"),
    RADServer("RADServer", "/RAD/server/", "服务器插件", PackageType.esd, "fa-solid fa-server"),
    RADResource("RADResource", "/RAD/resource/", "资源管理", PackageType.esd, "fa-solid fa-box-open"),
    RADProject("RADProject", "/RAD/project/", "编辑器工程插件", PackageType.esd, "fa-solid fa-project-diagram"),
    RADOrg("RADOrg", "/RAD/org/", "协同插件", PackageType.esd, "fa-solid fa-users"),
    esd("esd", "/esd/", "编辑器插件", PackageType.esd, "fa-solid fa-puzzle-piece"),
    systemnav("systemnav", "/system/nav/", "导航菜单", PackageType.sys, "fa-solid fa-bars"),
    action("action", "/action/", "系统菜单", PackageType.sys, "fa-solid fa-css3-alt"),
    system("system", "/system/", "统计监控", PackageType.sys, "fa-solid fa-chart-line"),
    db("db", "/db/", "库表管理", PackageType.sys, "fa-solid fa-database"),
    fdt("fdt", "/fdt/", "数据库示例", PackageType.sys, "fa-solid fa-database"),
    formula("formula", "/admin/formula/", "公式管理", PackageType.tool, "fa-solid fa-calculator"),
    orgmanager("orgmanager", "/admin/org/", "组织机构管理", PackageType.tool, "fa-solid fa-sitemap"),
    admin("admin", "/admin/", "管理工具", PackageType.tool, "fa-solid fa-tools"),
    bpd("bpd", "/admin/bpd/", "工作流插件", PackageType.tool, "fa-solid fa-exchange-alt"),
    esdright("esdright", "/esd/right/", "权限插件", PackageType.tool, "fa-solid fa-key"),
    esddic("esddic", "/esd/dic/", "字典表", PackageType.tool, "fa-solid fa-book"),
    esdpage("esdpage", "/esd/page/", "页面插件", PackageType.tool, "fa-solid fa-file-alt"),
    dsm("dsm", "/dsm/", "DSM建模", PackageType.dsm, "fa-solid fa-cogs"),
    dsmAgg("dsm", "/dsm/agg/", "聚合模型", PackageType.dsm, "fa-solid fa-project-diagram"),
    dsmEsdClass("dsmEsdClass", "/dsm/esdclass/", "实体关系", PackageType.dsm, "fa-solid fa-cogs"),
    dsmManger("dsmManger", "/dsm/manager/", "领域实例", PackageType.dsm, "fa-solid fa-cogs"),
    dsmAdmin("dsmAdmin", "/dsm/admin/", "控制台", PackageType.dsm, "fa-solid fa-tachometer-alt"),
    dsmWebSite("dsmWebSite", "/dsm/website/", "模板站点", PackageType.dsm, "fa-solid fa-globe"),
    dsmManager("dsmManager", "/dsm/manager/", "DSM管理", PackageType.dsm, "fa-solid fa-tools"),
    repository("repository", "/dsm/repository/", "库表关系", PackageType.dsm, "fa-solid fa-database"),
    dsmTable("dsmTable", "/dsm/repository/table/", "资源库", PackageType.dsm, "fa-solid fa-table"),
    dsmNav("dsmNav", "/dsm/nav/", "导航", PackageType.dsm, "fa-solid fa-bars"),
    dsmTemp("dsmTemp", "/dsm/temp/", "领域模板", PackageType.dsm, "fa-solid fa-template"),
    dsmWebSiteTemp("dsmWebSiteTemp", "/dsm/website/temp/", "模板管理", PackageType.dsm, "fa-solid fa-template"),
    dsmSelect("dsmSelect", "/dsm/website/select/", "站点模板", PackageType.dsm, "fa-solid fa-globe");
    
    private final String pattern;
    private String systemCode;
    private PackageType apiType;
    private String desc;
    private String imageClass;


    PackagePathType(String systemCode, String pattern, String desc, PackageType apiType, String imageClass) {
        this.systemCode = systemCode;
        this.pattern = pattern;
        this.desc = desc;
        this.apiType = apiType;
        this.imageClass = imageClass;


    }

    public PackageType getApiType() {
        return apiType;
    }

    public void setApiType(PackageType apiType) {
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

    public static PackagePathType fromSystemCode(String systemCode) {
        for (PackagePathType type : PackagePathType.values()) {
            if (type.getSystemCode().equals(systemCode)) {
                return type;
            }
        }
        return null;
    }


    public static PackagePathType equalsPath(String path) {
        for (PackagePathType type : PackagePathType.values()) {
            if (path.equals(type.pattern)) {
                return type;
            }
        }
        return null;
    }


    public static PackagePathType startPath(String path) {
        for (PackagePathType type : PackagePathType.values()) {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (path.indexOf(".") == -1 && !path.endsWith("/")) {
                path = path + "/";
            }
            if (path.startsWith(type.pattern)) {
                return type;
            }
        }
        return null;
    }

    public static PackagePathType[] listType(PackageType apiType) {
        List<PackagePathType> apiPathTypes = new ArrayList<PackagePathType>();
        for (PackagePathType type : PackagePathType.values()) {
            if (apiType == null || apiType.equals(APIType.all) || type.getApiType().equals(apiType)) {
                apiPathTypes.add(type);
            }
        }
        return apiPathTypes.toArray(new PackagePathType[]{});
    }


    @Override
    public String getType() {
        return name();
    }

    @Override
    public String getName() {
        return desc;
    }
}
