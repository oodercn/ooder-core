
package net.ooder.esd.engine.event;

import com.alibaba.fastjson.JSON;
import net.ooder.cluster.udp.ClusterEvent;
import net.ooder.common.*;
import net.ooder.common.cache.CacheManagerFactory;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.ClassUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.engine.event.JDSEventDispatcher;
import net.ooder.esb.config.manager.EsbBeanFactory;
import net.ooder.esb.config.manager.ExpressionTempBean;
import net.ooder.esb.config.manager.ServiceBean;
import net.ooder.server.JDSServer;
import net.ooder.vfs.VFSConstants;
import net.ooder.vfs.engine.event.FileListener;
import net.ooder.vfs.engine.event.FileObjectListener;
import net.ooder.vfs.engine.event.FileVersionListener;
import net.ooder.vfs.enums.FolderEventEnums;
import net.ooder.vfs.enums.VFSEventTypeEnums;

import java.util.*;
import java.util.Map.Entry;

public class ESDEventControl implements JDSEventDispatcher {

    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, ESDEventControl.class);

    // singleton instance
    private static ESDEventControl instance = null;

    public static Map<Class, List<ExpressionTempBean>> listenerMap = new HashMap<Class, List<ExpressionTempBean>>();

    public static Map<String, Long> dataEventMap = CacheManagerFactory.createCache(JDSConstants.ORGCONFIG_KEY, "dataEventTiemMap", 1 * 1024 * 1024, 60 * 60 * 1000);

    public static Map<String, ExpressionTempBean> listenerBeanMap = new HashMap<String, ExpressionTempBean>();

    public static ESDEventControl getInstance() {
        if (instance == null) {
            synchronized (ESDEventControl.class) {
                if (instance == null) {
                    instance = new ESDEventControl();
                }
            }
        }
        return instance;
    }

    protected ESDEventControl() {
        listenerBeanMap.putAll(net.ooder.web.client.ListenerTempAnnotationProxy.getListenerBeanMap());
        List<? extends ServiceBean> esbBeans = EsbBeanFactory.getInstance().loadAllServiceBean();
        for (ServiceBean esbBean : esbBeans) {
            if (esbBean instanceof ExpressionTempBean) {
                listenerBeanMap.put(esbBean.getId(), (ExpressionTempBean) esbBean);
            }

        }
        getListenerByType(PackageListener.class);
        getListenerByType(FileObjectListener.class);
        getListenerByType(FileListener.class);
        getListenerByType(FileVersionListener.class);
    }

    @Override
    public void dispatchEvent(JDSEvent event) throws JDSException {
        if (event instanceof PackageEvent) {
            dispatchPackageEvent((PackageEvent) event, false);
        } else if (event instanceof EUModuleVersionEvent) {
            dispatchModuleVersionEvent((EUModuleVersionEvent) event, false);
        } else if (event instanceof EUModuleObjectEvent) {
            dispatchModuleObjectEvent((EUModuleObjectEvent) event, false);
        } else if (event instanceof EUModuleEvent) {
            dispatchModuleEvent((EUModuleEvent) event, false);
        }

    }

    /**
     * 分发文件夹事件
     *
     * @param event 核心活动事件
     */
    public void dispatchPackageEvent(final PackageEvent event, boolean isCluster) throws JDSException {
        if (isCluster) {
            List<PackageListener> listeners = getListenerByType(PackageListener.class);
            for (int k = 0; k < listeners.size(); k++) {
                PackageListener fileListener = listeners.get(k);
                if (!JDSServer.getInstance().getCurrServerBean().getConfigCode().equals(ConfigCode.userdef)) {
                    dispatchPackageEvent(event, listeners.get(k));
                }
            }
        } else {
            event.setSystemCode(JDSServer.getInstance().getCurrServerBean().getId());
            String folderpath = event.getPackagePath();
            String systemCode = event.getSystemCode();
            if (folderpath != null) {
                String key = event.getID().getMethod() + folderpath;
                Long checkOutTime = dataEventMap.get(key);
                if (checkOutTime == null || System.currentTimeMillis() - checkOutTime > 1000) {
                    dataEventMap.put(key, System.currentTimeMillis());
                    repeatEvent(event, key);
                    List<EIPackageListener> listeners = getListenerByType(EIPackageListener.class);
                    for (int k = 0; k < listeners.size(); k++) {
                        EIPackageListener packageListener = (EIPackageListener) listeners.get(k);
                        dispatchEIPackageEvent(event, packageListener);
                    }
                }
            }

        }

    }

    public void dispatchModuleEvent(final EUModuleEvent event, boolean isCluster) throws JDSException {

        if (isCluster) {
            event.setSystemCode(JDSServer.getInstance().getCurrServerBean().getId());
            List<EUModuleListener> listeners = getListenerByType(EUModuleListener.class);
            for (int k = 0; k < listeners.size(); k++) {
                EUModuleListener fileListener = listeners.get(k);
                if (!JDSServer.getInstance().getCurrServerBean().getConfigCode().equals(ConfigCode.userdef)) {
                    dispatchModuleEvent(event, listeners.get(k));
                }
            }

        } else {
            event.setSystemCode(JDSServer.getInstance().getCurrServerBean().getId());
            String path = event.getFilePath();

            if (path != null && !path.equalsIgnoreCase("")) {
                String key = event.getID().getMethod() + path;
                Long checkOutTime = dataEventMap.get(key);
                if (checkOutTime == null || System.currentTimeMillis() - checkOutTime > 500) {
                    dataEventMap.put(key, System.currentTimeMillis());
                    repeatEvent(event, key);
                    List<EIModuleListener> listeners = getListenerByType(EIModuleListener.class);
                    for (int k = 0; k < listeners.size(); k++) {
                        EIModuleListener fileListener = listeners.get(k);
                        dispatchEIModuleEvent(event, listeners.get(k));
                    }
                    EIModuleListener customerFileListener = (EIModuleListener) event.getContextMap().get(VFSConstants.FileListener);
                    if (customerFileListener != null) {
                        dispatchEIModuleEvent(event, customerFileListener);
                    }
                }
            }
        }


    }


    public void dispatchModuleObjectEvent(final EUModuleObjectEvent event, boolean isCluster) throws JDSException {
        if (isCluster) {

            List<EUModuleObjectListener> listeners = getListenerByType(EUModuleObjectListener.class);
            for (int k = 0; k < listeners.size(); k++) {
                EUModuleObjectListener fileObjectListener = (EUModuleObjectListener) listeners.get(k);
                if (!JDSServer.getInstance().getCurrServerBean().getConfigCode().equals(ConfigCode.userdef)) {
                    dispatchModuleObjectEvent(event, (EUModuleObjectListener) listeners.get(k));
                }
            }
        } else {

            event.setSystemCode(JDSServer.getInstance().getCurrServerBean().getId());
            EUModuleObjectEvent fe = event;
            String path = (String) event.getSource();
            if (path != null && !path.equalsIgnoreCase("")) {
                String key = event.getID().getMethod() + path;
                Long checkOutTime = dataEventMap.get(key);
                if (checkOutTime == null || System.currentTimeMillis() - checkOutTime > 500) {
                    dataEventMap.put(key, System.currentTimeMillis());
                    repeatEvent(event, key);
                    List<EIModuleObjectListener> listeners = getListenerByType(EIModuleObjectListener.class);
                    for (int k = 0; k < listeners.size(); k++) {
                        EIModuleObjectListener fileListener = listeners.get(k);
                        dispatchEIModuleObjectEvent(event, fileListener);

                    }
                }
            }

        }
    }


    public void dispatchModuleVersionEvent(final EUModuleVersionEvent event, boolean isCluster) throws JDSException {
        EUModuleVersionEvent fe = event;
        String path = (String) event.getSource();
        String systemCode = (String) event.getContextMap().get(VFSConstants.SYSTEM_CODE);
        if (isCluster) {

            List<EUModuleVersionListener> listeners = getListenerByType(EUModuleVersionListener.class);
            for (int k = 0; k < listeners.size(); k++) {
                EUModuleVersionListener moduleVersionListener = (EUModuleVersionListener) listeners.get(k);
                if (!JDSServer.getInstance().getCurrServerBean().getConfigCode().equals(ConfigCode.userdef)) {
                    dispatchModuleVersionEvent(event, moduleVersionListener);
                }

            }


        } else {
            event.setSystemCode(JDSServer.getInstance().getCurrServerBean().getId());
            if (path != null && !path.equalsIgnoreCase("")) {
                String key = event.getID().getMethod() + path;
                Long checkOutTime = dataEventMap.get(key);
                if (checkOutTime == null || System.currentTimeMillis() - checkOutTime > 500) {
                    dataEventMap.put(key, System.currentTimeMillis());
                    repeatEvent(event, key);
                    List<EIModuleVersionListener> listeners = getListenerByType(EIModuleVersionListener.class);
                    for (int k = 0; k < listeners.size(); k++) {
                        EIModuleVersionListener fileListener = listeners.get(k);
                        dispatchEIModuleVersionEvent(event, fileListener);
                    }
                }
            }
        }
    }

    ;


    public boolean repeatEvent(ESDEvent event, String msgId) throws JDSException {
        Boolean isSend = false;
        if (JDSServer.getInstance().getCurrServerBean().getId().equals(event.getSystemCode())) {
            VFSEventTypeEnums type = VFSEventTypeEnums.fromEventClass(event.getClass());
            ClusterEvent clusterEvent = new ClusterEvent();
            clusterEvent.setEventId(event.getID().getMethod());
            event.setSystemCode(JDSServer.getInstance().getCurrServerBean().getId());

            if (event.getSource() instanceof String || event.getSource() instanceof Integer || event.getSource() instanceof Double) {
                clusterEvent.setSourceJson(event.getSource().toString());
            } else {
                String source = JSON.toJSONString(event.getSource());
                clusterEvent.setSourceJson(event.getSource().toString());
            }

            clusterEvent.setMsgId(msgId);
            clusterEvent.setSessionId(JDSServer.getInstance().getAdminUser().getSessionId());
            clusterEvent.setSystemCode(JDSServer.getInstance().getCurrServerBean().getId());
            clusterEvent.setEventName(type.getEventName());

            clusterEvent.setExpression("$RepeatESDEvent");
            String eventStr = JSON.toJSONString(clusterEvent);

            isSend = JDSServer.getClusterClient().getUDPClient().send(eventStr);
            logger.info("success repeatEvent [" + isSend + "]" + event.getID());
        }

        return isSend;

    }

    public <T> void dispatchClusterEvent(String objStr, String eventName, String event, String systemCode) throws JDSException {
        // if (systemCode != null && !systemCode.equals(JDSServer.getInstance().getCurrServerBean().getId())) {
        ESDEventTypeEnums type = ESDEventTypeEnums.fromName(eventName);
        switch (type) {
            case ModuleEvent:
                String path = objStr;
                EUModuleEvent fileEvent = new EUModuleEvent(path, EUModuleEventEnums.fromMethod(event), systemCode);
                this.dispatchModuleEvent(fileEvent, true);
                break;

            case PackageEvent:
                String folderpath = objStr;
                PackageEvent packageEvent = new PackageEvent(folderpath, FolderEventEnums.fromMethod(event), systemCode);
                this.dispatchPackageEvent(packageEvent, true);

                break;

            case ModuleVersionEvent:
                EUModuleVersionEvent moduleVersionEvent = new EUModuleVersionEvent(objStr, EUModuleVersionEventEnums.fromMethod(event), systemCode);
                this.dispatchModuleVersionEvent(moduleVersionEvent, true);
                break;
            case ModuleObjectEvent:
                EUModuleObjectEvent objectEvent = new EUModuleObjectEvent(objStr, EUModuleObjectEventEnums.fromMethod(event), systemCode);
                this.dispatchModuleObjectEvent(objectEvent, true);
                break;
            default:
                break;
        }
        //     }


    }

    public static List<ExpressionTempBean> getListenerTempBeanByType(Class<? extends EventListener> listenerClass) {

        // synchronized (listenerClass.getName().intern()) {
        List<JDSListener> listeners = new ArrayList<JDSListener>();
        Set<Entry<String, ExpressionTempBean>> tempEntry = listenerBeanMap.entrySet();

        List<ExpressionTempBean> tempLst = listenerMap.get(listenerClass);

        if (tempLst == null || tempLst.isEmpty()) {
            tempLst = new ArrayList<ExpressionTempBean>();
            for (Entry<String, ExpressionTempBean> entry : tempEntry) {
                ExpressionTempBean bean = entry.getValue();
                String classType = bean.getClazz();
                Class clazz = null;
                try {
                    clazz = ClassUtility.loadClass(classType);
                } catch (ClassNotFoundException e) {
                    continue;
                }

                if (listenerClass.isAssignableFrom(clazz)) {
                    tempLst.add(bean);
                }
                ;
            }
            listenerMap.put(listenerClass, tempLst);

        }
        return tempLst;
        //}
    }

    ;

    private static <T extends EventListener> List<T> getListenerByType(Class<T> listenerClass) {
        List<ExpressionTempBean> tempLst = getListenerTempBeanByType(listenerClass);
        List<T> listeners = new ArrayList<T>();
        for (ExpressionTempBean tempBean : tempLst) {
            T listener = JDSActionContext.getActionContext().Par("$" + tempBean.getId(), listenerClass);
            if (listener != null) {
                listeners.add(listener);
            }
        }
        return listeners;


    }


    private static void dispatchModuleObjectEvent(final EUModuleObjectEvent event, final EUModuleObjectListener listener) {
        try {
            switch (event.getID()) {

                case append:
                    listener.append(event);
                    break;
                case share:
                    listener.share(event);
                    break;
                case beforDownLaod:
                    listener.beforDownLoad(event);
                    break;
                case downLaoding:
                    listener.downLoading(event);
                case downLaodEnd:
                    listener.downLoadEnd(event);
                    break;
                case befaultUpLoad:
                    listener.befaultUpLoad(event);
                    break;
                case upLoading:
                    listener.upLoading(event);
                    break;
                case upLoadEnd:
                    listener.upLoadEnd(event);
                    break;
                case upLoadError:
                    listener.upLoadError(event);
                    break;

                default:
                    throw new JDSException("Unsupport FileObjectListener event type: " + event.getID(), JDSException.UNSUPPORTPROCESSEVENTERROR);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.warn("Listener execute failed!", e);
        }
    }


    private static void dispatchEIModuleObjectEvent(final EUModuleObjectEvent event, final EIModuleObjectListener listener) {
        try {
            switch (event.getID()) {

                case append:
                    listener.append(event);
                    break;
                case share:
                    listener.share(event);
                    break;
                case beforDownLaod:
                    listener.beforDownLoad(event);
                    break;
                case downLaoding:
                    listener.downLoading(event);
                case downLaodEnd:
                    listener.downLoadEnd(event);
                    break;
                case befaultUpLoad:
                    listener.befaultUpLoad(event);
                    break;
                case upLoading:
                    listener.upLoading(event);
                    break;
                case upLoadEnd:
                    listener.upLoadEnd(event);
                    break;
                case upLoadError:
                    listener.upLoadError(event);
                    break;

                default:
                    throw new JDSException("Unsupport FileObjectListener event type: " + event.getID(), JDSException.UNSUPPORTPROCESSEVENTERROR);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.warn("Listener execute failed!", e);
        }
    }

    private static void dispatchModuleVersionEvent(final EUModuleVersionEvent event, final EUModuleVersionListener listener) {
        try {
            switch (event.getID()) {
                case lockVersion:
                    listener.lockVersion(event);
                    break;
                case addFileVersion:
                    listener.addFileVersion(event);
                    break;
                case updateFileVersion:
                    listener.updateFileVersion(event);
                    break;
                case deleteFileVersion:
                    listener.deleteFileVersion(event);
                    break;

                default:
                    throw new JDSException("Unsupport ModuleVersionEvent event type: " + event.getID(), JDSException.UNSUPPORTPROCESSEVENTERROR);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.warn("Listener execute failed!", e);
        }
    }


    private static void dispatchEIModuleVersionEvent(final EUModuleVersionEvent event, final EIModuleVersionListener listener) {
        try {
            switch (event.getID()) {
                case lockVersion:
                    listener.lockVersion(event);
                    break;
                case addFileVersion:
                    listener.addFileVersion(event);
                    break;
                case updateFileVersion:
                    listener.updateFileVersion(event);
                    break;
                case deleteFileVersion:
                    listener.deleteFileVersion(event);
                    break;

                default:
                    throw new JDSException("Unsupport FileVersionEvent event type: " + event.getID(), JDSException.UNSUPPORTPROCESSEVENTERROR);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.warn("Listener execute failed!", e);
        }
    }


    private static void dispatchModuleEvent(final EUModuleEvent event, final EUModuleListener listener) {
        try {
            switch (event.getID()) {
                case beforDownLoad:
                    listener.beforDownLoad(event);
                    break;
                case downLoading:
                    listener.downLoading(event);
                    break;
                case downLoadEnd:
                    listener.downLoadEnd(event);
                    break;
                case beforeReName:
                    listener.reNameEnd(event);
                    break;
                case reStore:
                    listener.reStore(event);
                    break;
                case beforMove:
                    listener.beforMove(event);
                    break;
                case moveEnd:
                    listener.moveEnd(event);
                    break;
                case beforDelete:
                    listener.beforDelete(event);
                    break;
                case deleteEnd:
                    listener.deleteEnd(event);
                    break;
                case send:
                    listener.send(event);
                    break;
                case open:
                    listener.open(event);
                    break;
                case clear:
                    listener.clear(event);
                    break;
                case share:
                    listener.share(event);
                    break;
                case beforUpLoad:
                    listener.beforUpLoad(event);
                    break;
                case upLoading:
                    listener.upLoading(event);
                    break;
                case upLoadEnd:
                    listener.upLoadEnd(event);
                    break;
                case upLoadError:
                    listener.upLoadError(event);
                    break;
                case create:
                    listener.create(event);
                    break;
                case beforCopy:
                    listener.beforCopy(event);
                    break;
                case copyEnd:
                    listener.copyEnd(event);
                    break;
                case save:
                    listener.save(event);
                    break;
                case beforUpdate:
                    listener.beforUpdate(event);
                    break;
                case updateEnd:
                    listener.updateEnd(event);
                    break;


                default:
                    throw new JDSException("Unsupport file event type: " + event.getID(), JDSException.UNSUPPORTPROCESSEVENTERROR);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.warn("Listener execute failed!", e);
        }
    }


    private static void dispatchEIModuleEvent(final EUModuleEvent event, final EIModuleListener listener) {
        try {
            switch (event.getID()) {
                case beforDownLoad:
                    listener.beforDownLoad(event);
                    break;
                case downLoading:
                    listener.downLoading(event);
                    break;
                case downLoadEnd:
                    listener.downLoadEnd(event);
                    break;
                case beforeReName:
                    listener.reNameEnd(event);
                    break;
                case reStore:
                    listener.reStore(event);
                    break;
                case beforMove:
                    listener.beforMove(event);
                    break;
                case moveEnd:
                    listener.moveEnd(event);
                    break;
                case beforDelete:
                    listener.beforDelete(event);
                    break;
                case deleteEnd:
                    listener.deleteEnd(event);
                    break;
                case send:
                    listener.send(event);
                    break;
                case open:
                    listener.open(event);
                    break;
                case clear:
                    listener.clear(event);
                    break;
                case share:
                    listener.share(event);
                    break;
                case beforUpLoad:
                    listener.beforUpLoad(event);
                    break;
                case upLoading:
                    listener.upLoading(event);
                    break;
                case upLoadEnd:
                    listener.upLoadEnd(event);
                    break;
                case upLoadError:
                    listener.upLoadError(event);
                    break;
                case create:
                    listener.create(event);
                    break;
                case beforCopy:
                    listener.beforCopy(event);
                    break;
                case copyEnd:
                    listener.copyEnd(event);
                    break;
                case save:
                    listener.save(event);
                    break;
                case beforUpdate:
                    listener.beforUpdate(event);
                    break;
                case updateEnd:
                    listener.updateEnd(event);
                    break;


                default:
                    throw new JDSException("Unsupport file event type: " + event.getID(), JDSException.UNSUPPORTPROCESSEVENTERROR);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            logger.warn("Listener execute failed!", e);
        }
    }


    private static void dispatchPackageEvent(final PackageEvent event, final PackageListener listener) {
        try {
            switch (event.getID()) {

                case create:
                    listener.create(event);
                    break;
                case lock:
                    listener.lock(event);
                    break;
                case beforReName:
                    listener.beforReName(event);
                    break;
                case reNameEnd:
                    listener.reNameEnd(event);
                    break;
                case beforMove:
                    listener.beforMove(event);
                    break;
                case moving:
                    listener.moving(event);
                    break;
                case moveEnd:
                    listener.moveEnd(event);
                    break;
                case beforClean:
                    listener.beforClean(event);
                    break;
                case cleanEnd:
                    listener.cleanEnd(event);
                    break;
                case restore:
                    listener.restore(event);
                    break;

                case beforDelete:
                    listener.beforDelete(event);
                    break;
                case deleteing:
                    listener.deleteing(event);
                    break;
                case deleteEnd:
                    listener.deleteEnd(event);
                    break;
                case save:
                    listener.save(event);
                    break;

                case beforCopy:
                    listener.beforCopy(event);
                    break;
                case copying:
                    listener.copying(event);
                    break;
                case copyEnd:
                    listener.copyEnd(event);
                    break;
                default:
                    throw new JDSException("Unsupport folder event type: " + event.getID(), JDSException.UNSUPPORTPROCESSEVENTERROR);
            }
        } catch (Throwable e) {
            logger.warn("Listener execute failed!", e);
        }
    }


    private static void dispatchEIPackageEvent(final PackageEvent event, final EIPackageListener listener) {
        try {
            switch (event.getID()) {

                case create:
                    listener.create(event);
                    break;
                case lock:
                    listener.lock(event);
                    break;
                case beforReName:
                    listener.beforReName(event);
                    break;
                case reNameEnd:
                    listener.reNameEnd(event);
                    break;
                case beforMove:
                    listener.beforMove(event);
                    break;
                case moving:
                    listener.moving(event);
                    break;
                case moveEnd:
                    listener.moveEnd(event);
                    break;
                case beforClean:
                    listener.beforClean(event);
                    break;
                case cleanEnd:
                    listener.cleanEnd(event);
                    break;
                case restore:
                    listener.restore(event);
                    break;

                case beforDelete:
                    listener.beforDelete(event);
                    break;
                case deleteing:
                    listener.deleteing(event);
                    break;
                case deleteEnd:
                    listener.deleteEnd(event);
                    break;
                case save:
                    listener.save(event);
                    break;

                case beforCopy:
                    listener.beforCopy(event);
                    break;
                case copying:
                    listener.copying(event);
                    break;
                case copyEnd:
                    listener.copyEnd(event);
                    break;
                default:
                    throw new JDSException("Unsupport folder event type: " + event.getID(), JDSException.UNSUPPORTPROCESSEVENTERROR);
            }
        } catch (Throwable e) {
            logger.warn("Listener execute failed!", e);
        }
    }
}
