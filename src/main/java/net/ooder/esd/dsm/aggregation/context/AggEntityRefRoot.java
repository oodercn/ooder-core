package net.ooder.esd.dsm.aggregation.context;

import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.java.DynamicClassLoader;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.View;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.UIType;
import net.ooder.esd.annotation.view.GridViewAnnotation;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRefProxy;
import net.ooder.esd.dsm.temp.JavaTemp;

import net.ooder.annotation.Ref;
import net.ooder.annotation.RefType;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.web.util.MethodUtil;
import net.ooder.web.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


public class AggEntityRefRoot implements JavaRoot {

    public String packageName;


    public String basepath;
    public String className;

    public String space;

    public String cnName;

    public String moduleName;

    public DomainInst domainInst;

    public AggEntityConfig mainClass;

    public Set<AggEntityRefProxy> refs;

    public RefType refType;

    public ESDClass esdClass;

    public Set<String> imports = new LinkedHashSet<>();

    private static final Class[] customClass = new Class[]{
            PageUtil.class,
            EsbUtil.class,
            ResultModel.class,
            ListResultModel.class,
            TreeListResultModel.class,
            View.class,
            UIType.class,
            GridViewAnnotation.class,
            MethodChinaName.class,
            Controller.class,
            ModuleAnnotation.class,
            Ref.class,
            ToolBarMenu.class,
            CustomAnnotation.class,
            RequestMapping.class,
            ResponseBody.class};


    public AggEntityRefRoot(DomainInst domainInst, AggEntityConfig mainClass, Set<AggEntityRefProxy> refs, RefType refType) {

        this.domainInst = domainInst;
        this.packageName = domainInst.getPackageName();
        this.mainClass = mainClass;
        this.className = mainClass.getSourceClassName();
        this.cnName = mainClass.getSourceClass().getDesc();
        this.basepath = domainInst.getPackageName();
        this.space = domainInst.getSpace();
        this.refs = refs;
        this.refType = refType;

        try {
            esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(mainClass.getSourceClassName(), false);
            imports = MethodUtil.getAllImports(esdClass.getCtClass(), imports);
            for (Class clazz : customClass) {
                imports.add(clazz.getName());
                if (clazz.isAnnotation()) {
                    imports = MethodUtil.getAllImports(clazz, imports);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] innerPacks = new String[]{"api", "service", "module", "view", "dic"};
        String basePackage = domainInst.getPackageName() + "." + mainClass.getSourceClass().getEntityClass().getName().toLowerCase();

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


        try {
            Set<String> javaTempIds = domainInst.getJavaTempIds();
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


    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public ESDClass getEsdClass() {
        return esdClass;
    }

    public void setEsdClass(ESDClass esdClass) {
        this.esdClass = esdClass;
    }

    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }

    public AggEntityConfig getMainClass() {
        return mainClass;
    }

    public Set<AggEntityRefProxy> getRefs() {
        return refs;
    }

    public DomainInst getDomainInst() {
        return domainInst;
    }

    public void setDomainInst(DomainInst domainInst) {
        this.domainInst = domainInst;
    }

    public void setMainClass(AggEntityConfig mainClass) {
        this.mainClass = mainClass;
    }

    public void setRefs(Set<AggEntityRefProxy> refs) {
        this.refs = refs;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }


    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
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


    public RefType getRefType() {
        return refType;
    }

    public void setRefType(RefType refType) {
        this.refType = refType;
    }
}
