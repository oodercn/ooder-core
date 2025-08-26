package net.ooder.esd.dsm.repository.database;

import net.ooder.annotation.DBField;
import net.ooder.annotation.DBTable;
import net.ooder.common.JDSException;
import net.ooder.common.SystemStatus;
import net.ooder.common.database.ConnectionManagerFactory;
import net.ooder.common.database.bpm.DefaultColEnum;
import net.ooder.common.database.dao.DAOException;
import net.ooder.common.database.dao.DBMap;
import net.ooder.common.database.metadata.ColInfo;
import net.ooder.common.database.metadata.MetadataFactory;
import net.ooder.common.database.metadata.ProviderConfig;
import net.ooder.common.database.metadata.TableInfo;
import net.ooder.common.util.StringUtility;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.repository.database.proxy.DSMColProxy;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.dsm.temp.JavaTempManager;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.MySpace;
import net.ooder.esd.engine.ProjectCacheManager;
import net.ooder.esd.engine.config.DataBaseConfig;
import net.ooder.esd.engine.inner.INProject;
import net.ooder.esd.engine.inner.INProjectVersion;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.vfs.ct.CtVfsService;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FDTFactory {


    private final MySpace space;

    public static final String THREAD_LOCK = "Thread Lock";

    static Map<String, FDTFactory> managerMap = new HashMap<String, FDTFactory>();

    private final JavaTempManager javaTempManager;

    private final ProjectCacheManager projectCacheManager;

    private Map<String, String> configKeyCache = new HashMap<>();


    public static FDTFactory getInstance() throws JDSException {
        return getInstance(ESDFacrory.getAdminESDClient().getSpace());
    }

    public static FDTFactory getInstance(MySpace space) {
        String path = space.getPath();
        FDTFactory manager = managerMap.get(path);
        if (manager == null) {
            synchronized (path) {
                manager = managerMap.get(path);
                if (manager == null) {
                    manager = new FDTFactory(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }

    FDTFactory(MySpace space) {
        this.space = space;
        this.projectCacheManager = ProjectCacheManager.getInstance(space);
        this.javaTempManager = JavaTempManager.getInstance(space);
        List<ProviderConfig> providerConfigs = javaTempManager.getProviderConfigs();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (ProviderConfig config : providerConfigs) {
                    ConnectionManagerFactory.getInstance().updateProviderConfig(config);
                    if (config != null && config.getStatus().equals(SystemStatus.ONLINE)) {
                        MetadataFactory.getInstance(config.getConfigKey());
                    }
                }

            }
        }).start();

    }


    public void reLoad() {
        managerMap.clear();
        configKeyCache.clear();
    }


    public EUModule createTableModule(String tableName, String projectName, DBMap dbMap, String projectVersionName) throws JDSException {
        DSMTableProxy proxy = DSMFactory.getInstance().getRepositoryManager().getTableProxyByName(tableName, projectVersionName);
        String className = "form." + proxy.getFieldName() + "Module";
        EUModule tableModule = ESDFacrory.getAdminESDClient().getModule(className, projectName);

        if (tableModule == null) {
            List<String> hiddenNames = new ArrayList<String>();
            DefaultColEnum[] colEnums = DefaultColEnum.values();
            for (DefaultColEnum colEnum : colEnums) {
                hiddenNames.add(colEnum.name);
                DSMColProxy colProxy = proxy.getFieldByName(colEnum.name);
                if (colProxy != null) {
                    proxy.getHiddenFields().add(colProxy);
                }
            }


            tableModule = ESDFacrory.getAdminESDClient().createModule(className, projectName);

//                TablePanel formLayoutPanel = new TablePanel(tableModule, proxy, dbMap, 2);
//                tableModule.getComponent().addChildren(formLayoutPanel);

            ESDFacrory.getAdminESDClient().saveModule(tableModule, false);
        }
        if (tableModule != null) {
            tableModule.getComponent().clearFormValues();
            tableModule.getComponent().fillFormValues(dbMap, false);
        }


        return tableModule;
    }


    public DSMColProxy getColByFullName(String fillColName, String viewInstId) {
        if (fillColName != null && fillColName.indexOf(".") > -1) {
            String tableName = StringUtility.split(fillColName, ".")[0];
            String colName = StringUtility.split(fillColName, ".")[1];
            try {
                DSMTableProxy proxy = DSMFactory.getInstance().getRepositoryManager().getTableProxyByName(tableName, viewInstId);
                for (DSMColProxy colProxy : proxy.getAllFields()) {
                    if (colProxy.getDbcol().getName().equals(colName)) {
                        return colProxy;
                    }
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    public ColInfo getColInfoByName(String tablename, String colname) throws DAOException {
        ColInfo colInfo = null;
        TableInfo tableInfo = this.getTableInfoByFullName(tablename);
        if (tableInfo != null) {
            List<ColInfo> colInfos = tableInfo.getColList();
            for (ColInfo col : colInfos) {
                if (col.getFieldname().equals(colname) || col.getName().equals(colname)) {
                    colInfo = col;
                    continue;
                }
            }
        }
        return colInfo;
    }

    public TableInfo getTableInfoByFullName(String tableFullName) throws DAOException {
        String configKey = configKeyCache.get(tableFullName);
        TableInfo tableInfo = null;
        if (configKey == null) {
            if (StringUtility.split(tableFullName, ".").length == 2) {
                configKey = tableFullName.split("\\.")[0];
                String name = tableFullName.split("\\.")[1];
                MetadataFactory dbFactory = this.getMetadataFactory(configKey);
                if (dbFactory != null) {
                    tableInfo = dbFactory.getTableInfo(name);
                }
                configKeyCache.put(tableFullName, configKey);
            } else {
                List<ProviderConfig> configs = this.getAllDbConfig();
                for (ProviderConfig config : configs) {
                    try {
                        if (config.getConfigKey() != null && config.getServerURL() != null && getMetadataFactory(config.getConfigKey()) != null) {
                            MetadataFactory factory = getMetadataFactory(config.getConfigKey());

                            tableInfo = factory.getTableInfo(tableFullName);
                            if (tableInfo != null) {
                                configKeyCache.put(tableFullName, configKey);
                                continue;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            MetadataFactory dbFactory = this.getMetadataFactory(configKey);
            if (dbFactory != null) {
                tableInfo = dbFactory.getTableInfo(tableFullName);
            }
            configKeyCache.put(tableFullName, configKey);
        }

        return tableInfo;
    }


    public TableInfo createTable(Class clazz) {
        Map<String, ColInfo> fieldMap = new HashMap<String, ColInfo>();
        DBTable cAnn = AnnotationUtil.getClassAnnotation(clazz, DBTable.class);
        String tableName = cAnn.tableName().toLowerCase();
        TableInfo info = new TableInfo();
        info.setClassName(clazz.getName());
        info.setName(cAnn.tableName());
        info.setPkName(cAnn.primaryKey());
        info.setCnname(cAnn.cname());
        info.setConfigKey(cAnn.configKey());
        Field ms[] = clazz.getDeclaredFields();
        Method methods[] = clazz.getDeclaredMethods();
        for (int i = 0; i < ms.length; i++) {
            Field m = ms[i];
            DBField ma = m.getAnnotation(DBField.class);
            if (ma != null) {
                ColInfo col = new ColInfo();
                col.setName(m.getName());
                col.setFieldname(ma.dbFieldName());
                col.setColType(ma.dbType());
                col.setLength(ma.length());
                col.setCanNull(ma.isNull());
                col.setTablename(tableName);
                col.setUrl(info.getUrl());
                MethodChinaName mcn = m.getAnnotation(MethodChinaName.class);
                if (mcn != null) {
                    col.setCnname(mcn.cname());
                }
                if (cAnn.primaryKey().equals(col.getName())
                        || cAnn.primaryKey().equals(col.getFieldname())
                        ) {
                    col.setPk(true);
                }

                if (col.getCnname() == null) {
                    col.setCnname(col.getName());
                }
                fieldMap.put(col.getFieldname(), col);
                info.addCol(col);
            }
        }


        for (Method method : methods) {
            if (method.getName().startsWith("get") ||
                    (method.getName().startsWith("is") && (method.getReturnType().equals(boolean.class) || method.getReturnType().equals(Boolean.class)))
                            && method.getParameterTypes().length == 0) {
                String fieldName = method.getName().substring("get".length());
                if (method.getName().startsWith("is")) {
                    fieldName = method.getName().substring("is".length());
                }
                fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
                DBField ma = AnnotationUtil.getMethodAnnotation(method, DBField.class);
                if (ma != null) {
                    ColInfo col = fieldMap.get(fieldName);
                    if (col == null) {
                        col = new ColInfo();
                        col.setName(fieldName);
                        col.setFieldname(ma.dbFieldName());
                        col.setColType(ma.dbType());
                        col.setLength(ma.length());
                        col.setCanNull(ma.isNull());
                        col.setUrl(info.getUrl());
                        col.setTablename(tableName);
                        MethodChinaName mcn = AnnotationUtil.getMethodAnnotation(method, MethodChinaName.class);
                        if (mcn != null) {
                            col.setCnname(mcn.cname());
                        }
                        if (cAnn.primaryKey().equals(col.getName())
                                || cAnn.primaryKey().equals(col.getFieldname())
                                ) {
                            col.setPk(true);
                        }
                        if (col.getCnname() == null) {
                            col.setCnname(col.getName());
                        }
                        fieldMap.put(col.getFieldname(), col);
                        info.addCol(col);
                    }

                }
            }
        }
        return info;
    }


    public List<TableInfo> getTableByProject(String projectName) {
        INProjectVersion version = this.projectCacheManager.getProjectVersionByName(projectName);
        INProject project = projectCacheManager.getProjectById(version.getProjectId());
        List<DataBaseConfig> configList = project.getConfig().getDbConfigs();
        List<TableInfo> projecttables = new ArrayList<TableInfo>();
        for (DataBaseConfig dataBaseConfig : configList) {
            List<String> tableNames = dataBaseConfig.getTableName();
            MetadataFactory factory = null;
            try {
                factory = this.getMetadataFactory(dataBaseConfig.getConfigKey());
                if (factory != null) {
                    List<TableInfo> tables = factory.getTableInfos(dataBaseConfig.getSimpleName());
                    for (TableInfo table : tables) {
                        if (tableNames == null || tableNames.contains(table.getName())) {
                            projecttables.add(table);
                        }
                    }
                }
            } catch (DAOException e) {
                e.printStackTrace();
            }
        }
        return projecttables;
    }


    public MetadataFactory getMetadataFactory(String configKey) throws DAOException {
        MetadataFactory factory = MetadataFactory.getInstance(configKey);
        if (factory == null) {
            throw new DAOException("数据库链接失败！configKey[" + configKey + "]");
        }
        return factory;
    }

    public void removeDbConfig(String configKey) throws JDSException {
        javaTempManager.deleteProviderConfig(configKey);


    }

    public void updateDbConfig(ProviderConfig config) throws JDSException {
        if (config.getConfigKey() == null || config.getServerURL() == null || config.getDriver() == null) {
            throw new JDSException("配置错误！");
        }
        if (config.getConfigKey().equals("")) {
            config.setConfigKey(config.getConfigName());
        }

        javaTempManager.updateProviderConfig(config);
    }

    public List<ProviderConfig> getAllDbConfig() {
        return javaTempManager.getProviderConfigs();

    }


    public CtVfsService getVfsClient() {
        CtVfsService vfsClient = CtVfsFactory.getCtVfsService();
        return vfsClient;
    }

    public ProviderConfig getDbConfig(String dbConfigKey) {
        return javaTempManager.getProviderConfigByKey(dbConfigKey);
    }
}
