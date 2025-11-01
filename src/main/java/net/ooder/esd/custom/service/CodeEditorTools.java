package net.ooder.esd.custom.service;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.MethodChinaName;
import net.ooder.annotation.UserSpace;
import net.ooder.config.ResultModel;
import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.ComboInputAnnotation;
import net.ooder.esd.annotation.ui.ComboInputType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Aggregation(type = AggregationType.MENU, userSpace = UserSpace.SYS)
public class CodeEditorTools {


    @MethodChinaName(cname = "")
    @APIEventAnnotation(
            bindAction = {@CustomAction(script = "page.content.showFindWnd()", params = {"{arg[0]}"})}
    )
    @RequestMapping(method = RequestMethod.POST, value = "searchreplaceAction")
    @ComboInputAnnotation(inputType = ComboInputType.button)
    @CustomAnnotation(index = 1, caption = "查找替换", tips = "查找替换", imageClass = "ri-search-line")
    public @ResponseBody
    ResultModel<Boolean> searchreplaceAction(String content, String path) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        return result;
    }

    @MethodChinaName(cname = "")
    @RequestMapping(method = RequestMethod.POST, value = "jumptoAction")
    @ComboInputAnnotation(inputType = ComboInputType.button)
    @APIEventAnnotation(
            bindAction = {@CustomAction(script = "page.content.showJumpToWnd()", params = {"{arg[0]}"})}
    )
    @CustomAnnotation(index = 2, caption = "跳到行", tips = "跳到行", imageClass = "spafont spa-icon-jumpto")
    public @ResponseBody
    ResultModel<Boolean> jumptoAction(String content, String path) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        return result;
    }

    @MethodChinaName(cname = "")
    @RequestMapping(method = RequestMethod.POST, value = "reindentAction")
    @ComboInputAnnotation(inputType = ComboInputType.button)
    @APIEventAnnotation(
            bindAction = {@CustomAction(script = "page.content.reindent()")}
    )
    @CustomAnnotation(index = 3, caption = "调整缩进", tips = "调整缩进", imageClass = "fas-indent")
    public @ResponseBody
    ResultModel<Boolean> reindentAction(String content, String path) {
        ResultModel<Boolean> result = new ResultModel<Boolean>();

        return result;
    }


}
