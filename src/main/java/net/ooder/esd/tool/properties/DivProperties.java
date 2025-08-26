package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.bean.CustomDivBean;
import net.ooder.esd.bean.view.CustomDivFormViewBean;
import net.ooder.esd.bean.field.CustomDivFieldBean;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;


public class DivProperties extends FieldProperties {

    public DivProperties(Dock dock) {
        this.dock = dock;
    }

    ComponentType comboType;

    public DivProperties(ComponentType comboType) {
        this.comboType = comboType;
    }

    public DivProperties(CustomDivFormViewBean divBean) {
        initCustomBean(divBean);
    }

    public DivProperties(CustomDivBean divBean) {
        this.init(divBean);
    }

    public DivProperties(CustomDivFieldBean divBean) {
        this.init(divBean);
    }

    public void init(CustomDivFieldBean divBean) {
        if (divBean != null) {
            if (divBean.getContainerBean() != null) {
                this.init(divBean.getContainerBean());
            }
            if (divBean.getIframeAutoLoad() != null && !divBean.getAjaxAutoLoad().equals("")) {
                this.iframeAutoLoad = divBean.getIframeAutoLoad();
            }
            if (divBean.getAjaxAutoLoad() != null && !divBean.getAjaxAutoLoad().equals("")) {
                this.ajaxAutoLoad = divBean.getAjaxAutoLoad();
            }

            if (divBean.getWidth() != null && !divBean.getWidth().equals("")) {
                this.width = divBean.getWidth();
            }
            if (divBean.getHeight() != null && !divBean.getHeight().equals("")) {
                this.height = divBean.getHeight();
            }
            if (divBean.getHtml() != null && !divBean.getHtml().equals("")) {
                this.html = divBean.getHtml();
            }
            this.overflow = divBean.getOverflow();
            if (divBean.getViewBean() != null) {
                initCustomBean(divBean.getViewBean());
            }
        }
    }

    public void init(CustomDivBean divBean) {
        if (divBean != null) {
            if (divBean.getContainerBean() != null) {
                this.init(divBean.getContainerBean());
            }
            if (divBean.getIframeAutoLoad() != null && !divBean.getAjaxAutoLoad().equals("")) {
                this.iframeAutoLoad = divBean.getIframeAutoLoad();
            }
            if (divBean.getAjaxAutoLoad() != null && !divBean.getAjaxAutoLoad().equals("")) {
                this.ajaxAutoLoad = divBean.getAjaxAutoLoad();
            }

            if (divBean.getWidth() != null && !divBean.getWidth().equals("")) {
                this.width = divBean.getWidth();
            }
            if (divBean.getHeight() != null && !divBean.getHeight().equals("")) {
                this.height = divBean.getHeight();
            }
            if (divBean.getHtml() != null && !divBean.getHtml().equals("")) {
                this.html = divBean.getHtml();
            }
            this.overflow = divBean.getOverflow();
        }
    }


    public void initCustomBean(CustomDivFormViewBean divBean) {
        this.init(divBean);
        if (divBean.getContainerBean() != null) {
            super.init(divBean.getContainerBean());
        }
    }


    public ComponentType getComboType() {
        return comboType;
    }

    public void setComboType(ComponentType comboType) {
        this.comboType = comboType;
    }

    protected void init(CustomDivFormViewBean divBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(divBean), Map.class), this, false, false);
    }

    public DivProperties() {

    }

}


