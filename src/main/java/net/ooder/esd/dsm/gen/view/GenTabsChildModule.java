package net.ooder.esd.dsm.gen.view;

import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.view.BaseTabsViewBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.web.RequestParamBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GenTabsChildModule extends BaseGenChildModule<BaseTabsViewBean> {
    private final TabListItem currListItem;
    private final String projectName;


    public GenTabsChildModule(ModuleComponent moduleComponent, Component childComponent, BaseTabsViewBean tabsViewBean) {
        super(moduleComponent, childComponent, tabsViewBean);
        currListItem = tabsViewBean.findTabItem(target, tabsViewBean.getTabItems());
        projectName = moduleComponent.getProjectName();

    }

    @Override
    public AggRootBuild genAggBuild() throws JDSException {
        AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(customViewBean, cEuClassName, projectName);
        return aggRootBuild;
    }

    @Override
    public List<JavaGenSource> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        AggRootBuild aggRootBuild = genAggBuild();
        if (customViewBean != null) {
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
        return aggRootBuild.getAllGenBean();
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
                MethodConfig editorMethod = this.customViewBean.findEditorMethod(srcBean.getClassName());
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


}