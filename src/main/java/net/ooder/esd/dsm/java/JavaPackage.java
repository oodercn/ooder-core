package net.ooder.esd.dsm.java;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import net.ooder.common.JDSException;
import net.ooder.common.util.IOUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.DSMInst;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.gen.GenJava;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaPackage {
    String dsmId;
    String id;
    String name;
    String packageName;
    @JSONField(serialize = false)
    File packageFile;
    @JSONField(serialize = false)
    DSMInst dsmInst;

    DSMType dsmType;
    String imageClass = "fa-solid fa-code-branch";
    String projectVersionName;

    @JSONField(serialize = false)
    public Map<File, JavaPackage> packageFilePackageMap = new HashMap<>();

    JavaPackage(DSMInst dsmInst, File packageFile) {

        this.dsmInst = dsmInst;
        this.dsmType = dsmInst.getDsmType();
        this.projectVersionName = dsmInst.getProjectVersionName();
        this.packageFile = packageFile;
        if (this.packageFile == null) {
            this.packageFile = getRootFile(projectVersionName);
        }
        this.dsmId = dsmInst.getDsmId();
        this.projectVersionName = dsmInst.getProjectVersionName();
        initPackage(this.packageFile);

    }

    public JavaSrcBean upload(String javaName, InputStream inputStream) throws IOException, JDSException {
        if (!javaName.endsWith(".java")) {
            javaName = javaName + ".java";
        }
        CompilationUnit cu = StaticJavaParser.parse(inputStream);
        PackageDeclaration javaPackDeclaration = cu.getPackageDeclaration().get();
        if (!javaPackDeclaration.getName().equals(this.packageName)) {
            javaPackDeclaration.setName(packageName);
        }
        String content = cu.toString();
        return update(javaName, content);
    }


    public JavaSrcBean update(String javaName, String content) throws IOException, JDSException {
        JavaPackage tPackage = dsmInst.getPackageByName(packageName);
        if (!javaName.endsWith(".java")) {
            javaName = javaName + ".java";
        }
        File javaFile = new File(tPackage.getPackageFile(), javaName);
        ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("utf-8"));
        FileOutputStream output = new FileOutputStream(javaFile);
        IOUtility.copy(input, output);
        IOUtility.shutdownStream(input);
        IOUtility.shutdownStream(output);
        JavaSrcBean srcBean = dsmInst.getJavaSrcByPath(javaFile.getAbsolutePath());
        if (srcBean == null) {
            srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(javaFile, dsmInst, null);
        } else {
            srcBean.getDeclaration().reSet();
        }
        return srcBean;
    }


    private File getRootFile(String projectVersionName) {
        GenJava genJava = GenJava.getInstance(projectVersionName);
        String javaPath = genJava.getJavaBuildPath();
        File packageFile = new File(javaPath);
        if (!packageFile.exists()) {
            packageFile.mkdirs();
        }
        return packageFile;
    }

    public JavaPackage createChildPackage(File file) {
        JavaPackage childPackage = packageFilePackageMap.get(file);
        if (childPackage == null) {
            childPackage = new JavaPackage(dsmInst, file);
            packageFilePackageMap.put(file, childPackage);
        }
        return childPackage;
    }

    public JavaPackage createChildPackage(String packageName) {
        File file = getPackageFile();
        if (packageName.indexOf(".") > -1) {
            packageName = packageName.replace(".", "/");
        }

        File rootPackFile = new File(file, packageName);

        if (!rootPackFile.exists()) {
            rootPackFile.mkdirs();
        }
        JavaPackage childPackage = createChildPackage(rootPackFile);

        return childPackage;
    }

    @JSONField(serialize = false)
    public JavaPackage getParent() {
        File parentFile = this.getPackageFile().getParentFile();
        JavaPackage parentPackage = createChildPackage(parentFile);
        return parentPackage;
    }


    void initPackage(File packageFile) {
        try {
            GenJava genJava = GenJava.getInstance(projectVersionName);
            String path = genJava.getJavaBuildPath();
            File rootFile = new File(path);
            if (packageFile.getAbsolutePath().equals(rootFile.getAbsolutePath())) {
                this.packageName = packageFile.getName();
                this.name = packageFile.getName();
            } else {
                this.packageName = StringUtility.replace(packageFile.getAbsolutePath(), rootFile.getAbsolutePath(), "");
                this.packageName = StringUtility.replace(packageName, File.separator, ".");
                if (packageName.startsWith(".")) {
                    packageName = packageName.substring(1);
                }
                this.name = packageFile.getName();
            }
            this.packageFile = packageFile;
            this.id = dsmId + "[" + this.packageName + "]";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public JavaPackage(File packageFile) {
        initPackage(packageFile);
    }

    @JSONField(serialize = false)
    public List<JavaPackage> getChildPackages() {
        List<JavaPackage> allJavaPackages = new ArrayList<>();
        List<JavaPackage> javaPackages = listAllChildren();
        for (JavaPackage javaPackage : javaPackages) {
            if (javaPackage.listFiles().size() > 0) {
                allJavaPackages.add(javaPackage);
            }
        }
        return allJavaPackages;
    }

    @JSONField(serialize = false)
    public List<JavaPackage> listAllChildren() {
        List<JavaPackage> allJavaPackages = new ArrayList<>();
        List<JavaPackage> javaPackages = listChildren();
        // if (this.listAllFile().size() > 0) {
        allJavaPackages.addAll(javaPackages);
        for (JavaPackage javaPackage : javaPackages) {
            //if (javaPackage.listAllFile().size() > 0) {
            allJavaPackages.addAll(javaPackage.listAllChildren());
            //}
        }
        //}
        return allJavaPackages;
    }

    @JSONField(serialize = false)
    public List<JavaSrcBean> listAllFile() {
        List<JavaSrcBean> allJavaFiles = new ArrayList<>();
        allJavaFiles.addAll(this.listFiles());
        List<JavaPackage> javaPackages = listChildren();
        for (JavaPackage javaPackage : javaPackages) {
            allJavaFiles.addAll(javaPackage.listAllFile());
        }
        return allJavaFiles;
    }

    @JSONField(serialize = false)
    public List<JavaPackage> listChildren() {
        List<JavaPackage> javaPackage = new ArrayList<>();
        File[] files = packageFile.listFiles();
        if (files != null) {
            for (File childFile : files) {
                if (childFile.isDirectory()) {
                    JavaPackage childPackage = createChildPackage(childFile);
                    javaPackage.add(childPackage);
                }
            }
        }

        return javaPackage;
    }


    private JavaSrcBean getJavaSrcBeanByPath(File file) {
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        javaSrcBeans.addAll(dsmInst.getJavaEntities());
        for (JavaSrcBean javaSrcBean : javaSrcBeans) {
            if (javaSrcBean != null && javaSrcBean.getFile() != null) {
                if (javaSrcBean.getFile().equals(file)) {
                    return javaSrcBean;
                } else {
                    String filePath = file.getAbsolutePath();
                    GenJava genJava = GenJava.getInstance(projectVersionName);
                    String buildPath = new File(genJava.getJavaBuildPath()).getAbsolutePath();
                    String path = StringUtility.replace(filePath, buildPath, "");
                    path = StringUtility.replace(path, File.separator, "\\");
                    if (javaSrcBean.getFile().getAbsolutePath().equals(filePath) || javaSrcBean.getPath().endsWith(path)) {
                        return javaSrcBean;
                    }
                }
            }
        }
        return null;

    }


    public List<JavaSrcBean> listFiles() {
        List<JavaSrcBean> javaFiles = new ArrayList<>();
        File[] files = packageFile.listFiles();
        if (files != null) {
            List<File> fileList = new ArrayList<>();
            fileList.addAll(Arrays.asList(files));
            for (File childFile : fileList) {
                boolean find = true;
                if (this.getPattern() != null && !this.getPattern().equals("")) {
                    Pattern p = Pattern.compile(this.getPattern(), Pattern.CASE_INSENSITIVE);
                    Matcher namematcher = p.matcher(childFile.getName() == null ? "" : childFile.getName());
                    find = namematcher.find();
                }

                if (!childFile.isDirectory() && childFile.getName().endsWith(".java")) {
                    JavaSrcBean javaFile = getJavaSrcBeanByPath(childFile);
                    if (javaFile != null) {
                        javaFile.setDsmId(dsmId);
                    } else {
                        String javaFileName = childFile.getName();
                        File javaClassFile = new File(childFile.getParentFile(), javaFileName);
                        if (javaClassFile.exists()) {
                            javaFile = new JavaSrcBean(javaClassFile, dsmInst, null);
                            dsmInst.addJavaBean(javaFile);
                        }
                    }
                    if (find) {
                        javaFiles.add(javaFile);
                    }
                }
            }
        }
        return javaFiles;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public File getPackageFile() {
        return packageFile;
    }

    public void setPackageFile(File packageFile) {
        this.packageFile = packageFile;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getPattern() {
        String pattern = null;
        if (JDSActionContext.getActionContext().getParams(TreeListItem.ESDSearchPattern) != null) {
            pattern = JDSActionContext.getActionContext().getParams(TreeListItem.ESDSearchPattern).toString();
        }
        return pattern;
    }


    public DSMInst getDsmInst() {
        if (dsmInst == null) {
            try {
                dsmInst = DSMFactory.getInstance().getDSMInst(dsmId, dsmType);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return dsmInst;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof JavaPackage) {
            JavaPackage javaPackage = (JavaPackage) obj;
            if (this.getPackageName() != null && javaPackage.getPackageName() != null) {
                return javaPackage.getPackageName().equals(this.getPackageName());
            }
        }
        return super.equals(obj);
    }

    public void setDsmInst(DSMInst dsmInst) {
        this.dsmInst = dsmInst;
    }

    public String getDsmId() {
        return dsmId;
    }

    public void setDsmId(String dsmId) {
        this.dsmId = dsmId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DSMType getDsmType() {
        return dsmType;
    }

    public void setDsmType(DSMType dsmType) {
        this.dsmType = dsmType;
    }


    public String getProjectVersionName() {
        return projectVersionName;
    }

    public void setProjectVersionName(String projectVersionName) {
        this.projectVersionName = projectVersionName;
    }
}
