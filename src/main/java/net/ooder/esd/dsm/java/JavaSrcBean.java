package net.ooder.esd.dsm.java;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.IOUtility;
import net.ooder.common.util.JarLoader;
import net.ooder.common.util.StringUtility;
import net.ooder.common.util.java.DynamicClassLoader;
import net.ooder.esb.config.manager.EsbBeanFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.DSMInst;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.gen.GenJava;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class JavaSrcBean {


    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, JavaSrcBean.class);

    String path;
    String name;
    String target;
    Long date;
    String className;
    String moduleName;
    String packageName;
    String entityClassName;
    String methodName;
    @JSONField(serialize = false)
    JavaDeclaration declaration;

    @JSONField(serialize = false)
    JavaPackage javaPackage;

    String dsmId;
    DSMType dsmType;

    String sourceClassName;
    String javaTempId;
    String projectVersionName;


    public JavaSrcBean() {


    }

    public JavaSrcBean(Class clazz, DSMInst dsmInst) throws JDSException {
        this.dsmId = dsmInst.getDsmId();
        this.dsmType = dsmInst.getDsmType();
        projectVersionName = dsmInst.getProjectVersionName();
        this.className = clazz.getName();
        packageName = clazz.getPackage().getName();
        String javaName = clazz.getSimpleName() + ".java";
        javaPackage = dsmInst.getPackageByName(packageName);
        if (javaPackage == null) {
            javaPackage = dsmInst.createChildPackage(clazz.getPackage().getName());
        }
        File javaFile = new File(javaPackage.getPackageFile(), javaName);
        try {
            if (clazz.getClassLoader() instanceof JarLoader) {
                JarLoader jarLoader = (JarLoader) clazz.getClassLoader();
                byte[] bytes = jarLoader.loadJava(className);
                IOUtility.writeBytesToNewFile(bytes, javaFile);
                javaPackage.upload(javaName, new FileInputStream(javaFile));
                initFile(javaFile, dsmInst);
            } else {
                byte[] bytes = EsbBeanFactory.getInstance().getJavaSource(className);
                IOUtility.writeBytesToNewFile(bytes, javaFile);
            }

        } catch (IOException e) {
            throw new JDSException(e);
        }
    }


    public JavaSrcBean(File file, DSMInst dsmInst, String javaTempId) {
        this.javaTempId = javaTempId;
        this.dsmId = dsmInst.getDsmId();
        this.dsmType = dsmInst.getDsmType();
        projectVersionName = dsmInst.getProjectVersionName();
        initFile(file, dsmInst);
    }

    void initFile(File file, DSMInst dsmInst) {
        this.name = file.getName();
        this.date = file.lastModified();
        GenJava genJava = GenJava.getInstance(projectVersionName);
        String buildPath = new File(genJava.getJavaBuildPath()).getAbsolutePath();
        File rootFile = new File(buildPath);
        this.className = StringUtility.replace(file.getAbsolutePath(), rootFile.getAbsolutePath(), "");
        this.className = StringUtility.replace(className, File.separator, ".");

        if (className.startsWith(".")) {
            className = className.substring(1);
        }
        if (className.endsWith(".java")) {
            className = className.substring(0, className.length() - ".java".length());
        }

        if (name.endsWith(".java")) {
            name = name.substring(0, name.length() - ".java".length());
        }

        path = StringUtility.replace(className, ".", File.separator) + ".java";
        packageName = className.substring(0, className.length() - ("." + name).length());

        try {
            if (dsmInst == null) {
                dsmInst = DSMFactory.getInstance().getDSMInst(dsmId, dsmType);
            }
            javaPackage = new JavaPackage(dsmInst, this.getFile().getParentFile());

        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        this.getFile().delete();
        this.getJavaPackage().listAllFile().remove(this);
        this.getJavaPackage().getDsmInst().getJavaSrcBeans().remove(this);
    }

    public JavaPackage getJavaPackage() {
        if (javaPackage == null) {
            if (packageName != null) {
                try {
                    DSMInst dsmInst = DSMFactory.getInstance().getDSMInst(dsmId, dsmType);
                    javaPackage = dsmInst.getPackageByName(this.getPackageName());
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            } else if (this.getFile() != null) {
                initFile(this.getFile(), null);
            }
        }
        return javaPackage;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setDeclaration(JavaDeclaration declaration) {
        this.declaration = declaration;
    }

    public void setJavaPackage(JavaPackage javaPackage) {
        this.javaPackage = javaPackage;
    }

    public File getFile() {
        GenJava genJava = GenJava.getInstance(projectVersionName);
        String buildPath = new File(genJava.getJavaBuildPath()).getAbsolutePath();
        File rootFile = new File(buildPath);
        File file = new File(rootFile, this.path);
        return file;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDsmId() {
        return dsmId;
    }

    public void setDsmId(String dsmId) {
        this.dsmId = dsmId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }


    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }


    public String getJavaTempId() {
        return javaTempId;
    }

    public void setJavaTempId(String javaTempId) {
        this.javaTempId = javaTempId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getProjectVersionName() {
        return projectVersionName;
    }

    public void setProjectVersionName(String projectVersionName) {
        this.projectVersionName = projectVersionName;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public DSMType getDsmType() {
        return dsmType;
    }

    public void setDsmType(DSMType dsmType) {
        this.dsmType = dsmType;
    }


    public Class dynCompile(boolean dumpFile) throws IOException, JDSException {
        Class clazz = GenJava.getInstance(projectVersionName).dynCompile(className, this.getContent());
        File packageFile = this.getFile().getParentFile();
        String realClassName = className;
        if (realClassName.indexOf(".") > -1) {
            realClassName = realClassName.substring(realClassName.lastIndexOf(".") + 1);
        }

        realClassName = realClassName + ".class";
        File classFile = new File(packageFile, realClassName);
        if (dumpFile && clazz.getClassLoader() instanceof DynamicClassLoader) {
            DynamicClassLoader loader = (DynamicClassLoader) clazz.getClassLoader();
            loader.dumpFile(classFile);
            try {
                clazz = loadClass();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


        return clazz;
    }


    public Class loadClass() throws ClassNotFoundException {
        ClassUtility.fileClassMap.remove(className);
        String classPath = GenJava.getInstance(projectVersionName).getJavaBuildPath();
        Class clazz = ClassUtility.loadClassByFile(classPath, className);
        return clazz;
    }

    public JavaDeclaration getDeclaration() {
        try {
            if (declaration == null) {
                declaration = new JavaDeclaration(this);
            }
        } catch (Throwable e) {
            logger.warn(e);
        }

        return declaration;
    }

    @JSONField(serialize = false)
    public String getContent() throws IOException {
        initFile(this.getFile(), null);
        String content = IOUtility.toString(new FileInputStream(this.getFile()));
        return content;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof JavaSrcBean) {
            JavaSrcBean javaSrcBean = (JavaSrcBean) obj;
            if (this.getClassName() != null && javaSrcBean.getClassName() != null) {
                return javaSrcBean.getClassName().equals(this.getClassName());
            }
            return javaSrcBean.getPath().equals(this.getPath());
        }
        return super.equals(obj);
    }

}
