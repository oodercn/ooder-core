package net.ooder.esd.dsm.aggregation.context;

import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.common.util.java.DynamicClassLoader;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.annotation.*;
import net.ooder.esd.annotation.event.CustomCallBack;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.CustomMenuItem;
import net.ooder.esd.annotation.ui.UIType;
import net.ooder.esd.annotation.view.*;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.aggregation.AggEntityConfig;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.aggregation.ref.AggEntityRef;
import net.ooder.esd.dsm.temp.JavaTemp;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.web.util.MethodUtil;
import net.ooder.web.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


public class AggServiceRoot implements JavaRoot {

    public String packageName;

    public String className;

    public String rootId = "00000000-0000-0000-0000-000000000000";

    public String basepath;

    public String space;

    public String cnName;

    public DomainInst dsmBean;

    public String moduleName;

    public AggEntityRoot root;

    public ESDClass sourceClass;


    public ESDClass esdClass;

    public Set<String> imports = new LinkedHashSet<>();

    private List<ESDField> hiddenFields = new ArrayList<ESDField>();

    private static final Class[] customClass = new Class[]{
            PageUtil.class,
            EsbUtil.class,
            ResultModel.class,
            ListResultModel.class,
            TreeListResultModel.class,
            CustomMenuItem.class,
            CustomCallBack.class,
            UIType.class,
            View.class,
            Aggregation.class,
            APIEventAnnotation.class,
            FormAnnotation.class,
            GridAnnotation.class,
            TreeAnnotation.class,
            TabsViewAnnotation.class,
            FormViewAnnotation.class,
            TreeViewAnnotation.class,
            GalleryViewAnnotation.class,
            TabsAnnotation.class,
            GridViewAnnotation.class,
            ModuleAnnotation.class,
            MethodChinaName.class,
            Controller.class,
            ToolBarMenu.class,
            RequestMapping.class,
            ResponseBody.class
    };


    private AggEntityConfig config;

    public AggServiceRoot(DomainInst dsmBean, AggEntityConfig config, List<AggEntityRef> dsmRefs) {
        this.dsmBean = dsmBean;
        this.config = config;
        this.packageName = dsmBean.getPackageName();
        this.className = config.getSourceClassName();
        this.space = dsmBean.getSpace();
        this.sourceClass = config.getSourceClass();
        this.esdClass = config.getESDClass();
        this.basepath = dsmBean.getPackageName();
        this.hiddenFields = this.sourceClass.getHiddenFieldList();

        root = new AggEntityRoot(dsmBean,config, new ArrayList<>());


        try {
            for (Class clazz : customClass) {
                imports.add(clazz.getName());
            }
            imports = MethodUtil.getAllImports(sourceClass.getCtClass(), imports);
            imports = MethodUtil.getAllImports(config.getESDClass().getCtClass(), imports);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        Set<String> basePackages = new HashSet();
        String currPack = dsmBean.getPackageName() + "." + esdClass.getSourceClass().getEntityClass().getName().toLowerCase();
        basePackages.add(currPack);
        for (String domainPack : domainPacks) {
            basePackages.add(currPack + "." + domainPack);
        }


        Map<String, Class> classMap = new HashMap<>();
        classMap.putAll(ClassUtility.getDynClassMap());

        Set<Map.Entry<String, Class>> dynClassSet = classMap.entrySet();
        for (Map.Entry<String, Class> classEntry : dynClassSet) {
            Class dynClass = classEntry.getValue();
            for (String basePackage : basePackages) {
                if (dynClass.getPackage().getName().startsWith(basePackage)) {
                    this.imports.add(dynClass.getPackage().getName() + ".*");
                }
            }

        }


        for (Package pack : Package.getPackages()) {
            for (String packName : innerPacks) {
                for (String basePackage : basePackages) {
                    if (pack.getName().startsWith(basePackage + "." + packName)) {
                        this.imports.add(pack.getName() + ".*");
                    }
                }
            }
        }


        for (Package pack : ClassUtility.getAllDynPacks()) {
            for (String basePackage : basePackages) {
                if (pack.getName().startsWith(basePackage)) {
                    this.imports.add(pack.getName() + ".*");
                }
            }
        }


        if (esdClass.getCtClass().getClassLoader() instanceof DynamicClassLoader) {
            DynamicClassLoader dynamicClassLoader = (DynamicClassLoader) esdClass.getCtClass().getClassLoader();
            for (Package pack : dynamicClassLoader.getPackages()) {
                for (String basePackage : basePackages) {
                    if (pack.getName().startsWith(basePackage)) {
                        this.imports.add(pack.getName() + ".*");
                    }
                }
            }
        } else {
            for (Package pack : Package.getPackages()) {
                for (String basePackage : basePackages) {
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
                if (javaTemp != null && javaTemp.getAggregationType().equals(AggregationType.ROOT)) {
                    for (String basePackage : basePackages) {
                        if (javaTemp.getPackagePostfix() == null || javaTemp.getPackagePostfix().equals("..")) {
                            this.imports.add(basePackage + ".*");
                        } else {
                            this.imports.add(basePackage + "." + javaTemp.getPackagePostfix() + ".*");
                        }
                    }
                }
            }
            this.imports.remove(packageName + ".*");
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }

    public AggEntityConfig getConfig() {
        return config;
    }

    public void setConfig(AggEntityConfig config) {
        this.config = config;
    }

    boolean hasCols(ESDField colProxy, Set<ESDField> cols) {
        for (ESDField customCol : cols) {
            if (customCol.getName().toLowerCase().equals(colProxy.getName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public AggEntityRoot getRoot() {
        return root;
    }

    public void setRoot(AggEntityRoot root) {
        this.root = root;
    }

    public ESDClass getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(ESDClass sourceClass) {
        this.sourceClass = sourceClass;
    }

    public ESDClass getEsdClass() {
        return esdClass;
    }

    public void setEsdClass(ESDClass esdClass) {
        this.esdClass = esdClass;
    }

    public List<ESDField> getHiddenFields() {
        return hiddenFields;
    }

    public void setHiddenFields(List<ESDField> hiddenFields) {
        this.hiddenFields = hiddenFields;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
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

    public DomainInst getDsmBean() {
        return dsmBean;
    }

    public void setDsmBean(DomainInst dsmBean) {
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

    @Override
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }


}
