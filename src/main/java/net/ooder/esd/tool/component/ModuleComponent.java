package net.ooder.esd.tool.component;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.TypeUtils;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.CaselessStringKeyHashMap;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esb.config.formula.ModuleFormulaInst;
import net.ooder.esd.annotation.ViewGroupType;
import net.ooder.esd.annotation.action.CustomFormAction;
import net.ooder.esd.annotation.action.CustomPageAction;
import net.ooder.esd.annotation.event.*;
import net.ooder.esd.annotation.field.DialogAnnotation;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.bar.DynBar;
import net.ooder.esd.bean.bar.MenuDynBar;
import net.ooder.esd.bean.nav.BtnBean;
import net.ooder.esd.bean.view.CustomModuleBean;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomMethodInfo;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.custom.component.CustomDynLoadView;
import net.ooder.esd.custom.component.CustomModuleComponent;
import net.ooder.esd.custom.properties.BarProperties;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.manager.editor.PluginsFactory;
import net.ooder.esd.manager.plugins.api.APIFactory;
import net.ooder.esd.tool.DSMProperties;
import net.ooder.esd.tool.ModuleViewConfig;
import net.ooder.esd.tool.properties.*;
import net.ooder.esd.tool.properties.form.ComboInputProperties;
import net.ooder.esd.tool.properties.form.FormField;
import net.ooder.esd.tool.properties.form.HiddenInputProperties;
import net.ooder.esd.tool.properties.item.*;
import net.ooder.esd.tool.properties.list.AbsListProperties;
import net.ooder.esd.tool.properties.list.ListDataProperties;
import net.ooder.esd.tool.properties.list.ListFieldProperties;
import net.ooder.esd.tool.properties.list.TreeListProperties;
import net.ooder.esd.util.OODUtil;
import net.ooder.esd.util.json.ComponentsMapDeserializer;
import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.jds.core.esb.task.ExcuteObj;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.RequestParamBean;
import net.ooder.web.util.AnnotationUtil;
import org.mvel2.templates.TemplateRuntime;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ModuleComponent<M extends Component> extends Component<ModuleProperties, ModuleEventEnum> {
    @JSONField(serialize = false)
    protected static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, ModuleComponent.class);

    public String className;

    public String desc;

    public ModuleViewType moduleViewType;

    @JSONField(serialize = false)
    private boolean isCache = true;

    @JSONField(serialize = false)
    public Map<String, Object> valueMap;

    @JSONField(serialize = false)
    public static final String DefaultBoxfix = "Box";

    @JSONField(serialize = false)
    public static final String DefaultTopBoxfix = "Main";

    @JSONField(serialize = false)
    public static final String PAGECTXNAME = "PAGECTX";

    public EUFileType moduleType = EUFileType.EUClass;

    @JSONField(serialize = false)
    public DialogComponent<DialogProperties, DialogEventEnum> dialog;


    @JSONField(serialize = false)
    public PanelComponent panelComponent;

    @JSONField(serialize = false)
    public BlockComponent blockComponent;


    @JSONField(serialize = false)
    public DivComponent divComponent;

    @JSONField(serialize = false)
    public String projectName;

    @JSONField(serialize = false)
    public EUModule euModule;


    @JSONField(serialize = false)
    public CustomModuleBean moduleBean;


    @JSONField(serialize = false)
    MethodConfig methodAPIBean;

    //@JSONField(serialize = false)
    ModuleProperties properties = new ModuleProperties();

    @JSONField(serialize = false)
    M currComponent;

    @JSONField(serialize = false)
    Component navComponent;


    @JSONField(name = "Static")
    ModuleViewConfig viewConfig;

    @JSONField(serialize = false)
    public List<String> dependencies = new ArrayList<>();
    // 需要的模块
    @JSONField(serialize = false)
    public List<String> required = new ArrayList<String>();

    @JSONField(serialize = false)
    public Map<String, ModuleFunction> functions = new HashMap<String, ModuleFunction>();

    public List<ModuleFormulaInst> formulas = new ArrayList<>();

    public Map<String, String> customFunctions = new HashMap<String, String>();

    public String customAppend;

    public String afterAppend;

    public Map<String, String> moduleVar = new HashMap<String, String>();

    private String currComponentAlias;


    public ModuleComponent() {
        super(ComponentType.MODULE);
        this.properties = new ModuleProperties();
        if (components == null) {
            components = new HashMap<String, Component>();
        }

    }


    public ModuleComponent(M currComponent) {
        super(ComponentType.MODULE);
        this.properties = new ModuleProperties();
        if (components == null) {
            components = new HashMap<String, Component>();
        }
        String moduleName = OODUtil.formatJavaName(currComponent.getAlias(), true);
        BlockComponent blockComponent = new BlockComponent(Dock.fill, moduleName + DefaultTopBoxfix);
        blockComponent.getProperties().setBorderType(BorderType.none);
        blockComponent.addChildren(currComponent);
        this.addChildren(blockComponent);
        this.setCurrComponent(currComponent);

    }


    public void setCurrComponent(M currComponent) {
        this.currComponent = currComponent;
        if (currComponent != null) {
            this.currComponentAlias = currComponent.getAlias();
            if (currComponent.getParent() == null) {
                currComponent.setParent(this);
            }
            this.components.put(currComponentAlias, currComponent);
            ComponentType type = ComponentType.fromType(currComponent.getKey());
            moduleViewType = ModuleViewType.getModuleViewByCom(type);
            properties.setCurrComponentAlias(currComponentAlias);
        } else {
            this.currComponentAlias = null;
            properties.setCurrComponentAlias(null);
        }


    }


    @JSONField(deserializeUsing = ComponentsMapDeserializer.class, serialize = false)
    Map<String, Component> components;


    @JSONField(serialize = false)
    public MethodConfig getMethodAPIBean() {
        if (methodAPIBean == null) {
            try {
                if (projectName == null) {
                    projectName = DSMFactory.getInstance().getDefaultProjectName();
                }
                DSMProperties dsmProperties = this.getProperties().getDsmProperties();
                if (dsmProperties != null) {
                    if (dsmProperties.getSourceClassName() != null && dsmProperties.getSourceMethodName() != null) {
                        ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(dsmProperties.getSourceClassName());
                        if (apiClassConfig != null) {
                            methodAPIBean = apiClassConfig.getMethodByName(dsmProperties.getSourceMethodName());
                        }
                    }
                }

                if (methodAPIBean == null && className != null) {
                    methodAPIBean = ESDFacrory.getAdminESDClient().getMethodAPIBean(className, this.projectName);
                }

            } catch (JDSException e) {
                e.printStackTrace();
            }
        }

        return methodAPIBean;
    }

    public void reBindMethod(MethodConfig methodAPIBean) {
        this.methodAPIBean = methodAPIBean;
        DSMProperties dsmProperties = this.getProperties().getDsmProperties();
        if (dsmProperties != null) {
            dsmProperties.update(methodAPIBean);
        } else {
            dsmProperties = new DSMProperties(methodAPIBean, projectName);
            this.getProperties().setDsmProperties(dsmProperties);
        }

    }

    public ModuleViewConfig getViewConfig() {
        return viewConfig;
    }

    public void setViewConfig(ModuleViewConfig viewConfig) {
        this.viewConfig = viewConfig;
    }


    @JSONField(serialize = false)
    public <T extends Component> T getFristComponentByType(Class<? extends T> clazz) {
        if (components != null && !components.isEmpty()) {
            Set<String> comKey = components.keySet();
            for (String key : comKey) {
                Component component = components.get(key);
                if (clazz.isAssignableFrom(component.getClass())) {
                    return (T) component;
                }
            }
        }

        List<Component> componentList = this.getChildrenRecursivelyList();
        for (Component childComponent : componentList) {
            if (clazz.isAssignableFrom(childComponent.getClass())) {
                return (T) childComponent;
            }
        }
        return null;
    }


    @JSONField(serialize = false)
    public List<Action> getComponentAction(String alias) {
        List<Action> actions = new ArrayList<>();
        for (Action action : getAllAction()) {
            if (action.getTarget() != null && action.getTarget().equals(alias)) {
                actions.add(action);
            }
        }
        return actions;
    }

    @JSONField(serialize = false)
    public ModuleComponent getRealModuleComponent() throws JDSException {
        ModuleComponent moduleComponent = this;
        String dynClass = this.getProperties().getViewClass();
        while (dynClass != null && dynClass.equals(CustomDynLoadView.class.getName())) {
            String className = this.getClassName();

            if (!className.endsWith(CustomViewFactory.dynBuild)) {
                className = this.getClassName() + CustomViewFactory.dynBuild;
            }
            EUModule module = ESDFacrory.getAdminESDClient().getModule(className, this.projectName);
            if (module == null) {
                module = CustomViewFactory.getInstance().getView(className, projectName);
            }
            if (module != null) {
                moduleComponent = module.getComponent();
                dynClass = moduleComponent.getProperties().getViewClass();
            }
        }
        return moduleComponent;
    }


