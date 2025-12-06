package net.ooder.esd.dsm.gen.view;

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


    public GenLayoutChildModule(ModuleComponent moduleComponent, Component childComponent, CustomLayoutViewBean layoutViewBean) {
        super(moduleComponent, childComponent, layoutViewBean);
        this.currListItem = layoutViewBean.findTabItem(target);

    }


    @Override
    public AggRootBuild call() throws Exception {
        AggRootBuild aggRootBuild = null;
        JDSActionContext.setContext(autoruncontext);
        String projectName = moduleComponent.getProjectName();
        if (!childComponent.getKey().equals(ComponentType.MODULE.getType())) {
            aggRootBuild = BuildFactory.getInstance().getAggRootBuild(customViewBean, cEuClassName, projectName);
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
            cModuleBean.reBindMethod(customViewBean.getMethodConfig());
        }
        return aggRootBuild;
    }

    public LayoutListItem getCurrListItem() {
        return currListItem;
    }
}