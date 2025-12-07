package net.ooder.esd.dsm.gen.view;

import net.ooder.common.JDSException;
import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.bean.WidgetBean;
import net.ooder.esd.dsm.java.AggRootBuild;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.server.context.MinServerActionContextImpl;
import ognl.OgnlContext;

import java.util.List;
import java.util.concurrent.Callable;

public class GenFormChildModule implements Callable<List<JavaGenSource>> {
    private MinServerActionContextImpl autoruncontext;
    private OgnlContext onglContext;
    Component component;
    List<JavaSrcBean> javaSrcBeans;
    ModuleComponent moduleComponent;
    FieldFormConfig<? extends WidgetBean, ?> fieldFormConfig;
    AggRootBuild build;

    public GenFormChildModule(ModuleComponent moduleComponent, Component component, FieldFormConfig<? extends WidgetBean, ?> fieldFormConfig) {
        this.moduleComponent = moduleComponent;
        this.component = component;
        this.fieldFormConfig = fieldFormConfig;
        JDSContext context = JDSActionContext.getActionContext();
        autoruncontext = new MinServerActionContextImpl(context.getHttpRequest(), context.getOgnlContext());
        autoruncontext.getParamMap().putAll(context.getPagectx());
        autoruncontext.getParamMap().putAll(context.getContext());
        if (context.getSessionId() != null) {
            autoruncontext.setSessionId(context.getSessionId());
            autoruncontext.getSession().put("sessionHandle", context.getSession().get("sessionHandle"));
        }
        autoruncontext.setSessionMap(context.getSession());
        onglContext = autoruncontext.getOgnlContext();
        fieldFormConfig.update(moduleComponent, component);
        WidgetBean widgetConfig = fieldFormConfig.getWidgetConfig();
        try {
            build = widgetConfig.getFieldRootBuild();
        } catch (JDSException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<JavaGenSource> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        build.build();
        return build.getAllGenBean();
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public ModuleComponent getModuleComponent() {
        return moduleComponent;
    }

    public void setModuleComponent(ModuleComponent moduleComponent) {
        this.moduleComponent = moduleComponent;
    }

    public FieldFormConfig<? extends WidgetBean, ?> getFieldFormConfig() {
        return fieldFormConfig;
    }

    public void setFieldFormConfig(FieldFormConfig<? extends WidgetBean, ?> fieldFormConfig) {
        this.fieldFormConfig = fieldFormConfig;
    }

    public AggRootBuild getBuild() {
        return build;
    }

    public void setBuild(AggRootBuild build) {
        this.build = build;
    }

    public List<JavaSrcBean> getJavaSrcBeans() {
        return javaSrcBeans;
    }

    public void setJavaSrcBeans(List<JavaSrcBean> javaSrcBeans) {
        this.javaSrcBeans = javaSrcBeans;
    }
}