//    @JSONField(serialize = false)
//    public ModuleComponent getRealModuleComponent() throws JDSException {
//        ModuleComponent moduleComponent = this;
//        String dynClass = this.getProperties().getViewClass();
//        while (dynClass != null && dynClass.equals(CustomDynLoadView.class.getName())) {
//            String className = this.getClassName();
////
////            if (!className.endsWith(CustomViewFactory.dynBuild)) {
////                className = this.getClassName() + CustomViewFactory.dynBuild;
////            }
//
//            if (className.endsWith(CustomViewFactory.dynBuild)) {
//                className = moduleComponent.getEuModule().getRealClassName();
//            }
//
//            EUModule module = ESDFacrory.getAdminESDClient().getModule(className, this.projectName);
//            if (module == null) {
//                module = CustomViewFactory.getInstance().getView(className, projectName);
//            }
//            if (module != null) {
//                moduleComponent = module.getComponent();
//                dynClass = moduleComponent.getProperties().getViewClass();
//            }
//        }
//        return moduleComponent;
//    }
//

    @JSONField(serialize = false)
    public List<Action> getAllAction() {
        List<Action> actions = new ArrayList<>();
        List<Component> allComponent = new ArrayList<>();
        allComponent.add(this);
        allComponent.addAll(this.getChildrenRecursivelyList());
        for (Component component : allComponent) {
            Map<Object, Event> events = component.getEvents();
            Set<Object> keySet = events.keySet();
            for (Object key : keySet) {
                Event<Action, ?> event = events.get(key);
                for (Action action : event.getActions()) {
                    actions.add(action);
                }
            }
        }
        return actions;
    }

    @JSONField(serialize = false)
    public List<Action> findAction(String pattern) {
        List<Action> actions = new ArrayList<>();
        List<Component> allComponent = new ArrayList<>();
        allComponent.add(this);
        allComponent.addAll(this.getChildrenRecursivelyList());
        for (Component component : allComponent) {
            Map<Object, Event> events = component.getEvents();
            Set<Object> keySet = events.keySet();
            for (Object key : keySet) {
                Event<Action, ?> event = events.get(key);
                for (Action action : event.getActions()) {
                    if (pattern != null && !pattern.equals("")) {
                        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                        if (p.matcher(JSONObject.toJSONString(action)).find()) {
                            actions.add(action);
                        }
                    } else {
                        actions.add(action);
                    }
                }
            }
        }
        return actions;
    }

    @JSONField(serialize = false)
    public Action findActionById(String actionId) {
        List<Action> actions = findAction(null);

        for (Action action : actions) {
            if (action.getId().equals(actionId)) {
                return action;
            }
        }
        return null;
    }

    public Map<String, String> getModuleVar() {
        return moduleVar;
    }

    public void setModuleVar(Map<String, String> moduleVar) {
        this.moduleVar = moduleVar;
    }

    @JSONField(serialize = false)
    public Component findFristComponentByType(String type) {
        Class clazz = ComponentType.valueOf(type).getClazz();
        Component component = getFristComponentByType(clazz);
        return component;
    }

    @JSONField(serialize = false)
    public Component findComponentByAlias(String alias) {
        List<Component> componentList = this.findComponentsByAlias(alias);
        Component component = null;
        if (componentList.size() > 0) {
            component = componentList.get(0);
        }
        return component;
    }


    @JSONField(serialize = false)
    public List<TabListItem> getTabItems() {
        List<TabListItem> listItems = new ArrayList<>();
        List<Component> componentList = this.getChildrenRecursivelyList();
        for (Component component : componentList) {
            if (component.getProperties() instanceof TabsProperties) {
                TabsProperties properties = (TabsProperties) component.getProperties();
                listItems.addAll(properties.getItems());
            } else if (component.getProperties() instanceof TreeListProperties) {
                TreeListProperties properties = (TreeListProperties) component.getProperties();
                listItems.addAll(properties.getItems());
            }
        }
        return listItems;
    }


    @JSONField(serialize = false)
    public List<ButtonItem> getButtonItems() {
        List<ButtonItem> listItems = new ArrayList<>();
        List<Component> componentList = this.getChildrenRecursivelyList();
        for (Component component : componentList) {
            if (component.getProperties() instanceof AbsListProperties) {
                AbsListProperties properties = (AbsListProperties) component.getProperties();
                List<? extends UIItem> items = properties.getItems();
                if (items != null) {
                    for (UIItem buttonItem : items) {
                        if (buttonItem instanceof ButtonItem) {
                            listItems.add((ButtonItem) buttonItem);
                        }
                    }
                }

            }
        }
        return listItems;
    }


    @JSONField(serialize = false)
    public TreeListItem findBarItemById(String id) {
        TreeListItem item = null;
        List<TreeListItem> listItems = this.getBarItems();
        for (TreeListItem listItem : listItems) {
            if (listItem.getId().equals(id)) {
                item = listItem;
            }
        }
        return item;
    }

    @JSONField(serialize = false)
    public List<TreeListItem> getBarItems() {
        List<TreeListItem> listItems = new ArrayList<>();
        List<Component> componentList = this.getChildrenRecursivelyList();
        for (Component component : componentList) {
            if (component.getProperties() instanceof BarProperties) {
                BarProperties properties = (BarProperties) component.getProperties();
                List<? extends TreeListItem> items = properties.getItems();
                if (items != null) {
                    for (TreeListItem buttonItem : items) {
                        if (buttonItem instanceof TreeListItem) {
                            listItems.addAll(buttonItem.getChildrenRecursivelyList());
                        }
                    }
                }
            }
        }
        return listItems;
    }

    @JSONField(serialize = false)
    public TreeListItem findMenuItemById(String id) {
        TreeListItem item = null;
        List<TreeListItem> listItems = this.getMenuBarItems();
        for (TreeListItem listItem : listItems) {
            if (listItem.getId().equals(id)) {
                item = listItem;
            }
        }
        return item;
    }

    @JSONField(serialize = false)
    public List<TreeListItem> getMenuBarItems() {
        List<TreeListItem> listItems = new ArrayList<>();
        List<Component> componentList = this.getChildrenRecursivelyList();
        for (Component component : componentList) {
            if (component.getProperties() instanceof ListDataProperties) {
                ListDataProperties properties = (ListDataProperties) component.getProperties();
                List<? extends TreeListItem> items = properties.getItems();
                if (items != null) {
                    for (TreeListItem buttonItem : items) {
                        if (buttonItem instanceof TreeListItem) {
                            listItems.addAll(buttonItem.getChildrenRecursivelyList());
                        }
                    }
                }
            }
        }
        return listItems;
    }


    @JSONField(serialize = false)
    public ButtonItem findButtonItemById(String id) {
        ButtonItem item = null;
        List<ButtonItem> listItems = this.getButtonItems();
        for (ButtonItem listItem : listItems) {
            if (listItem.getId().equals(id)) {
                item = listItem;
            }
        }
        return item;
    }


    @JSONField(serialize = false)
    public UIItem findListItemById(String id) {
        UIItem item = null;
        List<UIItem> listItems = this.getListItems();
        for (UIItem listItem : listItems) {
            if (listItem.getId().equals(id)) {
                item = listItem;
            }
        }
        return item;
    }

    @JSONField(serialize = false)
    public List<UIItem> getListItems() {
        List<UIItem> listItems = new ArrayList<>();
        List<Component> componentList = this.getChildrenRecursivelyList();
        for (Component component : componentList) {
            if (component.getProperties() instanceof GridProperties) {
                GridProperties properties = (GridProperties) component.getProperties();
                List<? extends CmdItem> cmdItems = properties.getTagCmds();
                if (cmdItems != null) {
                    for (CmdItem cmdItem : cmdItems) {
                        listItems.add(cmdItem);
                    }
                }
                List<? extends Header> headerItems = properties.getHeader();
                if (headerItems != null) {
                    for (Header header : headerItems) {
                        listItems.add(header);
                    }
                }
            } else if (component.getProperties() instanceof GalleryProperties) {
                GalleryProperties properties = (GalleryProperties) component.getProperties();
                List<? extends CmdItem> cmdItems = properties.getTagCmds();
                if (cmdItems != null) {
                    for (CmdItem cmdItem : cmdItems) {
                        listItems.add(cmdItem);
                    }
                }

            } else if (component.getProperties() instanceof AbsListProperties) {
                AbsListProperties properties = (AbsListProperties) component.getProperties();
                List<? extends UIItem> items = properties.getItems();
                if (items != null) {
                    for (UIItem cmdItem : items) {
                        listItems.add(cmdItem);

                    }
                }
            } else if (component.getProperties() instanceof ListFieldProperties) {
                ListFieldProperties properties = (ListFieldProperties) component.getProperties();
                List<? extends TreeListItem> items = properties.getItems();
                if (items != null) {
                    for (TreeListItem treeListItem : items) {
                        listItems.addAll(treeListItem.getChildrenRecursivelyList());
                    }
                }
            }

        }
        return listItems;
    }


    @JSONField(serialize = false)
    public List<CmdItem> getCmdItems() {
        List<CmdItem> listItems = new ArrayList<>();
        List<Component> componentList = this.getChildrenRecursivelyList();
        for (Component component : componentList) {
            if (component.getProperties() instanceof AbsListProperties) {
                AbsListProperties properties = (AbsListProperties) component.getProperties();
                List<? extends UIItem> items = properties.getItems();
                if (items != null) {
                    for (UIItem cmdItem : items) {
                        if (cmdItem instanceof CmdItem) {
                            listItems.add((CmdItem) cmdItem);
                        }
                    }
                }

            }
        }
        return listItems;
    }

    @JSONField(serialize = false)
    public CmdItem findCmdItemById(String id) {
        CmdItem item = null;
        List<CmdItem> listItems = this.getCmdItems();
        for (CmdItem listItem : listItems) {
            if (listItem.getId().equals(id)) {
                item = listItem;
            }
        }
        return item;
    }

    @JSONField(serialize = false)
    public TabListItem findTabItemById(String id) {
        TabListItem item = null;
        List<TabListItem> listItems = this.getTabItems();
        for (TabListItem listItem : listItems) {
            if (listItem.getId().equals(id)) {
                item = listItem;
            }
        }
        return item;
    }

    @JSONField(serialize = false)
    public UIItem findItemById(String id) {
        UIItem item = this.findBarItemById(id);
        if (item == null) {
            item = this.findMenuItemById(id);
        }
        if (item == null) {
            item = this.findTabItemById(id);
        }
        if (item == null) {
            item = this.findCmdItemById(id);
        }
        if (item == null) {
            item = this.findButtonItemById(id);
        }
        if (item == null) {
            item = this.findListItemById(id);
        }


        return item;
    }


    @JSONField(serialize = false)
    public List<Component> findComponentsByTarget(String target) {
        List<Component> componentList = new ArrayList<Component>();
        List<Component> components = this.getChildrenRecursivelyList();
        for (Component childcomponent : components) {
            if (childcomponent.getTarget() != null && childcomponent.getTarget().equals(target)) {
                componentList.add(childcomponent);
            }
        }
        return componentList;
    }

    @JSONField(serialize = false)
    public List<Component> findComponentsByAlias(String alias) {
        List<Component> componentList = new ArrayList<Component>();
        if (alias != null) {
            Component component = components.get(alias);
            if (component != null) {
                componentList.add(component);
            } else {
                List<Component> components = this.getChildrenRecursivelyList();
                for (Component childcomponent : components) {
                    if (childcomponent.getAlias() != null && childcomponent.getAlias().equals(alias)) {
                        componentList.add(childcomponent);
                    }
                }

            }
        }
        return componentList;
    }

    @JSONField(serialize = false)
    public Component findComponentByFieldName(String alias) {
        List<Component> componentList = this.findComponentsByFieldName(alias);
        Component component = null;
        if (componentList.size() > 0) {
            component = componentList.get(0);
        }
        return component;
    }


    @JSONField(serialize = false)
    public List<Component> findComponentsByFieldName(String fieldName) {
        List<Component> componentList = new ArrayList<Component>();
        if (fieldName != null) {
            List<Component> allComponent = new ArrayList<>();
            for (String key : components.keySet()) {
                allComponent.add(components.get(key));
            }
            allComponent.add(this);
            //          allComponent.addAll(this.getChildrenRecursivelyList());
            for (Component childcomponent : allComponent) {
                String alias = childcomponent.getAlias();
                if (alias != null) {
                    alias = (OODUtil.formatJavaName(alias, true)).toLowerCase();
                    fieldName = (OODUtil.formatJavaName(fieldName, true)).toLowerCase();
                    if (fieldName.equals(alias)) {
                        componentList.add(childcomponent);
                    }
                }
            }
        }


        return componentList;
    }

    @Override
    public String getAlias() {
        if (alias == null) {
            alias = this.getCurrComponentAlias();
        }
        return alias;
    }

    @JSONField(serialize = false)
    public List<Component> findByPropertiesType(Class propertiesType, String pattern) {
        List<Component> componentList = new ArrayList<Component>();
        List<Component> childComponents = findComponents("", pattern);
        for (Component component : childComponents) {
            if (propertiesType.isAssignableFrom(component.getProperties().getClass())) {
                componentList.add(component);
            }
        }
        return componentList;
    }

    @JSONField(serialize = false)
    public ComponentList findComponents(ComponentType... componentTypes) {
        ComponentList componentList = new ComponentList();
        for (ComponentType componentType : componentTypes) {
            componentList.addAll(findComponents(componentType, null));
        }
        return componentList;
    }

    @JSONField(serialize = false)
    public List<Component> findComponents(ComponentType componentType, String pattern) {
        if (componentType == null) {
            return new ArrayList<>();
        }
        return findComponents(componentType.name(), pattern);
    }

    @JSONField(serialize = false)
    public List<Component> findComponents(String type, String pattern) {

        List<Component> componentList = new ArrayList<Component>();
        Class clazz = null;
        if (type != null && !type.equals("")) {
            clazz = ComponentType.valueOf(type).getClazz();
        }
        List<Component> allcomponentList = new ArrayList<Component>();


        if (components == null || components.isEmpty()) {
            allcomponentList = this.getChildrenRecursivelyList();
        } else {
            Set<String> comKey = components.keySet();
            for (String key : comKey) {
                Component component = components.get(key);
                allcomponentList.add(component);
            }
        }
        for (Component component : allcomponentList) {
            if (clazz == null || clazz.isAssignableFrom(component.getClass())) {
                if (pattern != null && !pattern.equals("")) {
                    Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                    List<Matcher> matchers = new ArrayList<Matcher>();
                    matchers.add(p.matcher(component.getAlias()));
                    String desc = "";
                    if (component.getProperties() != null && component.getProperties().getDesc() != null) {
                        desc = component.getProperties().getDesc();
                        matchers.add(p.matcher(desc));
                    }
                    switch (component.typeKey) {
                        case APICALLER:
                            APICallerComponent apiCallerComponent = (APICallerComponent) component;
                            if (apiCallerComponent.getProperties().getQueryURL() != null) {
                                Matcher urlmatcher = p.matcher(apiCallerComponent.getProperties().getQueryURL());
                                matchers.add(urlmatcher);
                            }

                            break;
                        case TREEGRID:
                            TreeGridComponent treeComponent = (TreeGridComponent) component;
                            List<Header> handlers = treeComponent.getProperties().getHeader();
                            for (Header header : handlers) {
                                if (header.getCaption() != null) {
                                    matchers.add(p.matcher(header.getCaption()));
                                }
                                matchers.add(p.matcher(header.getId()));
                            }
                            break;
                        case GALLERY:
                            GalleryComponent galleryComponent = (GalleryComponent) component;
                            List<GalleryItem> items = galleryComponent.getProperties().getItems();
                            if (items != null) {
                                for (GalleryItem item : items) {
                                    if (item.getCaption() != null) {
                                        if (item.getCaption() != null) {
                                            matchers.add(p.matcher(item.getCaption()));
                                        }
                                        matchers.add(p.matcher(item.getId()));
                                    }
                                }
                            }

                            break;
                        case LIST:
                            ListComponent listComponent = (ListComponent) component;
                            List<TreeListItem> listItems = listComponent.getProperties().getItems();
                            if (listItems != null) {
                                for (UIItem item : listItems) {
                                    if (item.getCaption() != null) {
                                        if (item.getCaption() != null) {
                                            matchers.add(p.matcher(item.getCaption()));
                                        }
                                        matchers.add(p.matcher(item.getId()));
                                    }
                                }
                            }
                            break;
                        case MENUBAR:
                            MenuBarComponent menuComponent = (MenuBarComponent) component;
                            List<TreeListItem> treeitems = menuComponent.getProperties().getItems();
                            if (treeitems != null) {
                                for (TreeListItem item : treeitems) {
                                    if (item.getCaption() != null) {
                                        if (item.getCaption() != null) {
                                            matchers.add(p.matcher(item.getCaption()));
                                        }
                                        matchers.add(p.matcher(item.getId()));
                                    }
                                }
                            }
                            break;
                        case TOOLBAR:
                            ToolBarComponent toolbarComponent = (ToolBarComponent) component;
                            treeitems = toolbarComponent.getProperties().getItems();
                            if (treeitems != null) {
                                for (TreeListItem item : treeitems) {
                                    if (item.getCaption() != null) {
                                        if (item.getCaption() != null) {
                                            matchers.add(p.matcher(item.getCaption()));
                                        }
                                        matchers.add(p.matcher(item.getId()));
                                    }
                                }
                            }
                            break;
                        case TREEBAR:
                            TreeBarComponent treeBarbarComponent = (TreeBarComponent) component;
                            treeitems = treeBarbarComponent.getProperties().getItems();
                            if (treeitems != null) {
                                for (TreeListItem item : treeitems) {
                                    if (item.getCaption() != null) {
                                        if (item.getCaption() != null) {
                                            matchers.add(p.matcher(item.getCaption()));
                                        }
                                        matchers.add(p.matcher(item.getId()));
                                    }
                                }
                            }
                            break;
                        case TREEVIEW:
                            TreeViewComponent treeViewbarComponent = (TreeViewComponent) component;
                            treeitems = treeViewbarComponent.getProperties().getItems();
                            if (treeitems != null) {
                                for (TreeListItem item : treeitems) {
                                    if (item.getCaption() != null) {
                                        if (item.getCaption() != null) {
                                            matchers.add(p.matcher(item.getCaption()));
                                        }
                                        matchers.add(p.matcher(item.getId()));
                                    }
                                }
                            }
                            break;
                        default:
                            break;
                    }

                    for (Matcher cmatchers : matchers) {
                        if (cmatchers.find() && !componentList.contains(component)) {
                            componentList.add(component);
                            continue;
                        }
                    }
                } else {
                    componentList.add(component);
                }
            }
        }

        return componentList;
    }

    public ModuleComponent(EUModule module) {
        super(ComponentType.MODULE);
        reSetModule(module);
    }

    void reSetModule(EUModule module) {
        this.euModule = module;
        this.alias = module.getName();
        this.desc = this.getProperties().getDesc();
        this.className = module.getClassName();
        this.projectName = module.getProjectVersion().getProjectName();
        if (components == null) {
            components = new HashMap<String, Component>();
        }
        CtxBaseComponent ctxBaseComponent = this.getCtxBaseComponent();
        ModuleProperties properties = this.getProperties();
        if (properties == null) {
            properties = new ModuleProperties();
        }

        ComponentList componentList = this.getChildrenRecursivelyList();
        for (Component component : componentList) {
            components.put(component.getAlias(), component);
        }
        this.addChildren(this.getCtxBaseComponent());

        this.setProperties(properties);

        if (viewConfig == null) {
            ModuleViewBean moduleViewBean = this.getModuleBean().getViewConfig();
            if (moduleViewBean != null) {
                viewConfig = new ModuleViewConfig(moduleViewBean);
            } else {
                viewConfig = new ModuleViewConfig();
            }
        }


    }


    @JSONField(serialize = false)
    public Component<PanelProperties, PanelEventEnum> getFristBoxComponent() {
        if (components == null || components.isEmpty()) {
            List<Component> componentList = this.getChildrenRecursivelyList();
            for (Component childComponent : componentList) {
                if (childComponent.getProperties() instanceof PanelProperties) {
                    PanelProperties panelProperties = (PanelProperties) childComponent.getProperties();
                    if (panelProperties.getVisibility() == null || (!panelProperties.getVisibility().equals(VisibilityType.hidden))) {
                        return childComponent;
                    }
                }
            }
        } else {
            Set<String> comKey = components.keySet();
            for (String key : comKey) {
                Component component = components.get(key);
                if (component.getProperties() instanceof PanelProperties) {
                    PanelProperties panelProperties = (PanelProperties) component.getProperties();
                    if (panelProperties.getVisibility() == null || (!panelProperties.getVisibility().equals(VisibilityType.hidden))) {
                        return component;
                    }
                }
            }
        }
        return null;
    }


    @JSONField(serialize = false)
    public Component getTopComponentBox() {
        Component component = null;
        if (components != null && !components.isEmpty()) {
            String moduleName = this.getAlias();
            if (this.getEuModule() != null && this.getEuModule().getName() != null) {
                moduleName = this.getEuModule().getName();
            }
            if (moduleName == null && this.getCurrComponent() != null) {
                moduleName = this.getCurrComponent().getAlias();
                moduleName = OODUtil.formatJavaName(moduleName, true);
            }
            component = this.components.get(moduleName + DefaultTopBoxfix);
            if (component == null) {
                if (moduleName.endsWith(CustomViewFactory.dynBuild)) {
                    moduleName = moduleName.substring(0, moduleName.length() - CustomViewFactory.dynBuild.length());
                    component = this.components.get(moduleName + DefaultTopBoxfix);
                }
            }
        }
        if (component == null) {
            component = this.getFristBoxComponent();
        }
        return component;

    }


    @JSONField(serialize = false)
    public Component getLastBoxComponent() {
        Component component = getFristBoxComponent();
        if (component == null) {
            List<Component> uiComponents = new ArrayList<Component>();
            ComponentList componentList = this.getChildren();
            if (componentList != null) {
                for (Component childcomponent : componentList) {
                    if (!(childcomponent instanceof APICallerComponent) && !(childcomponent instanceof CtxBaseComponent)) {
                        if (childcomponent.getProperties() instanceof ContainerProperties) {
                            ContainerProperties containerProperties = (ContainerProperties) childcomponent.getProperties();
                            if (containerProperties.getVisibility() == null || containerProperties.getVisibility().equals(VisibilityType.visible)) {
                                uiComponents.add(childcomponent);
                            }
                        } else if (childcomponent.getProperties() instanceof DivProperties) {
                            DivProperties divProperties = (DivProperties) childcomponent.getProperties();
                            if (divProperties.getVisibility() == null || divProperties.getVisibility().equals(VisibilityType.visible)) {
                                uiComponents.add(childcomponent);
                            }
                        }
                    }
                }
                if (uiComponents.size() > 0) {
                    while (uiComponents.size() == 1 && uiComponents.get(0).getChildren() != null
                            && uiComponents.get(0).getChildren().size() == 1
                            ) {
                        uiComponents = uiComponents.get(0).getChildren();
                    }
                }

                if (uiComponents.size() > 0) {
                    component = uiComponents.get(0);
                }
            }
        }
        return component;

    }


    @JSONField(serialize = false)
    public String getTitle() {
        String title = this.getProperties().getCaption();
        Component component = this.findComponentByAlias("formTitle");
        if (component != null && component instanceof LabelComponent) {
            title = ((LabelComponent) component).getProperties().getCaption();
        }

        if (title == null || title.equals("")) {
            title = this.getProperties().getDesc();
        }

        if (title == null || title.equals("")) {
            component = this.getTopComponentBox();
            if (component != null) {
                title = component.getProperties().getDesc();
            }
        }


        if (title == null || title.equals("")) {
            component = this.getFristBoxComponent();
            if (component != null && (component.getProperties() instanceof PanelProperties)) {
                PanelProperties properties = (PanelProperties) component.getProperties();
                title = properties.getCaption();
                if (title == null || title.equals("")) {
                    title = properties.getCaption();
                }
            }
        }

        if (title == null || title.equals("")) {
            title = this.getAlias();
        }

        return title;

    }

    @JSONField(serialize = false)
    public String getImageClass() {
        String imgClass = this.getProperties().getImageClass();
        if (imgClass == null || imgClass.equals("")) {
            Component component = getTopComponentBox();
            if (component != null && (component.getProperties() instanceof PanelProperties)) {
                PanelProperties properties = (PanelProperties) component.getProperties();
                imgClass = properties.getImageClass();
            }
        }
        return imgClass;
    }

    public void setChildren(String alias, Component component) {
        if (component != null && !component.getAlias().equals(alias)) {
            component.setAlias(alias);
        }
        this.setChildren(component);
        components.put(alias, component);
    }

    public void setComponents(Map<String, Component> components) {
        this.components = components;
        Map<String, Component> currComponents = new HashMap();
        currComponents.putAll(components);

        Set<String> comkeySet = currComponents.keySet();
        for (String key : comkeySet) {
            Component component = currComponents.get(key);
            String parentKey = component.getHost();
            Component parentComponent = currComponents.get(parentKey);
            if (parentComponent != null) {
                parentComponent.setChildren(component);
            } else {
                this.setChildren(component);
            }
        }

    }


    private Method bindUrl(String methodName, MethodConfig methodAPIBean) {
        Map<String, CustomMethodInfo> bindMethods = new HashMap<>();
        Method method = null;
        try {
            method = this.getClass().getMethod(StringUtility.getSetMethodName(methodName), new Class[]{String.class});
        } catch (NoSuchMethodException e) {
        }
        try {
            if (method == null) {
                method = this.getClass().getDeclaredMethod(StringUtility.getSetMethodName(methodName), new Class[]{String.class});
            }
        } catch (NoSuchMethodException e) {

        }
        if (method != null) {
            if (!bindMethods.containsKey(methodName)) {
                bindMethods.put(methodName, methodAPIBean.getCustomMethodInfo());
                OgnlUtil.setProperty(methodName, methodAPIBean.getUrl(), this, null);
            } else {
                this.logger.error("重复绑定[" + this.className + "===》" + methodName);
            }

        }
        return method;
    }

    private void addToolBar(APICallerComponent component, DynBar toolBar, MethodConfig methodAPIBean) throws JDSException {
        UrlPath urlPath = new UrlPathData(PAGECTXNAME, RequestPathTypeEnum.FORM, "");
        component.getProperties().addRequestData(urlPath);
        if (toolBar != null) {
            if (!methodAPIBean.isModule()) {
                toolBar.addMenu(component);
            } else {
                toolBar.addMenu(methodAPIBean.getEUClassName(), component);
            }
        }


        if (!methodAPIBean.isModule() && (component.getActions() == null || component.getActions().size() == 0)) {
            Component oldcomponent = this.findComponentByAlias(component.getAlias());
            if (oldcomponent == null) {
                addChildren(component);
            }
        }

    }

    private void addComponent(DynBar toolBar, MethodConfig methodAPIBean, ComponentType[] bindTypes) throws JDSException {
        APICallerComponent component = new APICallerComponent(methodAPIBean);
        APICallerProperties properties = component.getProperties();
        Set<CustomMenuItem> bindMenus = properties.getBindMenu();
        if (bindMenus != null && bindMenus.size() > 0) {
            for (CustomMenuItem bindMenu : bindMenus) {
                if (APIFactory.checkComponentType(bindTypes, bindMenu.getBindTypes())) {
                    if (bindMenu.getMenu() != null && bindMenu.getMenu().actions().length > 0) {
                        Action customAction = new Action(bindMenu.getMenu().actions()[0], MenuEventEnum.onMenuSelected);
                        component.setAlias(customAction.getTarget());
                    }
                    Method method = bindUrl(bindMenu.getMethodName(), methodAPIBean);
                    if (method == null) {
                        addToolBar(component, toolBar, methodAPIBean);
                    }
                }
            }
        } else if (toolBar != null) {
            addToolBar(component, toolBar, methodAPIBean);
        }

    }

    public void addApi(List<APICallerComponent> components) {
        for (APICallerComponent component : components) {
            Component oldcomponent = this.findComponentByAlias(component.getAlias());
            if (oldcomponent == null) {
                addChildren(component);
            }
        }
    }


    public void addBindService(Class bindClazz) throws JDSException {
        DSMProperties dsmProperties = this.getProperties().getDsmProperties();
        String domainId = null;
        if (dsmProperties != null) {
            domainId = dsmProperties.getDomainId();
        } else if (this.getMethodAPIBean() != null) {
            domainId = this.getMethodAPIBean().getDomainId();
        }
        if (domainId != null && !domainId.equals("")) {
            ApiClassConfig classConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClazz.getName());
            List<MethodConfig> methodAPIBeans = classConfig.getAllProxyMethods();
            for (MethodConfig methodAPIBean : methodAPIBeans) {
                String alias = bindClazz.getSimpleName() + "_" + methodAPIBean.getMethodName();
                if (findComponentByAlias(alias) == null) {
                    APICallerComponent apiCallerComponent = new APICallerComponent(methodAPIBean);
                    apiCallerComponent.setAlias(alias);
                    this.addChildren(new APICallerComponent(methodAPIBean));
                }
            }

        }

    }

    public void addBindService(Class bindClazz, DynBar toolBar, ComponentType[] bindTypes) throws JDSException {
        if (!bindClazz.equals(Void.class) && !bindClazz.equals(Enum.class)) {
            DynBar bar = PluginsFactory.getInstance().initMenuClass(bindClazz, null);
            if (bar != null && bar instanceof MenuDynBar && toolBar != null && toolBar instanceof MenuDynBar) {
                MenuDynBar menuDynBar = (MenuDynBar) bar;
                ((MenuDynBar) toolBar).addChild((MenuDynBar) bar);
                List<APICallerComponent> components = menuDynBar.getApis();
                addApi(components);
            } else {

                DSMProperties dsmProperties = this.getProperties().getDsmProperties();

                String domainId = null;
                if (dsmProperties != null) {
                    domainId = dsmProperties.getDomainId();
                } else if (this.getMethodAPIBean() != null) {
                    domainId = this.getMethodAPIBean().getDomainId();
                }
                if (domainId != null && !domainId.equals("")) {
                    ApiClassConfig classConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(bindClazz.getName());
                    List<MethodConfig> methodAPIBeans = classConfig.getAllProxyMethods();
                    for (MethodConfig methodAPIBean : methodAPIBeans) {
                        if (toolBar != null && methodAPIBean.getCustomMethodInfo().equals(ComboInputType.split)) {
                            toolBar.addSplit(methodAPIBean.getCustomMethodInfo().getId());
                        } else {
                            addComponent(toolBar, methodAPIBean, bindTypes);
                        }
                    }

                }
            }
        }
    }

    ;

    public List<CustomMethodInfo> checkExpression(List<CustomMethodInfo> fields) {
        List<CustomMethodInfo> successFields = new ArrayList<CustomMethodInfo>();
        List<ExcuteObj> tasks = new ArrayList<ExcuteObj>();
        for (CustomMethodInfo field : fields) {
            if (field.getExpression() != null && !field.getExpression().equals("")) {
                ExcuteObj obj = new ExcuteObj(field.getExpression(), Boolean.class, field);
                tasks.add(obj);
            } else {
                successFields.add(field);
            }
        }
        List<ExcuteObj> results = EsbUtil.parExpression(tasks);
        for (ExcuteObj result : results) {
            if (Boolean.valueOf(result.getObj().toString())) {
                successFields.add((CustomMethodInfo) result.getSource());
            }
        }
        return successFields;
    }

    public void addParams(Map<String, Object> params) {
        Set<String> nameSet = params.keySet();
        for (String name : nameSet) {
            if (name != null && !name.equals("")) {
                this.getCtxBaseComponent().addParams(name, params.get(name), false);
            }
        }
        //this.addChildren(ctxBaseComponent);
    }

    public void clearParams(Map<String, Object> params) {
        this.getCtxBaseComponent().clearParams();
    }

    @JSONField(serialize = false)
    public Map<String, Object> getValueMap() {
        Map<String, Object> formMap = new HashMap<>();
        List<Component> allComponent = getChildrenRecursivelyList();
        for (Component component : allComponent) {
            if (component.getProperties() instanceof FormField) {
                FormField fieldProperties = (FormField) component.getProperties();
                if (fieldProperties.getName() != null && !fieldProperties.getName().equals("") && fieldProperties.getValue() != null && !fieldProperties.getValue().equals("")) {
                    if (fieldProperties instanceof ComboInputProperties) {
                        ComboInputType inputType = ((ComboInputProperties) fieldProperties).getType();
                        ComboType comboType = inputType.getComboType();
                        if (comboType.equals(ComboType.number)) {
                            formMap.put(fieldProperties.getName(), TypeUtils.cast(fieldProperties.getValue(), Float.class, null));
                        }
                    } else {
                        formMap.put(fieldProperties.getName(), fieldProperties.getValue());
                    }

                }
            }
        }
        return formMap;
    }


    public void clearFormValues() {
        List<Component> allComponent = getChildrenRecursivelyList();
        CtxBaseComponent ctxBaseComponent = getCtxBaseComponent();
        List<String> customFieldNames = new ArrayList<>();
        for (Component component : allComponent) {
            if (component.getProperties() instanceof FormField) {
                ((FormField) component.getProperties()).setValue(null);
            }
        }
    }


    public void fillFormValues(Map<String, ?> valueMap, boolean removeCtx) {
        CaselessStringKeyHashMap caselessStringKeyHashMap = new CaselessStringKeyHashMap();
        if (valueMap != null) {
            caselessStringKeyHashMap.putAll(valueMap);
        }
        List<Component> allComponent = getChildrenRecursivelyList();
        CtxBaseComponent ctxBaseComponent = getCtxBaseComponent();
        List<String> customFieldNames = new ArrayList<>();
        for (Component component : allComponent) {
            if (component.getProperties() instanceof FormField) {
                FormField fieldProperties = (FormField) component.getProperties();
                if (fieldProperties.getName() == null) {
                    fieldProperties.setName(component.getAlias());
                }
                if (!(fieldProperties instanceof HiddenInputProperties)) {
                    if (component.getParent().getAlias() != null && !component.getParent().getAlias().equals(ctxBaseComponent.getAlias())) {
                        customFieldNames.add(fieldProperties.getName());
                    }
                }
                if (caselessStringKeyHashMap != null && fieldProperties.getName() != null && caselessStringKeyHashMap.get(fieldProperties.getName().toLowerCase()) != null) {
                    fieldProperties.setValue(caselessStringKeyHashMap.get(fieldProperties.getName().toLowerCase()));
                }
            }
        }
        if (removeCtx) {
            for (String fieldName : customFieldNames) {
                HiddenInputComponent inputComponent = ctxBaseComponent.getFieldByName(fieldName);
                if (inputComponent != null) {
                    HiddenInputProperties properties = inputComponent.getProperties();
                    if (!properties.getPid()) {
                        ctxBaseComponent.removeParams(fieldName);
                    }
//                    else {
//                        inputComponent.setAlias(inputComponent.getAlias() + "Hidden");
//                    }
                }

            }
            if (ctxBaseComponent.getChildren() != null && ctxBaseComponent.getChildren().size() > 0) {
                setChildren(ctxBaseComponent.getAlias(), ctxBaseComponent);
            } else {
                this.removeChildren(ctxBaseComponent);
            }

        }

        List<Component> modules = this.findComponents(ComponentType.MODULE, null);
        for (Component component : modules) {
            ModuleComponent childModule = (ModuleComponent) component;
            if (childModule.getClassName() != null && !childModule.getClassName().equals(this.getClassName())) {
                childModule.fillFormValues(valueMap, removeCtx);
            }
        }

    }

    @JSONField(serialize = false)
    public DialogComponent<DialogProperties, DialogEventEnum> getDialogComponent() {
        if (dialog == null) {
            DialogBean dialogBean = this.getModuleBean().getDialogBean();
            if (dialogBean == null && this.getModuleBean().getPanelType() != null && this.getModuleBean().getPanelType().equals(PanelType.dialog)) {
                dialogBean = new DialogBean();
                dialogBean.setCaption(this.getTitle());
            }

            if (dialogBean != null) {
                if (dialogBean.getImageClass() == null || dialogBean.getImageClass().equals(AnnotationUtil.getDefaultValue(DialogAnnotation.class, "imageClass"))) {
                    dialogBean.setImageClass(this.getModuleBean().getImageClass());
                }
                if (dialogBean.getCaption() == null || dialogBean.getCaption().equals(AnnotationUtil.getDefaultValue(DialogAnnotation.class, "caption"))) {
                    dialogBean.setCaption(this.getModuleBean().getCaption());
                }
                if (dialogBean.getName() == null || dialogBean.getName().equals(AnnotationUtil.getDefaultValue(DialogAnnotation.class, "name"))) {
                    dialogBean.setName(this.getModuleBean().getName());
                }
                if (dialogBean.getMdia()) {
                    dialog = new MDialogComponent(euModule.getName() + ComponentType.DIALOG.name() + DefaultBoxfix, new DialogProperties(dialogBean));
                } else {
                    dialog = new DialogComponent(euModule.getName() + ComponentType.DIALOG.name() + DefaultBoxfix, new DialogProperties(dialogBean));
                }
                dialog.addAction(new Action(CustomPageAction.CLOSE, DialogEventEnum.afterDestroy), false);
            }

        }
        return dialog;
    }


    @JSONField(serialize = false)
    public PanelComponent getModulePanelComponent() {
        CustomPanelBean panelBean = this.getModuleBean().getPanelBean();
        PanelType panelType = this.getModuleBean().getPanelType();
        if (panelComponent == null && panelBean != null && panelType != null && panelType.equals(PanelType.panel)) {
            if (panelBean.getDock() == null || panelBean.getDock().equals(Dock.none)) {
                panelBean.setDock(Dock.fill);
            }
            panelComponent = new PanelComponent(euModule.getName() + ComponentType.PANEL.name() + DefaultBoxfix, new PanelProperties(panelBean));
        }

        if (panelComponent != null && panelBean.getBtnBean() != null) {
            BtnBean btnBean = panelBean.getBtnBean();
            if (btnBean.getRefreshBtn() != null && btnBean.getRefreshBtn()) {
                Action action = new Action(CustomPageAction.RELOAD, PanelEventEnum.onRefresh);
                panelComponent.addAction(action);
            } else if (btnBean.getInfoBtn() != null && btnBean.getInfoBtn()) {
                //todo

            }
        }
        return panelComponent;
    }

    @JSONField(serialize = false)
    public BlockComponent getBlockPanelComponent() {
        CustomBlockBean blockBean = this.getModuleBean().getBlockBean();
        PanelType panelType = this.getModuleBean().getPanelType();
        if (blockComponent == null && blockBean != null && panelType != null && panelType.equals(PanelType.block)) {
            blockComponent = new BlockComponent(euModule.getName() + ComponentType.BLOCK.name() + DefaultBoxfix, new BlockProperties(blockBean));
        } else {
            blockComponent = new BlockComponent(euModule.getName() + ComponentType.BLOCK.name() + DefaultBoxfix, new BlockProperties(Dock.fill));
        }
        return blockComponent;
    }


    @JSONField(serialize = false)
    public DivComponent getDivComponent() {
        CustomDivBean divBean = this.getModuleBean().getDivBean();
        PanelType panelType = this.getModuleBean().getPanelType();
        if (divComponent == null && divBean != null && panelType != null && panelType.equals(PanelType.div)) {
            divComponent = new DivComponent(euModule.getName() + ComponentType.DIV.name() + DefaultBoxfix, new DivProperties(divBean));
        } else {
            divComponent = new DivComponent(euModule.getName() + ComponentType.BLOCK.name() + DefaultBoxfix, new DivProperties(Dock.fill));
        }
        return divComponent;
    }


    //数据对象
    @JSONField(serialize = false)
    public APICallerComponent[] genReLoadAPIComponent(MethodConfig methodAPIBean, String alias) throws JDSException {
        List<APICallerComponent> apiCallerComponents = new ArrayList<APICallerComponent>();
        //刷新调用
        APICallerComponent reloadAPI = new APICallerComponent(methodAPIBean);
        reloadAPI.setAlias(CustomFormAction.RELOAD.getTarget());

        APICallerProperties reloadProperties = reloadAPI.getProperties();
        UrlPathData treepathData = new UrlPathData(alias, RequestPathTypeEnum.FORM, "");
        reloadProperties.addRequestData(treepathData);

        UrlPathData formData = new UrlPathData(alias, ResponsePathTypeEnum.FORM, "data");
        reloadProperties.addResponseData(formData);
        apiCallerComponents.add(reloadAPI);


        return apiCallerComponents.toArray(new APICallerComponent[]{});
    }


    public Map<String, ModuleFunction> getFunctions() {
        return functions;
    }

    public void setFunctions(Map<String, ModuleFunction> functions) {
        this.functions = functions;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getDependencies() {
//        if (dependencies == null || dependencies.isEmpty()) {
//            List<Component> childModules = this.findComponents(ComponentType.Module, null);
//            for (Component child : childModules) {
//                if (!dependencies.contains(child.getModuleComponent().getClassName())) {
//                    dependencies.add(child.getModuleComponent().getClassName());
//                }
//            }
//        }


        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    @JSONField(serialize = false)
    public List<String> getRequired() {
        if (required == null || required.isEmpty()) {
            //this.required.clear();
            //预装载应用
            List<Action> actions = this.getAllAction();
            for (Action action : actions) {
                if (action != null) {
                    List<ActionTypeEnum> moduleAction = Arrays.asList(new ActionTypeEnum[]{ActionTypeEnum.module, ActionTypeEnum.otherModuleCall, ActionTypeEnum.page});
                    String target = action.getTarget();
                    if (target != null && !target.equals("dock") && !target.equals(this.getClassName()) && moduleAction.contains(action.getType())) {
                        if (target.startsWith("@{")) {
                            Map context = JDSActionContext.getActionContext().getContext();
                            target = (String) TemplateRuntime.eval(target, this, context);
                        }

                        if (!required.contains(target)) {
                            try {
                                EUModule euModule = ESDFacrory.getAdminESDClient().getModule(target, this.getProjectName());
                                if (euModule != null) {
                                    required.add(target);
                                }
                            } catch (JDSException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
            //预装载应用
            List<Component> childModules = this.findComponents(ComponentType.MODULE, null);
            for (Component child : childModules) {
                if (!required.contains(child.getModuleComponent().getClassName())) {
                    required.add(child.getModuleComponent().getClassName());
                }
            }
        }
        return required;
    }

    public void setRequired(List<String> required) {
        this.required = required;
    }


    public String getClassName() {
        if (className == null && moduleBean != null) {
            className = moduleBean.getEuClassName();
        }
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public ModuleProperties getProperties() {
        return properties;
    }

    public void setProperties(ModuleProperties properties) {
        this.properties = properties;
    }


    public Map<String, String> getCustomFunctions() {
        return customFunctions;
    }

    public void setCustomFunctions(Map<String, String> customFunctions) {
        this.customFunctions = customFunctions;
    }


    public EUModule getEuModule() {
        if (euModule == null) {
            try {
                euModule = ESDFacrory.getAdminESDClient().getModule(className, projectName);
            } catch (JDSException e) {
                e.printStackTrace();
            }
        }
        return euModule;
    }

    public void setEuModule(EUModule euModule) {
        this.euModule = euModule;
        reSetModule(euModule);
    }

    @JSONField(serialize = false)
    public CtxBaseComponent getCtxBaseComponent() {
        CtxBaseComponent ctxBaseComponent = null;
        ComponentList childs = this.getChildren();

        Component ctxComponent = null;
        if (childs != null && childs.size() > 0) {
            for (Component component : childs) {
                if (component.getAlias().equals(PAGECTXNAME)) {
                    ctxComponent = component;
                }
            }
        }


        if (ctxComponent == null) {
            ctxComponent = this.components.get(PAGECTXNAME);
        }


        if (ctxComponent == null) {
            ctxBaseComponent = new CtxBaseComponent(null);
            this.addChildren(ctxBaseComponent);
        } else if (!(ctxComponent instanceof CtxBaseComponent)) {
            ctxBaseComponent = new CtxBaseComponent(ctxComponent.getChildren());
            this.addChildren(ctxBaseComponent);
            components.put(PAGECTXNAME, ctxBaseComponent);
        } else {
            ctxBaseComponent = (CtxBaseComponent) ctxComponent;
        }

        return ctxBaseComponent;

    }

    public void setCtxBaseComponent(CtxBaseComponent ctxBaseComponent) {
        this.addChildren(ctxBaseComponent);
        components.put(PAGECTXNAME, ctxBaseComponent);
    }


    public EUFileType getModuleType() {
        return moduleType;
    }


    public void setModuleType(EUFileType moduleType) {
        this.moduleType = moduleType;
    }


    @Override
    public String toString() {
        ModuleProperties properties = this.getProperties();
        String title = this.getDesc();
        if (properties != null) {
            title = properties.getCaption();
        }

        if (title == null || title.equals("")) {
            if (this.getTopComponentBox() != null && this.getTopComponentBox().getProperties() != null) {
                title = this.getTopComponentBox().getProperties().getDesc();
            }
        }
        if (title == null) {
            title = this.getClassName();
        }
        return title;
    }

    public void reSet() {
        this.currComponentAlias = null;
        this.currComponent = null;
        this.moduleBean = null;
        this.navComponent = null;
        this.moduleViewType = null;
        this.getProperties().setCurrComponentAlias(null);


    }

    private M deepCheckComponent() {
        M deepComponent = null;
        ViewGroupType[] viewGroupTypes = new ViewGroupType[]{ViewGroupType.LAYOUT, ViewGroupType.NAV, ViewGroupType.MODULE, ViewGroupType.VIEW, ViewGroupType.CHARTS, ViewGroupType.MOBILE};
        List<Component> allComponents = this.getListGroupChild(viewGroupTypes);
        List<Component> childComponents = new ArrayList<>();
        for (Component component : allComponents) {
            String alias = component.getAlias();
            ComponentType componentType = ComponentType.fromType(component.getKey());
            if (!alias.equals(PAGECTXNAME) && !componentType.isBar() && !alias.endsWith(DefaultTopBoxfix)) {
                if (!Arrays.asList(skipComponents).contains(componentType)) {
                    childComponents.add(component);
                } else if (component.getProperties() instanceof ContainerProperties) {
                    ContainerProperties containerProperties = (ContainerProperties) component.getProperties();
                    Integer colLayout = containerProperties.getConLayoutColumns();
                    if (component.getChildren().size() > 1) {
                        childComponents.add(component);
                    } else if (colLayout != null && colLayout > 1) {
                        childComponents.add(component);
                    }
                }
            }
        }

        for (Component component : childComponents) {
            if (deepComponent == null || component.getChildrenRecursivelyList().contains(deepComponent)) {
                deepComponent = (M) component;
            }
        }

        return deepComponent;
    }

    public M getCurrComponent() {
        String curAlias = this.getCurrComponentAlias();
        if (curAlias.endsWith(DefaultTopBoxfix) || curAlias.equals(PAGECTXNAME) || curAlias.endsWith("BottomBlock")) {
            currComponent = null;
        } else if (currComponent == null) {
            List<Component> componentList = this.findComponentsByAlias(curAlias);
            if (componentList != null && componentList.size() > 0) {
                currComponent = (M) componentList.get(0);
            }
        }

        if (currComponent == null) {
            currComponent = deepCheckComponent();
        } else {
            ComponentType componentType = ComponentType.fromType(currComponent.getKey());
            if (componentType.isBar() || Arrays.asList(skipComponents).contains(componentType)) {
                currComponent = deepCheckComponent();
            }
        }


        if (currComponent == null) {
            BlockComponent blockComponent = guessCurrComponent();
            currComponent = (M) blockComponent;
        }

        if (currComponent == null) {
            currComponent = (M) this.getMainBoxComponent();
        }


        if (currComponent != null) {
            this.setCurrComponent(currComponent);
        }
        return currComponent;
    }

    public BlockComponent guessCurrComponent() {
        List<Component> childs = this.getChildren();
        if (childs != null) {
            for (Component component : childs) {
                if (component.getKey().equals(ComponentType.BLOCK.getType())) {
                    BlockComponent blockComponent = (BlockComponent) component;
                    if (!blockComponent.getProperties().getVisibility().equals(VisibilityType.hidden)) {
                        return (BlockComponent) component;
                    }
                }
            }
            for (Component component : childs) {
                if (component.guessCurrComponent() != null) {
                    return (BlockComponent) component;
                }
            }
        }

        return null;
    }

    public Map fillParams(Set<RequestParamBean> paramSet, Map<String, Object> valueMap) {
        Map<String, Object> paramMap = new HashMap<>();
        if (valueMap == null) {
            valueMap = new HashMap<>();
        }

        for (RequestParamBean params : paramSet) {
            String key = params.getParamName();
            if (params.getParamClass() != null && !params.getJsonData() && !ModuleComponent.class.isAssignableFrom(params.getParamClass())) {
                paramMap.put(key, valueMap.get(key));
            }
        }


        List<Component> modules = this.findComponents(ComponentType.MODULE, null);
        for (Component component : modules) {
            ModuleComponent childModule = (ModuleComponent) component;
            if (!childModule.getClassName().equals(this.getClassName())) {
                childModule.fillParams(paramSet, valueMap);
            }
        }

        if (this.getEuModule() != null) {
            Object projectName = paramMap.get("projectVersionName");
            if (projectName == null || projectName.equals("")) {
                paramMap.put("projectVersionName", euModule.getProjectVersion().getVersionName());
                paramMap.put("projectName", euModule.getProjectVersion().getVersionName());
            }
        }

        return paramMap;
    }

    public ModuleComponent clone() {
        ModuleComponent component = null;
        try {
            component = ESDFacrory.getAdminESDClient().cloneModuleComponent(this);
            component.setEuModule(this.getEuModule());
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return component;
    }

    @JSONField(serialize = false)
    public List<Component> getRealComponents(boolean hasApi) throws JDSException {
        ModuleComponent realModuleComponent = getRealModuleComponent();
        return realModuleComponent.getTopComponents(hasApi);
    }

    @JSONField(serialize = false)
    public List<Component> getTopComponents(boolean hasApi) {
        List<Component> components = new ArrayList<>();
        Component fristComponent = getLastBoxComponent();
        if (fristComponent != null) {
            if (fristComponent instanceof DialogComponent) {
                components.addAll(fristComponent.getChildren());
            } else if (
                    (fristComponent.getParent().getProperties() instanceof ContainerProperties ||
                            fristComponent.getParent().getProperties() instanceof DivProperties)
                            && !(this instanceof CustomModuleComponent)
                            && !(fristComponent.getParent() instanceof DialogComponent)
                    ) {
                components.add(fristComponent.getParent());
            } else {
                components.add(fristComponent);
            }
        }
        if (hasApi) {
            components.add(getCtxBaseComponent());
            ComponentList childrens = this.getChildren();
            for (Component component : childrens) {
                if (component.typeKey.equals(ComponentType.APICALLER)) {
                    components.add(component);
                }
            }

            // components.addAll(findComponents(ComponentType.APICaller, null));
        }
        return components;
    }


    public void updateFormula(ModuleFormulaInst formulaInst) throws JDSException {
        List<ModuleFormulaInst> formulaInstList = this.getFormulas();
        ModuleFormulaInst oldformulaInst = this.getFormula(formulaInst.getFormulaInstId());
        if (oldformulaInst != null) {
            oldformulaInst.setExpression(formulaInst.getExpression());
            oldformulaInst.setFormulaType(formulaInst.getFormulaType());
            oldformulaInst.setName(formulaInst.getName());
            oldformulaInst.setParams(formulaInst.getParams());
            oldformulaInst.setParticipantSelectId(formulaInst.getParticipantSelectId());
            oldformulaInst.setExpression(formulaInst.getExpression());
            oldformulaInst.setSelectDesc(formulaInst.getSelectDesc());
            oldformulaInst.setProjectName(formulaInst.getProjectName());
        } else {
            formulaInstList.add(formulaInst);
        }

        List<ModuleFormulaInst> formulaInstMap = this.getFormulas();
        CustomViewBean customViewBean = this.getMethodAPIBean().getView();
        if (!formulaInstMap.equals(customViewBean.getFormulas())) {
            customViewBean.setFormulas(formulaInstMap);
            DSMFactory.getInstance().saveCustomViewBean(customViewBean);
        }

    }


    @JSONField(serialize = false)
    public ModuleFormulaInst getFormula(String formulaId) {
        ModuleFormulaInst formula = null;
        for (ModuleFormulaInst formulaInst : this.getFormulas()) {
            if (formulaInst.getFormulaInstId() != null && formulaInst.getFormulaInstId().equals(formulaId)) {
                formula = formulaInst;
            }
        }
        return formula;
    }


    public List<ModuleFormulaInst> getFormulas() {
        if (formulas.isEmpty() && this.getMethodAPIBean() != null) {
            CustomViewBean viewBean = this.getMethodAPIBean().getView();
            if (viewBean != null && viewBean.getFormulas() != null) {
                formulas.addAll((viewBean).getFormulas());
            }
        }
        return formulas;
    }


    public void setFormulas(List<ModuleFormulaInst> formulas) {
        this.formulas = formulas;
    }


    public boolean isDio() {
        return this.findComponents(ComponentType.DIALOG, null).size() > 0;
    }


    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }

    public Map<String, Component> getComponents() {
        return components;
    }

    public String getCustomAppend() {
        return customAppend;
    }

    public void setCustomAppend(String customAppend) {
        this.customAppend = customAppend;
    }

    public String getAfterAppend() {
        return afterAppend;
    }

    public void setAfterAppend(String afterAppend) {
        this.afterAppend = afterAppend;
    }


    @JSONField(serialize = false)
    public Component getMainBoxComponent() {
        Component mainBlock = null;
        if (this.getEuModule() != null) {
            mainBlock = this.components.get(this.getEuModule().getName() + DefaultTopBoxfix);
        } else if (this.getChildren() != null) {
            for (Component topComponent : this.getChildren()) {
                if (topComponent.getAlias().endsWith(DefaultTopBoxfix) && topComponent instanceof BlockComponent) {
                    mainBlock = topComponent;
                }
            }
        }

        if (mainBlock == null) {
            mainBlock = new BlockComponent(Dock.fill, this.getEuModule().getName() + DefaultTopBoxfix);
        }

        if (mainBlock instanceof BlockComponent) {
            ((BlockComponent) mainBlock).getProperties().setBorderType(BorderType.none);
        }

        return mainBlock;
    }

    @JSONField(serialize = false)
    public String getCurrComponentAlias() {
        String blackBoxName = DefaultTopBoxfix;
        Component mainBlock = this.getMainBoxComponent();
        if (mainBlock != null) {
            blackBoxName = mainBlock.getAlias();
        }
        if (currComponentAlias == null) {
            currComponentAlias = blackBoxName;
        }
        if (currComponentAlias.equals("") || currComponentAlias.equals(blackBoxName)) {
            String componentAlias = this.properties.getCurrComponentAlias();
            if (componentAlias != null && !componentAlias.equals("") && !componentAlias.equals(blackBoxName)) {
                currComponentAlias = componentAlias;
            }
        }

        if (moduleViewType != null) {
            if (currComponentAlias.equals(blackBoxName) || findComponentByAlias(currComponentAlias) == null) {
                List<Component> components = this.getChildren();
                if (currComponentAlias.equals(blackBoxName) && getModuleBean() != null && mainBlock != null) {
                    components = mainBlock.getChildren();
                }
                if (components != null) {
                    for (Component component : components) {
                        ComponentType type = ComponentType.fromType(component.getKey());
                        if (Arrays.asList(moduleViewType.getComponentTypes()).contains(type)
                                && !component.getAlias().equals(blackBoxName)
                                && !component.getAlias().equals(PAGECTXNAME)) {
                            currComponentAlias = component.getAlias();
                            this.setCurrComponent((M) component);
                        }
                    }
                }

            }

            //循环所有组件
            if (currComponentAlias.equals(blackBoxName)) {
                List<Component> components = this.getChildrenRecursivelyList();
                for (Component component : components) {
                    ComponentType type = ComponentType.fromType(component.getKey());
                    if (Arrays.asList(moduleViewType.getComponentTypes()).contains(type)) {
                        currComponentAlias = component.getAlias();
                        this.setCurrComponent((M) component);
                    }
                }
            }


        }

        return currComponentAlias;
    }

    @JSONField(serialize = false)
    public ModuleViewType getModuleViewType() {
        if (moduleViewType == null && this.getProperties().getDsmProperties() != null) {
            moduleViewType = this.getProperties().getDsmProperties().getModuleViewType();
        }
        if (moduleViewType == null) {
            moduleViewType = guessComponentType();
        }
        return moduleViewType;
    }


    public ModuleViewType guessComponentType() {
        ModuleViewType moduleViewType = null;
        Component currComponent = this.getCurrComponent();
        if (currComponent != null) {
            ComponentType type = ComponentType.fromType(currComponent.getKey());
            if (type.equals(ComponentType.LAYOUT)) {
                ComponentList components = currComponent.getChildren();
                for (Component childComponent : components) {
                    if (childComponent.getTarget() != null && childComponent.getTarget().endsWith(PosType.before.name())) {
                        ComponentType navType = ComponentType.fromType(childComponent.getKey());
                        switch (navType) {
                            case TREEVIEW:
                                moduleViewType = ModuleViewType.NAVTREECONFIG;
                                break;
                            case GROUP:
                                moduleViewType = ModuleViewType.GROUPCONFIG;
                                break;
                            case MENUBAR:
                                moduleViewType = ModuleViewType.NAVMENUBARCONFIG;
                                break;
                            case GALLERY:
                                moduleViewType = ModuleViewType.NAVGALLERYCONFIG;
                                break;
                            case BUTTONLAYOUT:
                                moduleViewType = ModuleViewType.NAVBUTTONLAYOUTCONFIG;
                                break;
                            case FOLDINGTABS:
                                moduleViewType = ModuleViewType.NAVFOLDINGTREECONFIG;
                                break;
                        }
                    }
                }
            }
            if (moduleViewType == null) {
                moduleViewType = ModuleViewType.getModuleViewByCom(type);
            }
        }
        if (moduleViewType == null) {
            moduleViewType = ModuleViewType.BLOCKCONFIG;
        }
        return moduleViewType;
    }


    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    public void setCurrComponentAlias(String currComponentAlias) {
        this.currComponentAlias = currComponentAlias;
    }

    public CustomModuleBean getModuleBean() {
        if (moduleBean == null) {
            MethodConfig methodConfig = this.getMethodAPIBean();
            if (methodConfig != null) {
                moduleBean = methodConfig.getModuleBean();
            } else {
                moduleBean = new CustomModuleBean(this.getProperties());
            }
        }
        return moduleBean;
    }


    public void setModuleBean(CustomModuleBean moduleBean) {
        this.moduleBean = moduleBean;
    }

    @JSONField(serialize = false)
    public Component getNavComponent() {
        Component navComponent = this.getCurrComponent();
        if (navComponent != null && navComponent instanceof LayoutComponent) {
            LayoutComponent layout = (LayoutComponent) navComponent;
            navComponent = layout.getNavComponent();
        }
        return navComponent;
    }

    public void setNavComponent(Component navComponent) {
        this.navComponent = navComponent;
    }
}
