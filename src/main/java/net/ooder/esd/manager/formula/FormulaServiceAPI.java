package net.ooder.esd.manager.formula;

import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.annotation.MethodChinaName;
import net.ooder.common.ContextType;
import net.ooder.common.FormulaType;
import net.ooder.common.JDSException;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.esb.config.manager.ExpressionParameter;
import net.ooder.jds.core.esb.EsbUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/bpd/formula/")
@EsbBeanAnnotation(dataType = ContextType.Server)
public class FormulaServiceAPI implements FormulaService {
    @Override
    @RequestMapping(method = RequestMethod.POST, value = "GetFormulas")
    @MethodChinaName(cname = "获取表达式信息")
    public @ResponseBody
    ListResultModel<List<ParticipantSelect>> getFormulas(FormulaType type) {
        ListResultModel model = getService().getFormulas(type);
        return model;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "FormulaSearch")
    @MethodChinaName(cname = "查询表达式信息")
    public @ResponseBody
    ListResultModel<List<ParticipantSelect>> formulaSearch(FormulaType type, String forumla, String name) {
        ListResultModel model = getService().formulaSearch(type, forumla, name);
        return model;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "GetFormulaParameters")
    @MethodChinaName(cname = "获取表达式参数")
    public @ResponseBody
    ListResultModel<List<ExpressionParameter>> getFormulaParameters() {
        ListResultModel<List<ExpressionParameter>> model = new ListResultModel<List<ExpressionParameter>>();
        List<ExpressionParameter> ctparameters = new ArrayList<ExpressionParameter>();

        List<ExpressionParameter> parameters = getService().getFormulaParameters().getData();
        for (ExpressionParameter parameter : parameters) {
            ctparameters.add(new CtExpressionParameter(parameter));
        }
        model.setData(ctparameters);
        model.setSize(ctparameters.size());
        return model;
    }

    @Override
    public ResultModel<ExpressionParameter> getFormulaParameter(String parameterId) {
        return getService().getFormulaParameter(parameterId);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "getParticipantSelect")
    @MethodChinaName(cname = "获取表达式信息")
    public @ResponseBody
    ResultModel<ParticipantSelect> getParticipantSelect(String selectedId) {
        ResultModel<ParticipantSelect> model = new ResultModel<ParticipantSelect>();
        List<ExpressionParameter> ctparameters = new ArrayList<ExpressionParameter>();
        ParticipantSelect select = null;
        try {
            select = getService().getParticipantSelect(selectedId).get();
            select = new CtParticipantSelect(select);
            model.setData(select);
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return model;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "addParticipantSelect")
    @MethodChinaName(cname = "添加表达式")
    public @ResponseBody
    ResultModel<Boolean> addParticipantSelect(@RequestBody ParticipantSelect participantSelect) {
        ResultModel<Boolean> model = new ResultModel<Boolean>();
        return getService().addParticipantSelect(participantSelect);

    }


    @Override
    @RequestMapping(method = RequestMethod.POST, value = "delParticipantSelect")
    @MethodChinaName(cname = "删除表达式")
    public @ResponseBody
    ResultModel<Boolean> delParticipantSelect(String selectedId) {

        return getService().delParticipantSelect(selectedId);
    }


    @Override
    @RequestMapping(method = RequestMethod.POST, value = "addFormulaParameters")
    @MethodChinaName(cname = "添加表达式参数")
    public @ResponseBody
    ResultModel<Boolean> addFormulaParameters(@RequestBody ExpressionParameter parameter) {

        return getService().addFormulaParameters(parameter);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "delParameters")
    @MethodChinaName(cname = "删除表达式参数")
    public @ResponseBody
    ResultModel<Boolean> delParameters(String parameterId) {
        return getService().delParameters(parameterId);
    }


    public FormulaService getService() {
        FormulaService service = (FormulaService) EsbUtil.parExpression("$FormulaService");
        return service;
    }

}
