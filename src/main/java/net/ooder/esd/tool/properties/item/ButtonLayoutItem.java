package net.ooder.esd.tool.properties.item;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.annotation.Enumstype;
import net.ooder.annotation.IconEnumstype;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;


import net.ooder.esd.bean.nav.IButtonLayoutItem;
import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;

public class ButtonLayoutItem extends GalleryItem {
    @JSONField(serialize = false)
    public static final String ESDSearchPattern = "esdsearchpattern";
    public BorderType borderType;
    public Boolean activeLast;

    public ButtonLayoutItem() {
    }


    public ButtonLayoutItem(String id, String caption, String imageClass, String tips, Map<String, Object> params) {
        this.id = id;
        this.caption = caption;
        this.imageClass = imageClass;
        this.tagVar = params;
    }

    public ButtonLayoutItem(FieldModuleConfig itemConfig) throws JDSException {
        this.caption = itemConfig.getCaption();
        this.imageClass = itemConfig.getImageClass();
        String url = itemConfig.getUrl();
        if (url.startsWith("/")) {
            url = url.substring(1);
        }
        euClassName = StringUtility.replace(url, "/", ".");
        this.id = itemConfig.getId();
        if (tagVar == null) {
            this.tagVar = new HashMap<>();
        }

    }

    public ButtonLayoutItem(Enum enumType) {


        if (enumType instanceof IButtonLayoutItem) {
            IButtonLayoutItem buttonViewsItem = (IButtonLayoutItem) enumType;
            this.caption = buttonViewsItem.getCaption();
            this.imageClass = buttonViewsItem.getImageClass();
            this.comment = buttonViewsItem.getComment();
            this.bindClass = buttonViewsItem.getBindClass();

        } else {
            OgnlUtil.setProperties(BeanMap.create(enumType), this, false, false);
            if (enumType instanceof IconEnumstype) {
                if (id == null) {
                    this.id = ((IconEnumstype) enumType).getType();
                }
                if (caption == null) {
                    this.caption = ((IconEnumstype) enumType).getName();
                }
                this.imageClass = ((IconEnumstype) enumType).getImageClass();
            } else if (enumType instanceof Enumstype) {
                if (id == null) {
                    this.id = ((Enumstype) enumType).getType();
                }

                if (caption == null) {
                    this.caption = ((Enumstype) enumType).getName();
                }
            }
        }
    }

    public ButtonLayoutItem(String id, String caption, String imageClass) {
        this.id = id;
        this.caption = caption;
        this.imageClass = imageClass;
    }


    @JSONField(serialize = false)
    private ButtonLayoutItem getFristId(ButtonLayoutItem itemInfo) {
        if (itemInfo.getEuClassName() != null && !itemInfo.getEuClassName().equals("")) {
            return itemInfo;
        }
        return null;
    }


    @JSONField(serialize = false)
    public ButtonLayoutItem getFristClassItem(ButtonLayoutItem item) {
        ButtonLayoutItem fristItem = getFristId(item);
        if (fristItem == null) {
            fristItem = item;
        }
        return fristItem;
    }



    public Boolean getActiveLast() {
        return activeLast;
    }

    public void setActiveLast(Boolean activeLast) {
        this.activeLast = activeLast;
    }

    public String getPattern() {
        String pattern = null;
        if (JDSActionContext.getActionContext().getParams(ButtonLayoutItem.ESDSearchPattern) != null) {
            pattern = JDSActionContext.getActionContext().getParams(ButtonLayoutItem.ESDSearchPattern).toString();
        }
        return pattern;
    }


}
