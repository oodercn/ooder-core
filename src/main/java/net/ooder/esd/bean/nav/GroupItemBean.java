package net.ooder.esd.bean.nav;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.GroupCmd;
import net.ooder.esd.annotation.field.GroupFieldAnnotation;
import net.ooder.esd.annotation.event.CustomFormEvent;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.annotation.menu.CustomFormMenu;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.GroupItemAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.field.CustomGroupFieldBean;
import net.ooder.esd.bean.view.NavGroupViewBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.context.MethodRoot;
import net.ooder.esd.dsm.view.field.FieldFormConfig;
import net.ooder.esd.dsm.view.field.FieldItemConfig;
import net.ooder.esd.util.json.DefaultTabItem;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.GroupComponent;
import net.ooder.esd.tool.properties.item.LayoutListItem;
import net.ooder.esd.util.OODUtil;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.ConstructorBean;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.lang.reflect.Constructor;
import java.util.*;

@AnnotationType(clazz = GroupItemAnnotation.class)
public class GroupItemBean<T extends FieldItemConfig> extends LayoutListItem implements CustomBean {


    Boolean iniFold;

    Boolean lazyAppend;

    Boolean autoReload;

    Boolean dynDestory;

    Boolean lazyLoad;

    Class bindService;

    Class customService;

    String euClassName;

    String groupName;

    Boolean displayBar;

    Boolean optBtn;

    Boolean toggleBtn;

    Boolean refreshBtn;

    Boolean infoBtn;

    Boolean closeBtn;

    String html;

    ImagePos imagePos;

    String imageBgSize;

    String iconFontCode;

    BorderType borderType;

    Boolean noFrame;

    HAlignType hAlign;

    ToggleIconType toggleIcon;

    Set<CustomFormEvent> event = new LinkedHashSet<>();

    CustomPanelBean panelBean;

    ContainerBean containerBean;

    ConstructorBean constructorBean;


    @JSONField(serialize = false)
    MethodConfig methodConfig;

    @JSONField(serialize = false)
    NavGroupViewBean viewBean;

    GroupCmdBean cmdBean;

    @JSONField(serialize = false)
    public Set<ComponentType> bindTypes = new LinkedHashSet<>();

    @JSONField(serialize = false)
    Class fristClass;

    public String methodName;

    public String entityClassName;

    public String sourceMethodName;

    public String sourceClassName;

    public String domainId;

    public DefaultTabItem tabItem;

    @JSONField(serialize = false)
    MethodRoot methodRoot;

    Set<CustomFormMenu> bottombarMenu = new LinkedHashSet<>();

    Set<CustomFormMenu> customMenu = new LinkedHashSet<>();

    public GroupItemBean() {

    }

