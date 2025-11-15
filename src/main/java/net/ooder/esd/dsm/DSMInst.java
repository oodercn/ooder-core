package net.ooder.esd.dsm;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.java.JavaPackage;
import net.ooder.esd.dsm.java.JavaPackageManager;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.repository.RepositoryInst;

import java.io.File;
import java.util.*;

public abstract class DSMInst {
    private static final Log log = LogFactory.getLog(JDSConstants.CONFIG_KEY, DSMInst.class);
    public String dsmTempId;
    public String packageName = "net.ooder.test";
    public String space = "test";
    public String name;
    public String projectVersionName;
    public Long createTime;
    public String basepath;
    public String desc;
    public String className;
    public String euPackage;

    public Set<String> javaTempIds = new LinkedHashSet<>();

    public Set<String> openClassNames = new LinkedHashSet<>();

    @JSONField(serialize = false)
    public Map<File, JavaPackage> filePackageMap = new HashMap<>();

    @JSONField(serialize = false)
    public Map<String, JavaPackage> namePackageMap = new HashMap<>();

    @JSONField(serialize = false)
    public JavaPackage rootPackage;

    @JSONField(serialize = false)
    protected List<JavaSrcBean> javaEntities = new ArrayList<>();


    @JSONField(serialize = false)
    public abstract JavaPackage getRootPackage();

    @JSONField(serialize = false)
    public JavaPackage getProjectRoot() {
        JavaPackage projectRoot = JavaPackageManager.getInstance(this.projectVersionName).getRootPackage(this);
        return projectRoot;
    }

    @JSONField(serialize = false)
    public JavaPackage getPackageByName(String packageName) {
        JavaPackage javaPackage = null;
        JavaPackage projectRoot = this.getProjectRoot();
        for (JavaPackage childPackage : projectRoot.listAllChildren()) {
            if (childPackage.getPackageName().equals(packageName)) {
                javaPackage = childPackage;
            }
        }
        return javaPackage;
    }

    public JavaPackage createChildPackage(String packageName) {
        JavaPackage projectRoot = this.getProjectRoot();
        if (packageName.indexOf(".") > -1) {
            packageName = packageName.replace(".", "/");
        }

        File packageFile = new File(projectRoot.getPackageFile(), packageName);

        if (!packageFile.exists()) {
            packageFile.mkdirs();
        }
        JavaPackage childPackage = projectRoot.createChildPackage(packageFile);

        return childPackage;
    }

    public List<JavaSrcBean> loadJavaSrc(List<String> classNameList) {
        List<JavaSrcBean> javaSrcList = new ArrayList<>();
        for (String className : classNameList) {
            JavaSrcBean javaSrcBean = getJavaSrcByClassName(className);
            if (javaSrcBean != null && !javaSrcList.contains(javaSrcBean)) {
                javaSrcList.add(javaSrcBean);
            }
        }
        return javaSrcList;
    }

    public void addJavaBean(JavaSrcBean srcBean) {
        List<JavaSrcBean> javaSrcBeans = this.getJavaEntities();
        if (!javaSrcBeans.contains(srcBean)) {
            javaSrcBeans.add(srcBean);
        }

    }


    @JSONField(serialize = false)
    public JavaPackage getPackageByFile(File file) {
        JavaPackage javaPackage = null;
        JavaPackage rootPackage = this.getRootPackage();
        if (rootPackage.getPackageFile().equals(file)) {
            javaPackage = rootPackage;
        } else {
            for (JavaPackage childPackage : rootPackage.listAllChildren()) {
                if (childPackage.getPackageFile().equals(file)) {
                    javaPackage = childPackage;
                }
            }
        }
        return javaPackage;
    }

    @JSONField(serialize = false)
    public JavaSrcBean getJavaSrcBeanByMethod(MethodConfig methodConfig) {
        JavaSrcBean javaSrcBean = null;
        String sourceClassName = methodConfig.getSourceClassName();
        String methodName = methodConfig.getMethodName();
        List<JavaSrcBean> srcBeans = getJavaSrcListByMethod(sourceClassName, methodName);
        if (srcBeans.size() == 0) {
            String topSourceClassName = methodConfig.getTopSourceClass().getClassName();
            srcBeans = getJavaSrcListByMethod(topSourceClassName, methodName);
        }

        if (srcBeans.size() == 0) {
            if (methodConfig.getViewClass() != null) {
                JavaSrcBean ojavaSrcBean = getJavaSrcByClassName(methodConfig.getJavaClassName());
                if (ojavaSrcBean != null) {
                    javaSrcBean = ojavaSrcBean;
                }
            }
        } else {
            javaSrcBean = srcBeans.get(0);
        }

        if (javaSrcBean == null && methodConfig.getEUClassName() != null) {
            javaSrcBean = this.getJavaSrcByClassName(methodConfig.getEUClassName());
        }


        return javaSrcBean;
    }

    @JSONField(serialize = false)
    public abstract List<JavaSrcBean> getJavaSrcListByMethod(String sourceClassName, String methodName);

