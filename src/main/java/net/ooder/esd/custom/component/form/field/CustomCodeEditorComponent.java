package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.ToolBarMenuBean;
import net.ooder.esd.bean.bar.DynBar;
import net.ooder.esd.bean.field.base.CodeEditorFieldBean;
import net.ooder.esd.custom.component.CustomToolsBar;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.manager.editor.PluginsFactory;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.CodeEditorComponent;
import net.ooder.esd.tool.properties.BlockProperties;
import net.ooder.esd.tool.properties.ToolBarProperties;
import net.ooder.esd.tool.properties.form.CodeEditorProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.List;
import java.util.Map;

public class CustomCodeEditorComponent extends BlockComponent {

    @JSONField(serialize = false)
    public ToolBarMenuBean toolBar;

    public CustomCodeEditorComponent(EUModule euModule, FieldFormConfig<CodeEditorFieldBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getFieldname() + ComponentType.BLOCK.getType(), new BlockProperties(ComponentType.CODEEDITOR));
        this.setTarget(target);

        CodeEditorComponent codeEditorComponent = new CodeEditorComponent(field.getFieldname());
        CodeEditorFieldBean codeEditorFieldBean = field.getWidgetConfig();
        if (value != null) {
            if (value instanceof CodeEditorFieldBean) {
                String json = JSON.toJSONString(value);
                String obj = (String) TemplateRuntime.eval(json, value);
                OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), codeEditorFieldBean, false, false);
            }
        }
        CodeEditorProperties codeEditorProperties = new CodeEditorProperties(codeEditorFieldBean, field.getContainerBean());
        codeEditorProperties.setDock(Dock.fill);
        codeEditorProperties.setId(field.getId());
        codeEditorProperties.setName(field.getFieldname());
        codeEditorProperties.setRequired(field.getFieldBean().getRequired());
        codeEditorProperties.setDynCheck(field.getFieldBean().getDynCheck());
        codeEditorProperties.setCodeType(codeEditorFieldBean.getCodeType());
        codeEditorProperties.setDynLoad(codeEditorFieldBean.getDynLoad());

        if (field.getWidgetConfig().getToolBar() != null) {
            this.toolBar = codeEditorFieldBean.getToolBar();
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
        codeEditorComponent.setProperties(codeEditorProperties);
        codeEditorComponent.initContextMenu(euModule, field, FieldEventEnum.onContextmenu);
        codeEditorComponent.initEditor(euModule, field, FieldEventEnum.onChange);
        codeEditorComponent.initEvent(euModule, field);

    }


}
