package net.ooder.esd.manager.formula;

import net.ooder.annotation.FormulaParams;
import net.ooder.annotation.MethodChinaName;
import net.ooder.esb.config.manager.ExpressionParameter;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.FieldAnnotation;
import net.ooder.esd.annotation.FormAnnotation;
import net.ooder.esd.annotation.GridAnnotation;
import net.ooder.esd.annotation.event.CustomGridEvent;
import net.ooder.esd.annotation.field.TextEditorAnnotation;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.menu.GridMenu;

@GridAnnotation(customMenu = {GridMenu.ADD, GridMenu.DELETE, GridMenu.RELOAD},
        event = CustomGridEvent.EDITOR)
@FormAnnotation(bottombarMenu = {CustomFormMenu.SAVE, CustomFormMenu.CLOSE})
public class CtExpressionParameter implements ExpressionParameter {
    @CustomAnnotation(hidden = true, uid = true)
    private String parameterId;

    @CustomAnnotation(hidden = true, pid = true)
    private String participantSelectId;

    @CustomAnnotation(hidden = true)
    private String parameterenName;

    @MethodChinaName(cname = "参数代码")
    private String parameterCode;

    @MethodChinaName(cname = "参数名称")
    private String parameterName;

    @MethodChinaName(cname = "参数类型")
    @CustomAnnotation()
    private FormulaParams parameterType;

    @MethodChinaName(cname = "单选")
    @CustomAnnotation()
    private Boolean single = false;

    @MethodChinaName(cname = "参数值")
    @TextEditorAnnotation()
    @FieldAnnotation(colSpan = -1)
    @CustomAnnotation
    private String value;


    @CustomAnnotation(hidden = true)
    private String parameterValue;

    @MethodChinaName(cname = "参数描述")
    @TextEditorAnnotation()
    @FieldAnnotation(colSpan = -1)
    private String parameterDesc;

    public CtExpressionParameter() {

    }

    public CtExpressionParameter(ExpressionParameter expressionParameter) {
        this.parameterValue = expressionParameter.getParameterValue();
        this.value = expressionParameter.getValue();
        this.parameterId = expressionParameter.getParameterId();
        this.participantSelectId = expressionParameter.getParticipantSelectId();
        this.parameterCode = expressionParameter.getParameterCode();
        this.parameterName = expressionParameter.getParameterName();
        this.parameterenName = expressionParameter.getParameterenName();
        this.parameterDesc = expressionParameter.getParameterDesc();
        this.parameterType = expressionParameter.getParameterType();
        if (expressionParameter.getSingle() != null) {
            this.single = expressionParameter.getSingle();
        }


    }


    /**
     * @return Returns the parameterCode.
     */
    public String getParameterCode() {
        return parameterCode;
    }

    /**
     * @param parameterCode The parameterCode to set.
     */
    public void setParameterCode(String parameterCode) {
        this.parameterCode = parameterCode;
    }

    /**
     * @return Returns the parameterDesc.
     */
    public String getParameterDesc() {
        return parameterDesc;
    }

    /**
     * @param parameterDesc The parameterDesc to set.
     */
    public void setParameterDesc(String parameterDesc) {
        this.parameterDesc = parameterDesc;
    }

    /**
     * @return Returns the parameterId.
     */
    public String getParameterId() {
        return parameterId;
    }

    /**
     * @param parameterId The parameterId to set.
     */
    public void setParameterId(String parameterId) {
        this.parameterId = parameterId;
    }

    /**
     * @return Returns the parameterName.
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * @param parameterName The parameterName to set.
     */
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    /**
     * @return Returns the parameterType.
     */
    public FormulaParams getParameterType() {
        return parameterType;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {

        this.value = value;
    }

    /**
     * @param parameterType The parameterType to set.
     */
    public void setParameterType(FormulaParams parameterType) {
        this.parameterType = parameterType;
    }

    @Override
    public String getParameterValue() {
        return parameterValue;
    }

    @Override
    public void setParameterValue(String parameterValue) {

        this.parameterValue = parameterValue;
    }

    /**
     * @return Returns the participantId.
     */
    public String getParticipantSelectId() {
        return participantSelectId;
    }

    /**
     * @param participantSelectId The participantId to set.
     */
    public void setParticipantSelectId(String participantSelectId) {
        this.participantSelectId = participantSelectId;

    }

    public Boolean getSingle() {
        return single;
    }

    public void setSingle(Boolean single) {
        this.single = single;
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof CtExpressionParameter) {
            CtExpressionParameter parameter = (CtExpressionParameter) o;
            if (this.parameterCode == null) {
                return false;
            } else {
                return this.parameterCode.equalsIgnoreCase(parameter.getParameterCode());
            }
        } else if (o instanceof String) {
            String id = (String) o;
            if (this.parameterCode == null) {
                return false;
            } else {
                return this.parameterCode.equalsIgnoreCase(id);
            }
        } else {
            return false;
        }
    }

    public String getParameterenName() {
        return parameterenName;
    }

    public void setParameterenName(String parameterenName) {
        this.parameterenName = parameterenName;
    }
}
