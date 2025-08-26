package net.ooder.esd.manager.editor;

import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.event.*;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.annotation.ui.ResponsePathEnum;
import net.ooder.esd.bean.bar.MenuDynBar;
import net.ooder.esd.bean.bar.PopDynBar;
import net.ooder.web.json.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = {"/action/context/"})
public interface CustomMenuAction {

    @ResponseBody
    @RequestMapping(value = {"loadMenu"}, method = {RequestMethod.POST, RequestMethod.GET})
    @APIEventAnnotation(customResponseData = ResponsePathEnum.POPMENU
    )
    public ResultModel<PopDynBar> loadMenu(@JsonData Map<String, Object> tagVar, String viewBarId, @JsonData EventPos pos);


    @ResponseBody
    @RequestMapping(value = {"loadTreeMenu"}, method = {RequestMethod.POST, RequestMethod.GET})
    @APIEventAnnotation(customResponseData = ResponsePathEnum.POPMENU,
            bindTreeEvent = CustomTreeEvent.LOADMENU
    )
    public ResultModel<PopDynBar> loadTreeMenu(@JsonData Map<String, Object> tagVar, String viewBarId, @JsonData EventPos pos);


    @ResponseBody
    @RequestMapping(value = {"loadFieldMenu"}, method = {RequestMethod.POST, RequestMethod.GET})
    @APIEventAnnotation(customResponseData = ResponsePathEnum.POPMENU,
            bindFieldEvent = CustomFieldEvent.LOADMENU)
    public ResultModel<PopDynBar> loadFieldMenu(@JsonData Map<String, Object> tagVar, String viewBarId, @JsonData EventPos pos);


    @ResponseBody
    @RequestMapping(value = {"loadGridMenu"}, method = {RequestMethod.POST, RequestMethod.GET})
    @APIEventAnnotation(customResponseData = ResponsePathEnum.POPMENU,
            bindGridEvent = CustomGridEvent.LOADMENU
    )
    public ResultModel<PopDynBar> loadGridMenu(@JsonData Map<String, Object> tagVar, String viewBarId, @JsonData EventPos pos);


    @ResponseBody
    @RequestMapping(value = {"loadGalleryMenu"}, method = {RequestMethod.POST, RequestMethod.GET})
    @APIEventAnnotation(customResponseData = ResponsePathEnum.POPMENU,
            bindGalleryEvent = CustomGalleryEvent.LOADMENU
    )
    public ResultModel<PopDynBar> loadGalleryMenu(@JsonData Map<String, Object> tagVar, String viewBarId, @JsonData EventPos pos);

    @ResponseBody
    @RequestMapping(value = {"loadTitleBlockMenu"}, method = {RequestMethod.POST, RequestMethod.GET})
    @APIEventAnnotation(customResponseData = ResponsePathEnum.POPMENU,
            bindTitleBlockEvent = CustomTitleBlockEvent.LOADMENU)
    public ResultModel<PopDynBar> loadTitleBlockMenu(@JsonData Map<String, Object> tagVar, String viewBarId, @JsonData EventPos pos);

    @ResponseBody
    @RequestMapping(value = {"loadContentBlockMenu"}, method = {RequestMethod.POST, RequestMethod.GET})
    @APIEventAnnotation(customResponseData = ResponsePathEnum.POPMENU,
            bindContentBlockEvent = CustomContentBlockEvent.LOADMENU)
    public ResultModel<PopDynBar> loadContentBlockMenu(@JsonData Map<String, Object> tagVar, String viewBarId, @JsonData EventPos pos);

    @RequestMapping(value = "dynMenu.dyn")
    @APIEventAnnotation(customRequestData = RequestPathEnum.CTX, bindFieldEvent = CustomFieldEvent.DYNRELOAD, autoRun = true)
    @ResponseBody
    public ResultModel<MenuDynBar> dynMenu(String viewBarId, String alias, String sourceClassName, String domainId, String methodName);


}
