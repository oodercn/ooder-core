package net.ooder.esd.dsm.repository;

import net.ooder.annotation.*;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.java.JavaPackage;
import net.ooder.esd.dsm.repository.config.EntityConfig;
import net.ooder.esd.dsm.repository.config.FieldEntityConfig;
import net.ooder.esd.dsm.repository.entity.EntityRef;
import net.ooder.esd.dsm.repository.entity.EntityRefProxy;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.web.util.MethodUtil;

import java.util.*;


public class EntityRoot implements JavaRoot {

    public String packageName;

    public String className;

    public String rootId = "00000000-0000-0000-0000-000000000000";

    public String basepath;

    public String space;

    public String cnName;

    public String moduleName;

    public Set<String> imports = new LinkedHashSet<>();

    public RepositoryInst repositoryInst;

    public EntityConfig entityConfig;

    public List<EntityRefProxy> refs;

    public ESDClass esdClass;

    public List<EntityRefProxy> f2fs = new ArrayList<>();

    public List<EntityRefProxy> o2os = new ArrayList<>();

    public List<EntityRefProxy> o2ms = new ArrayList<>();

    public List<EntityRefProxy> m2os = new ArrayList<>();

    public List<EntityRefProxy> m2ms = new ArrayList<>();

    private List<FieldEntityConfig> allCols = new ArrayList<>();

    private Set<FieldEntityConfig> pkCols = new LinkedHashSet<FieldEntityConfig>();

    private Set<FieldEntityConfig> fkCols = new LinkedHashSet<FieldEntityConfig>();

    private static final Class[] customClass = new Class[]{
            MethodChinaName.class,
            Ref.class,
            Aggregation.class,
            ModuleAnnotation.class,
            ComponentType.class,
            Date.class,
            ESDEntity.class,
            EsbBeanAnnotation.class,
            JDSException.class,
            APIEventAnnotation.class,
            Aggregation.class,
            CustomAnnotation.class

    };
    private static final RangeType[] rangeTypes = new RangeType[]{
            RangeType.ENTITY,
            RangeType.ENTITYREF
    };