    public void update(GroupComponent component) {
        containerBean = new ContainerBean(component);
        if (containerBean == null) {
        } else {
            containerBean.update(component);
        }
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(component.getProperties()), Map.class), this, false, false);
        this.id = component.getProperties().getEuClassName();
    }


    public GroupItemBean(NavGroupViewBean viewBean, GroupComponent component) {
        this.viewBean = viewBean;
        update(component);
    }


    public GroupItemBean(Constructor constructor, NavGroupViewBean viewBean) {
        initViewBean(viewBean);
        initConstructor(constructor);
    }

    public GroupItemBean(MethodConfig methodConfig, NavGroupViewBean viewBean, TabItem tabItem) {
        initViewBean(viewBean);
        initMethodConfig(methodConfig);
        initItem(tabItem);
    }

    public GroupItemBean(Constructor constructor, NavGroupViewBean viewBean, TabItem tabItem) {
        initViewBean(viewBean);
        initConstructor(constructor);
        initItem(tabItem);
    }

    public GroupItemBean(FieldFormConfig<CustomGroupFieldBean, ?> fieldFormConfig) {
        this.domainId = viewBean.getDomainId();
        this.containerBean = viewBean.getContainerBean();
        this.sourceMethodName = viewBean.getMethodName();
        this.sourceClassName = viewBean.getSourceClassName();
        if (fieldFormConfig != null) {
            this.initMethodConfig(fieldFormConfig.getMethodConfig());
        }

    }

    public GroupItemBean(MethodConfig methodConfig, NavGroupViewBean viewBean) {
        initViewBean(viewBean);
        initMethodConfig(methodConfig);
        if (caption == null || !caption.equals("")) {
            this.caption = methodConfig.getCaption();
        }
        if (imageClass == null || imageClass.equals("")) {
            this.imageClass = methodConfig.getImageClass();
        }
    }

    public List<CustomBean> getAnnotationBeans() {
        List<CustomBean> annotationBeans = new ArrayList<>();
        annotationBeans.add(this);
        if (cmdBean != null) {
            annotationBeans.add(cmdBean);
        }
        if (methodRoot != null) {
            annotationBeans.addAll(methodRoot.getAllAnnotation());
        }
        return annotationBeans;
    }


    public MethodRoot getMethodRoot() {
        return methodRoot;
    }

    void initViewBean(NavGroupViewBean viewBean) {
        this.domainId = viewBean.getDomainId();
        this.containerBean = viewBean.getContainerBean();
        this.sourceMethodName = viewBean.getMethodName();
        this.sourceClassName = viewBean.getSourceClassName();
    }


    void initMethodConfig(MethodConfig methodConfig) {
        this.methodConfig = methodConfig;
        this.entityClassName = methodConfig.getSourceClassName();
        this.methodName = methodConfig.getMethodName();
        if (methodConfig.getView()!=null){
            this.imageClass = methodConfig.getView().getImageClass();
            this.containerBean = methodConfig.getView().getContainerBean();
        }

        this.euClassName = methodConfig.getEUClassName();
        this.name = OODUtil.formatJavaName(methodConfig.getMethodName(), true);
        Class[] paramClass = methodConfig.getMethod().getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }
        GroupItemAnnotation tabViewAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), GroupItemAnnotation.class);
        if (tabViewAnnotation != null) {
            fillData(tabViewAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(GroupItemAnnotation.class, this);
        }

        GroupFieldAnnotation groupFieldAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), GroupFieldAnnotation.class);
        if (groupFieldAnnotation != null) {
            AnnotationUtil.fillBean(groupFieldAnnotation, this);
        }

        GroupCmd groupCmd = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), GroupCmd.class);
        if (groupCmd != null) {
            cmdBean = new GroupCmdBean(groupCmd);
        }
        panelBean = new CustomPanelBean(AnnotationUtil.getAllAnnotations(methodConfig.getMethod(), true));
    }


    void initConstructor(Constructor constructor) {
        constructorBean = new ConstructorBean(constructor);
        Class[] paramClass = constructor.getParameterTypes();
        if (paramClass.length > 0) {
            this.fristClass = paramClass[0];
        }
        this.containerBean = new ContainerBean(AnnotationUtil.getAllConstructorAnnotations(constructor));
        panelBean = new CustomPanelBean(AnnotationUtil.getAllConstructorAnnotations(constructor));
        GroupItemAnnotation tabViewAnnotation = AnnotationUtil.getConstructorAnnotation(constructor, GroupItemAnnotation.class);
        if (tabViewAnnotation != null) {
            fillData(tabViewAnnotation);
        } else {
            AnnotationUtil.fillDefaultValue(GroupItemAnnotation.class, this);
        }
        GroupCmd groupCmd = AnnotationUtil.getConstructorAnnotation(constructor, GroupCmd.class);
        if (groupCmd != null) {
            cmdBean = new GroupCmdBean(groupCmd);
        }
    }


    void initItem(TabItem tabItem) {
        this.tabItem = new DefaultTabItem(tabItem);
        if (tabItem != null) {
            this.caption = tabItem.getName();
            this.imageClass = tabItem.getImageClass();
            if (tabItem.getBindClass() != null && tabItem.getBindClass().length > 0) {
                this.bindService = tabItem.getBindClass()[0];
            }
            this.dynDestory = tabItem.isDynDestory();
            this.lazyLoad = tabItem.isDynLoad();
            this.iniFold = tabItem.isIniFold();
        }
    }


    @Override
    public String getEuClassName() {
        return euClassName;
    }

    @Override
    public void setEuClassName(String euClassName) {
        this.euClassName = euClassName;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setContainerBean(ContainerBean containerBean) {
        this.containerBean = containerBean;
    }

    public Dock getDock() {
        return dock;
    }

    public void setDock(Dock dock) {
        this.dock = dock;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ImagePos getImagePos() {
        return imagePos;
    }

    public void setImagePos(ImagePos imagePos) {
        this.imagePos = imagePos;
    }

    public String getImageBgSize() {
        return imageBgSize;
    }

    public void setImageBgSize(String imageBgSize) {
        this.imageBgSize = imageBgSize;
    }

    public String getIconFontCode() {
        return iconFontCode;
    }

    public void setIconFontCode(String iconFontCode) {
        this.iconFontCode = iconFontCode;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public Boolean getNoFrame() {
        return noFrame;
    }

    public void setNoFrame(Boolean noFrame) {
        this.noFrame = noFrame;
    }

    public HAlignType gethAlign() {
        return hAlign;
    }

    public void sethAlign(HAlignType hAlign) {
        this.hAlign = hAlign;
    }

    public ToggleIconType getToggleIcon() {
        return toggleIcon;
    }

    public void setToggleIcon(ToggleIconType toggleIcon) {
        this.toggleIcon = toggleIcon;
    }


    public Boolean getToggleBtn() {
        return toggleBtn;
    }

    public void setToggleBtn(Boolean toggleBtn) {
        this.toggleBtn = toggleBtn;
    }

    public Set<CustomFormMenu> getBottombarMenu() {
        return bottombarMenu;
    }

    public void setBottombarMenu(Set<CustomFormMenu> bottombarMenu) {
        this.bottombarMenu = bottombarMenu;
    }

    public Set<CustomFormMenu> getCustomMenu() {
        return customMenu;
    }

    public void setCustomMenu(Set<CustomFormMenu> customMenu) {
        this.customMenu = customMenu;
    }


    public Boolean getDisplayBar() {
        return displayBar;
    }

    public void setDisplayBar(Boolean displayBar) {
        this.displayBar = displayBar;
    }

    public Boolean getOptBtn() {
        return optBtn;
    }

    public void setOptBtn(Boolean optBtn) {
        this.optBtn = optBtn;
    }

    public Boolean getRefreshBtn() {
        return refreshBtn;
    }

    public void setRefreshBtn(Boolean refreshBtn) {
        this.refreshBtn = refreshBtn;
    }

    public Boolean getInfoBtn() {
        return infoBtn;
    }

    public void setInfoBtn(Boolean infoBtn) {
        this.infoBtn = infoBtn;
    }

    public Boolean getCloseBtn() {
        return closeBtn;
    }

    public void setCloseBtn(Boolean closeBtn) {
        this.closeBtn = closeBtn;
    }

    public Class getCustomService() {
        return customService;
    }

    public void setCustomService(Class customService) {
        this.customService = customService;
    }

    @JSONField(serialize = false)
    public MethodConfig getMethodConfig() {
        if (methodConfig == null) {
            try {
                if (euClassName != null) {
                    methodConfig = ESDFacrory.getAdminESDClient().getMethodAPIBean(euClassName, DSMFactory.getInstance().getDefaultProjectName());
                    if (methodConfig == null) {
                        EUModule euModule = ESDFacrory.getAdminESDClient().getModule(euClassName, DSMFactory.getInstance().getDefaultProjectName());
                        if (euModule != null && euModule.getComponent() != null) {
                            methodConfig = euModule.getComponent().getMethodAPIBean();
                        }
                    }
                } else if (this.getEntityClassName() != null) {
                    ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(this.getEntityClassName());
                    methodConfig = apiClassConfig.getMethodByName(this.getMethodName());

                }
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

        return methodConfig;
    }

    public void setMethodConfig(MethodConfig methodConfig) {
        this.methodConfig = methodConfig;
    }

    public String getSourceMethodName() {
        return sourceMethodName;
    }


    public void setSourceMethodName(String sourceMethodName) {
        this.sourceMethodName = sourceMethodName;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof GroupItemBean) {
            return ((GroupItemBean) obj).getId().equals(this.getId());
        }
        return super.equals(obj);
    }

    public NavGroupViewBean getViewBean() {
        return viewBean;
    }

    public void setViewBean(NavGroupViewBean viewBean) {
        this.viewBean = viewBean;
    }

    public GroupItemBean<T> fillData(GroupItemAnnotation annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMethodName() {
        if (methodName == null) {
            methodName = name;
        }
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Boolean getLazyAppend() {
        return lazyAppend;
    }

    public void setLazyAppend(Boolean lazyAppend) {
        this.lazyAppend = lazyAppend;
    }


    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }

    public Boolean getDynDestory() {
        return dynDestory;
    }

    public void setDynDestory(Boolean dynDestory) {
        this.dynDestory = dynDestory;
    }


    public Boolean getAutoReload() {
        return autoReload;
    }

    public void setAutoReload(Boolean autoReload) {
        this.autoReload = autoReload;
    }


    public Set<CustomFormEvent> getEvent() {
        return event;
    }

    public void setEvent(Set<CustomFormEvent> event) {
        this.event = event;
    }

    public ConstructorBean getConstructorBean() {
        return constructorBean;
    }

    public void setConstructorBean(ConstructorBean constructorBean) {
        this.constructorBean = constructorBean;
    }

    public Class getFristClass() {
        return fristClass;
    }

    public void setFristClass(Class fristClass) {
        this.fristClass = fristClass;
    }


    public Set<ComponentType> getBindTypes() {
        return bindTypes;
    }

    public void setBindTypes(Set<ComponentType> bindTypes) {
        this.bindTypes = bindTypes;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }


    public Boolean getLazyLoad() {
        return lazyLoad;
    }

    public void setLazyLoad(Boolean lazyLoad) {
        this.lazyLoad = lazyLoad;
    }

    public Class getBindService() {
        return bindService;
    }

    public void setBindService(Class bindService) {
        this.bindService = bindService;
    }

    public DefaultTabItem getTabItem() {
        return tabItem;
    }

    public void setTabItem(DefaultTabItem tabItem) {
        this.tabItem = tabItem;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getId() {
        if (id == null || id.equals("")) {
            if (this.getTabItem() != null) {
                id = this.getTabItem().getType();
            } else {
                id = this.getGroupName();
            }
        }
        return id;
    }

    public void setMethodRoot(MethodRoot methodRoot) {
        this.methodRoot = methodRoot;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        if (groupName == null || groupName.equals("")) {
            if (getBindService() != null && !getBindService().equals(Void.class) && !getBindService().equals(Enum.class)) {
                ApiClassConfig entityConfig = null;
                try {
                    entityConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(getBindService().getName());
                    MethodConfig editorMethod = entityConfig.getMethodByEvent(CustomTabsEvent.TABEDITOR);
                    MethodConfig loadChildMethod = entityConfig.getTabsEvent(CustomTabsEvent.TABCHILD);
                    if (editorMethod != null) {
                        groupName = editorMethod.getEUClassName();
                    } else if (loadChildMethod != null) {
                        groupName = loadChildMethod.getEUClassName();
                    } else {
                        if (fristClass != null) {
                            groupName = fristClass.getName();
                        }

                    }
                } catch (JDSException e) {
                    e.printStackTrace();
                }
            } else if (methodConfig != null) {
                groupName = methodConfig.getEUClassName();
            } else if (fristClass != null) {
                groupName = fristClass.getName();
            }
        }


        return groupName;
    }


    public GroupCmdBean getCmdBean() {
        return cmdBean;
    }

    public void setCmdBean(GroupCmdBean cmdBean) {
        this.cmdBean = cmdBean;
    }

    public CustomPanelBean getPanelBean() {
        return panelBean;
    }

    public ContainerBean getContainerBean() {
        return containerBean;
    }

    public void setPanelBean(CustomPanelBean panelBean) {
        this.panelBean = panelBean;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
