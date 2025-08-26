package net.ooder.esd.manager.formula;

import net.ooder.common.FormulaType;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.esb.config.manager.ExpressionParameter;

import java.util.List;

public interface FormulaService {


    /**
     * @param selectedId
     * @return
     */
    ResultModel<ParticipantSelect> getParticipantSelect(String selectedId);


    /**
     * 添加公式
     *
     * @return
     */
    ResultModel<Boolean> addParticipantSelect(ParticipantSelect participantSelect);


    /**
     * 删除公式
     *
     * @return
     */
    ResultModel<Boolean> delParticipantSelect(String participantSelectId);


    /**
     * 获取公式参数
     *
     * @return
     */
    public ResultModel<Boolean> addFormulaParameters(ExpressionParameter parameter);

    /**
     * 获取公式参数
     *
     * @return
     */
    public ResultModel<Boolean> delParameters(String parameterId);

    /**
     * 获取公式列表
     *
     * @param type
     * @return
     */
    public ListResultModel<List<ParticipantSelect>> getFormulas(FormulaType type);

    /**
     * 获取公式列表
     *
     * @param type
     * @return
     */
    public ListResultModel<List<ParticipantSelect>> formulaSearch(FormulaType type, String forumla, String name);

    /**
     * 获取公式参数
     *
     * @return
     */
    public ListResultModel<List<ExpressionParameter>> getFormulaParameters();


    public ResultModel<ExpressionParameter> getFormulaParameter(String parameterId);
}
