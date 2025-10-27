package net.ooder.esd.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.AnnotationType;
import net.ooder.annotation.CustomBean;
import net.ooder.esd.annotation.LayoutItemAnnotation;
import net.ooder.esd.annotation.ui.AttachmentType;
import net.ooder.esd.annotation.ui.OverflowType;
import net.ooder.esd.annotation.ui.PosType;
import net.ooder.esd.tool.properties.item.LayoutListItem;
import net.ooder.esd.util.json.BindClassArrDeserializer;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

@AnnotationType(clazz = LayoutItemAnnotation.class)
public class CustomLayoutItemBean implements CustomBean {


    String id;

    Integer max;

    Integer min;

    Integer size;

    Boolean folded;

    Boolean locked;

    Boolean hidden;

    Boolean cmd;

    OverflowType overflow;

    PosType pos;

    String expression;

    String panelBgClr;

    String panelBgImg;

    String panelBgImgPos;

    String panelBgImgRepeat;

    AttachmentType panelBgImgAttachment;

    String itemClass;

    @JSONField(deserializeUsing = BindClassArrDeserializer.class)
    Class[] bindClass;

    String url;

    String parentAlias;

    Boolean flexSize;

    Boolean transparent;

    String title;

    String euClassName;


    public CustomLayoutItemBean(LayoutListItem layoutItem) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(layoutItem), Map.class), this, false, false);
        this.bindClass = layoutItem.getBindClass();
    }


    public CustomLayoutItemBean() {
        AnnotationUtil.fillDefaultValue(LayoutItemAnnotation.class, this);
    }

    public CustomLayoutItemBean(LayoutItemAnnotation itemAnnotation) {
        AnnotationUtil.fillBean(itemAnnotation, this);
        if (itemAnnotation.id().equals(AnnotationUtil.getDefaultValue(LayoutItemAnnotation.class, "id")) && pos != null) {
            this.id = pos.name();
        }
    }

    public Class[] getBindClass() {
        return bindClass;
    }

    public void setBindClass(Class[] bindClass) {
        this.bindClass = bindClass;
    }


    public String getParentAlias() {
        return parentAlias;
    }

    public void setParentAlias(String parentAlias) {
        this.parentAlias = parentAlias;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public String getPanelBgClr() {
        return panelBgClr;
    }

    public void setPanelBgClr(String panelBgClr) {
        this.panelBgClr = panelBgClr;
    }

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getTransparent() {
        return transparent;
    }

    public void setTransparent(Boolean transparent) {
        this.transparent = transparent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        if (min == null || min == 0) {
            min = 20;
        }
        this.min = min;
    }

    public Integer getMax() {
        if (max == null || max == 0) {
            max = 300;
        }
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Boolean getFolded() {
        return folded;
    }

    public void setFolded(Boolean folded) {
        this.folded = folded;
    }

    public Boolean getCmd() {
        return cmd;
    }

    public void setCmd(Boolean cmd) {
        this.cmd = cmd;
    }

    public PosType getPos() {
        return pos;
    }


    public String getPanelBgImg() {
        return panelBgImg;
    }

    public void setPanelBgImg(String panelBgImg) {
        this.panelBgImg = panelBgImg;
    }

    public String getPanelBgImgPos() {
        return panelBgImgPos;
    }

    public void setPanelBgImgPos(String panelBgImgPos) {
        this.panelBgImgPos = panelBgImgPos;
    }

    public String getPanelBgImgRepeat() {
        return panelBgImgRepeat;
    }

    public void setPanelBgImgRepeat(String panelBgImgRepeat) {
        this.panelBgImgRepeat = panelBgImgRepeat;
    }

    public AttachmentType getPanelBgImgAttachment() {
        return panelBgImgAttachment;
    }

    public void setPanelBgImgAttachment(AttachmentType panelBgImgAttachment) {
        this.panelBgImgAttachment = panelBgImgAttachment;
    }

    public Boolean getFlexSize() {
        return flexSize;
    }

    public void setFlexSize(Boolean flexSize) {
        this.flexSize = flexSize;
    }

    public void setPos(PosType pos) {
        this.pos = pos;
    }

    public OverflowType getOverflow() {
        return overflow;
    }

    public void setOverflow(OverflowType overflow) {
        this.overflow = overflow;
    }

    public String getEuClassName() {
        return euClassName;
    }

    public void setEuClassName(String euClassName) {
        this.euClassName = euClassName;
    }

    public String toEnumsStr() {
        StringBuffer enumBuffer = new StringBuffer();
        if (bindClass != null && bindClass.length > 0) {
            for (Class clazz : bindClass) {
                enumBuffer.append(clazz.getName() + ".class,");
            }
        } else {
            enumBuffer.append("null");
        }
        String enumStr = enumBuffer.toString();
        if (enumStr.endsWith(",")) {
            enumStr = enumStr.substring(0, enumStr.length() - 1);
        }
        return enumStr;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
