package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.JSON;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.bean.field.base.TextEditorFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.InputComponent;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomMultiLinsInputComponent extends InputComponent {
    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomMultiLinsInputComponent.class);


    public CustomMultiLinsInputComponent(EUModule euModule, FieldFormConfig<TextEditorFieldBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getFieldname());
        TextEditorFieldBean textEditorFieldBean = field.getWidgetConfig();

        if (value != null) {
            if (value instanceof TextEditorFieldBean) {
                String json = JSON.toJSONString(value);
                String obj = (String) TemplateRuntime.eval(json, value);
                OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), textEditorFieldBean, false, false);
            } else if (value instanceof String && !value.equals("")) {
                this.getProperties().setValue(value);
            }
        }


        this.getProperties().setId(field.getId());
        this.getProperties().setName(field.getFieldname());
        this.getProperties().setValue(value);

        if (field.getCustomBean() != null) {
            this.getProperties().setDesc(field.getCustomBean().getCaption());
            this.getProperties().setCaption(field.getCustomBean().getCaption());
            this.getProperties().setReadonly(field.getCustomBean().getReadonly());
            this.getProperties().setDisabled(field.getCustomBean().getDisabled());

        } else {
            this.getProperties().setCaption(field.getAggConfig().getCaption());
            this.getProperties().setDesc(field.getAggConfig().getCaption());
            this.getProperties().setReadonly(field.getAggConfig().getReadonly());
            this.getProperties().setDisabled(field.getAggConfig().getDisabled());

        }


        this.getProperties().setDynCheck(field.getFieldBean().getDynCheck());
        this.getProperties().setMultiLines(true);
        this.setTarget(target);
        this.getProperties().setDynLoad(textEditorFieldBean.getDynLoad());
        this.initEvent(euModule, field);
        initContextMenu(euModule, field, FieldEventEnum.onContextmenu);
        initEditor(euModule, field, FieldEventEnum.onChange);

    }


}
