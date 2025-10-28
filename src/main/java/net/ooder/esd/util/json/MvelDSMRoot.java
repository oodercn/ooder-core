package net.ooder.esd.util.json;

import net.ooder.context.JDSActionContext;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;

import java.util.Map;

public class MvelDSMRoot {

    public static final String DSMROOT = "DSMROOT";

    public EUModule currModule;

    public Component currComponent;

    public ModuleComponent moduleComponent;

    public Component topCurrComponent;

    public ModuleComponent topModuleComponent;

    public EUModule topModule;

    public MethodConfig methodBean;

    public MethodConfig topMethodBean;


    public MvelDSMRoot() {

    }

    public static MvelDSMRoot getInstance() {
        Map context = JDSActionContext.getActionContext().getContext();
        MvelDSMRoot dsmRoot = (MvelDSMRoot) context.get(DSMROOT);
        if (dsmRoot == null) {
            dsmRoot = new MvelDSMRoot();
            context.put(DSMROOT, dsmRoot);
        }
        return dsmRoot;
    }


    public Component getTopCurrComponent() {
        if (topCurrComponent == null) {
            ModuleComponent topModuleComponent = this.getTopModuleComponent();
            if (topModuleComponent != null) {
                topCurrComponent = topModuleComponent.getCurrComponent();
            }
        }
        return topCurrComponent;
    }

    public void setTopCurrComponent(Component topCurrComponent) {
        this.topCurrComponent = topCurrComponent;
    }

    public ModuleComponent getTopModuleComponent() {
        if (topModuleComponent == null) {
            EUModule topModule = this.getTopModule();
            if (topModuleComponent != null) {
                topModuleComponent = topModule.getComponent();
            }
        }
        return topModuleComponent;
    }

    public void setTopModuleComponent(ModuleComponent topModuleComponent) {
        this.topModuleComponent = topModuleComponent;
    }

    public Component getCurrComponent() {
        if (currComponent == null) {
            ModuleComponent moduleComponent = this.getModuleComponent();
            if (moduleComponent != null) {
                currComponent = moduleComponent.getCurrComponent();
            }
        }
        return currComponent;
    }

    public void setCurrComponent(Component currComponent) {
        this.currComponent = currComponent;
    }

    public EUModule getCurrModule() {
        if (currModule == null) {
            Map context = JDSActionContext.getActionContext().getContext();
            currModule = (EUModule) context.get(CustomViewFactory.CurrModuleKey);
        }
        return currModule;
    }

    public ModuleComponent getModuleComponent() {
        if (moduleComponent == null) {
            EUModule euModule = this.getCurrModule();
            if (euModule != null) {
                moduleComponent = euModule.getComponent();
            }
        }
        return moduleComponent;
    }

    public void setModuleComponent(ModuleComponent moduleComponent) {
        this.moduleComponent = moduleComponent;
    }

    public EUModule getTopModule() {
        if (topModule == null) {
            Map context = JDSActionContext.getActionContext().getContext();
            topModule = (EUModule) context.get(CustomViewFactory.TopModuleKey);
        }
        return topModule;
    }


    public MethodConfig getMethodBean() {
        if (methodBean == null) {
            Map context = JDSActionContext.getActionContext().getContext();
            methodBean = (MethodConfig) context.get(CustomViewFactory.MethodBeanKey);
        }
        return methodBean;
    }


    public MethodConfig getTopMethodBean() {
        if (topMethodBean == null) {
            Map context = JDSActionContext.getActionContext().getContext();
            topMethodBean = (MethodConfig) context.get(CustomViewFactory.TopMethodBeanKey);
        }
        return topMethodBean;
    }


    public void setCurrModule(EUModule currModule) {
        this.currModule = currModule;
    }

    public void setTopModule(EUModule topModule) {
        this.topModule = topModule;
    }

    public void setMethodBean(MethodConfig methodBean) {
        this.methodBean = methodBean;
    }

    public void setTopMethodBean(MethodConfig topMethodBean) {
        this.topMethodBean = topMethodBean;
    }
}
