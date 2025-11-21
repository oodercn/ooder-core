package net.ooder.esd.dsm.repository.database.context;

import net.ooder.common.JDSException;
import net.ooder.common.database.dao.DAO;
import net.ooder.common.util.ClassUtility;
import net.ooder.annotation.MethodChinaName;
import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.enums.RangeType;
import net.ooder.esd.dsm.repository.RepositoryInst;
import net.ooder.esd.dsm.repository.database.proxy.DSMColProxy;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.dsm.repository.database.ref.TableRef;
import net.ooder.esd.dsm.repository.database.ref.TableRefProxy;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.annotation.Aggregation;
import net.ooder.annotation.ESDEntity;
import net.ooder.annotation.UserSpace;
import net.ooder.web.util.MethodUtil;

import java.util.*;


public class TableRoot implements JavaRoot {

    public String packageName;

    public String className;

    public String rootId = "00000000-0000-0000-0000-000000000000";

    public String basepath;

    public String space;

    public String cnName;

    public String moduleName;

    public UserSpace userSpace;

    public RepositoryInst repositoryInst;

    public DSMTableProxy table;

    public List<TableRefProxy> refs;

    public List<TableRefProxy> f2fs = new ArrayList<>();

    public List<TableRefProxy> o2os = new ArrayList<>();

    public List<TableRefProxy> o2ms = new ArrayList<>();

    public List<TableRefProxy> m2os = new ArrayList<>();

    public List<TableRefProxy> m2ms = new ArrayList<>();

    private Set<DSMColProxy> allCols = new LinkedHashSet<DSMColProxy>();

    private Set<DSMColProxy> customCols = new LinkedHashSet<DSMColProxy>();

    private Set<DSMColProxy> hiddenCols = new LinkedHashSet<DSMColProxy>();

    private Set<DSMColProxy> captionCols = new LinkedHashSet<DSMColProxy>();

    private Set<DSMColProxy> otherCaptionCols = new LinkedHashSet<DSMColProxy>();

    private Set<DSMColProxy> otherCols = new LinkedHashSet<DSMColProxy>();

    private Set<DSMColProxy> pkCols = new LinkedHashSet<DSMColProxy>();

    private Set<DSMColProxy> fkCols = new LinkedHashSet<DSMColProxy>();

    public Set<String> imports = new LinkedHashSet<>();

