package net.ooder.esd.engine;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.EUFileType;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.component.CustomMenusBar;
import net.ooder.esd.engine.inner.INProjectVersion;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ComponentList;
import net.ooder.esd.tool.component.DialogComponent;
import net.ooder.esd.tool.component.HiddenInputComponent;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.DivProperties;
import net.ooder.esd.tool.properties.WidgetProperties;
import javassist.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EUModule<T extends ModuleComponent> extends EUFile {

    public String packageName;

    public String className;

    public String realClassName;

    public String simpleClassName;

    public String sourceClassName;

    public String sourceMethod;

    public String desc;


    @JSONField(serialize = false)
    public EUPackage euPackage;


    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @JSONField(serialize = false)
    public INProjectVersion projectVersion;

    public T component;

    @JSONField(serialize = false)
    public Map<String, Object> params = new HashMap<>();

    @JSONField(serialize = false)
    public List<Class> bindService = new ArrayList<Class>();


    public void clearParams(){
        component.getCtxBaseComponent().clearParams();
    }

    public EUModule(INProjectVersion projectVersion, String className, EUFileType fileType) {
        this.filetype = fileType;
        this.projectVersion = projectVersion;
        this.desc = projectVersion.getVersionName() + "[" + className + "]";
        if (className.endsWith(".cls")) {
            className = className.substring(0, className.length() - ".cls".length());
        }
        this.className = className;
        String[] paths = this.className.split("\\.");
        this.name = paths[paths.length - 1];
        this.packageName = "";
        for (int k = 0; k < (paths.length - 1); k++) {
            if (packageName.equals("")) {
                packageName = paths[k];
            } else {
                packageName = packageName + "." + paths[k];
            }
        }

        try {
            euPackage = ESDFacrory.getAdminESDClient().createPackage(projectVersion.getVersionName(), packageName);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        this.path = projectVersion.getPath() + StringUtility.replace(className, ".", "/") + ".cls";

        this.component = (T) new ModuleComponent(this);

        this.realClassName = className;

        if (realClassName.endsWith(CustomViewFactory.dynBuild)) {
            realClassName = realClassName.substring(0, realClassName.length() - CustomViewFactory.dynBuild.length());
        }

    }

    public EUModule(INProjectVersion projectVersion, String className) {
        this(projectVersion, className, EUFileType.EUClass);
    }


    public EUModule addBindService(Class clazz, CustomMenusBar toolBar, Map<String, ?> valueMap) throws NotFoundException, JDSException {
        if (!bindService.contains(clazz)) {
            bindService.add(clazz);
            component.valueMap.putAll(valueMap);
            if (toolBar != null) {
                component.addBindService(clazz, toolBar, null);
            }

        }
        return this;
    }

    public EUModule addParams(Map<String, Object> params) {
        this.component.addParams(params);
        return this;
    }


    public void update(boolean dynBuild) throws JDSException {
        ESDFacrory.getAdminESDClient().saveModule(this, dynBuild);
    }

    @JSONField(serialize = false)
    public Map<String, Object> getAllHiddenParams() {
        Map<String, Object> objectMap = new HashMap<>();
        ComponentList componentList = component.getChildrenRecursivelyList();
        for (Component component : componentList) {
            if (component.getKey().equals(ComponentType.HIDDENINPUT.getClassName())) {
                HiddenInputComponent hiddenInputComponent = (HiddenInputComponent) component;
                objectMap.put(hiddenInputComponent.getProperties().getName(), hiddenInputComponent.getProperties().getValue());
            }

        }
        return objectMap;

    }


    public List<Class> getBindService() {
        return bindService;
    }

    public void setBindService(List<Class> bindService) {
        this.bindService = bindService;
    }

    public T getComponent() {
        return component;
    }


    public void setComponent(T component) {

        this.component = component;
        if (component != null) {
            this.component.setEuModule(this);
        }
    }

    @JSONField(serialize = false)
    public List<Component> getTopUIComponent(boolean hasApi) {
        List<Component> components = new ArrayList<>();
        Component fristComponent = this.getComponent().getLastBoxComponent();
        if (fristComponent instanceof DialogComponent) {
            components.addAll(fristComponent.getChildren());
        } else if (fristComponent.getParent().getProperties() instanceof DivProperties) {
            components.add(fristComponent.getParent());
        } else if (fristComponent.getParent().getProperties() instanceof WidgetProperties) {
            components.add(fristComponent.getParent());
        } else {
            components.add(fristComponent);
        }

        if (hasApi) {
            components.add(component.getCtxBaseComponent());
            components.addAll(component.findComponents(ComponentType.APICALLER, null));
        }

        return components;
    }

    public String getSourceClassName() {
        if (sourceClassName == null && component != null) {
            DSMProperties dsmProperties = component.getProperties().getDsmProperties();
            if (dsmProperties != null) {
                sourceClassName = dsmProperties.getSourceClassName();
            }
        }
        return sourceClassName;
    }


    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }


    public String getSourceMethod() {
        if (sourceMethod == null && component != null) {
            DSMProperties dsmProperties = component.getProperties().getDsmProperties();
            if (dsmProperties != null) {
                sourceMethod = dsmProperties.getSourceMethodName();
            }

        }
        return sourceMethod;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

    public void setSimpleClassName(String simpleClassName) {
        this.simpleClassName = simpleClassName;
    }

    public void setSourceMethod(String sourceMethod) {
        this.sourceMethod = sourceMethod;
    }

    @JSONField(serialize = false)
    public List<Component> getTopComponents() {
        return getTopComponents(false);
    }

    public List<Component> getRealComponents(boolean hasApi) throws JDSException {
        return component.getRealComponents(hasApi);
    }

    public List<Component> getTopComponents(boolean hasApi) {
        return component.getTopComponents(hasApi);
    }

    public String getRealClassName() {
        return realClassName;
    }

    public void setRealClassName(String realClassName) {
        this.realClassName = realClassName;
    }

    @JSONField(serialize = false)
    public INProjectVersion getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(INProjectVersion projectVersion) {
        this.projectVersion = projectVersion;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public EUPackage getEuPackage() {
        return euPackage;
    }

    public void setEuPackage(EUPackage euPackage) {
        this.euPackage = euPackage;
    }


    @Override
    public String toString() {
        return this.getComponent().getDesc() == null ? this.getClassName() : this.getComponent().getDesc();
    }


}
