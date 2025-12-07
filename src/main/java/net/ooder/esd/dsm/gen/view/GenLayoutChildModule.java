package net.ooder.esd.dsm.gen.view;

import net.ooder.common.JDSException;
import net.ooder.common.util.ClassUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.view.CustomLayoutViewBean;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.item.LayoutListItem;

import java.util.List;

public class GenLayoutChildModule extends BaseGenChildModule<CustomLayoutViewBean> {
    private final LayoutListItem currListItem;
    private final String projectName;

    @Override
    public AggRootBuild genAggBuild() throws JDSException {
        AggRootBuild aggRootBuild = BuildFactory.getInstance().getAggRootBuild(customViewBean, cEuClassName, projectName);
        return aggRootBuild;
    }

    public GenLayoutChildModule(ModuleComponent moduleComponent, Component childComponent, CustomLayoutViewBean layoutViewBean) {
        super(moduleComponent, childComponent, layoutViewBean);
        this.currListItem = layoutViewBean.findTabItem(target);
        projectName = moduleComponent.getProjectName();
    }


    @Override
    public List<JavaGenSource> call() throws Exception {
        AggRootBuild aggRootBuild = genAggBuild();
        JDSActionContext.setContext(autoruncontext);
        if (!childComponent.getKey().equals(ComponentType.MODULE.getType())) {
            List<JavaGenSource> serviceList = aggRootBuild.getAggServiceRootBean();
            if (serviceList == null || serviceList.isEmpty()) {
                serviceList = aggRootBuild.build();
            }
            customViewBean.bindItem(serviceList, currListItem);
            for (JavaGenSource genSource : serviceList) {
                JavaSrcBean javaSrcBean = genSource.getSrcBean();
                if (javaSrcBean.getTarget() != null && javaSrcBean.getTarget().equals(target)) {
                    Class bindService = ClassUtility.loadClass(javaSrcBean.getClassName());
                    customViewBean.reBindService(bindService);
                }
            }
            cmoduleBean.reBindMethod(customViewBean.getMethodConfig());
        }
        return aggRootBuild.getAllGenBean();
    }

    public LayoutListItem getCurrListItem() {
        return currListItem;
    }
}