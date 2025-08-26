package net.ooder.esd.tool.properties.svg.comb.connector;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.bean.svg.SVGConnectorBean;
import net.ooder.esd.tool.properties.Properties;
import net.ooder.esd.tool.properties.svg.SVGAttr;
import net.ooder.esd.tool.properties.svg.comb.BGText;
import net.ooder.esd.tool.properties.svg.comb.Key;
import net.ooder.esd.tool.properties.svg.comb.Text;
import net.ooder.esd.tool.properties.svg.comb.circle.CircleProperties;
import net.ooder.esd.tool.properties.svg.comb.path.PathProperties;
import net.ooder.esd.tool.properties.svg.comb.rect.RectProperties;


public class ConnectorAttr extends SVGAttr {

    @JSONField(name = "KEY")
    public ConnectorKey KEY;

    public ConnectorAttr() {
        this.KEY = new ConnectorKey();
        this.BG = new BGText();
        this.TEXT = new Text();
    }

    public ConnectorAttr(SVGConnectorBean connectorBean) {
        this.setBG(connectorBean.getSvgBG());
        this.setKEY(connectorBean.getSvgKey());
        this.setTEXT(connectorBean.getSvgText());

    }


    public ConnectorAttr(Properties start, Properties end) {
        Key startKey = null;
        if (start instanceof CircleProperties) {
            startKey = ((CircleProperties) start).getAttr().getKEY();
        } else if (start instanceof RectProperties) {
            startKey = ((RectProperties) start).getAttr().getKEY();
        } else if (start instanceof PathProperties) {
            startKey = ((PathProperties) start).getAttr().getKEY();
        }
        Key endKey = null;
        if (end instanceof CircleProperties) {
            endKey = ((CircleProperties) end).getAttr().getKEY();
        } else if (end instanceof RectProperties) {
            endKey = ((RectProperties) end).getAttr().getKEY();
        } else if (end instanceof PathProperties) {
            endKey = ((PathProperties) end).getAttr().getKEY();
        }

        this.KEY = new ConnectorKey(startKey, endKey);
        this.TEXT = new Text();
        this.BG = new BGText();
    }

    public ConnectorKey getKEY() {
        return KEY;
    }

    public void setKEY(ConnectorKey KEY) {
        this.KEY = KEY;
    }
}
