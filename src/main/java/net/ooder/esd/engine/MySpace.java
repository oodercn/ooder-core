package net.ooder.esd.engine;


import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.ConfigCode;
import net.ooder.common.JDSException;
import net.ooder.config.UserBean;
import net.ooder.org.Person;
import net.ooder.org.PersonNotFoundException;
import net.ooder.server.JDSServer;
import net.ooder.server.OrgManagerFactory;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import net.ooder.vfs.VFSConstants;
import net.ooder.vfs.ct.CtVfsFactory;

public class MySpace {

    public static String configFileName = "SpaceConfig.cfg";

    public static String configUserFolder = "UserConfig";

    public static String javaRootName = "java";

    public String id;

    public String sysId;


    public String desc;

    public String name;

    public ConfigCode configCode = ConfigCode.app;

    public String path = "from/";

    public Person owner;

    @JSONField(serialize = false)
    public MySpaceConfig config = new MySpaceConfig();


    @JSONField(serialize = false)
    private Folder userFolder;


    @JSONField(serialize = false)
    private Folder javaRoot;


    @JSONField(serialize = false)
    private Folder rootfolder;

    public MySpace(Folder rootfolder, String sysId) {
        this.rootfolder = rootfolder;
        this.id = rootfolder.getID();
        this.sysId = sysId;
        this.name = rootfolder.getName();
        this.desc = rootfolder.getDescrition();
        this.path = rootfolder.getPath();
        try {
            if (JDSServer.getClusterClient().isLogin()){
                this.owner = OrgManagerFactory.getOrgManager().getPersonByID(rootfolder.getPersonId());
            }else{
                this.owner =  JDSServer.getClusterClient().getAdminPerson(UserBean.getInstance().getSystemCode());
            }

        } catch (PersonNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //批量预读
            rootfolder.getChildrenList();
            FileInfo fileInfo = CtVfsFactory.getCtVfsService().getFileByPath(rootfolder.getPath() + configFileName);
            if (fileInfo != null) {
                StringBuffer configJson = CtVfsFactory.getCtVfsService().readFileAsString(rootfolder.getPath() + configFileName, VFSConstants.Default_Encoding);
                if (configJson.length() > 0) {
                    this.config = JSONObject.parseObject(configJson.toString(), MySpaceConfig.class);
                }
            }
            this.userFolder = CtVfsFactory.getCtVfsService().mkDir(rootfolder.getPath() + configUserFolder);
            this.javaRoot = CtVfsFactory.getCtVfsService().mkDir(rootfolder.getPath() + javaRootName);

        } catch (JDSException e) {
            e.printStackTrace();
        }


    }

    public Folder getJavaRoot() {
        return javaRoot;
    }

    public void setJavaRoot(Folder javaRoot) {
        this.javaRoot = javaRoot;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MySpaceConfig getConfig() {
        return config;
    }

    public void setConfig(MySpaceConfig config) {
        this.config = config;
    }

    public Folder getRootfolder() {
        return rootfolder;
    }

    public ConfigCode getConfigCode() {
        return configCode;
    }

    public void setConfigCode(ConfigCode configCode) {
        this.configCode = configCode;
    }

    public Folder getUserFolder() {
        return userFolder;
    }

    public void setUserFolder(Folder userFolder) {
        this.userFolder = userFolder;
    }

    public void setRootfolder(Folder rootfolder) {
        this.rootfolder = rootfolder;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }
}


