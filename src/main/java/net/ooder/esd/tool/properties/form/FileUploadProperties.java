package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.FileUploadFieldBean;
import net.ooder.esd.tool.properties.FieldProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class FileUploadProperties extends FieldProperties {

    String src;
    Map<String, Object> params;
    String uploadUrl;
    Boolean prepareFormData;


    public FileUploadProperties() {
    }

    public FileUploadProperties(FileUploadFieldBean uploadFieldBean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(uploadFieldBean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);

        }



        this.uploadUrl = uploadFieldBean.getUploadUrl();
        this.src = uploadFieldBean.getSrc();
        this.prepareFormData = uploadFieldBean.getPrepareFormData();
        this.width = uploadFieldBean.getWidth();
        this.height = uploadFieldBean.getHeight();
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
