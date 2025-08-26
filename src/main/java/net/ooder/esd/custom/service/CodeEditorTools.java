package net.ooder.esd.custom.service;

import net.ooder.config.ResultModel;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esd.annotation.field.ComboInputAnnotation;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.event.ActionTypeEnum;
import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.UserSpace;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Aggregation(type=AggregationType.MENU,userSpace = UserSpace.SYS)
public class CodeEditorTools {


    @MethodChinaName(cname = "")
    @APIEventAnnotation(
            bindAction = {@CustomAction(name = "searchreplaceAction",method = "call",type = ActionTypeEnum.other, target = "callback", args = {"{page.content.showFindWnd()}"},  _return = true)}
    )
    @RequestMapping(method = RequestMethod.POST, value = "searchreplaceAction")
    @ComboInputAnnotation(inputType = ComboInputType.button)
    @CustomAnnotation(index = 2, caption = "查找/替换", tips = "查找/替换", imageClass = "spafont spa-icon-searchreplace")
    public @ResponseBody
    ResultModel<Boolean> searchreplaceAction(String content, String path) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        return result;
    }

    @MethodChinaName(cname = "")
    @RequestMapping(method = RequestMethod.POST, value = "jumptoAction")
    @ComboInputAnnotation(inputType = ComboInputType.button)
    @CustomAnnotation(index = 3, caption = "跳到行", tips = "跳到行", imageClass = "spafont spa-icon-jumpto")
    @APIEventAnnotation(
            bindAction = {@CustomAction(name = "jumptoAction",method = "call", type = ActionTypeEnum.other, target = "callback", args = {"{page.content.showJumpToWnd()}", "undefined", "undefined", "{args[2]}"}, _return = false)}
    )
    public @ResponseBody
    ResultModel<Boolean> jumptoAction(String content, String path) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        return result;
    }

    @MethodChinaName(cname = "")
    @RequestMapping(method = RequestMethod.POST, value = "reindentAction")
    @ComboInputAnnotation(inputType = ComboInputType.button)
    @CustomAnnotation(index = 4, caption = "调整缩进", tips = "调整缩进", imageClass = "spafont spa-icon-indent")
    @APIEventAnnotation(
            bindAction = {@CustomAction(name = "reindentAction",method = "call", type = ActionTypeEnum.other, target = "callback", args = {"{page.content.reindent()}"},  _return = false)}
    )
    public @ResponseBody
    ResultModel<Boolean> reindentAction(String content, String path) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        return result;
    }


}
