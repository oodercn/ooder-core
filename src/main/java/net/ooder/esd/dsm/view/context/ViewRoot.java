package net.ooder.esd.dsm.view.context;

import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.java.DynamicClassLoader;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.view.GridViewAnnotation;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.esd.dsm.view.ViewInst;

import net.ooder.annotation.Ref;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.web.util.MethodUtil;
import net.ooder.web.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


public class ViewRoot implements JavaRoot {

    public String packageName;

    public String className;

    public String cnName;

    public ViewInst dsmBean;

    public String basepath;

    public String space;

    public String moduleName;

    public List<JavaSrcBean> refs = new ArrayList<>();

    public List<JavaSrcBean> f2fs = new ArrayList<>();

    public List<JavaSrcBean> o2os = new ArrayList<>();

    public List<JavaSrcBean> o2ms = new ArrayList<>();

    public List<JavaSrcBean> m2os = new ArrayList<>();

    public List<JavaSrcBean> m2ms = new ArrayList<>();

    public Set<String> imports = new LinkedHashSet<>();

    public List<ESDClass> esdClasses;

    public List<JavaSrcBean> srcBeans;


    private static final Class[] customClass = new Class[]{

            PageUtil.class,
            EsbUtil.class,
            Date.class,
            ResultModel.class,
            ListResultModel.class,
            TreeListResultModel.class,
            View.class,
            GridAnnotation.class,
            FormAnnotation.class,
            TreeAnnotation.class,
            GalleryAnnotation.class,
            TabsAnnotation.class,
            GridViewAnnotation.class,
            MethodChinaName.class,
            Controller.class,
            ModuleAnnotation.class,
            Ref.class,
            ToolBarMenu.class,
            CustomAnnotation.class,
            RequestMapping.class,
            ResponseBody.class};


    public ViewRoot(ViewInst dsmBean, List<ESDClass> esdClassList, List<JavaSrcBean> javaSrcBeans) {
        this.dsmBean = dsmBean;
        this.packageName = dsmBean.getPackageName();
        this.dsmBean = dsmBean;
        this.cnName = dsmBean.getDesc();
        this.basepath = dsmBean.getPackageName();
        this.space = dsmBean.getSpace();
        this.esdClasses = esdClassList;
        this.srcBeans = javaSrcBeans;
        this.packageName = dsmBean.getPackageName();
        this.space = dsmBean.getSpace();
        this.basepath = dsmBean.getPackageName();

        try {
            for (Class clazz : customClass) {
                imports.add(clazz.getName());
                if (clazz.isAnnotation()) {
                    imports = MethodUtil.getAllImports(clazz, imports);
                }
            }
            for (ESDClass esdClass : esdClassList) {
                imports = MethodUtil.getAllImports(esdClass.getCtClass(), imports);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        String basePackage = dsmBean.getPackageName() + "." + dsmBean.getSpace().toLowerCase();
        for (Package pack : Package.getPackages()) {
            for (String packName : innerPacks) {
                if (pack.getName().startsWith(basePackage + "." + packName)) {
                    this.imports.add(pack.getName() + ".*");
                }
            }
        }


        Map<String, Class> classMap = new HashMap<>();
        classMap.putAll(ClassUtility.getDynClassMap());

        Set<Map.Entry<String, Class>> dynClassSet = classMap.entrySet();
        for (Map.Entry<String, Class> classEntry : dynClassSet) {
            Class dynClass = classEntry.getValue();
            if (dynClass.getPackage().getName().startsWith(basePackage)) {
                this.imports.add(dynClass.getPackage().getName() + ".*");
            }
        }

        for (ESDClass esdClass : esdClassList) {
            if (esdClass.getCtClass().getClassLoader() instanceof DynamicClassLoader) {
                DynamicClassLoader dynamicClassLoader = (DynamicClassLoader) esdClass.getCtClass().getClassLoader();
                for (Package pack : dynamicClassLoader.getPackages()) {
                    if (pack.getName().startsWith(basePackage)) {
                        this.imports.add(pack.getName() + ".*");
                    }
                }
            } else {
                for (Package pack : Package.getPackages()) {
                    if (pack.getName().startsWith(basePackage)) {
                        this.imports.add(pack.getName() + ".*");
                    }
                }
            }
        }

        try {
            Set<String> javaTempIds = dsmBean.getJavaTempIds();
            for (String javaTempId : javaTempIds) {
                JavaTemp javaTemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
                if (javaTemp != null) {
                    if (javaTemp.getPackagePostfix() == null || javaTemp.getPackagePostfix().equals("..")) {
                        this.imports.add(basePackage + ".*");
                    } else {
                        this.imports.add(basePackage + "." + javaTemp.getPackagePostfix() + ".*");
                    }
                }
            }
            this.imports.remove(packageName + ".*");
        } catch (JDSException e) {
            e.printStackTrace();
        }


        for (JavaSrcBean javaSrcBean : javaSrcBeans) {

            try {
                JavaTemp javaTemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaSrcBean.getJavaTempId());
                switch (javaTemp.getRefType()) {
                    case F2F:
                        this.f2fs.add(javaSrcBean);

                        break;
                    case M2M:
                        this.m2ms.add(javaSrcBean);
                        break;
                    case O2M:
                        o2ms.add(javaSrcBean);
                        break;
                    case O2O:
                        this.o2os.add(javaSrcBean);
                        break;
                    case M2O:
                        this.m2os.add(javaSrcBean);
                        break;
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public ViewInst getDsmBean() {
        return dsmBean;
    }

    public void setDsmBean(ViewInst dsmBean) {
        this.dsmBean = dsmBean;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
    }

    public List<JavaSrcBean> getSrcBeans() {
        return srcBeans;
    }

    public void setSrcBeans(List<JavaSrcBean> srcBeans) {
        this.srcBeans = srcBeans;
    }

    public List<ESDClass> getEsdClasses() {
        return esdClasses;
    }

    public void setEsdClasses(List<ESDClass> esdClasses) {
        this.esdClasses = esdClasses;
    }
}
