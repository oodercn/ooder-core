package net.ooder.esd.engine.enums;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import javassist.NotFoundException;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.common.JDSException;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.custom.component.CustomMenusBar;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.manager.editor.MenuActionService;
import net.ooder.esd.tool.component.MenuBarComponent;
import net.ooder.esd.tool.properties.MenuBarProperties;
import net.ooder.esd.util.json.CaseEnumsSerializer;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@AnnotationType(clazz = MenuBarMenu.class)
@CustomClass(viewType = CustomViewType.COMPONENT, clazz = CustomMenusBar.class, componentType = ComponentType.MENUBAR)
public class MenuBarBean<T extends Enum> implements CustomBean, Comparable<MenuBarBean> {
    @JSONField(deserializeUsing = CaseEnumsSerializer.class)
    CustomMenuType menuType;

    Class serviceClass = MenuActionService.class;

    String caption;

    String domainId;

    String viewInstId;

    String sourceClassName;

    String methodName;

    String parentId;

    String id;

    Dock dock;

    String top;

    String left;

    Integer index = 100;

    Boolean dynLoad = false;

    Boolean showCaption = true;

    String imageClass;

    Boolean lazy;

    HAlignType hAlign;

    VAlignType vAlign;

    String alias;

    Boolean handler;

    Integer autoShowTime;

    Class[] menuClasses;



    Boolean autoIconColor = true;

    Boolean autoItemColor = false;

    Boolean autoFontColor = false;

    @JSONField(serialize = false)
    CustomMenu[] menus;

    @JSONField(serialize = false)
    public List<T> enumItems;


    Set<String> customMenuServiceClass = new LinkedHashSet<>();

    public MenuBarBean() {

    }

    public MenuBarBean(MenuBarComponent menuBarComponent) {
        this.update(menuBarComponent);
    }

