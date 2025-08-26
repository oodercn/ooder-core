package net.ooder.esd.engine.task;

import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.esd.engine.ProjectCacheManager;
import net.ooder.esd.engine.inner.INProject;
import net.ooder.server.context.MinServerActionContextImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class SyncLoadProject<T extends List<INProject>> implements Callable<List<INProject>> {
    public static Log logger = LogFactory.getLog(SyncLoadProject.class);
    protected MinServerActionContextImpl autoruncontext;
    Set<String> projectIdList = new HashSet();
    ProjectCacheManager projectCacheManager;

    public SyncLoadProject(ProjectCacheManager projectCacheManager, String[] projectIds) {
        this.projectCacheManager = projectCacheManager;
        for (String projectId : projectIds) {
            projectIdList.add(projectId);
        }

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
    public List<INProject> call() throws Exception {
        JDSActionContext.setContext(autoruncontext);
        List<INProject> projectVersions = new ArrayList<>();
        for (String projectId : projectIdList) {
            INProject inProjectVersion = projectCacheManager.getProjectById(projectId);
            if (inProjectVersion != null) {
                logger.info("load Project..." + inProjectVersion.getProjectName());
                projectVersions.add(inProjectVersion);
            }

        }
        return projectVersions;
    }


}