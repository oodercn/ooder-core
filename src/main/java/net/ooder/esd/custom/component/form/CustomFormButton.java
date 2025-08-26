package net.ooder.esd.custom.component.form;


import net.ooder.esd.annotation.CustomAction;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.custom.action.CustomConditionAction;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ButtonComponent;
import net.ooder.jds.core.esb.EsbUtil;


public class CustomFormButton extends ButtonComponent {

    public CustomFormButton(EUModule euModule, FieldFormConfig field, String target, Object value) {
        super(field.getId());
        this.getProperties().setId(field.getId());
        this.getProperties().setName(field.getFieldname());
        this.getProperties().setDesc(field.getAggConfig().getCaption());
        this.setTarget(target);

    }


    public CustomFormButton(CustomFormMenu type) {
        super(type.getType() + "buttom");
        this.getProperties().setName(type.getType());
        this.getProperties().setCaption(type.caption());
        this.getProperties().setImageClass(type.getImageClass());

        CustomAction[] actionTypes = type.getActions();
        for (CustomAction actionType : actionTypes) {
            try {
                if (EsbUtil.parExpression(actionType.expression(), Boolean.class)) {
                    CustomConditionAction action = new CustomConditionAction(actionType, type,FieldEventEnum.onClick);
                    this.addAction(  action);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                // log.error("expression err[" + actionType.getExpression() + "]");
            }
        }
    }


}

