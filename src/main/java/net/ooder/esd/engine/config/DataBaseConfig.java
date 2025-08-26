package net.ooder.esd.engine.config;


import java.util.ArrayList;
import java.util.List;

public class DataBaseConfig {

    String configKey;

    List<String> tableName = new ArrayList<String>();

    String tableNames = "";

    String simpleName = "FDT";

    String projectName;

    List<String> ftlTemps;

    public DataBaseConfig() {

    }

    public DataBaseConfig(String configKey) {
        this.configKey = configKey;
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getTableNames() {
        tableNames = "";
        if (tableName != null && tableName.size() > 0) {
            for (String tn : tableName) {
                if (tn != null) {
                    tableNames = tableNames + tn.toUpperCase() + ";";
                }
            }
        }
        return tableNames;
    }

    public List<String> getTableName() {
        return tableName;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public void setTableName(List<String> tableName) {
        this.tableName = tableName;
    }

    public List<String> getFtlTemps() {
        return ftlTemps;
    }

    public void setFtlTemps(List<String> ftlTemps) {
        this.ftlTemps = ftlTemps;
    }
}
