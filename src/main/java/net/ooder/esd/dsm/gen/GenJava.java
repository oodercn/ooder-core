package net.ooder.esd.dsm.gen;

import freemarker.template.Configuration;
import freemarker.template.Template;
import javassist.ClassPool;
import javassist.NotFoundException;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.common.util.*;
import net.ooder.common.util.java.DynamicClassLoader;
import net.ooder.config.JDSConfig;
import net.ooder.config.JDSUtil;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.ESDClassManager;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.DSMInst;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.java.JavaPackage;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.temp.JavaTempManager;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.enums.ProjectDefAccess;
import net.ooder.esd.engine.inner.INProject;
import net.ooder.vfs.Folder;
import net.ooder.vfs.VFSConstants;
import net.ooder.web.APIConfigFactory;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;


public class GenJava {

    protected Log log = LogFactory.getLog(JDSConstants.CONFIG_KEY, GenJava.class);

    public static final String encoding = "UTF-8";

    public static final String THREAD_LOCK = "Thread Lock";

    public static final String JavaPath = "java";

    public static final String LibPath = "lib";


    private Configuration cfg;

    private String javaBuildPath;

    private String javaLibPath;


    private Folder javaFolder;

    private Folder libFolder;

    private File file;

    // GenJava javaGen;
    static Map<String, GenJava> javaGenMap = new HashMap<String, GenJava>();

