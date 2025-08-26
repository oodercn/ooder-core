package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.JSON;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.bean.field.base.TextEditorFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.RichEditorComponent;
import net.ooder.esd.tool.properties.form.RichEditorProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomTextEditorComponent extends RichEditorComponent {

    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomTextEditorComponent.class);

    public CustomTextEditorComponent(EUModule euModule, FieldFormConfig<TextEditorFieldBean, ?> field, String target, Object value, Map valueMap) {
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
        RichEditorProperties richEditorProperties = new RichEditorProperties(textEditorFieldBean,field.getContainerBean());

        richEditorProperties.setId(field.getId());
        richEditorProperties.setName(field.getFieldname());

        if (field.getCustomBean() != null) {
            richEditorProperties.setDesc(field.getCustomBean().getCaption());
            richEditorProperties.setReadonly(field.getCustomBean().getReadonly());
            richEditorProperties.setDisabled(field.getCustomBean().getDisabled());
        } else {
            richEditorProperties.setReadonly(field.getAggConfig().getReadonly());
            richEditorProperties.setDisabled(field.getAggConfig().getDisabled());
        }


        if (field.getCustomBean() != null) {
            richEditorProperties.setDynCheck(field.getFieldBean().getDynCheck());

        }
        richEditorProperties.setTextType("text");
        richEditorProperties.setCmdFilter(textEditorFieldBean.getCmdFilter());
        richEditorProperties.setCmdList(textEditorFieldBean.getCmdList());
        richEditorProperties.setDynLoad(textEditorFieldBean.getDynLoad());
        richEditorProperties.setLabelPos(textEditorFieldBean.getLabelPos());
        richEditorProperties.setEnableBar(textEditorFieldBean.getEnableBar());
        this.setTarget(target);
        this.setProperties(richEditorProperties);

        initContextMenu(euModule, field, FieldEventEnum.onContextmenu);
        initEditor(euModule, field, FieldEventEnum.onChange);
        this.initEvent(euModule, field);
    }


}
