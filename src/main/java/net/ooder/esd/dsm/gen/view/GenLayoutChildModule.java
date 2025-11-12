package net.ooder.esd.dsm.gen.view;

import net.ooder.common.util.ClassUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.view.CustomLayoutViewBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.ModuleProperties;
import net.ooder.esd.tool.properties.item.LayoutListItem;
import net.ooder.esd.util.OODUtil;
import net.ooder.server.context.MinServerActionContextImpl;
import net.ooder.server.httpproxy.core.HttpRequest;
import net.ooder.annotation.UserSpace;
import ognl.OgnlContext;

import java.util.List;
import java.util.concurrent.Callable;

public class GenLayoutChildModule implements Callable<CustomModuleBean> {
    private MinServerActionContextImpl autoruncontext;
    private OgnlContext onglContext;
    Component childComponent;
    ModuleComponent moduleComponent;
    CustomLayoutViewBean layoutViewBean;
    String parentClassName;
    String childRealPath;

    public GenLayoutChildModule(ModuleComponent moduleComponent, Component childComponent, CustomLayoutViewBean layoutViewBean) {
        JDSContext context = JDSActionContext.getActionContext();
        this.moduleComponent = moduleComponent;
        this.childComponent = childComponent;
        this.parentClassName = moduleComponent.getClassName();
        this.layoutViewBean = layoutViewBean;
        String simClass = OODUtil.formatJavaName(moduleComponent.getCurrComponent().getAlias(), true);
        if (!parentClassName.endsWith("." + simClass)) {
            this.parentClassName = parentClassName.toLowerCase() + "." + simClass;
        }
        String childSimClass = OODUtil.formatJavaName(childComponent.getAlias(), true);

        childRealPath = layoutViewBean.getXpath();
        if (!childRealPath.toLowerCase().endsWith("." + childSimClass.toLowerCase())) {
            this.childRealPath = childRealPath.toLowerCase() + "." + childSimClass;
        }
        this.autoruncontext = new MinServerActionContextImpl(context.getHttpRequest(),context.getOgnlContext());
        autoruncontext.getParamMap().putAll(context.getPagectx());
        autoruncontext.getParamMap().putAll(context.getContext());

        if (context.getSessionId() != null) {
            autoruncontext.setSessionId(context.getSessionId());
            autoruncontext.getSession().put("sessionHandle", context.getSession().get("sessionHandle"));
        }
        autoruncontext.setSessionMap(context.getSession());
        onglContext = autoruncontext.getOgnlContext();

    }


    @Override
    public CustomModuleBean call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        String cSimClass = OODUtil.formatJavaName(childComponent.getAlias(), true);
        String cPackageName = parentClassName.toLowerCase();
        String cEuClassName = cPackageName + "." + cSimClass;
        String projectName = moduleComponent.getProjectName();
        CustomModuleBean cModuleBean = null;
        if (!childComponent.getKey().equals(ComponentType.MODULE.getType())) {
            String target = childComponent.getTarget();
            LayoutListItem currListItem = layoutViewBean.findTabItem(target);
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(moduleComponent.getProjectName(), UserSpace.VIEW);
            String domainId = domainInst.getDomainId();
            ModuleViewType comModuleViewType = ModuleViewType.getModuleViewByCom(ComponentType.fromType(childComponent.getKey()));
            ModuleComponent cmoduleComponent = new ModuleComponent(childComponent);
            CustomViewBean customViewBean = layoutViewBean.getItemViewBean(currListItem, moduleComponent.getProjectName());
            if (customViewBean == null || !customViewBean.getModuleViewType().equals(comModuleViewType)) {
                DSMProperties    cdsmProperties = new DSMProperties(customViewBean);
                ModuleProperties cmoduleProperties = new ModuleProperties();
                cmoduleProperties.setMethodName(OODUtil.getGetMethodName(childComponent.getAlias()));
                cmoduleProperties.setDsmProperties(cdsmProperties);
                cmoduleComponent.setProperties(cmoduleProperties);
                cmoduleComponent.setClassName(cEuClassName);
                customViewBean = DSMFactory.getInstance().getViewManager().getDefaultViewBean(cmoduleComponent, domainId);
                customViewBean.setDomainId(domainId);
            } else {
                DSMProperties cdsmProperties = cmoduleComponent.getProperties().getDsmProperties();
                if (cdsmProperties==null){
                    cdsmProperties = new DSMProperties(customViewBean);
                    cdsmProperties.setRealPath(childRealPath);
                    cmoduleComponent.getProperties().setDsmProperties(cdsmProperties);
                }

                cmoduleComponent.setClassName(cEuClassName);
                customViewBean.updateModule(cmoduleComponent);
            }
            customViewBean.setXpath(childRealPath);
            cModuleBean = createModuleBean(cmoduleComponent);
            cmoduleComponent.setClassName(cEuClassName);

            AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(customViewBean, cEuClassName, projectName);

            List<JavaSrcBean> serviceList = aggRootBuild.getAggServiceRootBean();
            if (serviceList == null || serviceList.isEmpty()) {
                serviceList = aggRootBuild.build();
            }
            layoutViewBean.bindItem(serviceList, currListItem);

            for (JavaSrcBean javaSrcBean : serviceList) {
                if (javaSrcBean.getTarget() != null && javaSrcBean.getTarget().equals(target)) {
                    Class bindService = ClassUtility.loadClass(javaSrcBean.getClassName());
                    customViewBean.reBindService(bindService);
                }
            }

            cModuleBean.reBindMethod(customViewBean.getMethodConfig());
            DSMFactory.getInstance().saveCustomViewBean(customViewBean);
        }
        return cModuleBean;
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

}