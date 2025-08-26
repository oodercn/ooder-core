package net.ooder.esd.engine.event;

import net.ooder.esd.engine.MySpace;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.vfs.ct.CtVfsService;
import net.ooder.web.util.SpringPlugs;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class EventFactory {

    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    Map<String, APICallerProperties> apiMethodMap = new HashMap<String, APICallerProperties>();

    Map<String, List<Class>> classCache = new HashMap<String, List<Class>>();

    private final MySpace space;

    public static final String IFontMenuJsonFileName = "IServiceJson.json";

    public static final String LoclHostName = "localhost";


    public static final String THREAD_LOCK = "Thread Lock";

    static Map<String, EventFactory> managerMap = new HashMap<String, EventFactory>();

    public static EventFactory getInstance(MySpace space) {
        String path = space.getPath();
        EventFactory manager = managerMap.get(path);
        if (manager == null) {
            synchronized (THREAD_LOCK) {
                if (manager == null) {
                    manager = new EventFactory(space);
                    managerMap.put(path, manager);
                }
            }
        }
        return manager;
    }

    EventFactory(MySpace space) {
        this.space = space;
//
//        initLocal();
//        initServer();

    }

    public CtVfsService getVfsClient() {
        CtVfsService vfsClient = CtVfsFactory.getCtVfsService();
        return vfsClient;
    }


    private List<Class> scannerLocalClass(String packageName) {
        if (packageName == null) {
            packageName = "net.ooder.";
        }
        synchronized (packageName) {
            List<Class> classList = classCache.get(packageName);
            if (classList == null) {
                classList = new ArrayList<Class>();
                classList.addAll(this.scannerPackages(new String[]{packageName}));
                classCache.put(packageName, classList);
            }
            return classList;
        }

    }


    /**
     * 根据包路径获取包及子包下的所有类
     *
     * @param basePackages basePackage
     */
    private Set<Class<?>> scannerPackages(String[] basePackages) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(httpServletRequest.getServletContext());
        SpringPlugs springPlugs = webApplicationContext.getBean(SpringPlugs.class);
        return springPlugs.scannerPackages(basePackages);
    }


}


