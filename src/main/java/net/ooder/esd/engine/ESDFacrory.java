package net.ooder.esd.engine;

import com.alibaba.fastjson.JSONArray;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.cache.Cache;
import net.ooder.common.cache.CacheManagerFactory;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.config.UserBean;
import net.ooder.context.JDSActionContext;
import net.ooder.esb.config.manager.EsbBeanFactory;
import net.ooder.esb.util.EsbFactory;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.repository.database.FDTFactory;
import net.ooder.esd.engine.task.SyncCacheResource;
import net.ooder.esd.manager.plugins.api.APIFactory;
import net.ooder.esd.manager.plugins.font.FontFactory;
import net.ooder.esd.manager.plugins.img.ImgFactory;
import net.ooder.esd.manager.plugins.style.StyleFactory;
import net.ooder.hsql.HsqlDbCacheManager;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.util.ActionContext;
import net.ooder.server.JDSClientService;
import net.ooder.server.JDSServer;
import net.ooder.server.SubSystem;
import net.ooder.vfs.Folder;
import net.ooder.vfs.VFSConstants;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.RemoteConnectionManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ESDFacrory {


    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, ESDFacrory.class);

    static Map<JDSClientService, ESDClient> clientMap = new ConcurrentHashMap<JDSClientService, ESDClient>();

    static Map<String, ESDClient> spaceEsdClientMap = new ConcurrentHashMap<String, ESDClient>();

    public static final String THREAD_LOCK = "Thread Lock";

    public static final String ESDKEY = "ESDClient";

    public static final String defaultSpace = "form/myspace";

    public static final String URLS = "URLS";

    Cache hsqlCache = HsqlDbCacheManager.getCache("ESDLastData");

    static ESDFacrory factory;

    public static ESDFacrory getInstance() {
        if (factory == null) {
            synchronized (THREAD_LOCK) {
                if (factory == null) {
                    factory = new ESDFacrory();
                }
            }
        }
        return factory;
    }

    ESDFacrory() {

    }


    public void reload() throws JDSException {
        MySpace space = ESDFacrory.getAdminESDClient().getSpace();
        EsbBeanFactory.getInstance().reLoad();
        APIConfigFactory.getInstance().reload();
        ProjectCacheManager.getInstance(space);
        FontFactory.getInstance(space).reLoad();
        APIFactory.getInstance(space).reload();
        StyleFactory.getInstance(space).reLoad();
        ImgFactory.getInstance(space).reLoad();
        FDTFactory.getInstance(space).reLoad();
        DSMFactory.getInstance(space).reload();
        CustomViewFactory.getInstance(space).reLoad();

    }


    public void dyReload(String esbkey) throws JDSException {
        if (esbkey == null) {
            esbkey = "local";
        }
        //DSMFactory.getInstance().reload();
        // BuildFactory.getInstance().getClassManager().reload();
        Set<Class<?>> classes = EsbBeanFactory.getInstance().dyReLoad(esbkey);
        APIConfigFactory.getInstance().dyReload(classes);
        APIFactory.getInstance(ESDFacrory.getAdminESDClient().getSpace()).dyReload(classes);
        EsbFactory.reload();
    }

    public void setUrls(String account, List<PageHis> pages) {
        hsqlCache.put(account, JSONArray.toJSON(pages).toString());
    }

    public List<PageHis> getUrls(String account) {
        List<PageHis> urls = new ArrayList<>();
        String urlStr = (String) hsqlCache.get(account);
        if (urlStr != null && !urlStr.equals("")) {
            urls = JSONArray.parseArray(urlStr, PageHis.class);
        }
        return urls;
    }

    public PageHis getHisPage(String account, URL url) {
        List<PageHis> hisPages = ESDFacrory.getInstance().getUrls(account);
        PageHis page = null;
        for (PageHis his : hisPages) {
            if (his.getUrl().equals(url.toString())) {
                page = his;
            }
        }
        return page;
    }


    public void loadFromCache() {
        long time = System.currentTimeMillis();
        HsqlDbCacheManager.getInstance().loadCache(VFSConstants.CONFIG_CTVFS_KEY);
        logger.info("loadFromCache  times=" + (System.currentTimeMillis() - time));
    }

    public void reloadData(String path) throws JDSException {
        long time = System.currentTimeMillis();
        Long lasttime = (Long) hsqlCache.get(VFSConstants.CONFIG_CTVFS_KEY);
        if (lasttime == null || (System.currentTimeMillis() - lasttime) > 7 * 24 * 60 * 60 * 1000) {
            hsqlCache.put(VFSConstants.CONFIG_CTVFS_KEY, time);
            Folder folder = CtVfsFactory.getCtVfsService().getFolderByPath(path);
            List<SyncCacheResource> tasks = new ArrayList<>();
            for (Folder childfolder : folder.getChildrenList()) {
                SyncCacheResource cacheResourc = new SyncCacheResource(childfolder);
                tasks.add(cacheResourc);
            }
            try {
                List<Future<Boolean>> futures = RemoteConnectionManager.getConntctionService("CacheResource").invokeAll(tasks);
                for (Future<Boolean> resultFuture : futures) {
                    try {
                        resultFuture.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


        logger.info("loadFromCache  times=" + (System.currentTimeMillis() - time));
    }


    public void clearCache() {
        try {
            String spaceformUrl = JDSServer.getClusterClient().getSystem(JDSServer.getInstance().getAdminUser().getSystemCode()).getVfsPath();
            Map<String, Cache> cacheMap = CacheManagerFactory.getInstance().getCacheManager(VFSConstants.CONFIG_CTVFS_KEY).getAllCache();
            Set<String> keyset = cacheMap.keySet();
            for (String key : keyset) {
                if (key != null && key.startsWith(VFSConstants.CONFIG_CTVFS_KEY)) {
                    Cache cache = cacheMap.get(key);
                    if (cache != null) {
                        cache.clear();
                    }
                }
            }

            HsqlDbCacheManager.getInstance().clearAllCache();
            this.reloadData(spaceformUrl);
            //dumpCache();
        } catch (JDSException e) {
            e.printStackTrace();
        }
    }

    public void dumpCache() {
        long time = System.currentTimeMillis();

        try {
            DSMFactory.getInstance().getAggregationManager().commitTask();
            DSMFactory.getInstance().getViewManager().commitTask();
        } catch (JDSException e) {
            e.printStackTrace();
        }

        HsqlDbCacheManager.getInstance().dumpCache(VFSConstants.CONFIG_CTVFS_KEY);
        hsqlCache.put(VFSConstants.CONFIG_CTVFS_KEY, time);
        logger.info("dump cache times=" + (System.currentTimeMillis() - time));
    }


    public static ESDClient getESDClient(JDSClientService client) throws JDSException {
        ESDClient esdClient = clientMap.get(client);
        if (esdClient == null) {
            esdClient = new ESDClientImpl(client);
            clientMap.put(client, esdClient);
            // initDsm();
        }

        return esdClient;
    }
//

    public static MySpace getSpaceByPath(String spacePath) {
        MySpace space = null;
        try {
            if (JDSServer.getClusterClient().isLogin()) {
                List<SubSystem> systems = JDSServer.getClusterClient().getAllSystem();
                for (SubSystem subSystem : systems) {
                    if (subSystem.getVfsPath() != null && !subSystem.getVfsPath().equals("")) {
                        Folder folder = CtVfsFactory.getCtVfsService().mkDir(subSystem.getVfsPath());
                        if (folder != null && folder.getPath().equals(spacePath)) {
                            space = new MySpace(folder, subSystem.getSysId());
                        }
                        continue;
                    }

                }
            } else {
                space = new MySpace(CtVfsFactory.getCtVfsService().mkDir(defaultSpace), UserBean.getInstance().getSystemCode());
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return space;
    }

    public static ESDClient getESDClient(String spacePath) throws JDSException {
        synchronized (spacePath) {
            ESDClient esdClient = (ESDClient) JDSActionContext.getActionContext().getContext().get("ESDKEY");
            if (esdClient == null) {
                JDSClientService client = (JDSClientService) ActionContext.getContext().getContextMap().get("$JDSC");
                if (client == null) {
                    client = EsbUtil.parExpression("$JDSC", JDSClientService.class);
                    ActionContext.getContext().getContextMap().put("$JDSC", client);
                }

                if (client == null) {
                    client = JDSServer.getInstance().getAdminClient();
                }

                esdClient = spaceEsdClientMap.get(spacePath);

                if (esdClient == null) {
                    MySpace space = getSpaceByPath(spacePath);
                    esdClient = new ESDClientImpl(space, client);
                    spaceEsdClientMap.put(spacePath, esdClient);
                }
                JDSActionContext.getActionContext().getContext().put("ESDKEY", esdClient);
            }
            return esdClient;
        }


    }


    public static ProjectCacheManager getDefalutProjectManager() throws JDSException {
        ProjectCacheManager cacheManager = ProjectCacheManager.getCacheManager(defaultSpace);
        return cacheManager;
    }

    public static ProjectCacheManager getProjectCacheManager(String spaceName) throws JDSException {
        ProjectCacheManager cacheManager = ProjectCacheManager.getCacheManager(spaceName);
        return cacheManager;
    }

    public static ESDClient getUserESDClient() throws JDSException {
        ESDClient esdClient = (ESDClient) JDSActionContext.getActionContext().getContext().get("ESDKEY");
        if (esdClient == null) {
            JDSClientService client = (JDSClientService) ActionContext.getContext().getContextMap().get("$JDSC");
            if (client == null) {
                client = EsbUtil.parExpression("$JDSC", JDSClientService.class);
                ActionContext.getContext().getContextMap().put("$JDSC", client);
            }
            if (client != null) {
                esdClient = getESDClient(client);
                JDSActionContext.getActionContext().getContext().put("ESDKEY", esdClient);
            }
        }
        return esdClient;
    }

    public static ESDClient getAdminESDClient() throws JDSException {
        JDSClientService client = JDSServer.getInstance().getAdminClient();
        ESDClient esdClient = getESDClient(client);
        return esdClient;
    }


}

