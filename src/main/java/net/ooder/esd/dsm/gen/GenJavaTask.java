package net.ooder.esd.dsm.gen;

import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.logging.LogSetpLog;
import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.JavaRoot;
import net.ooder.esd.dsm.java.JavaGenSource;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.dsm.temp.JavaTemp;
import net.ooder.server.context.MinServerActionContextImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public abstract class GenJavaTask implements Callable<List<JavaGenSource>> {
    protected Log log = LogFactory.getLog(JDSConstants.CONFIG_KEY, GenJavaTask.class);
    protected MinServerActionContextImpl autoruncontext;
    public ChromeProxy chrome;
    public List<String> skipsTempIds = new ArrayList<>();
    public List<JavaTemp> viewTemps = new ArrayList<>();
    public Set<String> classList = new HashSet<>();

    public GenJavaTask() {
        JDSContext context = JDSActionContext.getActionContext();
        this.autoruncontext = new MinServerActionContextImpl(context.getHttpRequest(), context.getOgnlContext());
        autoruncontext.setParamMap(context.getContext());
        if (context.getSessionId() != null) {
            autoruncontext.setSessionId(context.getSessionId());
            autoruncontext.getSession().put("sessionHandle", context.getSession().get("sessionHandle"));
        }
        autoruncontext.setSessionMap(context.getSession());
    }

    public List<JavaGenSource> getSourceList() {
        List<JavaGenSource> sources = new ArrayList<>();
        for (String className : classList) {
            try {
                JavaGenSource javaGenSource = BuildFactory.getInstance().getJavaGenSource(className);
                if (javaGenSource != null) {
                    sources.add(javaGenSource);
                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return sources;
    }

    public List<JavaSrcBean> getJavaSrcBeanList() {
        List<JavaSrcBean> javaSrcBeans = new ArrayList<>();
        List<JavaGenSource> sources = getSourceList();
        for (JavaGenSource javaGenSource : sources) {
            javaSrcBeans.add(javaGenSource.getSrcBean());
        }
        return javaSrcBeans;
    }

    public List<JavaTemp> getJavaTemps() {
        List<JavaTemp> javaTemps = new ArrayList<>();
        List<JavaGenSource> sources = getSourceList();
        for (JavaGenSource javaGenSource : sources) {
            javaTemps.add(javaGenSource.getJavatemp());
        }
        return javaTemps;
    }

    public List<JavaRoot> getContextList() {
        List<JavaRoot> javaRoots = new ArrayList<>();
        List<JavaGenSource> sources = getSourceList();
        for (JavaGenSource javaGenSource : sources) {
            javaRoots.add(javaGenSource.getJavaRoot());
        }
        return javaRoots;
    }

    public List<String> getSkipsTempIds() {
        return skipsTempIds;
    }

    public void setSkipsTempIds(List<String> skipsTempIds) {
        this.skipsTempIds = skipsTempIds;
    }

    public ChromeProxy getChrome() {
        return chrome;
    }

    public void setChrome(ChromeProxy chrome) {
        this.chrome = chrome;
    }

    public List<JavaTemp> getViewTemps() {
        return viewTemps;
    }

    public void setViewTemps(List<JavaTemp> viewTemps) {
        this.viewTemps = viewTemps;
    }


    public Set<String> getClassList() {
        return classList;
    }

    public void setClassList(Set<String> classList) {
        this.classList = classList;
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        if (chrome == null) {
            chrome = new LogSetpLog();
        }
        return chrome;
    }

}
