package net.ooder.esd.engine.inner;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.MySpace;
import net.ooder.esd.engine.ProjectCacheManager;
import net.ooder.esd.engine.enums.ProjectVersionStatus;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import net.ooder.vfs.ct.CtVfsFactory;

import java.util.List;

public class INProjectVersion {

    private final MySpace space;
    @JSONField(serialize = false)
    public Boolean isBuild = false;

    public String projectId;

    public ProjectVersionStatus status = ProjectVersionStatus.UNDERTEST;

    public String versionId;

    public MySpace getSpace() {
        return space;
    }

    public String desc;

    public String projectName;

    public Integer version;

    public String versionName;

    public String path;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }


    public INProjectVersion(String projectId, String path, MySpace space) {
        this.path = path;
        this.space = space;
        Folder folder = getRootFolder();
        this.projectId = projectId;
        INProject project = ProjectCacheManager.getInstance(space).getProjectById(projectId);
        this.projectName = project.getProjectName();
        if (folder != null) {
            this.version = Integer.valueOf(folder.getName());
            this.versionId = folder.getID();
            if (folder.getState() != null) {
                switch (folder.getState()) {
                    case locked:
                        this.status = ProjectVersionStatus.UNDERREVISION;
                        break;
                    case tested:
                        this.status = ProjectVersionStatus.UNDERTEST;
                        break;
                    case normal:
                        this.status = ProjectVersionStatus.RELEASED;
                        break;
                    case deleted:
                        this.status = ProjectVersionStatus.DELETEED;
                        break;
                    default:
                        this.status = ProjectVersionStatus.UNDERTEST;
                }
            } else {
                status = ProjectVersionStatus.RELEASED;
            }
            this.versionName = this.projectName + ProjectCacheManager.VERSIONTOKEN + version;
            this.path = folder.getPath();
            this.desc = folder.getDescrition();
        }
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Boolean getBuild() {
        return isBuild;
    }

    public void setBuild(Boolean build) {
        isBuild = build;
    }


    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public INProject getProject() throws JDSException {
        return ProjectCacheManager.getInstance(this.getSpace()).getProjectById(projectId);

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }


    @JSONField(serialize = false)
    public List<FileInfo> getFiles() {
        return getRootFolder().getFileList();
    }

    @JSONField(serialize = false)
    public List<Folder> getFolders() {
        return getRootFolder().getChildrenList();
    }

    @JSONField(serialize = false)
    public Folder getRootFolder() {
        Folder folder = null;
        try {
            folder = CtVfsFactory.getCtVfsService().mkDir(path);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return folder;
    }


    ESDClient getESDClient() throws JDSException {
        return ESDFacrory.getAdminESDClient();
    }

    public ProjectVersionStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectVersionStatus status) {
        this.status = status;
    }

}
