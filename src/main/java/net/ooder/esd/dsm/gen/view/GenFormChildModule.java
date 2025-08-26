package net.ooder.esd.dsm.gen.view;

import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.server.context.MinServerActionContextImpl;
import net.ooder.server.httpproxy.core.HttpRequest;
import ognl.OgnlContext;

import java.util.List;
import java.util.concurrent.Callable;

public class GenFormChildModule implements Callable<FieldFormConfig> {
    private MinServerActionContextImpl autoruncontext;
    private OgnlContext onglContext;
    Component component;
    List<JavaSrcBean> javaSrcBeans;
    ModuleComponent moduleComponent;
    FieldFormConfig fieldFormConfig;

    public GenFormChildModule(ModuleComponent moduleComponent, Component component, FieldFormConfig fieldFormConfig) {
        this.moduleComponent = moduleComponent;
        this.component = component;
        this.fieldFormConfig = fieldFormConfig;
        JDSContext context = JDSActionContext.getActionContext();
        autoruncontext = new MinServerActionContextImpl(context.getHttpRequest(),context.getOgnlContext());
        autoruncontext.getParamMap().putAll(context.getPagectx());
        autoruncontext.getParamMap().putAll(context.getContext());
        if (context.getSessionId() != null) {
            autoruncontext.setSessionId(context.getSessionId());
            autoruncontext.getSession().put("sessionHandle", context.getSession().get("sessionHandle"));
        }
        autoruncontext.setSessionMap(context.getSession());
        onglContext = autoruncontext.getOgnlContext();

    }

    @Override
    public FieldFormConfig call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        javaSrcBeans= fieldFormConfig.update(moduleComponent, component);
        return fieldFormConfig;
    }

    public List<JavaSrcBean> getJavaSrcBeans() {
        return javaSrcBeans;
    }

    public void setJavaSrcBeans(List<JavaSrcBean> javaSrcBeans) {
        this.javaSrcBeans = javaSrcBeans;
    }
}