    private static final Class[] customClass = new Class[]{
            MethodChinaName.class,
            ComponentType.class,
            Date.class,
            DAO.class,
            ModuleAnnotation.class,
            ESDEntity.class,
            UserSpace.class,
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

    public TableRoot(RepositoryInst repositoryInst, DSMTableProxy tableProxy, List<TableRef> tableRefs, UserSpace userSpace) {
        this.repositoryInst = repositoryInst;
        this.userSpace = userSpace;
        this.packageName = repositoryInst.getPackageName();
        this.className = tableProxy.getClassName();
        this.space = repositoryInst.getSpace();
        this.table = tableProxy;
        this.refs = new ArrayList<TableRefProxy>();
        this.allCols = tableProxy.getDSMColList();
        this.basepath = repositoryInst.getPackageName();
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
        basePackages.add(repositoryInst.getPackageName() + "." + tableProxy.getClassName().toLowerCase());

        for (TableRef tableRef : tableRefs) {
            TableRefProxy tableRefProxy = new TableRefProxy(tableRef);
            String otherPackage = repositoryInst.getPackageName() + "." + tableRefProxy.getOtherTable().getClassName().toLowerCase();
            if (!basePackages.contains(otherPackage)) {
                basePackages.add(otherPackage);
            }
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
                DSMType dsmType = javaTemp.getDsmType();
                for (String basePackage : basePackages) {
                    if (javaTemp != null && Arrays.asList(rangeTypes).contains(rangeType) && dsmType.equals(DSMType.REPOSITORY)) {
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


        for (TableRef tableRef : tableRefs) {
            TableRefProxy tableRefProxy = new TableRefProxy(tableRef);
            if (tableRef.getPkField() != null && tableRef.getFkField() != null) {
                refs.add(tableRefProxy);
                switch (tableRef.getRef()) {
                    case F2F:
                        this.f2fs.add(tableRefProxy);
                        otherCols.add(tableRefProxy.getCaption());
                        otherCaptionCols.add(tableRefProxy.getOtherCaption());
                        DSMColProxy colProxy = tableRefProxy.getFk();
                        addFkField(colProxy);
                        break;
                    case M2M:
                        this.m2ms.add(tableRefProxy);
                        break;
                    case O2M:
                        o2ms.add(tableRefProxy);
                        otherCols.add(tableRefProxy.getCaption());
                        captionCols.add(tableRefProxy.getCaption());
                        break;
                    case O2O:
                        this.o2os.add(tableRefProxy);
                        break;
                    case M2O:
                        otherCols.add(tableRefProxy.getCaption());
                        otherCaptionCols.add(tableRefProxy.getOtherCaption());
                        addFkField(tableRefProxy.getFk());
                        this.m2os.add(tableRefProxy);
                        break;
                }
            }

        }


        this.pkCols = table.getPkFields();
        for (DSMColProxy colProxy : table.getHiddenFields()) {
            if (!hasCols(colProxy, fkCols)
                    && !hasCols(colProxy, pkCols)
                    ) {
                hiddenCols.add(colProxy);
            }
        }

        for (DSMColProxy colProxy : allCols) {
            if (tableProxy.getCaptionField() != null && tableProxy.getCaptionField().getTableFieldInfo().getFieldName().equals(colProxy.getTableFieldInfo().getFieldName())) {
                colProxy.getCustomBean().setCaptionField(true);
            }
            if (hasCols(colProxy, fkCols)) {
                colProxy.getTableFieldInfo().getFieldBean().setComponentType(ComponentType.COMBOINPUT);
                colProxy.getFieldBean().setComponentType(ComponentType.COMBOINPUT);
                colProxy.getTableFieldInfo().setHidden(true);
                colProxy.getCustomBean().setPid(true);
            } else if (hasCols(colProxy, hiddenCols)) {
                colProxy.getTableFieldInfo().getFieldBean().setComponentType(ComponentType.COMBOINPUT);
                colProxy.getFieldBean().setComponentType(ComponentType.COMBOINPUT);
                colProxy.getTableFieldInfo().setHidden(true);
            } else if (hasCols(colProxy, otherCaptionCols)) {
                colProxy.getTableFieldInfo().getFieldBean().setComponentType(ComponentType.COMBOINPUT);
                colProxy.getFieldBean().setComponentType(ComponentType.COMBOINPUT);
            }

        }

        for (DSMColProxy colProxy : tableProxy.getDSMColList()) {
            if (!hasCols(colProxy, fkCols)
                    && !hasCols(colProxy, hiddenCols)
                    && !hasCols(colProxy, pkCols)
                    && !hasCols(colProxy, otherCols)
                    && !hasCols(colProxy, captionCols)
                    && !hasCols(colProxy, otherCaptionCols)
                    ) {
                this.customCols.add(colProxy);
            }
        }

    }

    void addFkField(DSMColProxy colProxy) {
        fkCols.add(colProxy);
    }

    boolean hasCols(DSMColProxy colProxy, Set<DSMColProxy> cols) {
        for (DSMColProxy customCol : cols) {
            if (customCol != null && colProxy != null && customCol.getDbcol().getName().toLowerCase().equals(colProxy.getDbcol().getName().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public UserSpace getUserSpace() {
        return userSpace;
    }

    public void setUserSpace(UserSpace userSpace) {
        this.userSpace = userSpace;
    }

    public static Class[] getCustomClass() {
        return customClass;
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Set<DSMColProxy> getAllCols() {
        return allCols;
    }

    public void setAllCols(Set<DSMColProxy> allCols) {
        this.allCols = allCols;
    }

    public Set<DSMColProxy> getOtherCols() {
        return otherCols;
    }

    public void setOtherCols(Set<DSMColProxy> otherCols) {
        this.otherCols = otherCols;
    }

    public static RangeType[] getRangeTypes() {
        return rangeTypes;
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

    public DSMTableProxy getTable() {
        return table;
    }

    public void setTable(DSMTableProxy tableProxy) {
        this.table = tableProxy;
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

    public List<TableRefProxy> getRefs() {
        return refs;
    }

    public void setRefs(List<TableRefProxy> refs) {
        this.refs = refs;
    }

    public List<TableRefProxy> getF2fs() {
        return f2fs;
    }

    public void setF2fs(List<TableRefProxy> f2fs) {
        this.f2fs = f2fs;
    }

    public List<TableRefProxy> getO2os() {
        return o2os;
    }

    public void setO2os(List<TableRefProxy> o2os) {
        this.o2os = o2os;
    }

    public List<TableRefProxy> getO2ms() {
        return o2ms;
    }

    public void setO2ms(List<TableRefProxy> o2ms) {
        this.o2ms = o2ms;
    }

    public List<TableRefProxy> getM2os() {
        return m2os;
    }

    public void setM2os(List<TableRefProxy> m2os) {
        this.m2os = m2os;
    }

    public List<TableRefProxy> getM2ms() {
        return m2ms;
    }

    public void setM2ms(List<TableRefProxy> m2ms) {
        this.m2ms = m2ms;
    }

    public Set<DSMColProxy> getCustomCols() {
        return customCols;
    }

    public void setCustomCols(Set<DSMColProxy> customCols) {
        this.customCols = customCols;
    }

    public Set<DSMColProxy> getHiddenCols() {
        return hiddenCols;
    }

    public void setHiddenCols(Set<DSMColProxy> hiddenCols) {
        this.hiddenCols = hiddenCols;
    }

    public Set<DSMColProxy> getCaptionCols() {
        return captionCols;
    }

    public void setCaptionCols(Set<DSMColProxy> captionCols) {
        this.captionCols = captionCols;
    }

    public Set<DSMColProxy> getOtherCaptionCols() {
        return otherCaptionCols;
    }

    public void setOtherCaptionCols(Set<DSMColProxy> otherCaptionCols) {
        this.otherCaptionCols = otherCaptionCols;
    }

    public Set<DSMColProxy> getPkCols() {
        return pkCols;
    }

    public void setPkCols(Set<DSMColProxy> pkCols) {
        this.pkCols = pkCols;
    }

    public Set<DSMColProxy> getFkCols() {
        return fkCols;
    }

    public void setFkCols(Set<DSMColProxy> fkCols) {
        this.fkCols = fkCols;
    }


}
