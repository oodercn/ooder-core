package net.ooder.esd.tool.properties.item;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.LayoutItemAnnotation;
import net.ooder.esd.annotation.ui.AttachmentType;
import net.ooder.esd.annotation.ui.OverflowType;
import net.ooder.esd.annotation.ui.PosType;
import net.ooder.esd.bean.CustomLayoutItemBean;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.util.AnnotationUtil;

import java.util.Map;

public class LayoutListItem extends TabListItem {

    public String id;

    public  Integer max;

    public Integer min;

    public Integer size;

    public  Boolean folded;

    public Boolean locked;

    public Boolean hidden;

    public Boolean cmd;

    public  Boolean cmdDisplay;

    public  Boolean moveDisplay;

    public OverflowType overflow;
    ;
    public PosType pos;

    public  String expression;

    public   String panelBgClr;

    public  String panelBgImg;

    public  String panelBgImgPos;

    public String panelBgImgRepeat;

    public AttachmentType panelBgImgAttachment;

    public String itemClass;


    public  String url;

    public Boolean flexSize;

    public  Boolean transparent;

    public  String title;


    public LayoutListItem(Enum enumType) {
        super(enumType);
    }

    public LayoutListItem() {
        AnnotationUtil.fillDefaultValue(LayoutItemAnnotation.class, this);
    }

    public LayoutListItem(PosType posType) {
        AnnotationUtil.fillDefaultValue(LayoutItemAnnotation.class, this);
        this.id = posType.name();
        this.pos = posType;
        switch (posType) {
            case before:
                this.setSize(260);
                this.setFolded(false);
                this.max = 1000;
                this.min = 100;
                break;
            case main:
                this.flexSize = true;
                break;
            case after:
                this.setSize(260);
                this.setFolded(false);
                this.max = 1000;
                this.min = 180;
                break;

        }
    }


    public LayoutListItem(CustomLayoutItemBean item) {
        if (item == null) {
            AnnotationUtil.fillDefaultValue(LayoutItemAnnotation.class, this);
        } else {
            OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(item), Map.class), this, false, false);
        }

    }

    public LayoutListItem(LayoutItemAnnotation annotation) {
        AnnotationUtil.fillBean(annotation, this);

    }

    public String toEnumsStr() {
        StringBuffer enumBuffer = new StringBuffer();
        if (bindClass != null && bindClass.length > 0) {
            for (Class clazz : bindClass) {
                if (clazz != null) {
                    enumBuffer.append(clazz.getSimpleName() + ".class,");
                }
            }
        }

        if (enumBuffer.toString().equals("")) {
            enumBuffer.append(Void.class.getSimpleName() + ".class");
        }

        String enumStr = enumBuffer.toString();
        if (enumStr.endsWith(",")) {
            enumStr = enumStr.substring(0, enumStr.length() - 1);
        }

        return enumStr;
    }


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Boolean getLocked() {
        return locked;
    }

    @Override
    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    @Override
    public Boolean getHidden() {
        return hidden;
    }

    @Override
    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getCmdDisplay() {
        return cmdDisplay;
    }

    public void setCmdDisplay(Boolean cmdDisplay) {
        this.cmdDisplay = cmdDisplay;
    }

    public Boolean getMoveDisplay() {
        return moveDisplay;
    }

    public void setMoveDisplay(Boolean moveDisplay) {
        this.moveDisplay = moveDisplay;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public void setExpression(String expression) {
        this.expression = expression;
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

    @Override
    public String getItemClass() {
        return itemClass;
    }

    @Override
    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
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

    public OverflowType getOverflow() {
        return overflow;
    }

    public void setOverflow(OverflowType overflow) {
        this.overflow = overflow;
    }

    public PosType getPos() {
        return pos;
    }

    public void setPos(PosType pos) {
        this.pos = pos;
    }


    public String getPanelBgClr() {
        return panelBgClr;
    }

    public void setPanelBgClr(String panelBgClr) {
        this.panelBgClr = panelBgClr;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getFlexSize() {
        return flexSize;
    }

    public void setFlexSize(Boolean flexSize) {
        this.flexSize = flexSize;
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

}
