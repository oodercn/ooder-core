package net.ooder.esd.manager.plugins.api.node;


import net.ooder.annotation.RequestType;
import net.ooder.common.util.ClassUtility;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.web.RequestMethodBean;

import java.util.Map;

public class OODAPIConfig implements Comparable<OODAPIConfig> {
    String id;
    String caption;
    Boolean disabled = false;
    APICallerProperties properties;


    public OODAPIConfig(String id, String caption, Boolean disabled) {
        this.id = id;
        this.caption = caption;
        this.disabled = disabled;

    }

    OODAPIConfig(String id, String caption) {
        this.id = id;
        this.caption = caption;
    }

    public OODAPIConfig(MethodConfig methodBean) {
        String str = methodBean.getMetaInfo();
        this.id = methodBean.getUrl();
        this.caption = id + methodBean.getName() + "" + str;
        properties = new APICallerProperties(methodBean);

    }

    public OODAPIConfig(RequestMethodBean methodBean) {
        this.id = methodBean.getMappingBean().getName();
        Map<String, String> params = methodBean.getParamsMap();
        String str = "(";
        if (!methodBean.getRequestType().equals(RequestType.JSON)) {
            for (String paramName : params.keySet()) {
                Class clazz = null;
                try {
                    clazz = ClassUtility.loadClass(params.get(paramName));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                str = str + clazz.getSimpleName() + " " + paramName + ",";
            }
            if (str.endsWith(",")) {
                str = str.substring(0, str.length() - 1);
            }
        }
        str = str + ")";

        if (methodBean.getMethodChinaName() != null) {
            str = str + "[" + methodBean.getMethodChinaName().cname() + "]";
        }


        if (id == null) {
            id = methodBean.getUrl();
        }
        this.caption = id + methodBean.getName() + "" + str;
        properties = new APICallerProperties(methodBean);
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


    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public APICallerProperties getProperties() {
        return properties;
    }

    public void setProperties(APICallerProperties properties) {
        this.properties = properties;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof OODAPIConfig) {
            OODAPIConfig test = (OODAPIConfig) obj;
            return test.getId().equals(this.getId());
        }
        return super.equals(obj);
    }

    @Override
    public int compareTo(OODAPIConfig o) {
        return id.compareTo(o.id);
    }
}