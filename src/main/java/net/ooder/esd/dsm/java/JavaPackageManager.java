package net.ooder.esd.dsm.java;

import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.FileUtility;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.DSMInst;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.gen.GenJava;
import net.ooder.esd.dsm.repository.RepositoryInst;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaPackageManager {

    public Map<String, JavaPackage> rootPackageMap = new HashMap<>();

    File rootFile;

    String projectVersionName;

    public static final String THREAD_LOCK = "Thread Lock";

    static Map<String, JavaPackageManager> managerMap = new HashMap<String, JavaPackageManager>();

    public static JavaPackageManager getInstance(String projectVersionName) {
        JavaPackageManager manager = managerMap.get(projectVersionName);
        if (manager == null) {
            synchronized (THREAD_LOCK) {
                if (manager == null) {
                    manager = new JavaPackageManager(projectVersionName);
                    managerMap.put(projectVersionName, manager);
                }
            }
        }
        return manager;
    }


    JavaPackageManager(String projectVersionName) {
        this.projectVersionName = projectVersionName;
        GenJava genJava = GenJava.getInstance(projectVersionName);
        String javaPath = genJava.getJavaBuildPath();
        rootFile = new File(javaPath);
    }


    public JavaPackage getRootPackage(DSMInst dsmInst) {
        String uKey = dsmInst.getDsmType() + "[" + dsmInst.getDsmId() + "]";
        JavaPackage javaPackage = rootPackageMap.get(uKey);
        if (javaPackage == null) {
            javaPackage = new JavaPackage(dsmInst, null);
            rootPackageMap.put(uKey, javaPackage);
        }
        return javaPackage;
    }

    public JavaPackage findJavaPackageByName(String packageName) throws JDSException {
        JavaPackage javaPackage = null;

        RepositoryInst inst = DSMFactory.getInstance().getRepositoryManager().getProjectRepository(projectVersionName);
        if (inst != null) {
            javaPackage = getPackageByName(inst, packageName);
        }


        if (javaPackage == null) {
            List<DomainInst> domainInsts = DSMFactory.getInstance().getAllDomainInst(projectVersionName);
            for (DomainInst domainInst : domainInsts) {
                if (domainInst != null && javaPackage == null) {
                    javaPackage = getPackageByName(domainInst, packageName);
                    if (javaPackage == null) {
                        javaPackage = getPackageByName(domainInst.getViewInst(), packageName);
                    }
                }
            }
        }

        return javaPackage;
    }

    ;

    public JavaPackage findJavaPackageFile(File file) throws JDSException {
        JavaPackage javaPackage = null;

        RepositoryInst inst = DSMFactory.getInstance().getRepositoryManager().getProjectRepository(projectVersionName);
        if (inst != null) {
            javaPackage = inst.getPackageByFile(file);
        }

        if (javaPackage == null) {
            List<DomainInst> domainInsts = DSMFactory.getInstance().getAllDomainInst(projectVersionName);
            for (DomainInst domainInst : domainInsts) {
                if (domainInst != null) {
                    javaPackage = domainInst.getPackageByFile(file);
                    if (javaPackage == null) {
                        javaPackage = domainInst.getViewInst().getPackageByFile(file);
                    }
                    if (javaPackage != null) {
                        return javaPackage;
                    }
                }
            }
        }


        return javaPackage;
    }

    public JavaPackage getPackageByName(DSMInst dsmInst, String packageName) {
        JavaPackage javaPackage = null;
        JavaPackage projectRoot = this.getRootPackage(dsmInst);
        for (JavaPackage childPackage : projectRoot.listAllChildren()) {
            if (childPackage.getPackageName().equals(packageName)) {
                javaPackage = childPackage;
            }
        }
        return javaPackage;
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

//
//    public JavaPackage createPackage(DSMInst dsmInst, File file) {
//
//       String packageName= dsmInst.getDsmId() + "[" + file.getPath() + "]";
//        JavaPackage childPackage = packageFilePackageMap.get(file);
//        if (childPackage == null) {
//            childPackage = new JavaPackage(dsmInst, file);
//            packageFilePackageMap.put(file, childPackage);
//        } else {
//            childPackage.setJavaSrcBeans(dsmInst.getJavaSrcBeans());
//        }
//        return childPackage;
//    }


}