    public static GenJava getInstance(String projectVersionName) {

        if (projectVersionName == null) {
            try {
                DSMFactory.getInstance().getDefaultProjectName();
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        GenJava javaGen = javaGenMap.get(projectVersionName);

        if (javaGen == null) {
            synchronized (THREAD_LOCK) {
                if (javaGen == null) {
                    javaGen = new GenJava(projectVersionName);
                    javaGenMap.put(projectVersionName, javaGen);
                }
            }
        }
        return javaGen;
    }


    GenJava(String projectVersionName) {

        INProject dsmProject = null;
        try {
            if (projectVersionName == null) {
                projectVersionName = DSMFactory.getInstance().getDefaultProjectName();
            }
            dsmProject = ESDFacrory.getDefalutProjectManager().getProjectByName(projectVersionName);
            if (CompileJava.isDebug()) {
                this.javaBuildPath = CompileJava.getDebugClassPath();
                this.javaLibPath = CompileJava.getDebugLibPath();

            } else {
                this.javaBuildPath = JDSConfig.Config.rootServerHome().getAbsolutePath() + File.separator + ESDClient.EXPORT_PATH + File.separator + dsmProject.getProjectName() + File.separator + JavaPath + File.separator;
                File javaBuild = new File(javaBuildPath);

                if (!javaBuild.exists()) {
                    javaBuild.mkdirs();
                }
                if (javaBuild.listFiles().length > 0 || dsmProject.getDefAccess().equals(ProjectDefAccess.Public)) {
                    ClassUtility.getContextClassPath().add(javaBuildPath);
                }

                this.javaLibPath = JDSConfig.Config.currServerHome().getAbsolutePath() + File.separator + ESDClient.EXPORT_PATH + File.separator + dsmProject.getProjectName() + File.separator + LibPath + File.separator;

                File libBuild = new File(javaLibPath);
                if (!libBuild.exists()) {
                    libBuild.mkdirs();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        ClassPool pool = ClassPool.getDefault();
        file = new File(javaBuildPath);
        if (!file.exists()) {
            file.mkdirs();
        }


        try {
            pool.appendClassPath(javaBuildPath);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        log.info("JAVA sourcePath ===>" + this.javaBuildPath);


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

    private String formatFilePath(String path) {
        if (!"/".equals(File.separator)) {
            path = StringUtility.replace(path, "/", File.separator);
        } else {
            path = StringUtility.replace(path, "\\", File.separator);
        }
        return path;
    }


    public void reloadDsmInst(Set<File> javaFiles, DSMInst dsmInst) {
        Set<Class<?>> classSet = new HashSet<>();
        Set<String> classNameSet = ClassUtility.getFileObjectMap().keySet();
        for (File file : javaFiles) {
            if (file.exists()) {
                for (String className : classNameSet) {
                    JavaSrcBean javaFile = null;
                    try {
                        javaFile = BuildFactory.getInstance().getTempManager().genJavaSrc(file, dsmInst, null);
                        if (className.equals(javaFile.getClassName()) || className.startsWith(javaFile.getClassName() + "$")) {
                            try {
                                classSet.add(ClassUtility.loadClass(className));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        APIConfigFactory.getInstance().dyReload(classSet);
    }


    public void jarFiles(List<JavaSrcBean> javaSrcList, String jarPath, DSMInst dsmInst) {
        Set<File> javaFiles = new HashSet<>();
        for (JavaSrcBean javaSrcBean : javaSrcList) {
            File file = javaSrcBean.getFile();
            if (file != null && file.exists()) {
                javaFiles.add(javaSrcBean.getFile());
            }
        }
        jarFiles(javaFiles, jarPath, dsmInst);
    }

    public void jarFiles(Set<File> javaFiles, String jarPath, DSMInst dsmInst) {
        File tmpDir = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
        tmpDir.mkdirs();
        try {
            String javaRootPath = new File(javaBuildPath).getAbsolutePath();

            for (File file : javaFiles) {
                if (file.exists()) {
                    String sFolderPath = file.getParentFile().getAbsolutePath();
                    String tempFolderPath = StringUtility.replace(sFolderPath, javaRootPath, tmpDir.getAbsolutePath());
                    File tFolder = new File(tempFolderPath);
                    if (!tFolder.exists()) {
                        tFolder.mkdirs();
                    }
                    JavaSrcBean javaFile = null;
                    try {
                        javaFile = BuildFactory.getInstance().getTempManager().genJavaSrc(file, dsmInst, null);
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }

                    if (!file.getParentFile().equals(tFolder)) {
                        FileUtility.copyFileToDirectory(file, tFolder);
                    }

                    File packageFile = file.getParentFile();
                    Set<String> classNameSet = ClassUtility.getFileObjectMap().keySet();
                    for (String className : classNameSet) {
                        if (className.equals(javaFile.getClassName()) || className.startsWith(javaFile.getClassName() + "$")) {
                            String simpleClassName = className.substring(className.lastIndexOf(".") + 1, className.length());
                            String javaClassName = simpleClassName + ".java";
                            simpleClassName = simpleClassName + ".class";
                            File classFile = new File(packageFile, simpleClassName);
                            File javaClassFile = new File(packageFile, javaClassName);
                            Class clazz = null;
                            if (!classFile.exists()) {
                                if (javaClassFile.exists()) {
                                    String content = IOUtility.toString(new FileInputStream(javaClassFile));
                                    try {
                                        clazz = this.dynCompile(className, content);
                                    } catch (JDSException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        clazz = ClassUtility.loadClass(className);
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (clazz != null) {
                                    if (clazz.getClassLoader() instanceof DynamicClassLoader) {
                                        DynamicClassLoader loader = (DynamicClassLoader) clazz.getClassLoader();
                                        loader.dumpFile(classFile);
                                    }
                                }
                            }
                            if (classFile.exists()) {
                                FileUtility.copyFileToDirectory(classFile, tFolder);
                            }

                        }
                    }


                }
            }
            if (!jarPath.startsWith(javaLibPath)) {
                jarPath = javaLibPath + jarPath;
            }
            //   ZipUtil.jar(tmpDir.getAbsolutePath(), jarPath);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                FileUtility.deleteDirectory(tmpDir.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void createPackage(String path) {
        File pathFile = new File(FileUtility.getPath(path, File.separator.charAt(0)));
        if (pathFile != null && !pathFile.exists()) {
            pathFile.mkdirs();
        }
    }


    private boolean compile(String dsmId, ChromeProxy log) throws JDSException {
        return compile(this.javaBuildPath, log);
    }


    public boolean compileBaseDir(String baseDir, String dsmId, ChromeProxy log) throws JDSException {
        boolean f = false;
        if (log == null) {
            log = this.getCurrChromeDriver();
        }
        try {
            String javaRootPath = file.getAbsolutePath();
            Set<String> classSrcPaths = new HashSet<>();
            classSrcPaths.add(javaRootPath);
            f = CompileJava.compile(baseDir, classSrcPaths, log);
        } catch (IOException e) {
            log.printError(e.getMessage());
            throw new JDSException(e.getMessage());

        }
        return f;
    }

    public List<Class> dynCompile(List<JavaSrcBean> javaSrcBeans) throws JDSException {
        return dynCompile(javaSrcBeans, true);
    }

    private List<Class> dynCompile(List<JavaSrcBean> javaSrcBeans, boolean dynCompile) throws JDSException {
        List<Class> classes = new ArrayList<>();
        List<JavaSrcBean> cloneList = new ArrayList<>();
        cloneList.addAll(javaSrcBeans);
        List<JavaSrcBean> errorClass = new ArrayList<>();

        try {
            //尝试做一次全量预编译，但忽略错误
            compileJavaSrc(cloneList, null);
        } catch (JDSException ee) {

        }
        try {

            for (JavaSrcBean javaSrcBean : cloneList) {
                try {
                    if (javaSrcBean.getContent() != null && javaSrcBean.getContent().length() > 0) {
                        Class clazz = CompileJava.dynCompile(javaSrcBean.getClassName(), javaSrcBean.getContent(), javaBuildPath);
                        if (clazz != null) {
                            classes.add(clazz);
                        }
                    }

                } catch (Throwable e) {
                    errorClass.add(javaSrcBean);
                    //   e.printStackTrace();
                }
            }

            if (errorClass.size() > 0 && dynCompile) {
                classes.addAll(dynCompile(errorClass, false));
            }

            if (dynCompile) {
                for (Class clazz : classes) {
                    APIConfigFactory.getInstance().reload(clazz.getName());
                    ESDClassManager.getInstance().clear(clazz.getName());
                }
                CustomViewFactory.getInstance().reLoad();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new JDSException(e.getMessage());
        }

        return classes;

    }

    public Class dynCompile(String className, String javaCode) throws JDSException {
        Class clazz = null;
        synchronized (className) {
            try {
                clazz = CompileJava.dynCompile(className, javaCode, javaBuildPath);
                APIConfigFactory.getInstance().reload(className);
                ESDClassManager.getInstance().clear(clazz.getName());
                CustomViewFactory.getInstance().reLoad();
            } catch (Exception e) {
                e.printStackTrace();
                throw new JDSException(e.getMessage());
            }

        }
        return clazz;
    }

    public boolean compileJavaSrc(JavaSrcBean javaSrcBean, ChromeProxy log) throws JDSException {
        return compileJavaSrc(Arrays.asList(javaSrcBean), log);
    }

    ;

    public boolean compileJavaSrc(List<JavaSrcBean> javaSrcBeans, ChromeProxy log) throws JDSException {
        if (log == null) {
            log = this.getCurrChromeDriver();
        }
        Set<File> files = new LinkedHashSet<File>();
        for (JavaSrcBean javaSrcBean : javaSrcBeans) {
            File javaFile = javaSrcBean.getFile();
            files.add(javaFile);
        }
        boolean f = compileDsmInst(files, log);
        return f;
    }

    public boolean compileDsmInst(JavaPackage javaPackage, ChromeProxy log) throws JDSException {
        boolean f = compileJavaSrc(javaPackage.listAllFile(), log);
        return f;
    }


    public boolean compileDsmInst(Set<File> files, ChromeProxy log) throws JDSException {
        boolean f = true;
        if (log == null) {
            log = this.getCurrChromeDriver();
        }
        if (files.size() > 0) {
            Set<String> paths = new HashSet<>();
            File libfile = new File(javaLibPath);
            paths.add(libfile.getAbsolutePath());
            String javaRootPath = file.getAbsolutePath();
            Set<String> classSrcPaths = new HashSet<>();
            classSrcPaths.add(javaRootPath);
            f = compile(files, paths, classSrcPaths, log);
        }
        //this.reloadDsmInst(files, dsmInst);
        return f;
    }

    public boolean compile(Set<File> files, Set<String> tPaths, Set<String> classSrcPaths, ChromeProxy
            log) throws JDSException {
        boolean f = false;
        try {
            if (log == null) {
                log = this.getCurrChromeDriver();
            }
            f = CompileJava.compile(files, tPaths, classSrcPaths, log);
            CustomViewFactory.getInstance().reLoad();
        } catch (IOException e) {
            //  e.printStackTrace();
            // log.printError(e.getMessage());
            throw new JDSException(e.getMessage());
        }
        return f;
    }


    public File createJava(JavaTemp javatemp, JavaRoot root, ChromeProxy chrome) throws JDSException {
        if (chrome == null) {
            chrome = getCurrChromeDriver();
        }
        Writer out = null;
        File desFile = null;
        FileInputStream stream = null;
        try {
            String packagePath = root.getPackageName();
            String simpleClassName = root.getClassName();
            if (simpleClassName.indexOf(".") > -1) {
                simpleClassName = simpleClassName.substring(simpleClassName.lastIndexOf(".") + 1);
                packagePath = simpleClassName.substring(0, simpleClassName.lastIndexOf("."));
            }
            String fullClassName = packagePath + "." + simpleClassName;
            String javaFileName = this.javaBuildPath;//EsbBeanFactory.getInstance().getEsbBeanConfig().getJavabeansrcpath();
            if (!javaFileName.endsWith(File.separator)) {
                javaFileName = javaFileName + File.separator;
            }

            javaFileName += packagePath.replace(".", File.separator);
            javaFileName += File.separator + simpleClassName + ".java";
            String fullFileName = formatFilePath(javaFileName);

            cfg = this.getCfg();
            createPackage(fullFileName);
            desFile = new File(fullFileName);
            String tempHash = DigestUtils.md5Hex(javatemp.getContent());
            if (desFile.exists()) {
                stream = new FileInputStream(desFile);
            }

            if (stream == null || !DigestUtils.md5Hex(stream).equals(tempHash)) {
                out = new FileWriter(desFile);
                File tempFile = new File(JDSUtil.getJdsRealPath() + javatemp.getFileId());
                if (!tempFile.exists()) {
                    String hash = DigestUtils.md5Hex(javatemp.getContent());
                    String localFilePath = File.separator + JavaTempManager.ftlpath + File.separator + hash + ".ftl";
                    tempFile = new File(JDSUtil.getJdsRealPath() + localFilePath);
                    this.copyStreamToFile(getInputStream(javatemp.getContent()), tempFile);
                    javatemp.setPath(localFilePath);
                }
                Template temp = cfg.getTemplate(javatemp.getPath(), encoding);
                temp.process(root, out);
                out.flush();
                chrome.printLog("开始创建：" + root.getPackageName() + "." + fullClassName, true);
//                String javaClassPath = ESDFacrory.getESDClient().getSpace().getJavaRoot().getPath() + classPath;
//                CtVfsFactory.getCtVfsService().syncUpload(javaClassPath, desFile, JDSServer.getInstance().getAdminUser().getId());

            }


        } catch (Throwable e) {
            e.printStackTrace();
            String msg = "createJava error className  is " + root.getClassName() + " javatemp : " + javatemp.getRangeType() + "[" + javatemp.getName() + "]";
            log.error(msg);
            chrome.printError(msg);

            if (desFile != null && desFile.exists()) {
                desFile.delete();
            }
            // throw new JDSException(e);
        } finally {
            IOUtility.shutdownStream(stream);
            if (out != null) {
                try {

                    out.flush();
                    out.close();
                } catch (IOException e1) {
                    chrome.printError(e1.getMessage());
                    e1.printStackTrace();
                }
            }
        }
        return desFile;
    }

    private Configuration getCfg() {
        if (cfg == null) {
            this.cfg = new Configuration();
            cfg.setDefaultEncoding(VFSConstants.Default_Encoding);
            try {
                //cfg.setDirectoryForTemplateLoading(new File(JDSUtil.getJdsRealPath() + "classes"));\
                cfg.setDirectoryForTemplateLoading(new File(JDSUtil.getJdsRealPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return cfg;
    }

    private static String toSeparatorChar(String url) {
        String l = url.replace('/', File.separatorChar);
        l = l.replace(File.separatorChar + File.separator, File.separator);
        return l;

    }


    public Folder getJavaFolder() {
        return javaFolder;
    }

    public void setJavaFolder(Folder javaFolder) {
        this.javaFolder = javaFolder;
    }

    public Folder getLibFolder() {
        return libFolder;
    }

    public void setLibFolder(Folder libFolder) {
        this.libFolder = libFolder;
    }

    public String getJavaBuildPath() {
        return javaBuildPath;
    }

    public void setJavaBuildPath(String javaBuildPath) {
        this.javaBuildPath = javaBuildPath;
    }

    public String getJavaLibPath() {
        return javaLibPath;
    }

    public void setJavaLibPath(String javaLibPath) {
        this.javaLibPath = javaLibPath;
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome == null) {
            chrome = new LogSetpLog();
        }
        return chrome;
    }
}
