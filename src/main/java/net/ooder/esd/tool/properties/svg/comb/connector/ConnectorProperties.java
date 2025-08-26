package net.ooder.esd.tool.properties.svg.comb.connector;


import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.svg.SVGConnectorBean;
import net.ooder.esd.tool.component.SVGBaseComponent;
import net.ooder.esd.tool.properties.svg.SVGProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;


public class ConnectorProperties extends SVGProperties {

    String svgTag = "Connectors:Bezier";
    ConnectorAttr attr;
    String fromObj;
    String fromPoint = "right";
    String toObj;
    String toPoint = "left";


    public ConnectorProperties(SVGConnectorBean connectorBean) {
        svgTag = connectorBean.getSvgTag();
        OgnlUtil.setProperties((Map) JSON.toJSON(connectorBean.getSvgBean()), this, false, false);
        this.id = connectorBean.getSvgKey().getId();
        attr = new ConnectorAttr(connectorBean);

    }

    public ConnectorProperties() {
        attr = new ConnectorAttr();
    }

    public ConnectorProperties(SVGBaseComponent start, SVGBaseComponent end) {
        attr = new ConnectorAttr(start.getProperties(), end.getProperties());
        this.fromObj = start.getAlias();
        this.toObj = end.getAlias();
    }

    public String getFromObj() {
        return fromObj;
    }

    public void setFromObj(String fromObj) {
        this.fromObj = fromObj;
    }

    public String getFromPoint() {
        return fromPoint;
    }

    public void setFromPoint(String fromPoint) {
        this.fromPoint = fromPoint;
    }

    public String getToObj() {
        return toObj;
    }

    public void setToObj(String toObj) {
        this.toObj = toObj;
    }

    public String getToPoint() {
        return toPoint;
    }

    public void setToPoint(String toPoint) {
        this.toPoint = toPoint;
    }

    public String getSvgTag() {
        return svgTag;
    }

    public void setSvgTag(String svgTag) {
        this.svgTag = svgTag;
    }

    public ConnectorAttr getAttr() {
        return attr;
    }

    public void setAttr(ConnectorAttr attr) {
        this.attr = attr;
    }
}
