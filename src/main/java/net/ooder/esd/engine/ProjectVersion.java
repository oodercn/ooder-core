package net.ooder.esd.engine;

import net.ooder.common.JDSException;
import net.ooder.common.md5.MD5InputStream;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.engine.config.ProjectConfig;
import net.ooder.esd.engine.enums.PackageType;
import net.ooder.esd.engine.enums.ProjectVersionStatus;
import net.ooder.esd.tool.component.Component;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;

import java.util.List;
import java.util.Set;

public interface ProjectVersion {


    public String getVersionName();

    public Project getProject();

    public Folder getRootFolder();

    public String getDesc();

    public String getPath();

    public String getVersionId();

    public Integer getVersion();

    public ProjectVersionStatus getStatus();

    public EUModule createModule(String className) throws JDSException;

    public EUModule createCustomModule(String className) throws JDSException;

    public EUModule getModule(String className);

    public EUPackage getEUPackage(String packageName) throws JDSException;


    public void delModule(EUModule moduleComponent) throws JDSException;

    public FileInfo createFile(String path, MD5InputStream inputStream) throws JDSException;

    public void reLoadProject() throws JDSException;

    public Folder createFolder(String path) throws JDSException;

    public void delFile(List<String> paths) throws JDSException;

    public void delPackage(String packageName) throws JDSException;

    public List<EUPackage> getTopPackages() throws JDSException;

    public EUPackage getPackageByPath(String packageName) throws JDSException;

    public List<EUPackage> getAllPackage() throws JDSException;

    public List<EUPackage> getAllPackage(PackageType packageType) throws JDSException;

    public Set<EUModule> getAllModule() throws JDSException;

    public Set<EUModule> getAllCustomModule() throws JDSException;

    public Set<EUModule> getExtModule() throws JDSException;

    public Set<Component> getExtCom() throws JDSException;

    public void updateConfig(ProjectConfig config) throws JDSException;

    public void activateProjectVersion() throws JDSException;

    public void freezeProjectVersion() throws JDSException;

    public List<Component> searchComponent(List<ComponentType> types, String text);

    public int delete() throws JDSException;

}