    public void update(MenuBarComponent menuBarComponent) {
        AnnotationUtil.fillDefaultValue(MenuBarMenu.class, this);
        MenuBarProperties menuBarProperties = menuBarComponent.getProperties();
        List<TreeListItem> treeListItems = menuBarProperties.getItems();
        enumItems = new ArrayList<>();
        if (treeListItems != null) {
            for (TreeListItem item : treeListItems) {
                if (item.getEnumItem() != null) {
                    enumItems.add((T) item.getEnumItem());
                }

            }
        }
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(menuBarProperties), Map.class), this, false, false);
    }


    public MenuBarBean(Set<Annotation> annotations) {
        AnnotationUtil.fillDefaultValue(MenuBarMenu.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof MenuBarMenu) {
                initMenuBar((MenuBarMenu) annotation);
            }
        }

    }

    public MenuBarBean(MenuBarMenu menu) {
        initMenuBar(menu);
    }


    public void initMenuBar(MenuBarMenu menu) {
        this.serviceClass = menu.serviceClass();
        this.index = menu.index();
        this.lazy = menu.lazy();
        this.vAlign = menu.vAlign();
        this.hAlign = menu.hAlign();
        this.handler = menu.handler();
        if (!menu.top().equals("")) {
            this.top = menu.top();
        }
        if (!menu.left().equals("")) {
            this.left = menu.left();
        }
        this.autoShowTime = menu.autoShowTime();
        this.dynLoad = menu.dynLoad();
        this.menuClasses = menu.menuClasses();
        menuType = menu.menuType();
        if (!menu.caption().equals("") && menu.showCaption()) {
            caption = menu.caption();
        }
        if (!menu.imageClass().equals("")) {
            imageClass = menu.imageClass();
        }
    }

    public void addMenuClass(Class menuClass) {
        List<Class> menuClassList = new ArrayList<>();
        menuClassList.addAll(Arrays.asList(menuClasses));
        if (!menuClassList.contains(menuClass)) {
            menuClassList.add(menuClass);
            menuClasses = menuClassList.toArray(new Class[]{});
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof MenuBarBean) {
            return ((MenuBarBean) obj).getId().equals(this.getId());
        }
        return super.equals(obj);
    }

    public MenuBarBean(Class clazz, MenuBarMenu menu, MenuBarBean parent) {
        initMenuBar(menu);
        if (menuClasses.length == 0) {
            menuClasses = new Class[]{clazz};
        }

        Class menuClass = menuClasses[0];
        String name = "";
        if (menuClass != null && !menuClass.equals(Void.class)) {
            try {
                ESDClass menuEsdClass = BuildFactory.getInstance().getClassManager().loadAggregation(menuClass.getName());
                name = menuClass.getSimpleName();
                if (!menu.id().equals("")) {
                    name = menu.id();
                }

                if (menu.showCaption()) {
                    if (caption == null || caption.equals("")) {
                        this.caption = menuEsdClass.getDesc();
                    }
                }


                if (imageClass == null || imageClass.equals("")) {
                    this.imageClass = menuEsdClass.getImageClass();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

        if (parent != null) {
            this.menuType = parent.getMenuType();
            this.parentId = parent.getId();
            this.id = parentId + name;
        } else {
            String packageName = menuClass.getPackage().getName();
            try {
                APIConfig apiConfig = APIConfigFactory.getInstance().getAPIConfig(menuClass.getName());
                if (apiConfig != null) {
                    packageName = apiConfig.getPackageName();
                }
                name = packageName + "." + menuClass.getSimpleName();
                this.id = name.replace(".", "_");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public String getAlias() {
        if (alias == null || alias.equals("")) {
            if (methodName != null) {
                alias = methodName + "Menu";
            } else if (menuClasses != null && menuClasses.length > 0 && menuClasses[0] != null) {
                Class clazz = menuClasses[0];
                alias = clazz.getSimpleName();
            } else if (menus != null && menus.length > 0) {
                CustomMenu menu = menus[0];
                alias = menu.type() + "Menu";
            } else if (id != null) {
                alias = id;
            } else if (menuType != null) {
                alias = menuType.getType() + "Menu";
            } else {
                alias = "NullMenu";
            }

        }
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }



    public Boolean getAutoIconColor() {
        return autoIconColor;
    }

    public void setAutoIconColor(Boolean autoIconColor) {
        this.autoIconColor = autoIconColor;
    }

    public Boolean getAutoItemColor() {
        return autoItemColor;
    }

    public void setAutoItemColor(Boolean autoItemColor) {
        this.autoItemColor = autoItemColor;
    }

    public Boolean getAutoFontColor() {
        return autoFontColor;
    }

    public void setAutoFontColor(Boolean autoFontColor) {
        this.autoFontColor = autoFontColor;
    }

    public Integer getAutoShowTime() {
        return autoShowTime;
    }

    public void setAutoShowTime(Integer autoShowTime) {
        this.autoShowTime = autoShowTime;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
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

    public Boolean getShowCaption() {
        return showCaption;
    }

    public void setShowCaption(Boolean showCaption) {
        this.showCaption = showCaption;
    }

    public HAlignType gethAlign() {
        return hAlign;
    }

    public void sethAlign(HAlignType hAlign) {
        this.hAlign = hAlign;
    }

    public VAlignType getvAlign() {
        return vAlign;
    }

    public void setvAlign(VAlignType vAlign) {
        this.vAlign = vAlign;
    }

    public Boolean getHandler() {
        return handler;
    }

    public void setHandler(Boolean handler) {
        this.handler = handler;
    }

    public CustomMenuType getMenuType() {
        return menuType;
    }

    public void setMenuType(CustomMenuType menuType) {
        this.menuType = menuType;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<T> getEnumItems() {
        return enumItems;
    }

    public void setEnumItems(List<T> enumItems) {
        this.enumItems = enumItems;
    }

    public String getId() {
        if (id == null || id.equals("")) {
            id = this.getAlias();
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Boolean getDynLoad() {
        return dynLoad;
    }

    public void setDynLoad(Boolean dynLoad) {
        this.dynLoad = dynLoad;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public Boolean getLazy() {
        return lazy;
    }

    public void setLazy(Boolean lazy) {
        this.lazy = lazy;
    }

    public MenuBarBean fillData(MenuBarMenu annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public int compareTo(MenuBarBean o) {
        if (index == null) {
            return -1;
        }

        if (index != null && o.getIndex() != null) {
            return this.index - o.getIndex();
        }

        return 1;
    }

    public Class getServiceClass() {
        if (serviceClass == null || serviceClass.equals(Void.class)) {
            this.serviceClass = MenuActionService.class;
        }
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

    public CustomMenu[] getMenus() {
        return menus;
    }

    public void setMenus(CustomMenu[] menus) {
        this.menus = menus;
    }

    public Set<String> getCustomMenuServiceClass() {
        return customMenuServiceClass;
    }

    public void setCustomMenuServiceClass(Set<String> customMenuServiceClass) {
        this.customMenuServiceClass = customMenuServiceClass;
    }
}
