package net.ooder.esd.custom.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.Enumstype;
import net.ooder.annotation.IconEnumstype;
import net.ooder.esd.bean.nav.ButtonViewsItem;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.view.field.FieldItemConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

import java.util.Map;

public class ButtonViewsListItem extends NavTabListItem {

    Boolean iniFold;

    public ButtonViewsListItem() {
        super();
    }


    public ButtonViewsListItem(FieldItemConfig info) {
        caption = info.getCaption();
        id = info.getId();
        hidden = info.getColHidden();
        imageClass = info.getImageClass();
    }


    public ButtonViewsListItem(FieldModuleConfig itemConfig) {
        super(itemConfig);
    }


    public ButtonViewsListItem(TabItemBean childTabViewBean) {
        init(childTabViewBean, null);
    }

    public ButtonViewsListItem(TabItemBean childTabViewBean, Map<String, ?> valueMap) {
        init(childTabViewBean, valueMap);
    }


    public ButtonViewsListItem(ButtonViewsListItem listItem) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(listItem), Map.class), this, false, false);
    }

    public ButtonViewsListItem(ESDField info) {
        caption = info.getCaption();
        id = info.getId();
        hidden = info.isHidden();
        imageClass = info.getImageClass();
    }

    public ButtonViewsListItem(Enum enumType) {
        if (enumType instanceof ButtonViewsItem) {
            ButtonViewsItem buttonViewsItem = (ButtonViewsItem) enumType;
            this.caption = buttonViewsItem.getName();
            this.tips = buttonViewsItem.getName();
            this.imageClass = buttonViewsItem.getImageClass();
            this.closeBtn = buttonViewsItem.isCloseBtn();
            this.popBtn = buttonViewsItem.isPopBtn();
            this.iniFold = buttonViewsItem.isIniFold();
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
                if (tips == null) {
                    this.tips = ((IconEnumstype) enumType).getName();
                }

                this.imageClass = ((IconEnumstype) enumType).getImageClass();
            } else if (enumType instanceof Enumstype) {
                if (id == null) {
                    this.id = ((Enumstype) enumType).getType();
                }
                if (tips == null) {
                    this.tips = ((IconEnumstype) enumType).getName();
                }
                if (caption == null) {
                    this.caption = ((Enumstype) enumType).getName();
                }
            }
        }

    }


    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }

}
