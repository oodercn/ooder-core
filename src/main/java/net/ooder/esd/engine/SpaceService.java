package net.ooder.esd.engine;

import net.ooder.common.JDSException;
import net.ooder.common.md5.MD5InputStream;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;

import java.util.List;

public interface SpaceService {


    public void saveFile(String path, String content) throws JDSException;

    public FileInfo createFile(String path, MD5InputStream inputStream) throws JDSException;

    public FileInfo getFileByPath(String path) throws JDSException;

    public Folder getFolderByPath(String path) throws JDSException;

    public Folder createFolder(String path) throws JDSException;

    public Object copy(String spath, String tpath) throws JDSException;

    public Object reName(String path, String newName) throws JDSException;

    public void delFile(List<String> paths) throws JDSException;

    public List<Project> getAllProject();

    public Project createProject(String projectName, String desc, String tempName) throws JDSException;

    public Project getProjectByName() throws JDSException;

    public Project getProjectById() throws JDSException;

    public StringBuffer readFileAsString(String path) throws JDSException;

    public void updateConfig(MySpaceConfig config) throws JDSException;

}
