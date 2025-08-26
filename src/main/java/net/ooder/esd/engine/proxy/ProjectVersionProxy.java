package net.ooder.esd.engine.proxy;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.md5.MD5InputStream;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.Project;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.EUPackage;
import net.ooder.esd.engine.config.ProjectConfig;
import net.ooder.esd.engine.enums.PackageType;
import net.ooder.esd.engine.enums.ProjectVersionStatus;
import net.ooder.esd.engine.inner.INProjectVersion;
import net.ooder.esd.tool.component.Component;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectVersionProxy implements ProjectVersion {

    @JSONField(serialize = false)
    private final INProjectVersion version;

    @JSONField(serialize = false)
    private ESDClient client;

    public ProjectVersionProxy(INProjectVersion version, ESDClient client) {
        this.version = version;
        this.client = client;
    }


    @Override
    public String getVersionName() {
        return version.getVersionName();
    }

    @Override
    public Project getProject() {
        try {
            return client.getProjectById(version.getProjectId());
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Folder getRootFolder() {
        return version.getRootFolder();
    }

    @Override
    public String getDesc() {
        return version.getDesc();
    }

    @Override
    public String getPath() {
        return version.getPath();
    }

    @Override
    public String getVersionId() {
        return version.getVersionId();
    }

    @Override
    public Integer getVersion() {
        return version.getVersion();
    }

    @Override
    public ProjectVersionStatus getStatus() {
        return version.getStatus();
    }

    @Override
    public EUModule createModule(String className) throws JDSException {
        return client.createModule(className, this.getVersionName());
    }

    @Override
    public EUModule createCustomModule(String className) throws JDSException {
        return client.createModule(className, this.getVersionName());
    }


    @Override
    @JSONField(serialize = false)
    public EUModule getModule(String className) {
        try {
            return client.getModule(className, this.getVersionName());
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public EUPackage getEUPackage(String packageName) throws JDSException {

        return client.getPackageByPath(this.getVersionName(), packageName);
    }


    @Override
    public void delModule(EUModule moduleComponent) throws JDSException {
        client.delModule(moduleComponent);
    }


    @Override
    public FileInfo createFile(String path, MD5InputStream inputStream) throws JDSException {
        return client.uploadFile(inputStream, path, this.getVersionName());
    }

    @Override
    public void reLoadProject() throws JDSException {
        client.reLoadProject(this.getVersionName());
    }


    @Override
    public Folder createFolder(String path) throws JDSException {
        return client.createFolder(path, this.getVersionName());
    }


    @Override
    public void delFile(List<String> paths) throws JDSException {
        this.client.delFile(paths, this.getVersionName());
    }

    @Override
    public void delPackage(String packageName) throws JDSException {
        this.client.delPackage(this.getEUPackage(packageName));
    }

    @Override
    public List<EUPackage> getTopPackages() throws JDSException {
        return this.client.getTopPackages(this.getVersionName());
    }

    @Override
    public EUPackage getPackageByPath(String packageName) throws JDSException {
        return this.client.getPackageByPath(this.getVersionName(), packageName);
    }

    @Override
    public List<EUPackage> getAllPackage() throws JDSException {
        return this.client.getAllPackage(this.getVersionName());
    }

    @Override
    public List<EUPackage> getAllPackage(PackageType packageType) throws JDSException {
        return this.client.getAllPackage(this.getVersionName(), packageType);
    }

    @Override
    @JSONField(serialize = false)
    public Set<EUModule> getAllModule() throws JDSException {
        return this.client.build(this.getVersionName());
    }

    @Override
    public Set<EUModule> getAllCustomModule() throws JDSException {
        return this.client.buildCustomModule(this.getVersionName(), null, null, JDSActionContext.getActionContext().getContext(), null);
    }

    @Override
    public Set<EUModule> getExtModule() throws JDSException {
        Set<EUModule> allModules = this.getAllModule();
        Set<EUModule> extModules = new HashSet<EUModule>();
        for (EUModule euModule : allModules) {
            if (euModule.getClassName().startsWith("Module")) {
                extModules.add(euModule);
            }
        }
        return extModules;
    }

    @Override
    public Set<Component> getExtCom() throws JDSException {
        return this.client.getExtCom(this.getVersionName());
    }


    @Override
    public void updateConfig(ProjectConfig config) throws JDSException {
        this.client.updateProjectConfig(this.getProject().getId(), config);
    }

    @Override
    public void activateProjectVersion() throws JDSException {
        this.client.activateProjectVersion(this.getVersionId());
    }

    @Override
    public void freezeProjectVersion() throws JDSException {
        this.client.freezeProjectVersion(this.getVersionId());
    }

    @Override
    public List<Component> searchComponent(List<ComponentType> types, String text) {
        return null;
    }


    @Override
    public int delete() throws JDSException {
        return client.removeProcessVersion(this.getVersionName());
    }


}
