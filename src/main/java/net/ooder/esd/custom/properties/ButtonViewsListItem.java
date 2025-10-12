package net.ooder.esd.custom.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.annotation.Enumstype;
import net.ooder.annotation.IconEnumstype;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.nav.ButtonViewsItem;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.custom.ESDField;
import net.ooder.esd.dsm.view.field.FieldItemConfig;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.RequestParamBean;
import net.sf.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ButtonViewsListItem extends TabListItem {

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
            this.tips=buttonViewsItem.getName();
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

    public ButtonViewsListItem(TabItemBean childTabViewBean) {
        init(childTabViewBean, null);
    }

    public ButtonViewsListItem(TabItemBean childTabViewBean, Map<String, ?> valueMap) {
        init(childTabViewBean, valueMap);
    }

    void init(TabItemBean childTabViewBean, Map<String, ?> valueMap) {
        this.caption = childTabViewBean.getCaption();
        this.imageClass = childTabViewBean.getImageClass();
        this.closeBtn = childTabViewBean.getCloseBtn();
        this.popBtn = childTabViewBean.getPopBtn();
        this.iniFold = childTabViewBean.getIniFold();
        this.index = childTabViewBean.getIndex();
        if (childTabViewBean.getIndex() != null && childTabViewBean.getIndex() != -1) {
            this.tabindex = childTabViewBean.getIndex();
        }
        this.id = childTabViewBean.getId();
        if (tagVar == null) {
            this.tagVar = new HashMap<>();
        }
        if (valueMap == null) {
            valueMap = new HashMap<>();
        }

        MethodConfig childMethod = childTabViewBean.getMethodConfig();
        RequestParamBean[] requestParamBeanArr = new RequestParamBean[]{};
        if (childMethod != null) {
            requestParamBeanArr = (RequestParamBean[]) childMethod.getParamSet().toArray(new RequestParamBean[]{});
            this.euClassName = childMethod.getEUClassName();
            if (!childMethod.getImageClass().equals(MethodConfig.DefaultImageClass)) {
                this.imageClass = childMethod.getImageClass();
            }
        } else if (childTabViewBean.getConstructorBean() != null) {
            List<RequestParamBean> requestParamBeans = childTabViewBean.getConstructorBean().getParamList();
            requestParamBeanArr = requestParamBeans.toArray(new RequestParamBean[]{});

        }
        this.fillParams(requestParamBeanArr, valueMap);


    }

    public ButtonViewsListItem(FieldModuleConfig itemConfig) {
        this.caption = itemConfig.getCaption();
        this.imageClass = itemConfig.getImageClass();
        this.euClassName = itemConfig.getEuClassName();
        this.tabindex = itemConfig.getIndex();
        this.index = itemConfig.getIndex();
        if (euClassName == null && itemConfig.getUrl() != null) {
            String url = itemConfig.getUrl();
            if (url.startsWith("/")) {
                url = url.substring(1);
            }
            euClassName = StringUtility.replace(url, "/", ".");
        }
        this.id = itemConfig.getId();
        this.setIniFold(false);
        if (tagVar == null) {
            this.tagVar = new HashMap<>();
        }

    }

    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }

}
