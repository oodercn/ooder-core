package net.ooder.esd.custom.component.form.field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.ToolBarMenuBean;
import net.ooder.esd.bean.bar.DynBar;
import net.ooder.esd.bean.field.base.JavaEditorFieldBean;
import net.ooder.esd.custom.component.CustomToolsBar;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.manager.editor.PluginsFactory;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.BlockComponent;
import net.ooder.esd.tool.component.JavaEditorComponent;
import net.ooder.esd.tool.properties.BlockProperties;
import net.ooder.esd.tool.properties.form.CodeEditorProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import org.mvel2.templates.TemplateRuntime;

import java.util.List;
import java.util.Map;

public class CustomJavaEditorComponent extends BlockComponent {

    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CustomJavaEditorComponent.class);

    @JSONField(serialize = false)
    public ToolBarMenuBean toolBar;


    public CustomJavaEditorComponent(EUModule euModule, FieldFormConfig<JavaEditorFieldBean, ?> field, String target, Object value, Map valueMap) {
        super(field.getFieldname() + ComponentType.BLOCK.getType(), new BlockProperties(ComponentType.JAVAEDITOR));
        this.setTarget(target);

        JavaEditorComponent javaEditorComponent = new JavaEditorComponent(field.getFieldname());
        JavaEditorFieldBean javaEditorFieldBean = field.getWidgetConfig();
        if (value != null) {
            if (value instanceof JavaEditorFieldBean) {
                String json = JSON.toJSONString(value);
                String obj = (String) TemplateRuntime.eval(json, value);
                OgnlUtil.setProperties(JSON.parseObject(obj, Map.class), javaEditorFieldBean, false, false);
            }
        }

        CodeEditorProperties codeEditorProperties = new CodeEditorProperties(javaEditorFieldBean, field.getContainerBean());
        codeEditorProperties.setDock(Dock.fill);

        codeEditorProperties.setId(field.getId());
        codeEditorProperties.setName(field.getFieldname());
        codeEditorProperties.setRequired(field.getFieldBean().getRequired());
        codeEditorProperties.setDynCheck(field.getFieldBean().getDynCheck());
        codeEditorProperties.setCodeType("java");
        codeEditorProperties.setDynLoad(javaEditorFieldBean.getDynLoad());
        if (value instanceof String && !value.equals("")) {
            codeEditorProperties.setValue(value);
        }

        if (field.getWidgetConfig().getToolBar() != null) {
            this.toolBar = javaEditorFieldBean.getToolBar();
            this.toolBar.setGroupId(field.getFieldname() + "_" + ComponentType.TOOLBAR.getType());
            Class<DynBar>[] serviceObjs = toolBar.getMenuClasses();
            for (Class obj : serviceObjs) {
                if (!obj.equals(Void.class)) {
                    try {
                        CustomToolsBar customToolsBar = (CustomToolsBar) PluginsFactory.getInstance().initToolClass(obj);
                        if (customToolsBar != null) {
                            List<APICallerComponent> components = customToolsBar.getApis();
                            euModule.getComponent().addApi(components);
                            this.addChildren(customToolsBar);
                        }
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        this.addChildren(javaEditorComponent);

        javaEditorComponent.setProperties(codeEditorProperties);
        javaEditorComponent.initContextMenu(euModule, field, FieldEventEnum.onContextmenu);
        javaEditorComponent.initEditor(euModule, field, FieldEventEnum.onChange);
        javaEditorComponent.initEvent(euModule, field);


    }

}