    @JSONField(serialize = false)
    public RepositoryInst getRepositoryInst() {
        RepositoryInst repositoryInst = null;
        try {
            repositoryInst = DSMFactory.getInstance().getRepositoryManager().getProjectRepository(this.getProjectVersionName());
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return repositoryInst;
    }

    @JSONField(serialize = false)
    public JavaSrcBean getJavaSrcByPath(String filePath) {
        GenJava genJava = GenJava.getInstance(projectVersionName);
        String buildPath = genJava.getJavaBuildPath();
        String path = StringUtility.replace(filePath, buildPath, "");
        for (JavaSrcBean srcBean : getJavaSrcBeans()) {
            if (srcBean.getFile().getAbsolutePath().equals(filePath) || srcBean.getPath().equals(path)) {
                return srcBean;
            }
        }

        File javaFile = new File(filePath);
        if (javaFile.exists()) {
            JavaSrcBean srcBean = new JavaSrcBean(javaFile, this, null);
            return srcBean;
        }

        return null;
    }


    @JSONField(serialize = false)
    public JavaSrcBean getJavaSrcByClassName(String className) {
        JavaSrcBean javaSrcBean = null;
        for (JavaSrcBean srcBean : getJavaSrcBeans()) {
            if (srcBean.getClassName() != null && srcBean.getClassName().equals(className)) {
                javaSrcBean = srcBean;
            }
        }
        if (javaSrcBean == null) {
            javaSrcBean = reLoadJavaFile(className);
        }

        if (javaSrcBean == null) {
            try {
                Class clazz = ClassUtility.loadClass(className);
                if (clazz != null) {
                    javaSrcBean = new JavaSrcBean(clazz, this);
                }
            } catch (Exception e) {
                log.warn(e);
                // e.printStackTrace();
            }
        }

        if (javaSrcBean == null) {

        }


        return javaSrcBean;
    }

    private JavaSrcBean reLoadJavaFile(String className) {
        String javaPackageName = className.substring(0, className.lastIndexOf("."));
        JavaPackage javaPackage = this.getPackageByName(javaPackageName);
        if (javaPackage != null) {
            List<JavaSrcBean> javaSrcBeans = javaPackage.listFiles();
            for (JavaSrcBean srcBean : javaSrcBeans) {
                if (srcBean.getClassName() != null && srcBean.getClassName().equals(className)) {
                    return srcBean;
                }
            }
        }
        return null;
    }


    public String getEuPackage() {
        if (euPackage == null) {
            euPackage = projectVersionName + "." + space;
        } else if (!euPackage.endsWith("." + space)) {
            euPackage = projectVersionName + "." + space;
        }
        return euPackage;
    }

    public void setEuPackage(String euPackage) {
        this.euPackage = euPackage;
    }

    public void setRootPackage(JavaPackage rootPackage) {
        this.rootPackage = rootPackage;
    }

    public String getDsmTempId() {
        return dsmTempId;
    }

    public void setDsmTempId(String dsmTempId) {
        this.dsmTempId = dsmTempId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectVersionName() {
        return projectVersionName;
    }

    public void setProjectVersionName(String projectVersionName) {
        this.projectVersionName = projectVersionName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    public Set<String> getJavaTempIds() {
        return javaTempIds;
    }

    public void setJavaTempIds(Set<String> javaTempIds) {
        this.javaTempIds = javaTempIds;
    }

    public Map<File, JavaPackage> getFilePackageMap() {
        return filePackageMap;
    }

    public void setFilePackageMap(Map<File, JavaPackage> filePackageMap) {
        this.filePackageMap = filePackageMap;
    }

    public Map<String, JavaPackage> getNamePackageMap() {
        return namePackageMap;
    }

    public void setNamePackageMap(Map<String, JavaPackage> namePackageMap) {
        this.namePackageMap = namePackageMap;
    }


    public List<JavaSrcBean> getJavaEntities() {
        return javaEntities;
    }

    protected void setJavaEntities(List<JavaSrcBean> javaEntities) {
        this.javaEntities = javaEntities;
    }


    public void removeJavaSrc(JavaSrcBean javaSrcBean) {
        Set<JavaSrcBean> javaSrcBeans = new HashSet<>();
        this.getJavaEntities().remove(javaSrcBean);


    }

    public void updateBeans(Set<JavaSrcBean> beans) {
        for (JavaSrcBean srcBean : beans) {
            updateBeans(srcBean, this.getJavaEntities());
        }
    }

    private void updateBeans(JavaSrcBean javaSrcBean, List<JavaSrcBean> beans) {
        boolean isUpdate = false;
        for (JavaSrcBean bean : beans) {
            if (javaSrcBean.equals(bean)) {
                isUpdate = true;
                bean.setJavaTempId(javaSrcBean.getJavaTempId());
                bean.setDate(System.currentTimeMillis());
                bean.setClassName(javaSrcBean.getClassName());
                bean.setPath(javaSrcBean.getPath());
                bean.setDsmId(javaSrcBean.getDsmId());
            }
        }
        if (!isUpdate) {
            beans.add(javaSrcBean);
        }
    }


    public Set<String> getOpenClassNames() {
        return openClassNames;
    }

    public void setOpenClassNames(Set<String> openClassNames) {
        this.openClassNames = openClassNames;
    }

    public abstract DSMType getDsmType();

    public abstract String getDsmId();

    @JSONField(serialize = false)
    public abstract List<JavaSrcBean> getJavaSrcBeans();

}
