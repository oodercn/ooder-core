package net.ooder.esd.engine;


import net.ooder.common.JDSException;
import net.ooder.common.md5.MD5InputStream;
import net.ooder.common.util.CnToSpell;
import net.ooder.common.util.StringUtility;
import net.ooder.engine.ConnectInfo;
import net.ooder.esd.engine.enums.ProjectDefAccess;
import net.ooder.esd.engine.inner.INProject;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import net.ooder.vfs.VFSConstants;
import net.ooder.vfs.VFSException;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.vfs.ct.CtVfsService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SpaceServiceImpl implements SpaceService {

    private MySpace space;

    List<Project> projectList = new ArrayList<Project>();

    Map<String, Project> projectMap = new HashMap<String, Project>();

    public SpaceServiceImpl(MySpace space) {
        this.space = space;
    }

    @Override
    public void saveFile(String path, String content) throws JDSException {
        path = formartPath(path);
        this.getVfsClient().saveFileAsContent(path, content, VFSConstants.Default_Encoding);
    }

    @Override
    public FileInfo createFile(String name, MD5InputStream inputStream) throws JDSException {
        String path = formartPath(name);
        this.getVfsClient().upload(path, inputStream, null);
        FileInfo fileInfo = this.getVfsClient().getFileByPath(path);
        return fileInfo;
    }

    @Override
    public FileInfo getFileByPath(String path) throws JDSException {
        return this.getVfsClient().getFileByPath(this.formartPath(path));
    }

    @Override
    public Folder getFolderByPath(String path) throws JDSException {
        return this.getVfsClient().getFolderByPath(this.formartPath(path));
    }

    @Override
    public Folder createFolder(String path) throws JDSException {
        path = formartPath(path);
        Folder folder = this.getVfsClient().mkDir(path);
        return folder;
    }

    @Override
    public Object copy(String spath, String tpath) throws JDSException {
        spath = this.formartPath(spath);
        tpath = this.formartPath(tpath);
        Folder folder = getVfsClient().getFolderByPath(spath);
        Folder tfolder = getVfsClient().mkDir(tpath);

        Object obj = null;
        if (folder != null) {
            getVfsClient().copyFolder(spath, tfolder.getPath());
            obj = this.getVfsClient().getFolderByPath(tfolder.getPath() + folder.getName());
        } else {
            FileInfo tempFile = getVfsClient().getFileByPath(spath);
            if (tempFile.getFolder().getPath().equals(tpath)) {
                throw new VFSException("不能拷贝到自己目录内！");
            }
            FileInfo newFile = getVfsClient().copyFile(tempFile, tfolder);
            obj = newFile;
        }
        return obj;
    }


    @Override
    public Object reName(String path, String newName) throws JDSException {
        Object obj = null;
        path = this.formartPath(path);
        Folder folder = getVfsClient().getFolderByPath(path);
        if (folder != null) {
            if (!folder.getDescrition().equals(newName)) {
                folder.setDescrition(newName);
                folder.setName(CnToSpell.getFullSpell(newName));
                getVfsClient().updateFolderInfo(folder, CnToSpell.getFullSpell(newName), newName);
                Folder newFolder = this.getFolderByPath(folder.getParent().getPath() + newName);
                obj = newFolder;
            }
        } else {
            FileInfo fileInfo = getVfsClient().getFileByPath(path);
            FileInfo newFile = this.getVfsClient().getFileByPath(fileInfo.getFolder().getPath() + newName);
            if (newFile == null) {
                if (fileInfo.getDescrition() == null || !fileInfo.getDescrition().equals(newName)) {
                    newFile = getVfsClient().updateFileInfo(fileInfo, CnToSpell.getFullSpell(newName), newName);
                    obj = newFile;
                }
            } else {
                throw new JDSException("文件已存在");

            }
        }
        return obj;
    }

    @Override
    public void delFile(List<String> paths) throws JDSException {
        for (String path : paths) {
            path = formartPath(path);
            Folder folder = getVfsClient().getFolderByPath(path);
            if (folder != null) {
                getVfsClient().deleteFolder(folder.getID());
            } else {
                getVfsClient().deleteFile(getVfsClient().getFileByPath(path).getID());
            }
        }
    }

    @Override
    public List<Project> getAllProject() {
        return null;
    }


    public Project createProject(String projectName, String desc, String tempPath) throws JDSException {
        Project project = projectMap.get(projectName);
        if (project != null) {
            throw new JDSException("[" + projectName + "]工程已存在！");
        }
        ConnectInfo connectInfo = this.getClient().getConnectInfo();
        INProject inproject = ProjectCacheManager.getInstance(space).cloneProject(projectName, desc, tempPath, ProjectDefAccess.Public, connectInfo.getUserID());
        project = getClient().getProjectById(inproject.getId());
        projectMap.put(projectName, project);
        projectList.add(project);
        return project;

    }

    String formartPath(String path) {
        String packagePath = this.space.getPath();
        if (packagePath != null && !packagePath.equals("") && !path.startsWith(packagePath)) {

            if (!packagePath.endsWith("/")) {
                packagePath = packagePath + "/";
            }
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            path = packagePath + path;
            path = StringUtility.replace(path, "//", "/.");
        }

        return path;
    }
    @Override
    public Project getProjectByName() throws JDSException {
        return null;
    }

    @Override
    public Project getProjectById() throws JDSException {
        return null;
    }

    @Override
    public StringBuffer readFileAsString(String path) throws JDSException {
        path = formartPath(path);
        StringBuffer buffer = getVfsClient().readFileAsString(path, VFSConstants.Default_Encoding);
        return buffer;
    }


    @Override
    public void updateConfig(MySpaceConfig config) throws JDSException {

        if (config == null) {
            config = space.getConfig();
        }

        //     this.getVfsClient().saveFileAsContent(space.getPath() + Project.configFileName, JSONObject.toJSONString(config), VFSConstants.Default_Encoding);
    }



    public ESDClient getClient() throws JDSException {

        ESDClient client = ESDFacrory.getAdminESDClient();

        return client;
    }

    public CtVfsService getVfsClient() {
        CtVfsService vfsClient = CtVfsFactory.getCtVfsService();
        return vfsClient;
    }
}
