package net.ooder.esd.custom.properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.esd.tool.properties.item.TabListItem;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.RequestParamBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavTabListItem extends TabListItem {
    Boolean iniFold = true;

    public NavTabListItem() {
        super();
    }

    public NavTabListItem(TabListItem listItem) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(listItem), Map.class), this, false, false);
    }

    public NavTabListItem(Enum enumType) {
        super(enumType);
    }

    public NavTabListItem(String id, String caption, String imgClass) {
        super(id, caption, imgClass);
    }


    public NavTabListItem(TabItemBean childTabViewBean, Map<String, ?> valueMap) {
        init(childTabViewBean, valueMap);
    }

    public NavTabListItem(FieldModuleConfig itemConfig) {
        this.init(itemConfig);
    }


    public void init(TabItemBean childTabViewBean, Map<String, ?> valueMap) {
        this.id = childTabViewBean.getId();
        this.caption = childTabViewBean.getCaption();
        this.imageClass = childTabViewBean.getImageClass();
        this.closeBtn = childTabViewBean.getCloseBtn();
        this.popBtn = childTabViewBean.getPopBtn();
        this.bindClass = childTabViewBean.getBindClass();
        this.index = childTabViewBean.getIndex();
        this.euClassName = childTabViewBean.getClassName();
        if (childTabViewBean.getIndex() != null && childTabViewBean.getIndex() != -1) {
            this.tabindex = childTabViewBean.getIndex();
        }


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
            if (euClassName == null) {
                this.euClassName = childMethod.getEUClassName();
            }
            if (this.imageClass == null && !childMethod.getImageClass().equals(MethodConfig.DefaultImageClass)) {
                this.imageClass = childMethod.getImageClass();
            }
        } else if (childTabViewBean.getConstructorBean() != null) {
            List<RequestParamBean> requestParamBeans = childTabViewBean.getConstructorBean().getParamList();
            requestParamBeanArr = requestParamBeans.toArray(new RequestParamBean[]{});
        }

        if (childTabViewBean.getTabItem() != null && childTabViewBean.getTabItem().getClass().isEnum() && euClassName.indexOf(CustomViewFactory.INMODULE__) == -1) {
            this.euClassName = euClassName + CustomViewFactory.INMODULE__ + childTabViewBean.getTabItem();
        }

        this.fillParams(requestParamBeanArr, valueMap);

    }

    public void fillParams(RequestParamBean[] requestParamBeans, Map valueMap) {
        if (valueMap == null) {
            valueMap = JDSActionContext.getActionContext().getContext();
        }
        for (RequestParamBean paramBean : requestParamBeans) {
            Object obj = valueMap.get(paramBean.getParamName());
            if (obj != null && !obj.equals("")) {
                this.getTagVar().put(paramBean.getParamName(), obj);
            } else {
                Object value = TypeUtils.cast(JDSActionContext.getActionContext().getParams(paramBean.getParamName()), paramBean.getParamClass(), null);
                this.getTagVar().put(paramBean.getParamName(), value);
            }

        }
    }

    void init(FieldModuleConfig itemConfig) {
        this.caption = itemConfig.getCaption();
        this.imageClass = itemConfig.getImageClass();
        this.euClassName = itemConfig.getEuClassName();
        this.tabindex = itemConfig.getIndex();
        this.index = itemConfig.getIndex();
        if (euClassName == null) {
            if (itemConfig.getMethodConfig() != null) {
                euClassName = itemConfig.getEuClassName();
            } else {
                String url = itemConfig.getUrl();
                if (url.startsWith("/")) {
                    url = url.substring(1);
                }
                euClassName = StringUtility.replace(url, "/", ".");
            }
        }
        this.id = itemConfig.getId();
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
