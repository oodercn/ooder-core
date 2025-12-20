package net.ooder.esd.engine.enums;


import net.ooder.annotation.IconEnumstype;
import net.ooder.esd.annotation.ui.CustomImageType;
import net.ooder.esd.manager.plugins.api.enums.APIType;

import java.util.ArrayList;
import java.util.List;

public enum PackagePathType implements IconEnumstype {
    App("App", "/App/", "用户工程", PackageType.sys, "ri-node-tree"),
    bpm("bpm", "/bpm/", "流程应用", PackageType.custom, "ri-node-tree"),
    bpmadmin("bpmadmin", "/bpm/admin/", "流程配置", PackageType.admin, "ri-settings-3-line"),
    custom("bpmcustom", "/bpm/custom/", "流程示例", PackageType.custom, "ri-flow-chart-line"),
    bpmdisplay("bpmdisplay", "/bpm/custom/display/", "流转控制", PackageType.custom, "ri-exchange-line"),
    bpmlist("bpmlist", "/bpm/custom/list/", "工作�?", PackageType.custom, "ri-list-check-line"),
    bpmform("bpmform", "/bpm/form/", "工作流表�?", PackageType.custom, "ri-file-line"),
    formplugins("formplugins", "/fdtform/plugins", "表单插件", PackageType.custom, "ri-puzzle-line"),
    img("img", "/img/", "图片�?", PackageType.userdef, "ri-image-line"),
    css("css", "/css/", "样式�?", PackageType.userdef, "ri-css3-line"),
    java("java", "/java/", "JAVA编译�?", PackageType.esd, "ri-code-box-line"),
    debugproject("debugproject", "/debugproject/", "预览", PackageType.esd, "ri-code-box-line"),
    Module("Module", "/Module/", "嵌入模块", PackageType.userdef, "ri-puzzle-line"),
    preview("preview", "/preview/", "预览", PackageType.admin, "ri-search-line"),
    Debug("Debug", "/Debug/", "调试运行", PackageType.admin, "ri-bug-line"),
    RAD("RAD", "/RAD/", "编辑�?", PackageType.admin, "ri-cube-line"),
    RADDB("RADDB", "/RAD/db/", "数据库插�?", PackageType.esd, "ri-database-line"),
    RADServer("RADServer", "/RAD/server/", "服务器插�?", PackageType.esd, "ri-server-line"),
    RADResource("RADResource", "/RAD/resource/", "资源管理", PackageType.esd, "ri-box-3-line"),
    RADProject("RADProject", "/RAD/project/", "编辑器工程插�?", PackageType.esd, "ri-node-tree"),
    RADOrg("RADOrg", "/RAD/org/", "协同插件", PackageType.esd, "ri-user-group-line"),
    esd("esd", "/esd/", "编辑器插�?", PackageType.esd, "ri-puzzle-line"),
    systemnav("systemnav", "/system/nav/", "导航菜单", PackageType.sys, "ri-menu-line"),
    action("action", "/action/", "系统菜单", PackageType.sys, "ri-css3-line"),
    system("system", "/system/", "统计监控", PackageType.sys, "ri-line-chart-line"),
    db("db", "/db/", "库表管理", PackageType.sys, "ri-database-line"),
    fdt("fdt", "/fdt/", "数据库示�?", PackageType.sys, "ri-database-line"),
    formula("formula", "/admin/formula/", "公式管理", PackageType.tool, "ri-calculator-line"),
    orgmanager("orgmanager", "/admin/org/", "组织机构管理", PackageType.tool, "ri-node-tree"),
    admin("admin", "/admin/", "管理工具", PackageType.tool, "ri-tools-line"),
    bpd("bpd", "/admin/bpd/", "工作流插�?", PackageType.tool, "ri-exchange-line"),
    esdright("esdright", "/esd/right/", "权限插件", PackageType.tool, "ri-key-line"),
    esddic("esddic", "/esd/dic/", "字典�?", PackageType.tool, "ri-book-line"),
    esdpage("esdpage", "/esd/page/", "页面插件", PackageType.tool, "ri-file-line"),
    dsm("dsm", "/dsm/", "DSM建模", PackageType.dsm, "ri-settings-3-line"),
    dsmAgg("dsm", "/dsm/agg/", "聚合模型", PackageType.dsm, "ri-node-tree"),
    dsmEsdClass("dsmEsdClass", "/dsm/esdclass/", "实体关系", PackageType.dsm, "ri-settings-3-line"),
    dsmManger("dsmManger", "/dsm/manager/", "领域实例", PackageType.dsm, "ri-settings-3-line"),
    dsmAdmin("dsmAdmin", "/dsm/admin/", "控制�?", PackageType.dsm, "ri-speed-line"),
    dsmWebSite("dsmWebSite", "/dsm/website/", "模板站点", PackageType.dsm, "ri-earth-line"),
    dsmManager("dsmManager", "/dsm/manager/", "DSM管理", PackageType.dsm, "ri-tools-line"),
    repository("repository", "/dsm/repository/", "库表关系", PackageType.dsm, "ri-database-line"),
    dsmTable("dsmTable", "/dsm/repository/table/", "资源�?", PackageType.dsm, "ri-table-line"),
    dsmNav("dsmNav", "/dsm/nav/", "导航", PackageType.dsm, "ri-menu-line"),
    dsmTemp("dsmTemp", "/dsm/temp/", "领域模板", PackageType.dsm, "ri-file-copy-line"),
    dsmWebSiteTemp("dsmWebSiteTemp", "/dsm/website/temp/", "模板管理", PackageType.dsm, "ri-file-copy-line"),
    dsmSelect("dsmSelect", "/dsm/website/select/", "站点模板", PackageType.dsm, "ri-earth-line");
    
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
