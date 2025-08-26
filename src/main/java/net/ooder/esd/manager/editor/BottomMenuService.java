package net.ooder.esd.manager.editor;

import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.bean.BottomBarMenuBean;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.component.CustomBottomBar;
import net.ooder.esd.dsm.DSMFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = {"/action/context/"})
public class BottomMenuService {

    public BottomMenuService() {

    }


    @RequestMapping(value = "dynButtomMenu.dyn")
    @APIEventAnnotation(customRequestData = RequestPathEnum.CTX, bindFieldEvent = CustomFieldEvent.DYNRELOAD, autoRun = true)
    @ResponseBody
    public ResultModel<CustomBottomBar> dynButtomMenu(String viewBarId, String alias, String sourceClassName, String domainId, String methodName) {
        ResultModel<CustomBottomBar> dynBarResultModel = new ResultModel();
        try {
            CustomBottomBar viewBar = null;
            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceClassName);
            MethodConfig methodAPIBean = apiClassConfig.getMethodByName(methodName);
            BottomBarMenuBean menuBarBean = methodAPIBean.getView().getBottomBar();
            if (menuBarBean != null) {
                viewBar = PluginsFactory.getInstance().fillDynBottomBar(menuBarBean);
            } else {
                viewBar = PluginsFactory.getInstance().getViewBarById(viewBarId, CustomBottomBar.class, true);
                viewBar.setAlias(alias);
            }
            dynBarResultModel.setData(viewBar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dynBarResultModel;
    }

}
