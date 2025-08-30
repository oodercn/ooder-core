package net.ooder.esd.manager.plugins.api.node;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.cluster.ServerNode;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.EUPackage;
import net.ooder.esd.engine.enums.PackageType;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.RequestMethodBean;


import net.ooder.esd.tool.properties.Properties;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class APIComponentNode implements Comparable<APIComponentNode> {


    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, APIComponentNode.class);

    public String id;

    public String path;

    public String alias;

    public boolean disabled = false;

    public boolean group = false;

    public boolean draggable = true;

    public String key;

    public String cls = "ood.APICaller";

    public String imageClass = "fa-solid fa-code";

    public String caption;

    public String expression;


    public boolean iniFold = false;

    public List<APIComponentNode> sub;

    public Map<String, String> tagVar = new HashMap<String, String>();

    public Properties iniProp;

    public APIComponentNode() {

    }

    public APIComponentNode(String host, String caption, String pattern, String imageClass) {
        this.caption = caption;
        this.id = host + ":" + pattern;
        this.imageClass = imageClass;

    }


    public APIComponentNode(APIPaths paths, boolean hasMethod, String pattern, List<String> filterIds) {

        this.id = paths.getPath();
        this.caption = paths.getDesc();
        this.alias = paths.getName();
        this.path = paths.getPath();
        this.imageClass = paths.getImageClass();
        RequestMethodBean methodBean = APIConfigFactory.getInstance().getRequestMappingBean(id);
        if (methodBean == null) {
            methodBean = APIConfigFactory.getInstance().findMethodBean(id);
        }


        List<APIPaths> childs = paths.getChildren();
        if (paths.getSource() instanceof APICallerProperties) {
            this.iniProp = (APICallerProperties) paths.getSource();
            this.draggable = true;
        }


        for (APIPaths cpath : childs) {
            if (cpath.getSource() instanceof APICallerProperties && hasMethod) {
                APICallerProperties sourceProperties = (APICallerProperties) cpath.getSource();
                APIComponentNode componentNode = new APIComponentNode(new APICallerComponent(sourceProperties.getName(), sourceProperties));

                componentNode.setImageClass(cpath.getImageClass());
                componentNode.setCaption(cpath.getDesc());
                componentNode.setId(cpath.getPath());
                if (filterIds == null || filterIds.contains(cpath.getPath())) {
                    if (pattern != null && !pattern.equals("")) {
                        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                        Matcher namematcher = p.matcher(sourceProperties.getName());
                        Matcher descmatcher = p.matcher(sourceProperties.getDesc());
                        Matcher urlmatcher = p.matcher(sourceProperties.getQueryURL());
                        if (namematcher.find() || descmatcher.find() || urlmatcher.find()) {
                            addSub(componentNode);
                        }
                    } else {
                        addSub(componentNode);
                    }

                }

            } else if (filterIds == null || filterIds.contains(cpath.getPath())) {
                APIComponentNode componentNode = new APIComponentNode(cpath, hasMethod, pattern, filterIds);
                if (componentNode.getSub() != null && componentNode.getSub().size() > 0) {
                    addSub(componentNode);
                }

            }

        }
    }

    public APIComponentNode(ProjectVersion version, String pattern) {
        this(version, pattern, null);
    }

    public APIComponentNode(ProjectVersion version, String pattern, PackageType type) {
        this.imageClass = "fa-solid fa-cubes";
        this.id = version.getVersionName();
        this.caption = version.getDesc() == null ? version.getVersionName() : version.getDesc();
        try {
            List<EUPackage> euPackages = version.getAllPackage(type);
            for (EUPackage euPackage : euPackages) {
                APIComponentNode packageNode = new APIComponentNode(euPackage, pattern);
                if (packageNode.getSub() != null && packageNode.getSub().size() > 0) {
                    addSub(packageNode);
                }
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }
    }


    public APIComponentNode(EUPackage euPackage, String pattern) {
        this.imageClass = euPackage.getImageClass();
        this.id = euPackage.getId();
        this.path = euPackage.getPackageName();
        this.caption = euPackage.getPackageName() + "(" + euPackage.getDesc() + ")";
        this.setIniFold(true);

        Set<EUModule> modules = euPackage.listModules();

        for (EUModule module : modules) {
            if (module.getComponent().findComponents(ComponentType.APICALLER, pattern).size() > 0) {
                addSub(new APIComponentNode(module.getComponent(), pattern));
            }

        }
    }


    public APIComponentNode(ModuleComponent component, String pattern) {
        List<APICallerComponent> apis = component.findComponents(ComponentType.APICALLER, pattern);
        this.caption = component.getDesc();
        this.id = component.getClassName();
        this.imageClass = "fa-solid fa-file-code";
        for (APICallerComponent api : apis) {
            addSub(new APIComponentNode(api));
        }
    }

    public APIComponentNode(ServerNode serverNode) {
        this.id = serverNode.getId();
        this.imageClass = "fa-solid fa-bank";
        this.caption = serverNode.getName();
        this.alias = serverNode.getName();
        this.path = serverNode.getUrl();
    }


    public void addSub(APIComponentNode node) {
        if (sub == null) {
            sub = new ArrayList<APIComponentNode>();
        }
        if (!sub.contains(node)) {
            sub.add(node);
        }
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public APIComponentNode(APICallerProperties properties) {
        this(new APICallerComponent(properties.getName(), properties));
    }

    public APIComponentNode(APICallerComponent component) {
        this.id = component.getProperties().getId();
        component.getProperties().setEvents(component.getEvents());
        this.caption = StringUtility.replace(component.getAlias(), "/", "");
        this.cls = component.getKey();
        this.key = component.getKey();
        this.iniProp = component.getProperties();
        this.alias = component.getAlias();
        this.path = component.getProperties().getId();
    }


    public APIComponentNode(APIConfig config) {
        this.id = config.getClassName();
        this.caption = config.getName() + "[" + config.getUrl() + "]";
        this.cls = config.getClassName();
        this.key = "ood.api.group";
        this.alias = config.getClassName();
        this.path = config.getUrl();
        this.draggable = false;


    }

    public List<APIComponentNode> getSub() {
        if (sub != null) {
            Collections.sort(sub, new Comparator<APIComponentNode>() {
                public int compare(APIComponentNode o1, APIComponentNode o2) {
                    if (o1.getPath() == null || o2.getPath() == null) {
                        return -1;
                    }
                    return o1.getPath().compareTo(o1.getPath());
                }
            });
        }
        return sub;
    }

    public void setSub(List<APIComponentNode> sub) {
        this.sub = sub;
    }


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Properties getIniProp() {
        return iniProp;
    }

    public void setIniProp(Properties iniProp) {
        this.iniProp = iniProp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }


    public boolean isIniFold() {
        return iniFold;
    }

    public void setIniFold(boolean iniFold) {
        this.iniFold = iniFold;
    }

    public Map<String, String> getTagVar() {
        return tagVar;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public void setTagVar(Map<String, String> tagVar) {
        this.tagVar = tagVar;
    }


    @JSONField(serialize = false)
    public ESDClient getClient() throws JDSException {

        ESDClient client = ESDFacrory.getAdminESDClient();

        return client;
    }

    @Override
    public int compareTo(APIComponentNode o) {
        if (o.getPath() == null || this.getPath() == null) {
            return -1;
        }

        return o.getPath().compareTo(this.getPath());
    }
}