    public EntityRoot(RepositoryInst repositoryInst, EntityConfig entityConfig, List<EntityRef> entityRefs) {
        this.repositoryInst = repositoryInst;
        this.packageName = repositoryInst.getPackageName();
        this.className = entityConfig.getRootClass().getEntityClassName();
        this.esdClass = entityConfig.getESDClass();
        this.space = repositoryInst.getSpace();
        this.entityConfig = entityConfig;
        this.refs = new ArrayList<EntityRefProxy>();
        this.allCols = entityConfig.getFieldList();
        this.cnName = entityConfig.getRootClass().getDesc() == null ? entityConfig.getRootClass().getClassName() : entityConfig.getRootClass().getDesc();
        this.basepath = repositoryInst.getPackageName();

        ESDClass sourceClass = entityConfig.getRootClass();
        ESDClass esdClass = entityConfig.getESDClass();

        try {
            for (Class clazz : customClass) {
                imports.add(clazz.getPackage().getName() + ".*");
                if (clazz.isAnnotation()) {
                    imports = MethodUtil.getAllImports(clazz, imports);
                }
            }
            imports = MethodUtil.getAllImports(sourceClass.getCtClass(), imports);
            imports = MethodUtil.getAllImports(esdClass.getCtClass(), imports);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String[] innerPacks = new String[]{"api", "service", "module", "view", "dic"};
        String basePackage = repositoryInst.getPackageName() + "." + esdClass.getSourceClass().getEntityClass().getName().toLowerCase();

        List<JavaPackage> javaPackages = repositoryInst.getRootPackage().listAllChildren();
        this.imports.add(basePackage + ".*");
        for (JavaPackage javaPackage : javaPackages) {
            for (String packName : innerPacks) {
                if (javaPackage.getPackageName().startsWith(basePackage + "." + packName)) {
                    this.imports.add(javaPackage.getPackageName() + ".*");
                }
            }
        }


//
//        for (Package pack : Package.getPackages()) {
//            for (String packName : innerPacks) {
//                if (pack.getName().startsWith(basePackage + "." + packName)) {
//                    this.imports.add(pack.getName() + ".*");
//                }
//            }
//        }


//        Map<String, Class> classMap = new HashMap<>();
//        classMap.putAll(ClassUtility.getDynClassMap());
//        Set<Map.Entry<String, Class>> dynClassSet = classMap.entrySet();
//        for (Map.Entry<String, Class> classEntry : dynClassSet) {
//            Class dynClass = classEntry.getValue();
//            if (dynClass.getPackage().getName().startsWith(basePackage)) {
//                this.imports.add(dynClass.getPackage().getName() + ".*");
//            }
//        }
//
//
//        if (sourceClass.getCtClass().getClassLoader() instanceof DynamicClassLoader) {
//            DynamicClassLoader dynamicClassLoader = (DynamicClassLoader) sourceClass.getCtClass().getClassLoader();
//            for (Package pack : dynamicClassLoader.getPackages()) {
//                if (pack.getName().startsWith(basePackage)) {
//                    this.imports.add(pack.getName() + ".*");
//                }
//            }
//        } else {
//            for (Package pack : Package.getPackages()) {
//                if (pack.getName().startsWith(basePackage)) {
//                    this.imports.add(pack.getName() + ".*");
//                }
//            }
//        }


        try {
            Set<String> javaTempIds = repositoryInst.getJavaTempIds();
            for (String javaTempId : javaTempIds) {
                JavaTemp javaTemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
                RangeType rangeType = javaTemp.getRangeType();
                if (javaTemp != null && Arrays.asList(rangeTypes).contains(rangeType)) {
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


        for (EntityRef entityRef : entityRefs) {
            EntityRefProxy entityProxy = new EntityRefProxy(entityRef);

            refs.add(entityProxy);
            switch (entityRef.getRefBean().getRef()) {
                case F2F:
                    this.f2fs.add(entityProxy);
                    break;
                case M2M:
                    this.m2ms.add(entityProxy);
                    break;
                case O2M:
                    o2ms.add(entityProxy);

                    break;
                case O2O:
                    this.o2os.add(entityProxy);
                    break;
                case M2O:
                    this.m2os.add(entityProxy);
                    break;
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

    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
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

    public RepositoryInst getRepositoryInst() {
        return repositoryInst;
    }

    public void setRepositoryInst(RepositoryInst repositoryInst) {
        this.repositoryInst = repositoryInst;
    }

    public EntityConfig getEntityConfig() {
        return entityConfig;
    }

    public void setEntityConfig(EntityConfig entityConfig) {
        this.entityConfig = entityConfig;
    }

    public List<EntityRefProxy> getRefs() {
        return refs;
    }

    public void setRefs(List<EntityRefProxy> refs) {
        this.refs = refs;
    }

    public List<EntityRefProxy> getF2fs() {
        return f2fs;
    }

    public void setF2fs(List<EntityRefProxy> f2fs) {
        this.f2fs = f2fs;
    }

    public List<EntityRefProxy> getO2os() {
        return o2os;
    }

    public void setO2os(List<EntityRefProxy> o2os) {
        this.o2os = o2os;
    }

    public List<EntityRefProxy> getO2ms() {
        return o2ms;
    }

    public void setO2ms(List<EntityRefProxy> o2ms) {
        this.o2ms = o2ms;
    }

    public List<EntityRefProxy> getM2os() {
        return m2os;
    }

    public void setM2os(List<EntityRefProxy> m2os) {
        this.m2os = m2os;
    }

    public List<EntityRefProxy> getM2ms() {
        return m2ms;
    }

    public void setM2ms(List<EntityRefProxy> m2ms) {
        this.m2ms = m2ms;
    }

    public List<FieldEntityConfig> getAllCols() {
        return allCols;
    }

    public void setAllCols(List<FieldEntityConfig> allCols) {
        this.allCols = allCols;
    }

    public Set<FieldEntityConfig> getPkCols() {
        return pkCols;
    }

    public void setPkCols(Set<FieldEntityConfig> pkCols) {
        this.pkCols = pkCols;
    }

    public Set<FieldEntityConfig> getFkCols() {
        return fkCols;
    }

    public void setFkCols(Set<FieldEntityConfig> fkCols) {
        this.fkCols = fkCols;
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

    public ESDClass getEsdClass() {
        return esdClass;
    }

    public void setEsdClass(ESDClass esdClass) {
        this.esdClass = esdClass;
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


}
