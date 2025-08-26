package net.ooder.esd.dsm.temp;

import com.alibaba.fastjson.JSONObject;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.IconEnumstype;
import net.ooder.common.JDSException;
import net.ooder.common.database.metadata.MetadataFactory;
import net.ooder.common.database.metadata.ProviderConfig;
import net.ooder.common.md5.MD5InputStream;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.FileUtility;
import net.ooder.common.util.IOUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.annotation.ViewType;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.DSMInst;
import net.ooder.esd.dsm.domain.enums.*;
import net.ooder.esd.dsm.enums.*;
import net.ooder.esd.dsm.java.JavaPackage;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.engine.MySpace;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.server.JDSServer;
import net.ooder.server.SubSystem;
import net.ooder.server.ct.CtSubSystem;
import net.ooder.server.service.SysWebManager;
import net.ooder.vfs.*;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.vfs.ct.CtVfsService;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.*;

public class JavaTempManager {

    public static final String THREAD_LOCK = "Thread Lock";

    public static final String thumbnailpath = "thumbnail";

    public static final String javatemppath = "javatempbean";

    public static final String dbconfigpath = "dbconfig";

    public static final String dsmbeantemp = "dsmbeantemp";

    public static final String ftlpath = "ftl";

    public static final String TempPath = "temp";

    static Map<String, JavaTempManager> managerMap = new HashMap<String, JavaTempManager>();


    private Folder dbconfigFolder;

    private Folder tempFolder;

    private Folder tempBeanFolder;

    private Folder dsmBeanFolder;

    private Folder thumbnailFolder;

    List<DSMBean> dsmBeans = new ArrayList<>();

    Map<String, JavaTemp> tempMap = new HashMap<>();

    List<ProviderConfig> providerConfigs = new ArrayList<>();


