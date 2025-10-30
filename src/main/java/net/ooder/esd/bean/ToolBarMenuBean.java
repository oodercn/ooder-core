package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import javassist.NotFoundException;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.CustomMenu;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.field.CustomListBean;
import net.ooder.esd.bean.field.FieldBaseBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.custom.component.CustomToolsBar;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.ToolBarComponent;
import net.ooder.esd.tool.properties.ToolBarProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.util.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.*;

@AnnotationType(clazz = ToolBarMenu.class)
@CustomClass(viewType = CustomViewType.COMPONENT, clazz = CustomToolsBar.class, componentType = ComponentType.TOOLBAR)
public class ToolBarMenuBean<T extends Enum> extends FieldBaseBean<ToolBarComponent> {

    public String domainId;

    public String viewInstId;

    public String id;

    public String xpath;

    String sourceClassName;

    String methodName;

    Class serviceClass;

    public Boolean showCaption;

    public HAlignType hAlign;

    public VAlignType vAlign;

    public String groupId;

    public Boolean handler;

    public Boolean formField;

    public Class bindService;

    public PositionType position;

    public Boolean disabled;

    public Boolean lazy;

    public Boolean dynLoad;

    public String iconFontSize;

    public Dock dock;


    Boolean autoIconColor = false;

    Boolean autoItemColor = false;

    Boolean autoFontColor = false;
    @JSONField(serialize = false)
    public CustomMenu[] menus;


    @JSONField(serialize = false)
    public List<T> enumItems;

    Class[] menuClasses;

    String alias;

    CustomListBean customListBean;


    public ToolBarMenuBean() {


    }


    public void update(ToolBarComponent toolBarComponent) {
        AnnotationUtil.fillDefaultValue(ToolBarMenu.class, this);
        enumItems = new ArrayList<>();
        ToolBarProperties toolBarProperties = toolBarComponent.getProperties();
        List<TreeListItem> treeListItems = toolBarProperties.getItems();
        if (treeListItems != null) {
            for (TreeListItem item : treeListItems) {
                if (item.getEnumItem() != null) {
                    enumItems.add((T) item.getEnumItem());
                }

            }
        }
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(toolBarProperties), Map.class), this, false, false);
    }

    @Override
    public List<JavaSrcBean> update(ModuleComponent moduleComponent, ToolBarComponent component) {
        this.update(component);
        customListBean = new CustomListBean();
        return customListBean.update(moduleComponent, component);
    }

    public ToolBarMenuBean(ToolBarComponent toolBarComponent) {
        AnnotationUtil.fillDefaultValue(ToolBarMenu.class, this);
        update(toolBarComponent);
    }

    public ToolBarMenuBean(ESDField esdField, Set<Annotation> annotations) {
        super(annotations);
        AnnotationUtil.fillDefaultValue(ToolBarMenu.class, this);
        for (Annotation annotation : annotations) {
            if (annotation instanceof ToolBarMenu) {
                fillData((ToolBarMenu) annotation);
            }
        }
        this.customListBean = new CustomListBean(esdField, annotations);
    }

    public ToolBarMenuBean(ToolBarMenu annotation) {
        this.initToolBar(annotation);
    }

    public ToolBarMenuBean(Class clazz, ToolBarMenu annotation) {
        if (annotation != null) {
            initToolBar(annotation);
        } else {
            AnnotationUtil.fillDefaultValue(ToolBarMenu.class, this);
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

    void initToolBar(ToolBarMenu annotation) {

        this.menuClasses = annotation.menuClasses();
        this.menus = annotation.menus();
        this.lazy = annotation.lazy();
        this.showCaption = annotation.showCaption();
        this.dynLoad = annotation.dynLoad();
        this.hAlign = annotation.hAlign();
        this.vAlign = annotation.vAlign();
        this.groupId = annotation.groupId();
        this.handler = annotation.handler();
        this.formField = annotation.formField();
        this.bindService = annotation.bindService();
        this.disabled = annotation.disabled();
        this.iconFontSize = annotation.iconFontSize();
        this.dock = annotation.dock();
        this.position = annotation.position();


        this.autoFontColor = annotation.autoFontColor();
        this.autoIconColor = annotation.autoIconColor();
        this.autoItemColor = annotation.autoItemColor();


    }


    public String getAlias() {
        if (alias == null || alias.equals("")) {
            if (methodName != null) {
                alias = methodName + "Bar";
            } else if (menuClasses != null && menuClasses.length > 0 && menuClasses[0] != null) {
                Class clazz = menuClasses[0];
                alias = clazz.getSimpleName();
            } else if (menus != null && menus.length > 0) {
                CustomMenu menu = menus[0];
                alias = menu.type() + "Bar";
            } else if (id != null) {
                alias = id;
            } else {
                alias = "NullBar";
            }

        }
        return alias;
    }

    public PositionType getPosition() {
        return position;
    }

    public void setPosition(PositionType position) {
        this.position = position;
    }

    public List<T> getEnumItems() {
        return enumItems;
    }

    public void setEnumItems(List<T> enumItems) {
        this.enumItems = enumItems;
    }

    public ToolBarMenuBean fillData(ToolBarMenu annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public CustomListBean getCustomListBean() {
        return customListBean;
    }

    public void setCustomListBean(CustomListBean customListBean) {
        this.customListBean = customListBean;
    }

    @Override
    public String getXpath() {
        return xpath;
    }

    @Override
    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public void setBindService(Class bindService) {
        this.bindService = bindService;
    }


    public CustomMenu[] getMenus() {
        return menus;
    }

    public void setMenus(CustomMenu[] menus) {
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Boolean getHandler() {
        return handler;
    }

    public void setHandler(Boolean handler) {
        this.handler = handler;
    }

    public Boolean getFormField() {
        return formField;
    }

    public void setFormField(Boolean formField) {
        this.formField = formField;
    }

    public Class getBindService() {
        return bindService;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getIconFontSize() {
        return iconFontSize;
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


    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.TOOLBAR;
    }

    @JSONField(serialize = false)
    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = super.getAnnotationBeans();
        if (!annotationBeans.contains(this)) {
            annotationBeans.add(this);
        }
        return annotationBeans;
    }


    @JSONField(serialize = false)
    public Set<Class> getOtherClass() {
        Set<Class> classes = new HashSet<>();
        if (menuClasses != null) {
            for (Class clazz : menuClasses) {
                if (clazz != null && !clazz.equals(Void.class)) {
                    classes.add(clazz);
                }
            }
        }
        if (this.bindService != null && !bindService.equals(Void.class)) {
            classes.add(bindService);
        }

        return classes;
    }
}
