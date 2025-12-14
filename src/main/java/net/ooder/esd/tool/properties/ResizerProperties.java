package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.annotation.ui.UIPositionType;
import net.ooder.esd.util.json.CaseEnumsSerializer;

public class ResizerProperties extends Properties {

    //<< can be used in addResizer({*})
    // handler visible?
    Boolean forceVisible;
    // movable
    String forceMovable;

    // only show right/bottom handlers
    Boolean singleDir;
    // can change width
    Boolean vertical;
    // can chang height
    Boolean horizontal;

    Integer minHeight;
    Integer minWidth;
    Integer maxHeight;
    Integer maxWidth;

    // with px (base: 1em=12px)
    Integer handlerSize;
    // border 1
    Integer handlerOffset;
    Boolean readonly;
    Boolean disabled;
    Boolean leftConfigBtn;
    Boolean rightConfigBtn;
    Boolean rotatable;

    Integer left;
    Integer top;
    Integer height;
    Integer width;
    @JSONField(serializeUsing = CaseEnumsSerializer.class, deserializeUsing = CaseEnumsSerializer.class)
    UIPositionType position;
    String display;
//>>

    public Boolean isForceVisible() {
        return forceVisible;
    }

    public void setForceVisible(Boolean forceVisible) {
        this.forceVisible = forceVisible;
    }

    public String getForceMovable() {
        return forceMovable;
    }

    public void setForceMovable(String forceMovable) {
        this.forceMovable = forceMovable;
    }

    public Boolean isSingleDir() {
        return singleDir;
    }

    public void setSingleDir(Boolean singleDir) {
        this.singleDir = singleDir;
    }

    public Boolean isVertical() {
        return vertical;
    }

    public void setVertical(Boolean vertical) {
        this.vertical = vertical;
    }

    public Boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(Boolean horizontal) {
        this.horizontal = horizontal;
    }

    public Integer getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(Integer minHeight) {
        this.minHeight = minHeight;
    }

    public Integer getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(Integer minWidth) {
        this.minWidth = minWidth;
    }

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
    }

    public Integer getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(Integer maxWidth) {
        this.maxWidth = maxWidth;
    }

    public Integer getHandlerSize() {
        return handlerSize;
    }

    public void setHandlerSize(Integer handlerSize) {
        this.handlerSize = handlerSize;
    }

    public Integer getHandlerOffset() {
        return handlerOffset;
    }

    public void setHandlerOffset(Integer handlerOffset) {
        this.handlerOffset = handlerOffset;
    }

    public Boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public Boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean isLeftConfigBtn() {
        return leftConfigBtn;
    }

    public void setLeftConfigBtn(Boolean leftConfigBtn) {
        this.leftConfigBtn = leftConfigBtn;
    }

    public Boolean isRightConfigBtn() {
        return rightConfigBtn;
    }

    public void setRightConfigBtn(Boolean rightConfigBtn) {
        this.rightConfigBtn = rightConfigBtn;
    }

    public Boolean isRotatable() {
        return rotatable;
    }

    public void setRotatable(Boolean rotatable) {
        this.rotatable = rotatable;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public UIPositionType getPosition() {
        return position;
    }

    public void setPosition(UIPositionType position) {
        this.position = position;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }


}
