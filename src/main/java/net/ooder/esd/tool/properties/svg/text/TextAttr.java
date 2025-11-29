package net.ooder.esd.tool.properties.svg.text;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.bean.svg.SVGTextBean;
import net.ooder.esd.tool.properties.svg.SVGAttr;
import net.ooder.esd.tool.properties.svg.comb.Text;


public class TextAttr extends SVGAttr {
    @JSONField(name = "KEY")
    TextKey KEY;


    public TextAttr(SVGTextBean textBean) {
        this.KEY = textBean.getKEY();
    }

    public TextAttr() {
        KEY = new TextKey();
        TEXT = new Text();
    }


    public TextKey getKEY() {
        return KEY;
    }

    public void setKEY(TextKey KEY) {
        this.KEY = KEY;
    }


}
