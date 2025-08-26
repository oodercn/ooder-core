package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.esd.util.json.EMSerializer;

public class AnimBinderStatus {

    String left;
    String top;
    @JSONField(serializeUsing = EMSerializer.class)
    String width;
    @JSONField(serializeUsing = EMSerializer.class)
    String height;
    String rotate;
    Double translateX;
    Double translateY;
    Double scaleX;
    Double scaleY;
    String skewX;
    String skewY;
    String color;
    String fontSize;
    Integer lineHeight;
    String backgroundColor;
    String backgroundPositionX;
    String backgroundPositionY;

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getRotate() {
        return rotate;
    }

    public void setRotate(String rotate) {
        this.rotate = rotate;
    }

    public Double getTranslateX() {
        return translateX;
    }

    public void setTranslateX(Double translateX) {
        this.translateX = translateX;
    }

    public Double getTranslateY() {
        return translateY;
    }

    public void setTranslateY(Double translateY) {
        this.translateY = translateY;
    }

    public Double getScaleX() {
        return scaleX;
    }

    public void setScaleX(Double scaleX) {
        this.scaleX = scaleX;
    }

    public Double getScaleY() {
        return scaleY;
    }

    public void setScaleY(Double scaleY) {
        this.scaleY = scaleY;
    }

    public String getSkewX() {
        return skewX;
    }

    public void setSkewX(String skewX) {
        this.skewX = skewX;
    }

    public String getSkewY() {
        return skewY;
    }

    public void setSkewY(String skewY) {
        this.skewY = skewY;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public Integer getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(Integer lineHeight) {
        this.lineHeight = lineHeight;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBackgroundPositionX() {
        return backgroundPositionX;
    }

    public void setBackgroundPositionX(String backgroundPositionX) {
        this.backgroundPositionX = backgroundPositionX;
    }

    public String getBackgroundPositionY() {
        return backgroundPositionY;
    }

    public void setBackgroundPositionY(String backgroundPositionY) {
        this.backgroundPositionY = backgroundPositionY;
    }
}
