package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.VideoFieldBean;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.Map;

public class VideoProperties extends AudioProperties {


    String poster;

    public VideoProperties(){

    }
    public VideoProperties(VideoFieldBean fieldBean, ContainerBean containerBean) {
        OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(fieldBean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);
        }

    }


    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
