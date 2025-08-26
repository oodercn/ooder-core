package net.ooder.esd.custom.component.form.field.combo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.bean.bar.DynBar;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.ToolBarMenuBean;
import net.ooder.esd.bean.field.base.RichEditorFieldBean;
import net.ooder.esd.custom.component.CustomToolsBar;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.manager.editor.PluginsFactory;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.RichEditorComponent;
import net.ooder.esd.tool.properties.BlockProperties;
import net.ooder.esd.tool.properties.ToolBarProperties;
import net.ooder.esd.tool.properties.form.RichEditorProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.List;
import java.util.Map;

public class CustomComboRichEditorComponent extends BlockComponent {

    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomComboRichEditorComponent.class);


    @JSONField(serialize = false)
    public ToolBarMenuBean toolBar;


    public CustomComboRichEditorComponent(EUModule euModule, FieldFormConfig<RichEditorFieldBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getFieldname() + ComponentType.BLOCK.getType(), new BlockProperties(ComponentType.CODEEDITOR));
        this.setTarget(target);
        this.getProperties().setComboType(ComponentType.CODEEDITOR);
        RichEditorComponent codeEditorComponent = new RichEditorComponent(field.getFieldname());
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


        if (field.getWidgetConfig().getToolBar() != null) {
            this.toolBar = richEditorFieldBean.getToolBar();
            this.toolBar.setId(field.getFieldname() + "_" + toolBar.getId());
            this.toolBar.setGroupId(field.getFieldname() + "_" + ComponentType.TOOLBAR.getType());
            Class<DynBar>[] serviceObjs = toolBar.getMenuClasses();
            for (Class obj : serviceObjs) {
                if (!obj.equals(Void.class)) {
                    try {
                        CustomToolsBar customToolsBar = PluginsFactory.getInstance().initToolClass(obj);

                        if (customToolsBar != null) {
                            List<APICallerComponent> components = customToolsBar.getApis();
                            euModule.getComponent().addApi(components);
                            ToolBarProperties toolBarProperties = JSONObject.parseObject(JSON.toJSONString(customToolsBar.getProperties()), ToolBarProperties.class);
                            CustomToolsBar toolsBar = JSONObject.parseObject(JSON.toJSONString(customToolsBar), CustomToolsBar.class);
                            toolsBar.setProperties(toolBarProperties);

                            toolsBar.setAlias(codeEditorComponent.getAlias() + ComponentType.TOOLBAR.getType());
                            this.addChildren(toolsBar);
                        }
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        this.addChildren(codeEditorComponent);
        codeEditorComponent.setProperties(properties);
        codeEditorComponent.initContextMenu(euModule, field, FieldEventEnum.onContextmenu);
        codeEditorComponent.initEditor(euModule, field, FieldEventEnum.onChange);
        codeEditorComponent.initEvent(euModule, field);

    }


}
