package net.ooder.esd.custom.properties;

import com.alibaba.fastjson.util.TypeUtils;
import net.ooder.annotation.Enumstype;
import net.ooder.annotation.IconEnumstype;
import net.ooder.common.util.StringUtility;
import net.ooder.context.JDSActionContext;


import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.bean.nav.TabItemBean;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.RequestParamBean;
import net.sf.cglib.beans.BeanMap;

import java.util.*;

public class StacksListItem extends NavTabListItem {
    Boolean iniFold = true;

    public StacksListItem() {
        super();
    }

    public StacksListItem(Enum enumType) {
        OgnlUtil.setProperties(BeanMap.create(enumType), this, false, false);
        this.setId(enumType.name());

        if (enumType instanceof IconEnumstype) {
            if (id==null){
                this.id = ((IconEnumstype) enumType).getType();
            }
             if (caption==null){
                 this.caption= ((IconEnumstype) enumType).getName();
             }
            this.imageClass = ((IconEnumstype) enumType).getImageClass();
        } else if (enumType instanceof Enumstype) {
            if (id==null){
                this.id = ((Enumstype) enumType).getType();
            }

            if (caption==null) {
                this.caption = ((Enumstype) enumType).getName();
            }
        }
    }


    public StacksListItem(TabItemBean childTabViewBean, Map<String, ?> valueMap) {
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
        List<RequestParamBean> requestParamBeans = new ArrayList<>();
        MethodConfig childMethod = childTabViewBean.getMethodConfig();

        if (childTabViewBean.getConstructorBean() != null) {
            requestParamBeans = childTabViewBean.getConstructorBean().getParamList();
        } else if (childMethod != null) {
            requestParamBeans = Arrays.asList((RequestParamBean[]) (childMethod.getParamSet().toArray(new RequestParamBean[]{})));
            this.euClassName = childMethod.getEUClassName();
            if (!childMethod.getImageClass().equals(MethodConfig.DefaultImageClass)) {
                this.imageClass = childMethod.getImageClass();
            }
        }

        if (requestParamBeans.size() > 0) {
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
    }

    public StacksListItem(FieldModuleConfig itemConfig) {
        this.caption = itemConfig.getCaption();
        this.imageClass = itemConfig.getImageClass();
        this.euClassName = itemConfig.getEuClassName();
        this.tabindex = itemConfig.getIndex();
        this.index = itemConfig.getIndex();
        if (euClassName == null) {
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
