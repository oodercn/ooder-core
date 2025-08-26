package net.ooder.esd.engine;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.java.JavaPackage;
import net.ooder.esd.engine.enums.PackagePathType;
import net.ooder.esd.manager.plugins.api.APIFactory;
import net.ooder.esd.manager.plugins.api.node.APIPaths;
import net.ooder.esd.engine.inner.INProjectVersion;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.web.APIConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EUPackage implements Comparable<EUPackage> {


    String name;
    String path;
    String packageName;
    String id;
    @JSONField(serialize = false)
    String folderId;
    @JSONField(serialize = false)
    String projectVersionName;

    PackagePathType packagePathType;

    String desc;

    String imageClass = "spafont spa-icon-package";


    @JSONField(serialize = false)
    public ProjectVersion getProjectVersion() {
        ProjectVersion version = null;
        try {
            version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectVersionName);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return version;
    }

    @JSONField(serialize = false)
    public Folder getFolder() {
        Folder folder = null;
        try {
            folder = CtVfsFactory.getCtVfsService().getFolderById(folderId);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return folder;
    }

    public EUPackage(INProjectVersion projectVersion, Folder folder) {
        this.projectVersionName = projectVersion.getVersionName();
        List<FileInfo> fileInfos = folder.getFileList();
        this.name = folder.getDescrition() == null ? folder.getName() : folder.getDescrition();
        this.id = folder.getID();
        this.path = folder.getPath();
        this.packageName = StringUtility.replace(path, projectVersion.getPath(), "");
        this.packageName = StringUtility.replace(this.packageName, "/", ".");
        if (packageName.endsWith(".")) {
            packageName = packageName.substring(0, packageName.length() - 1);
        }
        this.folderId = folder.getID();
        this.desc = folder.getDescrition() == null ? packageName : folder.getDescrition();
        String subpath = StringUtility.replace(folder.getPath(), projectVersion.getPath(), "/");
        try {
            APIPaths apiPaths = APIFactory.getInstance().getAPIPaths(subpath);
            if (apiPaths != null) {
                if (apiPaths.getSource() instanceof APIConfig) {
                    APIConfig config = (APIConfig) apiPaths.getSource();
                    this.desc = packageName + "(" + config.getDesc() + ")";
                    this.imageClass = apiPaths.getImageClass();
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }

        this.packagePathType = PackagePathType.equalsPath(subpath);
        if (packagePathType != null) {
            this.desc = packageName + "(" + packagePathType.getDesc() + ")";
            this.imageClass = packagePathType.getImageClass();
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JavaPackage getJavaPackage() {
        JavaPackage javaPackage = null;
        try {
            javaPackage = BuildFactory.getInstance().findJavaPackage(this.projectVersionName, packageName);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return javaPackage;
    }

    public List<EUPackage> listChildren() {
        List<EUPackage> euPackages = new ArrayList<>();
        Folder folder = this.getFolder();
        for (Folder childFolder : folder.getChildrenList()) {
            String packageName = StringUtility.replace(childFolder.getPath(), this.getProjectVersion().getPath(), "");
            packageName = StringUtility.replace(packageName, "/", ".");
            if (packageName.endsWith(".")) {
                packageName = packageName.substring(0, packageName.length() - 1);
            }
            EUPackage euPackage = null;
            try {
                euPackage = ESDFacrory.getAdminESDClient().getPackageByPath(projectVersionName, packageName);
                if (euPackage != null) {
                    euPackages.add(euPackage);
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return euPackages;
    }

    public PackagePathType getPackagePathType() {
        return packagePathType;
    }

    public void setPackagePathType(PackagePathType packagePathType) {
        this.packagePathType = packagePathType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<EUModule> listModules() {
        Set<EUModule> modules = new HashSet<>();
        Folder folder = this.getFolder();
        if (folder != null) {
            List<FileInfo> fileInfos = folder.getFileList();
            for (FileInfo fileInfo : fileInfos) {
                try {
                    if (fileInfo != null && fileInfo.getName().endsWith(".cls")) {
                        EUModule euModule = ESDFacrory.getAdminESDClient().getModule(fileInfo.getPath(), this.getProjectVersion().getVersionName());
                        if (euModule != null) {
                            modules.add(euModule);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        return modules;
    }

//    public Set<EUModule> listModuleRecursivelys() {
//        Set<EUModule> modules = new HashSet<>();
//        Folder folder = this.getFolder();
//        if (folder != null) {
//            List<FileInfo> fileInfos = folder.getFileListRecursively();
//            for (FileInfo fileInfo : fileInfos) {
//                try {
//                    if (fileInfo != null && fileInfo.getName().endsWith(".cls")) {
//                        EUModule euModule = ESDFacrory.getAdminESDClient().getModule(fileInfo.getPath(), this.getProjectVersion().getVersionName());
//                        if (euModule != null && euModule.getSourceClassName() != null) {
//                            modules.add(euModule);
//                        }
//                    }
//                } catch (JDSException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return modules;
//    }

    public Set<EUModule> listRealModules(CustomViewType type) {
        Set<EUModule> navs = new HashSet<>();
        List<EUModule> modules = listModules(type);
        for (EUModule navModule : modules) {
            if (navModule.getRealClassName() == null) {
                navs.add(navModule);
            } else {
                EUModule realModule = null;
                try {
                    if (!navModule.getRealClassName().endsWith(CustomViewFactory.dynBuild)) {
                        realModule = ESDFacrory.getAdminESDClient().getModule(navModule.getRealClassName(), this.projectVersionName);
                    }
                    if (realModule != null) {
                        navs.add(realModule);
                    }
                } catch (JDSException e) {
                    e.printStackTrace();
                }

            }
        }

        return navs;
    }

    public List<EUModule> listModules(CustomViewType type) {
        List<EUModule> modules = listAllModule();
        List<EUModule> typeModules = new ArrayList<>();
        for (EUModule module : modules) {
            if (module != null && module.getComponent().getModuleViewType() != null && module.getComponent().getModuleViewType().equals(type)) {
                typeModules.add(module);
            }
        }
        return typeModules;
    }


    public List<EUPackage> findChildren(String type, String pattern) {
        List<EUPackage> filtePackages = new ArrayList<>();
        List<EUPackage> allPackages = listChildren();

        for (EUPackage euPackage : allPackages) {
            if (euPackage.findModules(type, pattern).size() > 0) {
                filtePackages.add(euPackage);
            }
        }
        return filtePackages;
    }


    public List<EUModule> findModules(String type, String pattern) {
        Set<EUModule> allModules = listModules();
        List<EUModule> filteModules = new ArrayList<EUModule>();
        for (EUModule module : allModules) {
            if (module != null) {
                if (module.getComponent().findComponents(type, pattern).size() > 0) {
                    filteModules.add(module);
                }
            }
        }
        return filteModules;
    }


    public List<FileInfo> listFiles() {
        List<FileInfo> fileInfos = new ArrayList<>();
        for (FileInfo fileInfo : this.getFolder().getFileList()) {
            if (!fileInfo.getName().endsWith(".cls")) {
                fileInfos.add(fileInfo);
            }
        }
        return fileInfos;
    }


    public List<EUModule> listAllModule() {
        List<EUModule> modules = new ArrayList<>();
        modules.addAll(this.listModules());
        List<EUPackage> euPackages = listChildren();
        for (EUPackage euPackage : euPackages) {
            modules.addAll(euPackage.listAllModule());
        }
        return modules;
    }


    public List<EUPackage> listAllPackage() {
        List<EUPackage> packages = new ArrayList<>();
        packages.addAll(this.listChildren());
        List<EUPackage> euPackages = listChildren();
        for (EUPackage euPackage : euPackages) {
            packages.addAll(euPackage.listAllPackage());
        }
        return packages;
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
    public int compareTo(EUPackage o) {
        return path.compareTo(o.getPath());
    }


}
