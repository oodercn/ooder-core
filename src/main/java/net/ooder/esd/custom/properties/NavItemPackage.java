package net.ooder.esd.custom.properties;

import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.EUPackage;
import net.ooder.web.APIConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NavItemPackage {

    private EUModule parentModule;

    private Class ctClass;

    public String name;

    public String id;

    public String desc;

    public APIConfig apiConfig;

    public List<NavItemPkgProperties> items = new ArrayList<>();


    public NavItemPackage(List<EUPackage> euPackages, EUModule parentModule, String projectName, Map<String, ?> valueMap) {
        for (EUPackage euPackage : euPackages) {
            //EUModule module= euPackage.listModules();

        }
    }


    public Class getCtClass() {
        return ctClass;
    }

    public void setCtClass(Class ctClass) {
        this.ctClass = ctClass;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public APIConfig getApiConfig() {
        return apiConfig;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setApiConfig(APIConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    public List<NavItemPkgProperties> getItems() {
        return items;
    }

    public void setItems(List<NavItemPkgProperties> items) {
        this.items = items;
    }

}
