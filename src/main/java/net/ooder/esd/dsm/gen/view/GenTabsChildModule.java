package net.ooder.esd.dsm.gen.view;

import net.ooder.annotation.UserSpace;
import net.ooder.common.util.ClassUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.CustomViewBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.BaseTabsViewBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.ModuleProperties;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.esd.util.OODUtil;
import net.ooder.server.context.MinServerActionContextImpl;
import net.ooder.web.RequestParamBean;
import ognl.OgnlContext;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class GenTabsChildModule implements Callable<AggRootBuild> {
    private MinServerActionContextImpl autoruncontext;
    private OgnlContext onglContext;
    Component childComponent;
    ModuleComponent moduleComponent;
    BaseTabsViewBean tabsViewBean;
    String parentClassName;
    String childRealPath;

    public GenTabsChildModule(ModuleComponent moduleComponent, Component childComponent, BaseTabsViewBean tabsViewBean) {
        JDSContext context = JDSActionContext.getActionContext();
        this.moduleComponent = moduleComponent;
        this.childComponent = childComponent;
        this.parentClassName = moduleComponent.getClassName();
        this.tabsViewBean = tabsViewBean;
        String simClass = OODUtil.formatJavaName(moduleComponent.getAlias(), true);
        if (!parentClassName.endsWith("." + simClass)) {
            this.parentClassName = parentClassName.toLowerCase() + "." + simClass;
        }
        String childSimClass = OODUtil.formatJavaName(childComponent.getAlias(), true);

        childRealPath = tabsViewBean.getXpath();
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

    }

    @Override
    public AggRootBuild call() throws Exception {
        AggRootBuild aggRootBuild = null;
        JDSActionContext.setContext(autoruncontext);
        String projectName = moduleComponent.getProjectName();
        CustomModuleBean cModuleBean = null;
        CustomViewBean customViewBean = null;
        String cEuClassName = null;

        String target = childComponent.getTarget();
        TabListItem currListItem = this.tabsViewBean.findTabItem(target, tabsViewBean.getTabItems());
        DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(moduleComponent.getProjectName(), UserSpace.VIEW);
        DSMProperties dsmProperties = moduleComponent.getProperties().getDsmProperties();
        String domainId = domainInst.getDomainId();

        if (childComponent instanceof ModuleComponent) {
            ModuleComponent cmoduleComponent = (ModuleComponent) childComponent;
            cEuClassName = cmoduleComponent.getClassName();
            if (cmoduleComponent != null && cmoduleComponent.getMethodAPIBean() != null) {
                cModuleBean = cmoduleComponent.getMethodAPIBean().getModuleBean();
            } else {
                customViewBean = DSMFactory.getInstance().getViewManager().getDefaultViewBean(cmoduleComponent, domainId);
                customViewBean.setDomainId(domainId);

                if (customViewBean.getMethodConfig() == null) {
                    customViewBean.updateModule(cmoduleComponent);
                }
                cModuleBean = createModuleBean(cmoduleComponent);
                cmoduleComponent.setClassName(cEuClassName);
            }
        } else {

            String cSimClass = OODUtil.formatJavaName(childComponent.getAlias(), true);
            String cPackageName = parentClassName.toLowerCase();
            cEuClassName = cPackageName + "." + cSimClass;
            ModuleViewType comModuleViewType = ModuleViewType.getModuleViewByCom(ComponentType.fromType(childComponent.getKey()));
            ModuleComponent cmoduleComponent = new ModuleComponent(childComponent);
            customViewBean = tabsViewBean.getItemViewBean(currListItem, moduleComponent.getProjectName());
            if (customViewBean == null || !customViewBean.getModuleViewType().equals(comModuleViewType)) {
                DSMProperties cdsmProperties = new DSMProperties();
                ModuleProperties cmoduleProperties = new ModuleProperties();
                if (dsmProperties != null) {
                    cdsmProperties.setDomainId(dsmProperties.getDomainId());
                }
                cdsmProperties.setRealPath(childRealPath);
                cmoduleProperties.setMethodName(OODUtil.getGetMethodName(childComponent.getAlias()));
                cmoduleProperties.setDsmProperties(cdsmProperties);
                cmoduleComponent.setProperties(cmoduleProperties);
                cmoduleComponent.setClassName(cEuClassName);
                customViewBean = DSMFactory.getInstance().getViewManager().getDefaultViewBean(cmoduleComponent, domainId);
                customViewBean.setDomainId(domainId);
            } else {
                DSMProperties cdsmProperties = cmoduleComponent.getProperties().getDsmProperties();
                if (cdsmProperties == null) {
                    cdsmProperties = new DSMProperties();
                    if (dsmProperties != null) {
                        cdsmProperties.setDomainId(dsmProperties.getDomainId());
                    }
                    cmoduleComponent.getProperties().setDsmProperties(cdsmProperties);
                }
                cdsmProperties.setRealPath(childRealPath);
                cmoduleComponent.setClassName(cEuClassName);
                customViewBean.updateModule(cmoduleComponent);
            }
            customViewBean.setXpath(childRealPath);
            cModuleBean = createModuleBean(cmoduleComponent);
            cmoduleComponent.setClassName(cEuClassName);

        }

        if (customViewBean != null) {
            aggRootBuild = BuildFactory.getInstance().getAggRootBuild(customViewBean, cEuClassName, projectName);
            List<JavaGenSource> serviceList = aggRootBuild.getAggServiceRootBean();
            if (aggRootBuild.getAggServiceRootBean().isEmpty() || aggRootBuild.getRepositorySrcList().isEmpty() || aggRootBuild.getViewSrcList().isEmpty()) {
                serviceList = aggRootBuild.build();
            }
            bindItem(serviceList, currListItem);
            for (JavaGenSource genSource : serviceList) {
                JavaSrcBean javaSrcBean = genSource.getSrcBean();
                if (javaSrcBean.getTarget() == null || javaSrcBean.getTarget().equals(target)) {
                    Class bindService = ClassUtility.loadClass(javaSrcBean.getClassName());
                    customViewBean.reBindService(bindService);
                }
            }
            cModuleBean.setJavaSrcBeans(aggRootBuild.getAllSrcBean());
            cModuleBean.reBindMethod(customViewBean.getMethodConfig());
            aggRootBuild.update();
        }


        return aggRootBuild;
    }


    protected Set<MethodConfig> bindItem(List<JavaGenSource> javaSrcBeanList, TabListItem currListItem) {
        List<Class> classList = new ArrayList<>();
        if (currListItem.getBindClass() != null && currListItem.getBindClass().length > 0) {
            for (Class clazz : currListItem.getBindClass()) {
                try {
                    if (clazz != null) {
                        classList.add(ClassUtility.loadClass(clazz.getName()));
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        Set<MethodConfig> methodConfigs = new HashSet<>();
        for (JavaGenSource srcBean : javaSrcBeanList) {
            try {
                MethodConfig editorMethod = this.tabsViewBean.findEditorMethod(srcBean.getClassName());
                if (editorMethod != null) {
                    currListItem.setEuClassName(editorMethod.getEUClassName());
                    RequestParamBean[] requestParamBeanArr = (RequestParamBean[]) editorMethod.getParamSet().toArray(new RequestParamBean[]{});
                    currListItem.fillParams(requestParamBeanArr, null);
                    methodConfigs.add(editorMethod);
                    Class clazz = ClassUtility.loadClass(srcBean.getClassName());
                    if (!classList.contains(clazz)) {
                        classList.add(clazz);
                    }
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        currListItem.setBindClass(classList.toArray(new Class[]{}));
        return methodConfigs;
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