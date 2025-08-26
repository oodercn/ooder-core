package net.ooder.esd.bean.nav;

import com.alibaba.fastjson.annotation.JSONField;

import net.ooder.annotation.IconEnumstype;
import net.ooder.esd.util.json.BindClassArrDeserializer;

public interface IButtonLayoutItem extends IconEnumstype {

    public String getComment();

    public void setComment(String comment);

    public void setCaption(String caption);

    public String getCaption();

    public String getImagePos();

    public void setImagePos(String imagePos);

    public String getImageBgSize();

    public void setImageBgSize(String imageBgSize);

    public String getImageRepeat();

    public void setImageRepeat(String imageRepeat);

    public String getIconFontSize();

    public void setIconFontSize(String iconFontSize);

    public String getIconFontCode();

    public void setIconFontCode(String iconFontCode);

    public String getIconStyle();

    public void setIconStyle(String iconStyle);

    public String getFlagText();

    public void setFlagText(String flagText);

    public String getFlagClass();

    public void setFlagClass(String flagClass);

    public String getFlagStyle();

    public void setFlagStyle(String flagStyle);

    public String getValueSeparator();

    public void setValueSeparator(String valueSeparator);

    public String getBindClassName();

    public void setBindClassName(String bindClassName);

    //兼容已有数据绑定转换
    @JSONField(deserializeUsing = BindClassArrDeserializer.class)
    public Class[] getBindClass();

}
