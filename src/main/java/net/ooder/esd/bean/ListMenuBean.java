package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.CustomBean;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.ListMenu;
import net.ooder.esd.tool.properties.list.ListFieldProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;
import javassist.NotFoundException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@AnnotationType(clazz = ListMenu.class)
public class ListMenuBean<T extends ListFieldProperties> implements CustomBean {

    public String domainId;

    public String viewInstId;

    public String id;

    public Class bindService;

    public Boolean lazy;

    public Boolean dynLoad;

    public String iconFontSize;

    public HashSet<CustomMenu> menus;

    String sourceClassName;

    String methodName;

    Class serviceClass;

    Class[] menuClasses;

    public ListMenuBean() {
    }

    public ListMenuBean(T properties) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(properties), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public ListMenuBean(ListMenu annotation) {
        this.initListMenu(annotation);
    }

    public ListMenuBean(Class clazz, ListMenu annotation) {
        if (annotation != null) {
            initListMenu(annotation);
        } else {
            AnnotationUtil.fillDefaultValue(ListMenu.class, this);
        }

        if (menuClasses.length == 0) {
            menuClasses = new Class[]{clazz};
        }

        Class menuClass = menuClasses[0];
        String name = "";
        if (menuClass != null && !menuClass.equals(Void.class)) {
            name = menuClass.getSimpleName();
        }
        String packageName = menuClass.getPackage().getName();
        try {
            APIConfig apiConfig = APIConfigFactory.getInstance().getAPIConfig(menuClass.getName());
            if (apiConfig != null && apiConfig.getPackageName() != null) {
                packageName = apiConfig.getPackageName();
            }
            name = packageName + "." + menuClass.getSimpleName();
            this.id = name.replace(".", "_");
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

    }


    void initListMenu(ListMenu annotation) {

        this.menuClasses = annotation.menuClasses();
        this.menus = new HashSet<>(Arrays.asList(annotation.menus()));
        this.lazy = annotation.lazy();
        this.dynLoad = annotation.dynLoad();
        this.bindService = annotation.bindService();
        this.iconFontSize = annotation.iconFontSize();


    }

    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classes = new HashSet<>();
        if (serviceClass != null) {
            classes.add(serviceClass);
        }
        if (bindService != null) {
            classes.add(bindService);
        }
        if (menuClasses != null) {
            for (Class menuClass : menuClasses) {
                classes.add(menuClass);
            }
        }
        return ClassUtility.checkBase(classes);
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getViewInstId() {
        return viewInstId;
    }

    public void setViewInstId(String viewInstId) {
        this.viewInstId = viewInstId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }


    public void setBindService(Class bindService) {
        this.bindService = bindService;
    }


    public HashSet<CustomMenu> getMenus() {
        return menus;
    }

    public void setMenus(HashSet<CustomMenu> menus) {
        this.menus = menus;
    }

    public Boolean getLazy() {
        return lazy;
    }

    public void setLazy(Boolean lazy) {
        this.lazy = lazy;
    }

    public Boolean getDynLoad() {
        return dynLoad;
    }

    public void setDynLoad(Boolean dynLoad) {
        this.dynLoad = dynLoad;
    }


    public Class getBindService() {
        return bindService;
    }


    public String getIconFontSize() {
        return iconFontSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public Class[] getMenuClasses() {
        return menuClasses;
    }

    public void setMenuClasses(Class[] menuClasses) {
        this.menuClasses = menuClasses;
    }

    public void setIconFontSize(String iconFontSize) {
        this.iconFontSize = iconFontSize;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
