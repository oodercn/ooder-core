package net.ooder.esd.engine;

import net.ooder.common.ConfigCode;
import net.ooder.common.JDSException;
import net.ooder.common.ReturnType;
import net.ooder.common.database.dao.DBMap;
import net.ooder.common.database.metadata.MetadataFactory;
import net.ooder.common.database.metadata.ProviderConfig;
import net.ooder.common.database.metadata.TableInfo;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.md5.MD5InputStream;
import net.ooder.config.BPDProjectConfig;
import net.ooder.engine.ConnectInfo;
import net.ooder.engine.JDSSessionHandle;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esb.config.formula.FormulaInst;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.temp.DSMBean;
import net.ooder.esd.engine.enums.PackageType;
import net.ooder.esd.manager.plugins.api.enums.APIType;
import net.ooder.esd.manager.plugins.api.node.APIPaths;
import net.ooder.esd.manager.plugins.api.node.OODAPIConfig;
import net.ooder.esd.manager.plugins.font.node.FontConfig;
import net.ooder.esd.manager.plugins.img.node.ImgConfig;
import net.ooder.esd.manager.plugins.style.node.StyleConfig;
import net.ooder.esd.engine.config.DevUserConfig;
import net.ooder.esd.engine.config.LocalServer;
import net.ooder.esd.engine.config.ProjectConfig;
import net.ooder.esd.engine.config.RemoteServer;
import net.ooder.esd.engine.config.dsm.DSMProjectConfig;
import net.ooder.esd.engine.enums.ProjectDefAccess;
import net.ooder.esd.engine.enums.ProjectResourceType;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.server.SubSystem;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import net.ooder.web.APIConfig;
import net.ooder.web.RequestMethodBean;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ESDClient {

    public static final String CLASSPATT = ".cls";

    public static final String JSPATT = ".js";

    public static final String JAR_PATH = "deploy";

    public static final String WEBAPP_PATH = "webapp";

    public static final String EXPORT_PATH = "export";

    /**
     * 取得系统标识
     */
    @MethodChinaName(cname = "取得系统标识", display = false)
    public String getSystemCode();


    /**
     * 取得系统标识
     */
    @MethodChinaName(cname = "取得系统标识", display = false)
    public ConfigCode getConfigCode();


    /**
     * 取得SessionHandle
     *
     * @return
     */
    @MethodChinaName(cname = "取得SessionHandle", display = false)
    public JDSSessionHandle getSessionHandle();

    /**
     * 登陆
     *
     * @param connInfo 登陆连接信息
     * @throws JDSException
     */
    @MethodChinaName(cname = "登陆", returnStr = "connect($connInfo)", display = false)
    public void connect(ConnectInfo connInfo) throws JDSException;

    /**
     * 注销
     *
     * @return 结果标识
     * @throws JDSException
     */
    @MethodChinaName(cname = "注销", returnStr = "disconnect()", display = false)
    public ReturnType disconnect() throws JDSException;

    /**
     * 取得登录人信息
     *
     * @return
     */
    @MethodChinaName(cname = "取得登录人信息")
    public ConnectInfo getConnectInfo();

    /**
     * 获取用户空间
     *
     * @return
     */
    public MySpace getSpace();


    ModuleComponent cloneModuleComponent(ModuleComponent component) throws JDSException;

    /**
     * 高速裝載
     */

    public void quickReLoad();

    /**
     * 获取所有工程
     *
     * @param type
     * @return
     */
    @MethodChinaName(cname = "获取所有工程")
    public List<Project> getAllProject(ProjectDefAccess type);

    /**
     * 获取所有资源工程
     *
     * @param type
     * @return
     */
    @MethodChinaName(cname = "获取所有资源工程")
    public List<Project> getResourceAllProject(ProjectResourceType type);


    /**
     * 获取所属子系统
     *
     * @return
     */
    @MethodChinaName(cname = "获取所属子系统")
    public SubSystem getOwnSystem();

    /**
     * 创建工程
     *
     * @param projectName
     * @param desc
     * @param type
     * @return
     * @throws JDSException
     */
    @MethodChinaName(cname = "创建工程")
    public Project createProject(String projectName, String desc, ProjectDefAccess type) throws JDSException;

    /**
     * 创建资源工程
     *
     * @param type
     * @return
     */
    @MethodChinaName(cname = "创建资源工程")
    public Project createResourceProject(String projectName, String desc, ProjectResourceType type) throws JDSException;

    /**
     * 创建工程分支
     *
     * @param projectName
     * @return
     * @throws JDSException
     */
    @MethodChinaName(cname = "新建版本")
    public ProjectVersion createProcessVersion(String projectName) throws JDSException;

    @MethodChinaName(cname = "删除版本")
    public int removeProcessVersion(String versionName) throws JDSException;

    @MethodChinaName(cname = "重新装载")
    public void reLoadProject(String projectName) throws JDSException;

    @MethodChinaName(cname = "重新装载")
    public void reLoadProjectRoot(String projectName) throws JDSException;

    @MethodChinaName(cname = "获取当前激活版本")
    public ProjectVersion getActivityProjectVersion(String projectId) throws JDSException;

    @MethodChinaName(cname = "COPY 类文件")
    public EUModule copyClass(String versionName, String className, String tclassName) throws JDSException;

    @MethodChinaName(cname = "克隆工程")
    public Project cloneProject(String projectName, String desc, String tempName, ProjectDefAccess type) throws JDSException;

    @MethodChinaName(cname = "克隆工程")
    public Project cloneResourceProject(String projectName, String desc, String tempName, ProjectResourceType resourceType) throws JDSException;

    @MethodChinaName(cname = "更新工程信息")
    public Project updateProjectInfo(String projectName, String desc) throws JDSException;

    @MethodChinaName(cname = "文件操作")
    public FileInfo getFileByPath(String... paths) throws JDSException;

    @MethodChinaName(cname = "获取文件夹")
    public Folder getFolderByPath(String... paths) throws JDSException;
    @MethodChinaName(cname = "创建文件夹")
    public Folder createFolder(String... paths) throws JDSException;
    @MethodChinaName(cname = "COPY 文件")
    public Object copy(String projectName, String spath, String tpath) throws JDSException;
    @MethodChinaName(cname = "重命名文件")
    public Object reName(String projectName, String path, String newName) throws JDSException;
    @MethodChinaName(cname = "删除文件")
    public void delFile(List<String> paths, String projectName) throws JDSException;
    @MethodChinaName(cname = "激活工程版本")
    public void activateProjectVersion(String versionId) throws JDSException;
    @MethodChinaName(cname = "解冻工程版本")
    public void freezeProjectVersion(String versionId) throws JDSException;
    @MethodChinaName(cname = "删除工程")
    public void delProject(String projectId) throws JDSException;
    @MethodChinaName(cname = "根据工程名称获取工程")
    public Project getProjectByName(String projectName) throws JDSException;
    @MethodChinaName(cname = "根据工程版本ID获取工程")
    public ProjectVersion getProjectVersionById(String projectId) throws JDSException;
    @MethodChinaName(cname = "根据工程版本名称获取工程")
    public ProjectVersion getProjectVersionByName(String versionName) throws JDSException;
    @MethodChinaName(cname = "获取工程列表")
    public List<ProjectVersion> getProjectVersionList(String projectName) throws JDSException;
    @MethodChinaName(cname = "根据工程ID获取工程")
    public Project getProjectById(String projectId) throws JDSException;
    @MethodChinaName(cname = "更新用户工程")
    public void updateSpaceConfig(MySpaceConfig config) throws JDSException;
    @MethodChinaName(cname = "更新开发者信息")
    public void updateUserConfig(DevUserConfig config) throws JDSException;
    @MethodChinaName(cname = "获取开发者信息")
    public DevUserConfig getUserConfig() throws JDSException;
    @MethodChinaName(cname = "获取该版本下所有服务地址")
    public List<APIPaths> getAPIPathsByProject(String versionName) throws JDSException;
    @MethodChinaName(cname = "查询该版本下所有服务地址")
    public List<OODAPIConfig> searchLocalService(String versionName, String pattern) throws JDSException;
    @MethodChinaName(cname = "获取工程该版本所有字体库")
    public List<FontConfig> getFontByProject(String versionName) throws JDSException;
    @MethodChinaName(cname = "获取工程各版本字体库配置")
    public FontConfig getFont(String projectName, String fontId) throws JDSException;
    @MethodChinaName(cname = "获取图片资源配置")
    public ImgConfig getImgConfig(String projectName, String imgConfigId) throws JDSException;
    @MethodChinaName(cname = "编译打包图片资源")
    public ImgConfig buildImgConfig(String projectName, String path) throws JDSException;
    @MethodChinaName(cname = "刷新图片资源库")
    public void reLoadImageConfig() throws JDSException;
    @MethodChinaName(cname = "获取样式资源库")
    public StyleConfig getStyleConfig(String styleConfigId) throws JDSException;
    @MethodChinaName(cname = "编译样式资源")
    public StyleConfig buildStyleConfig(String projectName, String path) throws JDSException;
    @MethodChinaName(cname = "检索API接口服务")
    public List<APIConfig> scannerLocalClass(String packageName, String patten) throws JDSException;
    @MethodChinaName(cname = "检索本地页面自定义服务")
    public List<APICallerProperties> scannerLocalMethod(String packageName, String patten) throws JDSException;
    @MethodChinaName(cname = "检索指定包机构下的服务")
    public List<APIPaths> scannerLocalPaths(String packageName, String patten) throws JDSException;
    @MethodChinaName(cname = "根据路径获取服务集合")
    public APIPaths getAPIPaths(String path) throws JDSException;
    @MethodChinaName(cname = "获取根目录服务集合")
    public List<APIPaths> getAPITopPaths(String pattern, APIType apiType) throws JDSException;
    @MethodChinaName(cname = "根据路径获取服务配置")
    public APICallerProperties getAPIMethodConfig(String path) throws JDSException;
    @MethodChinaName(cname = "根据路径获取原生代码服务")
    RequestMethodBean getRequestMethodBean(String path, String projectName) throws JDSException;
    @MethodChinaName(cname = "根据路径获取原生代码服务配置")
    MethodConfig getMethodAPIBean(String path, String projectName) throws JDSException;
    @MethodChinaName(cname = "根据路径获取服务配置")
    public List<APICallerProperties> getAPIMethodsByProject(String projectName) throws JDSException;
    @MethodChinaName(cname = "根据路径获取所有服务配置")
    public List<APIConfig> getAPIConfigByProject(String projectName) throws JDSException;
    @MethodChinaName(cname = "获取所有数据库配置资源")
    public List<ProviderConfig> getAllDbConfig() throws JDSException;
    @MethodChinaName(cname = "移除数据库配置")
    public void removeDbConfig(String dbConfigKey) throws JDSException;
    @MethodChinaName(cname = "更新数据库配置")
    public void updateDbConfig(ProviderConfig config) throws JDSException;
    @MethodChinaName(cname = "更新工程配置")
    public void updateProjectConfig(String projectId, ProjectConfig config) throws JDSException;
    @MethodChinaName(cname = "更新工程应用APP配置")
    public void updateBPDConfig(String projectId, BPDProjectConfig config) throws JDSException;
    @MethodChinaName(cname = "更新工程DSM配置")
    public void updateDSMConfig(String projectId, DSMProjectConfig config) throws JDSException;
    @MethodChinaName(cname = "更新工程权限表达式配置")
    public void updateFormulaConfig(String projectId, FormulaInst formulaInst) throws JDSException;
    @MethodChinaName(cname = "删除权限表达式配置")
    public void deleteFormulaConfig(String projectId, String formulaInstId) throws JDSException;

    @MethodChinaName(cname = "更新持久层仓库配置")
    public void updateRepositoryInstConfig(String projectId, RepositoryInst config) throws JDSException;
    @MethodChinaName(cname = "更新数据库配置")
    public ProviderConfig getDbConfig(String configKey) throws JDSException;
    @MethodChinaName(cname = "获取数据库配置信息")
    public MetadataFactory getDbFactory(String configKey) throws JDSException;
    @MethodChinaName(cname = "保存文件")
    public FileInfo saveFile(StringBuffer content, String... paths) throws JDSException;
    @MethodChinaName(cname = "保存模块")
    public void saveModule(EUModule module, boolean dynBuild) throws JDSException;
    @MethodChinaName(cname = "保存模块源码")
    public EUModule saveModuleAsJson(String versionName, String path, String json) throws JDSException;
    @MethodChinaName(cname = "获取模块")
    public EUModule getModule(String className, String versionName) throws JDSException;
    @MethodChinaName(cname = "装载模块")
    public Set<EUModule> loadModules(Set<String> className, String versionName) throws JDSException;
    @MethodChinaName(cname = "从指定地址获取通用模块")
    public EUModule getCustomModule(String path, String versionName, Map<String, ?> valueMap) throws JDSException;
    @MethodChinaName(cname = "从注解驱动读取通用模块")
    public EUModule getCustomModule(MethodConfig methodConfig, String versionName, Map<String, ?> valueMap) throws JDSException;

    @MethodChinaName(cname = "重新编译模块信息")
    public EUModule rebuildCustomModule(String path, String versionName, Map<String, ?> valueMap) throws JDSException;
    @MethodChinaName(cname = "创建源码包")
    public EUPackage createPackage(String projectVersionName, String packageName) throws JDSException;
    @MethodChinaName(cname = "获取模块")
    public EUModule getModule(String className, String versionName, boolean reload) throws JDSException;
    @MethodChinaName(cname = "编译工程下所有模块")
    public Set<EUModule> build(String versionName) throws JDSException;
    @MethodChinaName(cname = "获取工程下所有源码包")
    public List<EUPackage> getAllPackage(String versionName) throws JDSException;
    @MethodChinaName(cname = "获取工程下指定类型的所有源码包")
    public List<EUPackage> getAllPackage(String versionName, PackageType packageType) throws JDSException;
    @MethodChinaName(cname = "动态编译指定包结构下的模块")
    public Set<EUModule> buildCustomModule(String versionName, String packageName, String esdPackageName, Map<String, ?> valueMap, ChromeProxy chrome) throws JDSException;
    @MethodChinaName(cname = "动态编译指定包结构下的模块")
    public Set<EUModule> buildPackage(String versionName, String packageName, Map<String, ?> valueMap, ChromeProxy chrome) throws JDSException;
    @MethodChinaName(cname = "创建模块")
    public EUModule createModule(String versionName, String className) throws JDSException;
    @MethodChinaName(cname = "创建通用模块")
    public EUModule createCustomModule(String versionName, String className, Map<String, ?> valueMap) throws JDSException;
    @MethodChinaName(cname = "创建数据库表模块")
    public EUModule createTableModule(String versionName, String tableName, DBMap<String, ?> valueMap, String viewInstId) throws JDSException;
    @MethodChinaName(cname = "获取数据库表模块")
    public EUModule getTableModule(String versionName, String tableName, String viewInstId) throws JDSException;
    @MethodChinaName(cname = "上传资源文件")
    public FileInfo uploadFile(MD5InputStream inputStream, String... paths) throws JDSException;
    @MethodChinaName(cname = "逐行读取文件")
    public String readFileAsString(String... paths) throws JDSException;
    @MethodChinaName(cname = "运行期动态编译模块")
    public <T extends ModuleComponent> EUModule<T> buildDynCustomModule(Class<T> customClass, Map<String, ?> valueMap, boolean save) throws JDSException;
    @MethodChinaName(cname = "删除模块")
    public Boolean delModule(EUModule moduleComponent) throws JDSException;
    @MethodChinaName(cname = "删除文件包")
    public void delPackage(EUPackage euPackage) throws JDSException;
    @MethodChinaName(cname = "导入源码文件包")
    public List<Object> importFile(List<String> strings, String... tpath) throws JDSException;
    @MethodChinaName(cname = "获取图像配置")
    public List<ImgConfig> getImgByProject(String projectName) throws JDSException;
    @MethodChinaName(cname = "获取风格样式配置")
    public List<StyleConfig> getStyleByProject(String projectName) throws JDSException;
    @MethodChinaName(cname = "运行期编译JSON")
    public StringBuffer genJSON(EUModule module, String tempName,boolean prettyFormat);
    @MethodChinaName(cname = "运行期编译JSON")
    public StringBuffer genJSON(ModuleComponent moduleComponent, String tempName,boolean prettyFormat);
    @MethodChinaName(cname = "根据组件获取模块列表")
    public List<EUModule> getComponentByProject(String projectName) throws JDSException;
    @MethodChinaName(cname = "获取扩展模块")
    public Set<EUModule> getExtModule(String versionName) throws JDSException;
    @MethodChinaName(cname = "获取扩展组件")
    public Set<Component> getExtCom(String versionName) throws JDSException;
    @MethodChinaName(cname = "获取工程库表信息")
    public List<TableInfo> getTablesByProject(String projectName, String simpleName) throws JDSException;
    @MethodChinaName(cname = "根据外部链接获取库表信息")
    public TableInfo getTableInfoByFullName(String tableFullName) throws JDSException;
    @MethodChinaName(cname = "获取文件夹信息")
    public EUPackage getPackageByPath(String projectVersionName, String packageName) throws JDSException;
    @MethodChinaName(cname = "获取DSM基础配置")
    public List<DSMBean> getDSMBeanList() throws JDSException;
    @MethodChinaName(cname = "获取DSM视图基础配置")
    public DSMBean getDSMBeanById(String viewInstId) throws JDSException;
    @MethodChinaName(cname = "创建视图基础配置")
    public DSMBean createDSMBean(String configKey) throws JDSException;
    @MethodChinaName(cname = "更新视图基础配置")
    public DSMBean updateDSMBean(DSMBean dsmBean) throws JDSException;
    @MethodChinaName(cname = "删除视图基础配置")
    public void deleteDSMBean(String viewInstId) throws JDSException;
    @MethodChinaName(cname = "获取工程根目录包结构")
    public List<EUPackage> getTopPackages(String projectVersionName) throws JDSException;
    @MethodChinaName(cname = "导出工程")
    public void exportProject(String projectName, ChromeProxy chrome, boolean deploy, boolean download) throws JDSException;
    @MethodChinaName(cname = "导出到本地ESDServer")
    public void exportLocalServer(String projectName, String serverId, ChromeProxy chrome) throws JDSException;
    @MethodChinaName(cname = "导出到远程ESDServer")
    public void exportRemoteServer(String projectName, String serverId, ChromeProxy chrome) throws JDSException;
    @MethodChinaName(cname = "发布到远程ESDServer")
    public void publicRemote(String projectName, String className, String remoteServerId, Boolean open) throws JDSException;
    @MethodChinaName(cname = "发布资源到本地ESDServer")
    public void publicLocalResource(String projectName, String filePath, String json, String localServerId) throws JDSException;
    @MethodChinaName(cname = "发布本地ESDServer")
    public void publicLocal(String projectName, EUModule module, String serverId, Boolean open) throws JDSException;
    @MethodChinaName(cname = "获取远程默认ESDServer")
    public RemoteServer getDefaultRemoteServer(String projectName) throws JDSException;
    @MethodChinaName(cname = "获取本地默认ESDServer")
    public LocalServer getDefaultLocalServer(String projectName) throws JDSException;

}


