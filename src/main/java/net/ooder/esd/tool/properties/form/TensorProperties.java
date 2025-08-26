package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.FileUploadFieldBean;
import net.ooder.esd.tool.properties.FieldProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class TensorProperties extends FieldProperties {

    String src;
    Map<String, Object> params;
    String uploadUrl;
    Boolean prepareFormData;


    public TensorProperties() {
    }

    public TensorProperties(FileUploadFieldBean uploadFieldBean) {
        this.dock = uploadFieldBean.getDock();
        this.uploadUrl = uploadFieldBean.getUploadUrl();
        this.src = uploadFieldBean.getSrc();
        this.prepareFormData = uploadFieldBean.getPrepareFormData();
        this.width = uploadFieldBean.getWidth();
        this.height = uploadFieldBean.getHeight();
    }

    public TensorProperties(FileUploadFieldBean bean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(bean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);

        }

    }



    public Boolean getPrepareFormData() {
        return prepareFormData;
    }

    public void setPrepareFormData(Boolean prepareFormData) {
        this.prepareFormData = prepareFormData;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

}
