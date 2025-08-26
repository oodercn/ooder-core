package net.ooder.esd.manager.editor;

import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.config.ResultModel;
import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.esd.annotation.event.EventPos;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.bar.MenuDynBar;
import net.ooder.esd.bean.bar.PopDynBar;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.component.CustomMenusBar;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.engine.enums.MenuBarBean;
import net.ooder.web.json.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.Set;

@Controller
@EsbBeanAnnotation
@RequestMapping(value = {"/action/context/"})
public class MenuActionService implements CustomMenuAction {
    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, MenuActionService.class);

    public MenuActionService() {

    }


    @ResponseBody
    @RequestMapping(value = {"loadMenu"}, method = {RequestMethod.POST, RequestMethod.GET})
    public ResultModel<PopDynBar> loadMenu(@JsonData Map<String, Object> tagVar, String viewBarId, @JsonData EventPos pos) {
        ResultModel<PopDynBar> resultModel = new ResultModel<PopDynBar>();
        try {
            PopDynBar viewBar = PluginsFactory.getInstance().getViewBarById(viewBarId, PopDynBar.class, true);
            Set<String> keySet = tagVar.keySet();
            for (String key : keySet) {
                viewBar.addTag(key, tagVar.get(key));
            }
            viewBar.setPos(pos);
            resultModel.setData(viewBar);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return resultModel;
    }


    @Override
    public ResultModel<PopDynBar> loadTreeMenu(@JsonData Map<String, Object> tagVar, String viewBarId, @JsonData EventPos pos) {
        return loadMenu(tagVar, viewBarId, pos);
    }

    @Override
    public ResultModel<PopDynBar> loadFieldMenu(@JsonData Map<String, Object> tagVar, String viewBarId, @JsonData EventPos pos) {
        return loadMenu(tagVar, viewBarId, pos);
    }

    @Override
    public ResultModel<PopDynBar> loadGridMenu(@JsonData Map<String, Object> tagVar, String viewBarId, @JsonData EventPos pos) {
        return loadMenu(tagVar, viewBarId, pos);
    }

    @Override
    public ResultModel<PopDynBar> loadGalleryMenu(@JsonData Map<String, Object> tagVar, String viewBarId, @JsonData EventPos pos) {
        return loadMenu(tagVar, viewBarId, pos);
    }

    @Override
    public ResultModel<PopDynBar> loadTitleBlockMenu(@JsonData Map<String, Object> tagVar, String viewBarId, @JsonData EventPos pos) {
        return loadMenu(tagVar, viewBarId, pos);
    }

    @Override
    public ResultModel<PopDynBar> loadContentBlockMenu(@JsonData Map<String, Object> tagVar, String viewBarId, @JsonData EventPos pos) {
        return loadMenu(tagVar, viewBarId, pos);
    }

    @ResponseBody
    public ResultModel<MenuDynBar> dynMenu(String viewBarId, String alias, String sourceClassName, String domainId, String methodName) {
        ResultModel<MenuDynBar> dynBarResultModel = new ResultModel();
        try {
            MenuDynBar viewBar = null;
            ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(sourceClassName);
            MethodConfig methodAPIBean = apiClassConfig.getMethodByName(methodName);
            MenuBarBean menuBarBean = methodAPIBean.getView().getMenuBar();
            if (menuBarBean != null) {
                viewBar = PluginsFactory.getInstance().fillDynMenu(menuBarBean);
            } else {
                viewBar = PluginsFactory.getInstance().getViewBarById(viewBarId, CustomMenusBar.class, true);
                viewBar.setAlias(alias);
            }
            dynBarResultModel.setData(viewBar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dynBarResultModel;
    }

}
