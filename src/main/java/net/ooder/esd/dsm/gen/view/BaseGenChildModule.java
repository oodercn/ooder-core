package net.ooder.esd.dsm.gen.view;

import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.LayoutComponent;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.ModuleProperties;
import net.ooder.esd.util.OODUtil;
import net.ooder.server.context.MinServerActionContextImpl;
import ognl.OgnlContext;

import java.util.List;
import java.util.concurrent.Callable;

public abstract class BaseGenChildModule<T extends CustomViewBean> implements Callable<List<JavaGenSource>> {
    protected final String cEuClassName;
    protected final String target;
    protected CustomModuleBean cmoduleBean;
    protected MinServerActionContextImpl autoruncontext;
    protected OgnlContext onglContext;
    Component childComponent;
    ModuleComponent moduleComponent;
    CustomViewBean customViewBean;
    T parentViewBean;
    String parentClassName;
    String childRealPath;


    public BaseGenChildModule(ModuleComponent moduleComponent, Component childComponent, T parentViewBean) {
        JDSContext context = JDSActionContext.getActionContext();
        this.moduleComponent = moduleComponent;
        this.parentViewBean = parentViewBean;
        this.childComponent = childComponent;
        this.parentClassName = moduleComponent.getClassName();
        String simClass = OODUtil.formatJavaName(moduleComponent.getCurrComponent().getAlias(), true);
//        if (!parentClassName.endsWith("." + simClass)  && moduleComponent.getCurrComponent() instanceof LayoutComponent) {
//            this.parentClassName = parentClassName.toLowerCase() + "." + simClass;
//        }
        String childSimClass = OODUtil.formatJavaName(childComponent.getAlias(), true);
        childRealPath = parentViewBean.getXpath();
        if (!childRealPath.toLowerCase().endsWith("." + childSimClass.toLowerCase())) {
            this.childRealPath = childRealPath.toLowerCase() + "." + childSimClass;
        }
        this.autoruncontext = new MinServerActionContextImpl(context.getHttpRequest(), context.getOgnlContext());
        autoruncontext.getParamMap().putAll(context.getPagectx());
        autoruncontext.getParamMap().putAll(context.getContext());

        if (context.getSessionId() != null) {
            autoruncontext.setSessionId(context.getSessionId());
            autoruncontext.getSession().put("sessionHandle", context.getSession().get("sessionHandle"));
        }
        autoruncontext.setSessionMap(context.getSession());
        onglContext = autoruncontext.getOgnlContext();
        String cSimClass = OODUtil.formatJavaName(childComponent.getAlias(), true);
        String cPackageName = parentClassName.toLowerCase();
        this.cEuClassName = cPackageName + "." + cSimClass;
        this.target = childComponent.getTarget();
        try {
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(moduleComponent.getProjectName(), UserSpace.VIEW);
            String domainId = domainInst.getDomainId();
            ModuleViewType comModuleViewType = ModuleViewType.getModuleViewByCom(ComponentType.fromType(childComponent.getKey()));
            ModuleComponent cmoduleComponent = new ModuleComponent(childComponent, cEuClassName);
            if (customViewBean == null) {
                customViewBean = genChildViewBean(moduleComponent, childComponent, cEuClassName);
            } else if (!customViewBean.getModuleViewType().equals(comModuleViewType)) {
                ModuleProperties cmoduleProperties = new ModuleProperties();
                DSMProperties cdsmProperties = new DSMProperties(customViewBean);
                cmoduleProperties.setMethodName(OODUtil.getGetMethodName(childComponent.getAlias()));
                cmoduleProperties.setDsmProperties(cdsmProperties);
                cmoduleComponent.setProperties(cmoduleProperties);
                cmoduleComponent.setClassName(cEuClassName);
                this.customViewBean = (T) DSMFactory.getInstance().getViewManager().getDefaultViewBean(cmoduleComponent, domainId);
                customViewBean.setDomainId(domainId);
            } else {
                DSMProperties cdsmProperties = cmoduleComponent.getProperties().getDsmProperties();
                if (cdsmProperties == null) {
                    cdsmProperties = new DSMProperties(customViewBean);
                    cdsmProperties.setRealPath(childRealPath);
                    cmoduleComponent.getProperties().setDsmProperties(cdsmProperties);
                }
                cmoduleComponent.setClassName(cEuClassName);
                customViewBean.updateModule(cmoduleComponent);
            }
            customViewBean.setXpath(childRealPath);
            this.cmoduleBean = createModuleBean(cmoduleComponent);
            cmoduleComponent.setClassName(cEuClassName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public abstract AggRootBuild genAggBuild() throws JDSException;

    public T genChildViewBean(ModuleComponent moduleComponent, Component childComponent, String cEuClassName) throws JDSException {

        DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(moduleComponent.getProjectName(), UserSpace.VIEW);
        String domainId = domainInst.getDomainId();
        ModuleComponent cmoduleComponent = new ModuleComponent(childComponent, cEuClassName);
        DSMProperties cdsmProperties = new DSMProperties();
        ModuleProperties cmoduleProperties = new ModuleProperties();
        DSMProperties dsmProperties = moduleComponent.getProperties().getDsmProperties();
        if (dsmProperties != null) {
            cdsmProperties.setDomainId(dsmProperties.getDomainId());
        }
        cmoduleProperties.setMethodName(OODUtil.getGetMethodName(childComponent.getAlias()));
        cmoduleProperties.setDsmProperties(cdsmProperties);
        cmoduleComponent.setProperties(cmoduleProperties);
        cmoduleComponent.setClassName(cEuClassName);
        cdsmProperties.setRealPath(childRealPath);

        T customViewBean = (T) DSMFactory.getInstance().getViewManager().getDefaultViewBean(cmoduleComponent, domainId);
        customViewBean.setDomainId(domainId);
        customViewBean.setXpath(childRealPath);
        customViewBean.updateModule(cmoduleComponent);

        return customViewBean;
    }


    protected CustomModuleBean createModuleBean(ModuleComponent cmoduleComponent) {
        CustomModuleBean cModuleBean = new CustomModuleBean(cmoduleComponent);
        cModuleBean.reBindMethod(cmoduleComponent.getMethodAPIBean());
        cmoduleComponent.setModuleBean(cModuleBean);
        Component childComponent = cmoduleComponent.getCurrComponent();
        cModuleBean.setTarget(childComponent.getTarget());
        cModuleBean.setEuClassName(cmoduleComponent.getClassName());
        return cModuleBean;
    }


    public String getcEuClassName() {
        return cEuClassName;
    }

    public String getTarget() {
        return target;
    }

    public CustomModuleBean getCmoduleBean() {
        return cmoduleBean;
    }

    public void setCmoduleBean(CustomModuleBean cmoduleBean) {
        this.cmoduleBean = cmoduleBean;
    }

    public MinServerActionContextImpl getAutoruncontext() {
        return autoruncontext;
    }

    public void setAutoruncontext(MinServerActionContextImpl autoruncontext) {
        this.autoruncontext = autoruncontext;
    }

    public OgnlContext getOnglContext() {
        return onglContext;
    }

    public void setOnglContext(OgnlContext onglContext) {
        this.onglContext = onglContext;
    }

    public Component getChildComponent() {
        return childComponent;
    }

    public void setChildComponent(Component childComponent) {
        this.childComponent = childComponent;
    }

    public ModuleComponent getModuleComponent() {
        return moduleComponent;
    }

    public void setModuleComponent(ModuleComponent moduleComponent) {
        this.moduleComponent = moduleComponent;
    }

    public CustomViewBean getCustomViewBean() {
        return customViewBean;
    }

    public void setCustomViewBean(CustomViewBean customViewBean) {
        this.customViewBean = customViewBean;
    }

    public String getParentClassName() {
        return parentClassName;
    }

    public void setParentClassName(String parentClassName) {
        this.parentClassName = parentClassName;
    }

    public String getChildRealPath() {
        return childRealPath;
    }

    public void setChildRealPath(String childRealPath) {
        this.childRealPath = childRealPath;
    }
}