    public static JavaTempManager getInstance(MySpace space) {
        String path = space.getPath();
        JavaTempManager manager = managerMap.get(path);
        if (manager == null) {
            synchronized (THREAD_LOCK) {
                if (manager == null) {
                    manager = new JavaTempManager(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }

    JavaTempManager(MySpace space) {

        try {


                this.dbconfigFolder = space.getRootfolder().createChildFolder(dbconfigpath, JDSServer.getInstance().getAdminUser().getId());
                this.dsmBeanFolder = space.getRootfolder().createChildFolder(dsmbeantemp, JDSServer.getInstance().getAdminUser().getId());
                this.tempBeanFolder = space.getRootfolder().createChildFolder(javatemppath, JDSServer.getInstance().getAdminUser().getId());
                this.thumbnailFolder = space.getRootfolder().createChildFolder(thumbnailpath, JDSServer.getInstance().getAdminUser().getId());
                loadJavaTemps();
                providerConfigs = loadDbConfig();
                dsmBeans = loadDsmBeans();

                this.tempFolder = space.getRootfolder().createChildFolder(TempPath, JDSServer.getInstance().getAdminUser().getId());
                tempFolder.getChildrenRecursivelyList();
                //批量预读配置
                Set<String> versions = new HashSet<>();
                List<FileInfo> fileInfos = tempFolder.getFileListRecursively();
                for (FileInfo fileInfo : fileInfos) {
                    versions.add(fileInfo.getCurrentVersonId());
                }
                CtVfsFactory.getCtVfsService().loadVersionByIds(versions);

        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    public DSMBean updateDSMBean(DSMBean tempBean) throws JDSException {
        if (tempBean != null) {
            if (tempBean.getDsmTempId() == null || tempBean.getDsmTempId().equals("")) {
                tempBean.setDsmTempId(UUID.randomUUID().toString());
            }
            FileInfo fileInfo = this.dsmBeanFolder.createFile(tempBean.getDsmTempId(), tempBean.getName(), null);
            this.getVfsClient().saveFileAsContent(fileInfo.getPath(), JSONObject.toJSONString(tempBean), VFSConstants.Default_Encoding);

        }
        return tempBean;
    }

    public ProviderConfig updateProviderConfig(ProviderConfig providerConfig) throws JDSException {
        if (providerConfig != null) {

            if (providerConfig.getConfigKey() == null || providerConfig.getConfigKey().equals("")) {
                providerConfig.setConfigKey(UUID.randomUUID().toString());
            } else {
                MetadataFactory factory = MetadataFactory.getInstance(providerConfig.getConfigKey());
                if (factory != null) {
                    factory.updateProviderConfig(providerConfig);
                }
            }
            this.deleteProviderConfig(providerConfig.getConfigKey());
            this.providerConfigs.add(providerConfig);

            FileInfo fileInfo = this.dbconfigFolder.createFile(providerConfig.getConfigKey(), providerConfig.getConfigKey(), null);
            this.getVfsClient().saveFileAsContent(fileInfo.getPath(), JSONObject.toJSONString(providerConfig), VFSConstants.Default_Encoding);

        }
        return providerConfig;
    }

    public ProviderConfig getProviderConfigByKey(String configKey) {
        if (configKey != null && !configKey.equals("")) {
            List<ProviderConfig> providerConfigs = this.getProviderConfigs();
            for (ProviderConfig dsmBean : providerConfigs) {
                if (dsmBean != null && (dsmBean.getConfigKey().equals(configKey) || dsmBean.getConfigName().equals(configKey))) {
                    return dsmBean;
                }
            }
        }
        return null;
    }


    public void deleteDSMBean(String dsmid) throws JDSException {
        if (dsmid != null) {
            String[] refIds = StringUtility.split(dsmid, ";");
            for (String id : refIds) {
                DSMBean dsmBean = this.getDSMBeanById(id);
                if (dsmBean != null) {
                    this.dsmBeans.remove(dsmBean);
                    this.getVfsClient().deleteFile(dsmBean.getDsmTempId());
                }
            }
        }
    }

    public void deleteProviderConfig(String configKey) throws JDSException {
        if (configKey != null) {
            String[] refIds = StringUtility.split(configKey, ";");
            for (String id : refIds) {
                ProviderConfig dsmBean = this.getProviderConfigByKey(id);
                if (dsmBean != null) {
                    this.providerConfigs.remove(dsmBean);
                    this.getVfsClient().deleteFile(dsmBean.getConfigKey());
                }
            }
        }
    }


    public DSMBean createDSMBean(String uuid) {
        DSMBean tempBean = null;
        if (uuid == null || uuid.equals("")) {
            uuid = UUID.randomUUID().toString();
        }
        tempBean = this.getDSMBeanById(uuid);
        if (tempBean == null) {
            tempBean = new DSMBean();
            tempBean.setDsmTempId(uuid);
        }
        return tempBean;
    }

    public DSMBean getDSMBeanById(String dsmId) {
        if (dsmId != null && !dsmId.equals("")) {
            List<DSMBean> dsmBeanList = this.getDSMBeanList();
            for (DSMBean dsmBean : dsmBeanList) {
                if (dsmBean != null && dsmBean.getDsmTempId().equals(dsmId)) {
                    return dsmBean;
                }
            }
        }
        return null;
    }

    public List<DSMBean> getDSMBeanList() {
        if (dsmBeans.isEmpty() && JDSServer.getClusterClient().isLogin()) {
            dsmBeans = loadDsmBeans();
        }
        return dsmBeans;
    }


    public JavaSrcBean genJavaSrc(File file, DSMInst dsmInst, String javaTempId) {
        String filePath = file.getAbsolutePath();
        JavaSrcBean srcBean = null;
        if (dsmInst != null) {
            srcBean = dsmInst.getJavaSrcByPath(filePath);
            if (srcBean == null) {
                srcBean = new JavaSrcBean(file, dsmInst, javaTempId);
            }
            if (javaTempId != null) {
                srcBean.setJavaTempId(javaTempId);
            }

            dsmInst.getJavaSrcBeans().add(srcBean);
        } else {
            srcBean = new JavaSrcBean(file, dsmInst, javaTempId);
        }
        return srcBean;
    }


    public JavaTemp updateJavaTemp(JavaTemp javaTemp) throws JDSException {
        String javaTempId = javaTemp.getJavaTempId();
        if (javaTempId == null || javaTempId.equals("")) {
            javaTempId = UUID.randomUUID().toString();
            javaTemp.setJavaTempId(javaTempId);
        } else {
            JavaTemp ojavaTemp = this.getJavaTempById(javaTempId);
            if (ojavaTemp != null) {
                javaTemp = ojavaTemp.fill(javaTemp);
            }
        }

        String fieldId = javaTemp.getFileId();
        if (fieldId == null) {
            fieldId = javaTempId;
        }
        FileInfo fileInfo = this.getVfsClient().getFileById(fieldId);
        if (fileInfo == null) {
            fileInfo = this.tempBeanFolder.createFile(javaTemp.getJavaTempId(), javaTemp.getName(), null);
        }
        javaTemp.setFileId(fileInfo.getID());
        javaTemp.setJavaTempId(fileInfo.getID());
        this.tempMap.put(javaTemp.getFileId(), javaTemp);
        this.getVfsClient().saveFileAsContent(fileInfo.getPath(), JSONObject.toJSONString(javaTemp), VFSConstants.Default_Encoding);
        return javaTemp;
    }


    private void copyStreamToFile(InputStream input, File file) throws IOException {
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (file.exists() && !file.canWrite()) {
            final String message = "Unable to open file " + file + " for writing.";
            throw new IOException(message);
        }
        if (input != null) {
            final FileOutputStream output = new FileOutputStream(file);
            IOUtility.copy(input, output);
            IOUtility.shutdownStream(input);
            IOUtility.shutdownStream(output);
        }

    }


    private List<JavaSrcBean> mergeBeans(List<JavaSrcBean> javaSrcBeans, List<JavaSrcBean> beans) {
        for (JavaSrcBean bean : javaSrcBeans) {
            updateBeans(bean, beans);
        }
        return beans;
    }


    private List<JavaSrcBean> updateBeans(JavaSrcBean javaSrcBean, List<JavaSrcBean> beans) {
        boolean isUpdate = false;
        for (JavaSrcBean bean : beans) {
            if (javaSrcBean.equals(bean)) {
                isUpdate = true;
                bean.setJavaTempId(javaSrcBean.getJavaTempId());
                bean.setDate(System.currentTimeMillis());
                bean.setClassName(javaSrcBean.getClassName());
            }
        }
        if (!isUpdate) {
            beans.add(javaSrcBean);
        }
        return beans;
    }


    public void uploadThumbnail(MD5InputStream inputStream, String fileName, String tempId, ThumbnailType type) throws JDSException {
        //
        int index = fileName.lastIndexOf(".");
        String mimeType = ".png";
        if (index > 0) {
            mimeType = fileName.substring(index).toLowerCase();
        }

        File tempFile = null;
        FileOutputStream out = null;
        FileInputStream in = null;
        try {
            tempFile = File.createTempFile("" + System.currentTimeMillis(), ".temp");
            out = new FileOutputStream(tempFile);
            IOUtility.copy(inputStream, out);
            in = new FileInputStream(tempFile);
            String hash = DigestUtils.md5Hex(in);
            String localFileName = hash + mimeType;
            FileVersion version = this.getVfsClient().upload(thumbnailFolder.getPath() + localFileName, tempFile, null);

            switch (type) {
                case dsmTemp:
                    DSMBean temp = getDSMBeanById(tempId);
                    temp.setThumbnailFileId(version.getFileId());
                    temp.setImage(File.separator + thumbnailpath + File.separator + localFileName);
                    updateDSMBean(temp);
                    break;
                case javaTemp:
                    JavaTemp javatemp = this.getJavaTempById(tempId);
                    javatemp.setThumbnailFileId(version.getFileId());
                    javatemp.setImage(File.separator + thumbnailpath + File.separator + localFileName);
                    updateJavaTemp(javatemp);
                    break;
                case sysTemp:
                    SysWebManager manager = EsbUtil.parExpression(SysWebManager.class);
                    SubSystem system = manager.getSubSystemInfo(tempId).get();
                    CtSubSystem ctSubSystem = new CtSubSystem(system);
                    ctSubSystem.setIcon(File.separator + thumbnailpath + File.separator + localFileName);
                    manager.saveSystemInfo(ctSubSystem);
                    break;

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new VFSException(e);
        } finally {
            tempFile.deleteOnExit();
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void cloneConfig(String source, String target) {
        // tempFolder
    }


    private InputStream getInputStream(String content) {
        if (content == null) {
            return null;
        }
        try {
            return new ByteArrayInputStream(content.getBytes(VFSConstants.Default_Encoding));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<JavaTemp> getAllJavaTemps() {
        Set<String> filedIdSet = tempMap.keySet();
        List<JavaTemp> javaTempList = new ArrayList<>();
        for (String filedId : filedIdSet) {
            JavaTemp javaTemp = this.getJavaTempById(filedId);
            javaTempList.add(javaTemp);
        }
        return javaTempList;
    }


    public List<DSMBean> loadDsmBeans() {
        List<DSMBean> dsmBeanList = new ArrayList<>();
        List<FileInfo> fieldInfos = dsmBeanFolder.getFileList();
        for (FileInfo fileInfo : fieldInfos) {
            try {
                StringBuffer jsonBuffer = this.getVfsClient().readFileAsString(fileInfo.getPath(), null);
                DSMBean dsmBean = JSONObject.parseObject(jsonBuffer.toString(), DSMBean.class);
                if (dsmBean != null) {
                    dsmBeanList.add(dsmBean);
                }

            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return dsmBeanList;
    }

    public List<ProviderConfig> getProviderConfigs() {
        if (providerConfigs == null) {
            providerConfigs = this.loadDbConfig();
        }
        return providerConfigs;
    }

    public void setProviderConfigs(List<ProviderConfig> providerConfigs) {
        this.providerConfigs = providerConfigs;
    }

    ;

    public List<ProviderConfig> loadDbConfig() {
        List<ProviderConfig> providerConfigList = new ArrayList<>();
        List<FileInfo> fieldInfos = dbconfigFolder.getFileList();
        for (FileInfo fileInfo : fieldInfos) {
            try {
                StringBuffer jsonBuffer = this.getVfsClient().readFileAsString(fileInfo.getPath(), null);
                String json = jsonBuffer.toString();
                if (json.length() > 0) {
                    ProviderConfig providerConfig = JSONObject.parseObject(jsonBuffer.toString(), ProviderConfig.class);
                    if (providerConfig != null) {
                        providerConfigList.add(providerConfig);
                    }
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return providerConfigList;
    }


    public List<String> loadJavaTemps() {
        List<String> javaTemps = new ArrayList<>();
        List<FileInfo> fieldInfos = tempBeanFolder.getFileList();
        for (FileInfo fileInfo : fieldInfos) {
            try {
                StringBuffer jsonBuffer = this.getVfsClient().readFileAsString(fileInfo.getPath(), null);
                String json = jsonBuffer.toString();
                if (json.length() > 0) {
                    JavaTemp javaTemp = JSONObject.parseObject(jsonBuffer.toString(), JavaTemp.class);
                    if (javaTemp != null) {
                        javaTemp.setFileId(fileInfo.getID());
                        javaTemp.setJavaTempId(fileInfo.getID());
                        if (!javaTemps.contains(fileInfo.getID())) {
                            javaTemps.add(javaTemp.getFileId());
                        }
                        tempMap.put(javaTemp.getFileId(), javaTemp);
                    }
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return javaTemps;
    }


    //
    public void deleteJavaFile(JavaSrcBean javaFile) {
        ClassUtility.getFileObjectMap().remove(javaFile.getClassName());
        ClassUtility.getDynClassMap().remove(javaFile.getClassName());
        try {
            File file = javaFile.getFile();
            FileUtility.forceDelete(file);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void deleteJavaPackage(JavaPackage javaPackage) {
        if (javaPackage != null && javaPackage.getPackageFile().exists()) {
            List<JavaSrcBean> javaFiles = javaPackage.listAllFile();
            for (JavaSrcBean javaFile : javaFiles) {
                ClassUtility.getFileObjectMap().remove(javaFile.getClassName());
                ClassUtility.getDynClassMap().remove(javaFile.getClassName());
            }
            try {
                FileUtility.forceDelete(javaPackage.getPackageFile());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }


    }

    public void deleteJavaTemp(String javaTempId) throws JDSException {
        if (javaTempId != null) {
            String[] tempIds = StringUtility.split(javaTempId, ";");
            for (String id : tempIds) {
                JavaTemp javaTemp = this.getJavaTempById(id);
                if (javaTemp != null) {
                    this.tempMap.remove(javaTemp.getFileId());
                    this.getVfsClient().deleteFile(javaTemp.getFileId());
                }
            }
        }
    }

    public JavaTemp createJavaTemp() throws JDSException {
        JavaTemp javaTemp = new JavaTemp();
        String tempId = UUID.randomUUID().toString();
        javaTemp.setJavaTempId(tempId);
        FileInfo fileInfo = tempBeanFolder.createFile(tempId, tempId, null);
        javaTemp.setPath(fileInfo.getPath());
        return javaTemp;
    }


    public List<JavaTemp> getJavaTempByDSMId(String dsmId, DSMType dsmType) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        Set<String> tempIds = new HashSet<>();
        DSMInst dsmInst = DSMFactory.getInstance().getDSMInst(dsmId, dsmType);
        if (dsmInst != null) {
            tempIds = dsmInst.getJavaTempIds();
        }

        for (String javaTempId : tempIds) {
            JavaTemp javaTemp = getJavaTempById(javaTempId);
            javaTemps.add(javaTemp);
        }
        return javaTemps;
    }


    public Set<String> getDSMBeanTempId(DSMTempType dsmTempType) throws JDSException {
        List<DSMBean> tempBeans = getDSMBeanList();
        Set<String> javaTempIds = new HashSet<>();
        for (DSMBean bean : tempBeans) {
            if (bean.getType() == null || bean.getType().equals(dsmTempType)) {
                javaTempIds.addAll(bean.getJavaTempIds());
            }
        }
        return javaTempIds;
    }

    public Set<String> getDSMBeanTempId(DSMTempType dsmTempType, DSMType dsmType) throws JDSException {
        List<DSMBean> tempBeans = getDSMBeanList();
        Set<String> javaTempIds = new HashSet<>();
        Set<String> tempIds = new HashSet<>();
        for (DSMBean bean : tempBeans) {
            if (bean.getType() == null || bean.getType().equals(dsmTempType)) {
                javaTempIds.addAll(bean.getJavaTempIds());
            }
        }
        for (String tempId : javaTempIds) {
            JavaTemp javatemp = this.getJavaTempById(tempId);
            if (javatemp != null) {
                if (dsmType == null || javatemp.getDsmType() == null || dsmType.equals(javatemp.getDsmType())) {
                    tempIds.add(tempId);
                }
            }
        }
        return tempIds;
    }


    public List<JavaTemp> getDSMTempTypeList(DSMTempType dsmTempType) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();

        List<DSMBean> tempBeans = getDSMBeanList();
        for (DSMBean bean : tempBeans) {
            if (bean.getType() == null || bean.getType().equals(dsmTempType)) {
                for (String tempId : bean.getJavaTempIds()) {
                    javaTemps.add(this.getJavaTempById(tempId));
                }
            }
        }
        return javaTemps;
    }

    public List<JavaTemp> getDSMTypeTemps(DSMType type) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaTemp> allJavaTemps = getAllJavaTemps();
        for (JavaTemp temp : allJavaTemps) {
            if (type.equals(DSMType.ALL)) {
                javaTemps.add(temp);
            } else if (temp.getDsmType() != null && temp.getDsmType().equals(type)) {
                javaTemps.add(temp);
            }

        }
        return javaTemps;
    }

    public List<JavaTemp> getRepositoryCatTemps(ViewType viewType, RangeType rangeType, RepositoryType... repositoryTypes) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaTemp> allJavaTemps = getRepositoryTypeTemps(repositoryTypes);
        for (JavaTemp javatemp : allJavaTemps) {
            if (javatemp.getRangeType() != null && javatemp.getRangeType().equals(rangeType)) {
                if (javatemp.getViewType() != null && javatemp.getViewType().equals(viewType)) {
                    javaTemps.add(javatemp);
                }
            }
        }
        return javaTemps;
    }

    public List<JavaTemp> getCustomViewTemps(ViewType type) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaTemp> allJavaTemps = getDSMTypeTemps(DSMType.VIEW);

        for (JavaTemp temp : allJavaTemps) {
            if (temp.getViewType() == null || temp.getViewType().equals(type)) {
                javaTemps.add(temp);
            }
        }
        return javaTemps;
    }

    public List<JavaTemp> getUserDomainTemps(ViewType type) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaTemp> allJavaTemps = getDSMTypeTemps(DSMType.USERDOMAIN);

        for (JavaTemp temp : allJavaTemps) {
            if (temp.getViewType() == null || temp.getViewType().equals(type)) {
                javaTemps.add(temp);
            }
        }
        return javaTemps;
    }

    public List<JavaTemp> getRepositoryViewTemps(ViewType type, RepositoryType... repositoryTypes) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaTemp> allJavaTemps = getCustomViewTemps(type);
        for (JavaTemp temp : allJavaTemps) {
            if (temp.getRepositoryType() != null && Arrays.asList(repositoryTypes).contains(temp.getRepositoryType())) {
                javaTemps.add(temp);
            } else {
                javaTemps.add(temp);
            }
        }
        return javaTemps;
    }

    public List<JavaTemp> getAggViewTemps(ViewType type, RepositoryType... repositoryTypes) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaTemp> allJavaTemps = getAggregationTemps(AggregationType.VIEW);
        for (JavaTemp temp : allJavaTemps) {
            if (temp.getRepositoryType() != null && Arrays.asList(repositoryTypes).contains(temp.getRepositoryType())) {
                javaTemps.add(temp);
            } else {
                javaTemps.add(temp);
            }
        }
        return javaTemps;
    }

    public List<JavaTemp> getViewTemps(DSMType dsmType, ViewType type, RepositoryType... repositoryTypes) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        if (dsmType == null) {
            dsmType = DSMType.VIEW;
        }
        List<JavaTemp> allJavaTemps = new ArrayList<>();

        switch (dsmType) {
            case VIEW:
                allJavaTemps = getDSMTypeTemps(dsmType);
                break;
            case REPOSITORY:
                allJavaTemps = getRepositoryTypeTemps(RepositoryType.values());
                break;
            case AGGREGATION:
                allJavaTemps = getAggregationTemps(AggregationType.VIEW);
                break;
        }

        for (JavaTemp temp : allJavaTemps) {
            if (temp.getViewType() == null || temp.getViewType().equals(type)) {
                if (dsmType.equals(DSMType.REPOSITORY) && temp.getRepositoryType() != null && Arrays.asList(repositoryTypes).contains(temp.getRepositoryType())) {
                    javaTemps.add(temp);
                } else {
                    javaTemps.add(temp);
                }
            }
        }
        return javaTemps;
    }


    public List<JavaTemp> getRepositoryTypeTemps(RepositoryType... type) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaTemp> allJavaTemps = getDSMTypeTemps(DSMType.REPOSITORY);
        for (JavaTemp temp : allJavaTemps) {
            if (temp.getRepositoryType() != null && Arrays.asList(type).contains(temp.getRepositoryType())) {
                javaTemps.add(temp);
            }
        }
        return javaTemps;
    }


    public List<JavaTemp> getAggregationTemps(AggregationType type) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaTemp> allJavaTemps = getDSMTypeTemps(DSMType.AGGREGATION);
        for (JavaTemp temp : allJavaTemps) {
            if (temp.getAggregationType() != null && temp.getAggregationType().equals(type)) {
                javaTemps.add(temp);
            }
        }
        return javaTemps;
    }

    public List<JavaTemp> getDomainTemps(CustomDomainType type) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaTemp> allJavaTemps = getDSMTypeTemps(DSMType.CUSTOMDOMAIN);
        for (JavaTemp temp : allJavaTemps) {
            if (temp.getCustomDomainType() != null && temp.getCustomDomainType().equals(type)) {
                javaTemps.add(temp);
            }
        }
        return javaTemps;
    }

    public List<JavaTemp> getCustomDomainTemps(IconEnumstype type) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        if (type instanceof CustomDomainType) {
            javaTemps = getDomainTemps((CustomDomainType) type);
        } else if (type instanceof BpmDomainType) {
            javaTemps = getBpmCustomDomainTemps((BpmDomainType) type);
        } else if (type instanceof OrgDomainType) {
            javaTemps = getOrgCustomDomainTemps((OrgDomainType) type);
        } else if (type instanceof NavDomainType) {
            javaTemps = getNavCustomDomainTemps((NavDomainType) type);
        } else if (type instanceof MsgDomainType) {
            javaTemps = getMsgCustomDomainTemps((MsgDomainType) type);
        }
        return javaTemps;
    }


    public List<JavaTemp> getBpmCustomDomainTemps(BpmDomainType type) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaTemp> allJavaTemps = getCustomDomainTemps(CustomDomainType.BPM);
        for (JavaTemp temp : allJavaTemps) {
            if (temp.getBpmDomainType()
                    != null && temp.getBpmDomainType().equals(type)) {
                javaTemps.add(temp);
            }
        }
        return javaTemps;
    }

    public List<JavaTemp> getOrgCustomDomainTemps(OrgDomainType type) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaTemp> allJavaTemps = getCustomDomainTemps(CustomDomainType.ORG);
        for (JavaTemp temp : allJavaTemps) {
            if (temp.getOrgDomainType() == null || type.equals(OrgDomainType.ALL) || temp.getOrgDomainType().equals(type)) {
                javaTemps.add(temp);
            }
        }
        return javaTemps;
    }

    public List<JavaTemp> getNavCustomDomainTemps(NavDomainType type) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaTemp> allJavaTemps = getCustomDomainTemps(CustomDomainType.NAV);
        for (JavaTemp temp : allJavaTemps) {
            if (temp.getNavDomainType() != null && temp.getNavDomainType().equals(type)) {
                javaTemps.add(temp);
            }
        }
        return javaTemps;
    }

    public List<JavaTemp> getMsgCustomDomainTemps(MsgDomainType type) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaTemp> allJavaTemps = getCustomDomainTemps(CustomDomainType.MSG);
        for (JavaTemp temp : allJavaTemps) {
            if (temp.getMsgDomainType() != null && temp.getMsgDomainType().equals(type)) {
                javaTemps.add(temp);
            }
        }
        return javaTemps;
    }


    public List<JavaTemp> getRangeTemps(RangeType type) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaTemp> allJavaTemps = getAllJavaTemps();
        for (JavaTemp temp : allJavaTemps) {
            if (temp.getRefType() != null && temp.getRefType().equals(type)) {
                javaTemps.add(temp);
            }
        }
        return javaTemps;
    }


    public List<JavaTemp> getAggTemp(AggregationType aggregationType) throws JDSException {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaTemp> allJavaTemps = getAllJavaTemps();
        for (JavaTemp temp : allJavaTemps) {
            if (temp.getAggregationType() != null && temp.getAggregationType().equals(aggregationType)) {
                javaTemps.add(temp);
            }
        }
        return javaTemps;
    }


    public JavaTemp getJavaTempById(String javaTempId) {
        JavaTemp javaTemp = null;
        if (javaTempId != null && !javaTempId.equals("")) {
            javaTemp = tempMap.get(javaTempId);
            if (javaTemp == null) {
                try {
                    FileInfo fileInfo = this.getVfsClient().getFileById(javaTempId);
                    if (fileInfo != null) {
                        StringBuffer jsonBuffer = this.getVfsClient().readFileAsString(fileInfo.getPath(), null);
                        String json = jsonBuffer.toString();
                        if (json.length() > 0) {
                            javaTemp = JSONObject.parseObject(jsonBuffer.toString(), JavaTemp.class);
                            if (javaTemp != null) {
                                javaTemp.setFileId(fileInfo.getID());
                                javaTemp.setJavaTempId(fileInfo.getID());
                                tempMap.put(javaTemp.getFileId(), javaTemp);
                            }
                        }
                    }

                } catch (JDSException e) {
                    e.printStackTrace();
                }
            }
        }
        return javaTemp;
    }


    public CtVfsService getVfsClient() {
        CtVfsService vfsClient = CtVfsFactory.getCtVfsService();
        return vfsClient;
    }

}
