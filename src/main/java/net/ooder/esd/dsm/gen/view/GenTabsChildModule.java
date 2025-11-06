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

public class GenTabsChildModule implements Callable<CustomModuleBean> {
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
    public CustomModuleBean call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        String cSimClass = OODUtil.formatJavaName(childComponent.getAlias(), true);
        String cPackageName = parentClassName.toLowerCase();
        String cEuClassName = cPackageName + "." + cSimClass;
        String projectName = moduleComponent.getProjectName();
        CustomModuleBean cModuleBean = null;
        if (!childComponent.getKey().equals(ComponentType.MODULE.getType())) {
            String target = childComponent.getTarget();
            TabListItem currListItem = this.tabsViewBean.findTabItem(target, tabsViewBean.getTabItems());
            DomainInst domainInst = DSMFactory.getInstance().getDefaultDomain(moduleComponent.getProjectName(), UserSpace.VIEW);
            DSMProperties dsmProperties = moduleComponent.getProperties().getDsmProperties();
            String domainId = domainInst.getDomainId();
            ModuleViewType comModuleViewType = ModuleViewType.getModuleViewByCom(ComponentType.fromType(childComponent.getKey()));
            ModuleComponent cmoduleComponent = new ModuleComponent(childComponent);
            CustomViewBean customViewBean = tabsViewBean.getItemViewBean(currListItem, moduleComponent.getProjectName());
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
            AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(customViewBean, cEuClassName, projectName);
            List<JavaSrcBean> serviceList = aggRootBuild.getAggServiceRootBean();
            if (serviceList == null || serviceList.isEmpty()) {
                serviceList = aggRootBuild.build();
            }
            bindItem(serviceList, currListItem);
            for (JavaSrcBean javaSrcBean : serviceList) {
                if (javaSrcBean.getTarget() != null && javaSrcBean.getTarget().equals(target)) {
                    Class bindService = ClassUtility.loadClass(javaSrcBean.getClassName());
                    customViewBean.reBindService(bindService);
                }
            }

            cModuleBean.setJavaSrcBeans(aggRootBuild.getAllSrcBean());
            cModuleBean.reBindMethod(customViewBean.getMethodConfig());
            DSMFactory.getInstance().saveCustomViewBean(customViewBean);
        }
        return cModuleBean;
    }


    protected Set<MethodConfig> bindItem(List<JavaSrcBean> javaSrcBeanList, TabListItem currListItem) {
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
        for (JavaSrcBean srcBean : javaSrcBeanList) {
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