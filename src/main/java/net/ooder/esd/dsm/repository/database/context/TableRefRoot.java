package net.ooder.esd.dsm.repository.database.context;

import net.ooder.common.JDSException;
import net.ooder.common.database.dao.DAO;
import net.ooder.common.util.ClassUtility;
import net.ooder.annotation.MethodChinaName;
import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.dsm.repository.database.ref.TableRefProxy;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.annotation.Aggregation;
import net.ooder.annotation.ESDEntity;
import net.ooder.web.util.MethodUtil;

import java.util.*;


public class TableRefRoot implements JavaRoot {

    public String packageName;

    public String className;

    public String basepath;

    public String space;

    public String cnName;

    public String moduleName;

    public RepositoryInst repositoryInst;

    public DSMTableProxy table;

    public TableRefProxy ref;


    public Set<String> imports = new LinkedHashSet<>();

    private static final Class[] customClass = new Class[]{
            MethodChinaName.class,
            ComponentType.class,
            Date.class,
            DAO.class,
            ESDEntity.class,
            EsbBeanAnnotation.class,
            JDSException.class,
            APIEventAnnotation.class,
            Aggregation.class,
            CustomAnnotation.class
    };
    private static final RangeType[] rangeTypes = new RangeType[]{
            RangeType.TABLE,
            RangeType.MODULE,
            RangeType.REF
    };


    public TableRefRoot(RepositoryInst repositoryInst, TableRefProxy ref) {
        this.repositoryInst = repositoryInst;
        this.packageName = repositoryInst.getPackageName();
        this.className = ref.getMainTable().getClassName();
        this.cnName = ref.getMainTable().getCnname();
        this.basepath = repositoryInst.getPackageName();
        this.table = ref.getMainTable();
        this.ref = ref;
        this.space = repositoryInst.getSpace();


        try {
            for (Class clazz : customClass) {
                imports.add(clazz.getPackage().getName() + ".*");
                if (clazz.isAnnotation()) {
                    imports = MethodUtil.getAllImports(clazz, imports);
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String[] innerPacks = new String[]{"service", "module"};
        List<String> basePackages = new ArrayList<>();

        basePackages.add(repositoryInst.getPackageName() + "." + ref.getMainTable().getClassName().toLowerCase());

        String otherPackage = repositoryInst.getPackageName() + "." + ref.getOtherTable().getClassName().toLowerCase();
        if (!basePackages.contains(otherPackage)) {
            basePackages.add(otherPackage);
        }


        for (Package pack : Package.getPackages()) {
            for (String basePackage : basePackages) {
                for (String packName : innerPacks) {
                    if (pack.getName().startsWith(basePackage + "." + packName)) {
                        this.imports.add(pack.getName() + ".*");
                    }
                }
            }

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
            for (String basePackage : basePackages) {
                if (pack.getName().startsWith(basePackage)) {
                    this.imports.add(pack.getName() + ".*");
                }
            }
        }


        try {
            Set<String> javaTempIds = repositoryInst.getJavaTempIds();
            for (String javaTempId : javaTempIds) {
                JavaTemp javaTemp = BuildFactory.getInstance().getTempManager().getJavaTempById(javaTempId);
                RangeType rangeType = javaTemp.getRangeType();
                for (String basePackage : basePackages) {
                    if (javaTemp != null && Arrays.asList(rangeTypes).contains(rangeType)) {
                        if (javaTemp.getPackagePostfix() == null || javaTemp.getPackagePostfix().equals("..")) {
                            this.imports.add(basePackage + ".*");
                        } else {
                            this.imports.add(basePackage + "." + javaTemp.getPackagePostfix() + ".*");
                        }
                    }
                }
            }
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

    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }

    public RepositoryInst getRepositoryInst() {
        return repositoryInst;
    }

    public void setRepositoryInst(RepositoryInst repositoryInst) {
        this.repositoryInst = repositoryInst;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public DSMTableProxy getTable() {
        return table;
    }

    public void setTable(DSMTableProxy table) {
        this.table = table;
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

    public TableRefProxy getRef() {
        return ref;
    }

    public void setRef(TableRefProxy ref) {
        this.ref = ref;
    }
}
