package net.ooder.esd.engine.enums;


import net.ooder.annotation.IconEnumstype;
import net.ooder.esd.annotation.ui.CustomImageType;
import net.ooder.esd.manager.plugins.api.enums.APIType;

import java.util.ArrayList;
import java.util.List;

public enum PackagePathType implements IconEnumstype {
    //App("App", "/App/", "用户工程", PackageType.userdef, "bpmfont bpmgongzuoliu"),
    App("App", "/App/", "用户工程", PackageType.sys, "bpmfont bpmgongzuoliu"),
    // test("test", "/test/", "本地测试", PackageType.userdef, "spafont spa-icon-tools"),
    bpm("bpm", "/bpm/", "流程应用", PackageType.custom, "bpmfont bpmgongzuoliuxitong"),
    bpmadmin("bpmadmin", "/bpm/admin/", "流程配置", PackageType.admin, "bpmfont bpmgongzuoliuxitongpeizhi"),
    custom("bpmcustom", "/bpm/custom/", "流程示例", PackageType.custom, "bpmfont bpmgongzuoliucheng"),
    bpmdisplay("bpmdisplay", "/bpm/custom/display/", "流转控制", PackageType.custom, "bpmfont bpmgongzuoliu"),
    bpmlist("bpmlist", "/bpm/custom/list/", "工作箱", PackageType.custom, "bpmfont bpmVueFlyActivityOperation"),
    bpmform("bpmform", "/bpm/form/", "工作流表单", PackageType.custom, "spafont spa-icon-page"),
    formplugins("formplugins", "/fdtform/plugins", "表单插件", PackageType.custom, "spafont spa-icon-tools"),
    img("img", "/img/", "图片库", PackageType.userdef, "spafont spa-icon-pic"),
    css("css", "/css/", "样式库", PackageType.userdef, "spafont spa-icon-c-cssbox"),
    java("java", "/java/", "JAVA编译器", PackageType.esd, "spafont spa-icon-c-cssbox"),
    debugproject("debugproject", "/debugproject/", "预览", PackageType.esd, "spafont spa-icon-c-cssbox"),
    Module("Module", "/Module/", "嵌入模块", PackageType.userdef, "spafont spa-icon-alignw"),
    preview("preview", "/preview/", "预览", PackageType.admin, "iconfont iconbug"),
    Debug("Debug", "/Debug/", "调试运行", PackageType.admin, "iconfont iconbug"),
    RAD("RAD", "/RAD/", "编辑器", PackageType.admin, "fa fa-cubes"),
    RADDB("RADDB", "/RAD/db/", "数据库插件", PackageType.esd, "spafont spa-icon-conf"),
    RADServer("RADServer", "/RAD/server/", "服务器插件", PackageType.esd, "bpmfont bpm-gongzuoliu-moxing"),
    RADResource("RADResource", "/RAD/resource/", "资源管理", PackageType.esd, CustomImageType.opt.getImageClass()),
    RADProject("RADProject", "/RAD/project/", "编辑器工程插件", PackageType.esd, "bpmfont bpm-gongzuoliu-moxing"),
    RADOrg("RADOrg", "/RAD/org/", "协同插件", PackageType.esd, CustomImageType.land.getImageClass()),
    esd("esd", "/esd/", "编辑器插件", PackageType.esd, "spafont spa-icon-tools"),
    systemnav("systemnav", "/system/nav/", "导航菜单", PackageType.sys, "spafont spa-icon-shukongjian"),
    action("action", "/action/", "系统菜单", PackageType.sys, "spafont spa-icon-c-cssbox"),
    system("system", "/system/", "统计监控", PackageType.sys, "spafont spa-icon-designview"),
    db("db", "/db/", "库表管理", PackageType.sys, "iconfont iconchucun"),
    fdt("fdt", "/fdt/", "数据库示例", PackageType.sys, "xui-icon-menu"),
    formula("formula", "/admin/formula/", "公式管理", PackageType.tool, "spafont spa-icon-function"),
    orgmanager("orgmanager", "/admin/org/", "组织机构管理", PackageType.tool, "bpmfont bpmgongzuoliu"),
    admin("admin", "/admin/", "管理工具", PackageType.tool, "spafont spa-icon-config"),
    bpd("bpd", "/admin/bpd/", "工作流插件", PackageType.tool, "spafont spa-icon-c-databinder"),
    esdright("esdright", "/esd/right/", "权限插件", PackageType.tool, "bpmfont bpmyuxiandengjibanli"),
    esddic("esddic", "/esd/dic/", "字典表", PackageType.tool, "spafont spa-icon-c-hiddeninput"),
    esdpage("esdpage", "/esd/page/", "页面插件", PackageType.tool, "spafont spa-icon-c-hiddeninput"),
    dsm("dsm", "/dsm/", "DSM建模", PackageType.dsm, "spafont spa-icon-conf"),
    dsmAgg("dsm", "/dsm/agg/", "聚合模型", PackageType.dsm, "spafont spa-icon-tools"),
    dsmEsdClass("dsmEsdClass", "/dsm/esdclass/", "实体关系", PackageType.dsm, "spafont spa-icon-conf"),
    dsmManger("dsmManger", "/dsm/manager/", "领域实例", PackageType.dsm, "spafont spa-icon-conf"),
    dsmAdmin("dsmAdmin", "/dsm/admin/", "控制台", PackageType.dsm, "xui-uicmd-restore"),
    dsmWebSite("dsmWebSite", "/dsm/website/", "模板站点", PackageType.dsm, "spafont spa-icon-module"),
    dsmManager("dsmManager", "/dsm/manager/", "DSM管理", PackageType.dsm, "spafont spa-icon-settingprj"),
    repository("repository", "/dsm/repository/", "库表关系", PackageType.dsm, "iconfont iconchucun"),
    dsmTable("dsmTable", "/dsm/repository/table/", "资源库", PackageType.dsm, "spafont spa-icon-c-grid"),
    dsmNav("dsmNav", "/dsm/nav/", "导航", PackageType.dsm, "spafont spa-icon-shukongjian"),
    dsmTemp("dsmTemp", "/dsm/temp/", "领域模板", PackageType.dsm, "iconfont iconchangjingguanli"),
    dsmWebSiteTemp("dsmWebSiteTemp", "/dsm/website/temp/", "模板管理", PackageType.dsm, "iconfont iconchangjingguanli"),
    dsmSelect("dsmSelect", "/dsm/website/select/", "站点模板", PackageType.dsm, "spafont spa-icon-module");


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
