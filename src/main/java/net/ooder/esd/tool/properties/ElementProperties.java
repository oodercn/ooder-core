package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.base.ElementFieldBean;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class ElementProperties extends FieldProperties {

    public String nodeName;

    public String html;

    public String className;

    public Map<String, Object> attributes;

    public Boolean selectable;

    public Integer tabindex;

    public ElementProperties() {

    }

    public ElementProperties(ElementFieldBean fieldBean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldBean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);
        }

        this.html = fieldBean.getHtml();
        this.className = fieldBean.getClassName();
        this.width = fieldBean.getWidth();
        this.height = fieldBean.getHeight();
        this.selectable = fieldBean.getSelectable();
        this.tabindex = fieldBean.getTabindex();
        this.attributes = JSONObject.parseObject(fieldBean.getAttributes(), Map.class);

    }




    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public Integer getTabindex() {
        return tabindex;
    }

    public void setTabindex(Integer tabindex) {
        this.tabindex = tabindex;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
