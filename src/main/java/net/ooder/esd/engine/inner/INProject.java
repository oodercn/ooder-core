package net.ooder.esd.engine.inner;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.common.util.CaselessStringKeyHashMap;
import net.ooder.config.BPDProjectConfig;
import net.ooder.config.UserBean;
import net.ooder.esb.config.formula.FormulaInst;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.engine.MySpace;
import net.ooder.esd.engine.ProjectCacheManager;
import net.ooder.esd.engine.config.ProjectConfig;
import net.ooder.esd.engine.config.dsm.DSMProjectConfig;
import net.ooder.esd.engine.config.dsm.FormulasConfig;
import net.ooder.esd.engine.enums.ProjectDefAccess;
import net.ooder.esd.engine.enums.ProjectResourceType;
import net.ooder.esd.engine.enums.ProjectRoleType;
import net.ooder.esd.engine.enums.ProjectVersionStatus;
import net.ooder.org.Person;
import net.ooder.org.PersonNotFoundException;
import net.ooder.server.JDSServer;
import net.ooder.server.OrgManagerFactory;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import net.ooder.vfs.VFSConstants;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.vfs.ct.CtVfsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class INProject {

    public static String configFileName = "ProjectConfig.cfg";
    public static String bpdFileName = "BPDConfig.cfg";
    public static String configFolderName = "config";
    public static String repositoryFileName = "RepositoryConfig.cfg";
    public static String formulaFileName = "FormulaConfig.cfg";
    public static String dsmFileName = "DSMConfig.cfg";
    private final MySpace space;

    public MySpace getSpace() {
        return space;
    }

    public String id;
    public String desc;

    @JSONField(serialize = false)
    public ProjectConfig config = new ProjectConfig();

    @JSONField(serialize = false)
    private BPDProjectConfig bpdProjectConfig;

    @JSONField(serialize = false)
    private RepositoryInst repository;

    @JSONField(serialize = false)
    private DSMProjectConfig dsmProjectConfig;

    @JSONField(serialize = false)
    List<FormulaInst> formulas = new ArrayList<>();

    @JSONField(serialize = false)
    private Folder rootfolder;
    public String projectName;
    public String xuiConfig;
    public String path;
    public ProjectDefAccess defAccess;


    public Person owner;

    public String getDesc() {
        return desc;
    }


    public void setDesc(String desc) {
        this.desc = desc;
    }


    public INProject(Folder folder, MySpace space) {
        this.id = folder.getID();
        this.space = space;
        this.projectName = folder.getName();
        this.path = folder.getPath();
        this.rootfolder = folder;
        this.defAccess = ProjectDefAccess.fromPath(folder.getParent().getName());
        this.desc = folder.getDescrition();

        try {
            String filePath = folder.getPath() + configFileName;
            FileInfo configFile = this.getVfsClient().getFileByPath(filePath);
            if (configFile != null) {
                StringBuffer configJson = this.getVfsClient().readFileAsString(filePath, VFSConstants.Default_Encoding);
                if (configJson.length() > 0) {
                    this.config = JSONObject.parseObject(configJson.toString(), ProjectConfig.class);
                }
            }


            Folder configFolder = this.getVfsClient().getFolderByPath(folder.getPath() + configFolderName);
            if (configFolder != null) {
                List<FileInfo> configFiles = configFolder.getFileList();
                for (FileInfo fileInfo : configFiles) {
                    if (fileInfo.getName().equals(bpdFileName)) {
                        StringBuffer configJson = this.getVfsClient().readFileAsString(fileInfo.getPath(), VFSConstants.Default_Encoding);
                        if (configJson.length() > 0) {
                            this.bpdProjectConfig = JSONObject.parseObject(configJson.toString(), BPDProjectConfig.class);
                        }
                    }
                    if (fileInfo.getName().equals(formulaFileName)) {
                        StringBuffer configJson = this.getVfsClient().readFileAsString(fileInfo.getPath(), VFSConstants.Default_Encoding);
                        if (configJson.length() > 0) {
                            FormulasConfig formulasConfig = JSONObject.parseObject(configJson.toString(), FormulasConfig.class);
                            this.formulas = formulasConfig.getFormulas();
                        }
                    }

                    if (fileInfo.getName().equals(repositoryFileName)) {
                        StringBuffer configJson = this.getVfsClient().readFileAsString(fileInfo.getPath(), VFSConstants.Default_Encoding);
                        if (configJson.length() > 0) {
                            this.repository = JSONObject.parseObject(configJson.toString(), RepositoryInst.class);
                        }
                    }
                    if (fileInfo.getName().equals(dsmFileName)) {
                        StringBuffer configJson = this.getVfsClient().readFileAsString(fileInfo.getPath(), VFSConstants.Default_Encoding);
                        if (configJson.length() > 0) {
                            this.dsmProjectConfig = JSONObject.parseObject(configJson.toString(), DSMProjectConfig.class);
                        }
                    }

                }
            }


            try {
                if (JDSServer.getClusterClient().isLogin()) {
                    List<Person> personList = this.getDevPersons(ProjectRoleType.own);
                    if (personList.size() > 0) {
                        this.owner = personList.get(0);
                    } else if (rootfolder.getPersonId() != null) {
                        this.owner = OrgManagerFactory.getOrgManager().getPersonByID(rootfolder.getPersonId());
                    }
                } else {
                    this.owner = JDSServer.getClusterClient().getAdminPerson(UserBean.getInstance().getSystemCode());
                }

            } catch (PersonNotFoundException e) {
                e.printStackTrace();
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }

    public DSMProjectConfig getDsmProjectConfig() {
        if (dsmProjectConfig == null) {
            dsmProjectConfig = new DSMProjectConfig();
        }
        CaselessStringKeyHashMap<String, DomainInst> dsmInstMap = dsmProjectConfig.getAggregationInst();
        if (dsmInstMap == null || (dsmInstMap.size() < UserSpace.values().length - 1)) {
            for (UserSpace catType : UserSpace.values()) {
                String defaultDomainId = this.getProjectName() + "_" + catType.name();
                DomainInst defaultDomainInst = dsmInstMap.get(defaultDomainId);
                if (defaultDomainInst == null) {
                    try {
                        defaultDomainInst = DSMFactory.getInstance().createDefaultDomain(this.getProjectName(), catType);
                        defaultDomainInst.setDomainId(defaultDomainId);
                        defaultDomainInst.setProjectVersionName(this.getProjectName());
                        dsmInstMap.put(defaultDomainId, defaultDomainInst);
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return dsmProjectConfig;
    }

    public void setDsmProjectConfig(DSMProjectConfig dsmProjectConfig) {
        this.dsmProjectConfig = dsmProjectConfig;
    }

    public ProjectDefAccess getDefAccess() {
        return defAccess;
    }

    public void setDefAccess(ProjectDefAccess defAccess) {
        this.defAccess = defAccess;
    }


    public RepositoryInst getRepository() {
        if (repository == null) {
            repository = new RepositoryInst(this.getProjectName());
        }
        return repository;
    }


    public void setRepository(RepositoryInst repository) {
        this.repository = repository;
    }

    @JSONField(serialize = false)
    public List<Person> getDevPersons(ProjectRoleType type) {
        List<Person> defPersons = new ArrayList<Person>();
        List<String> remoePersonIds = new ArrayList<String>();
        Set<String> defPersonIds = config.getDevPersons(type);
        for (String personId : defPersonIds) {
            Person person = null;
            try {
                person = OrgManagerFactory.getOrgManager().getPersonByID(personId);
                defPersons.add(person);
            } catch (PersonNotFoundException e) {
                remoePersonIds.add(personId);
            }

        }
        if (remoePersonIds.size() > 0) {
            defPersonIds.removeAll(remoePersonIds);
        }
        return defPersons;
    }

    public ProjectConfig addDevPersons(ProjectRoleType type, String personId) {
        config.addDevPersons(type, personId);
        return config;
    }

    public ProjectConfig removeDevPersons(ProjectRoleType type, String personId) {
        config.removeDevPersons(type, personId);
        return config;
    }


    public List<INProjectVersion> getVersions() {
        List<INProjectVersion> versions = new ArrayList<INProjectVersion>();
        try {
            versions = ProjectCacheManager.getInstance(space).getVersions(this.getProjectName());
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return versions;
    }


    @JSONField(serialize = false)
    public INProjectVersion getActiviteVersion() {
        List<INProjectVersion> versions = this.getVersions();
        for (INProjectVersion version : versions) {
            if (version != null && version.getSpace() != null && version.getStatus().equals(ProjectVersionStatus.RELEASED)) {
                return version;
            }
        }

        INProjectVersion activiteVersion = null;
        if (versions.size() > 0) {
            activiteVersion = versions.get(versions.size() - 1);
        }

        return activiteVersion;
    }


    public Folder getRootfolder() {
        return rootfolder;
    }

    public void setRootfolder(Folder rootfolder) {
        this.rootfolder = rootfolder;
    }


    public ProjectResourceType getResourceType() {
        ProjectResourceType type = null;
        if (config != null) {
            type = config.getResourceType();
        }
        return type;
    }

    public void setResourceType(ProjectResourceType resourceType) {
        if (config != null) {
            config.setResourceType(resourceType);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getXuiConfig() {
        return xuiConfig;
    }

    public void setXuiConfig(String xuiConfig) {
        this.xuiConfig = xuiConfig;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ProjectConfig getConfig() {
        return config;
    }

    public void setConfig(ProjectConfig config) {
        this.config = config;
    }

    public BPDProjectConfig getBpdProjectConfig() {
        if (bpdProjectConfig == null) {
            bpdProjectConfig = new BPDProjectConfig();
        }
        return bpdProjectConfig;
    }

    public List<FormulaInst> getFormulas() {
        return formulas;
    }

    public FormulaInst getFormula(String formulaId) {
        FormulaInst formula = null;
        for (FormulaInst formulaInst : formulas) {
            if (formulaInst.getFormulaInstId() != null && formulaInst.getFormulaInstId().equals(formulaId)) {
                formula = formulaInst;
            }
        }
        return formula;
    }


    public void setFormulas(List<FormulaInst> formulas) {
        this.formulas = formulas;
    }

    public void setBpdProjectConfig(BPDProjectConfig bpdProjectConfig) {
        this.bpdProjectConfig = bpdProjectConfig;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    @JSONField(serialize = false)
    public CtVfsService getVfsClient() {
        CtVfsService vfsClient = CtVfsFactory.getCtVfsService();
        return vfsClient;
    }
}
