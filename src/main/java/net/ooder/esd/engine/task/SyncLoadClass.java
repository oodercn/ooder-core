package net.ooder.esd.engine.task;

import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.EUModule;
import net.ooder.server.context.MinServerActionContextImpl;

import java.util.concurrent.Callable;

public class SyncLoadClass<T extends EUModule> implements Callable<EUModule> {
    protected MinServerActionContextImpl autoruncontext;
    ESDClient client;
    String esdClassName;
    String versionName;

    public SyncLoadClass(ESDClient client, String esdClassName, String versionName) {
        this.client = client;
        this.esdClassName = esdClassName;
        this.versionName = versionName;
        JDSContext context = JDSActionContext.getActionContext();
        this.autoruncontext = new MinServerActionContextImpl(context.getHttpRequest(), context.getOgnlContext());
        autoruncontext.setParamMap(context.getContext());
        if (context.getSessionId() != null) {
            autoruncontext.setSessionId(context.getSessionId());
            autoruncontext.getSession().put("sessionHandle", context.getSession().get("sessionHandle"));
        }
        autoruncontext.setSessionMap(context.getSession());
    }

    @Override
    public EUModule call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        return client.getModule(esdClassName, versionName, false);
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getEsdClassName() {
        return esdClassName;
    }

    public void setEsdClassName(String esdClassName) {
        this.esdClassName = esdClassName;
    }
}

