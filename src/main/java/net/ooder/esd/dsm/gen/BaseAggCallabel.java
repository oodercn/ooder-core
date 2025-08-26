package net.ooder.esd.dsm.gen;

import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.server.context.MinServerActionContextImpl;

import java.util.List;
import java.util.concurrent.Callable;

public abstract class BaseAggCallabel implements Callable<List<JavaSrcBean>> {
    protected MinServerActionContextImpl autoruncontext;

    public BaseAggCallabel() {
        JDSContext context = JDSActionContext.getActionContext();
        this.autoruncontext = new MinServerActionContextImpl(context.getHttpRequest(),context.getOgnlContext());
        autoruncontext.setParamMap(context.getContext());
        if (context.getSessionId() != null) {
            autoruncontext.setSessionId(context.getSessionId());
            autoruncontext.getSession().put("sessionHandle", context.getSession().get("sessionHandle"));
        }
        autoruncontext.setSessionMap(context.getSession());
    }


}
