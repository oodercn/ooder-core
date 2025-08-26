package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.JSON;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.bean.field.base.RichEditorFieldBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.RichEditorComponent;
import net.ooder.esd.tool.properties.form.RichEditorProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.Map;

public class CustomRichEditorComponent extends RichEditorComponent {

    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomRichEditorComponent.class);


    public CustomRichEditorComponent(EUModule euModule, FieldFormConfig<RichEditorFieldBean, ?> field, String target, Object value, Map valueMap) {
        this.setTarget(target);
        RichEditorFieldBean richEditorFieldBean = field.getWidgetConfig();
        if (value != null) {
            if (value instanceof RichEditorFieldBean) {
                String json = JSON.toJSONString(value);
                String obj = (String) TemplateRuntime.eval(json, value);
                OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), richEditorFieldBean, false, false);
            }
        }
        RichEditorProperties properties = new RichEditorProperties(richEditorFieldBean, field.getContainerBean());
        if (value != null) {
            if (value instanceof String && !value.equals("")) {
                properties.setCaption((String) value);
            }
        } else if (valueMap != null) {
            value = valueMap.get(field.getFieldname());
            if (value != null && !value.equals("")) {
                properties.setCaption((String) value);
            }
        }

        this.setProperties(properties);
        this.initContextMenu(euModule, field, FieldEventEnum.onContextmenu);
        this.initEditor(euModule, field, FieldEventEnum.onChange);
        this.initEvent(euModule, field);

    }


}
