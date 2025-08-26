package net.ooder.esd.tool.properties.form;

import com.alibaba.fastjson.JSON;
import net.ooder.esd.annotation.ui.TagCmdsAlign;
import net.ooder.esd.bean.ContainerBean;
import net.ooder.esd.bean.field.base.RadioBoxFieldBean;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.esd.tool.properties.list.ListFieldProperties;
import net.ooder.jds.core.esb.util.OgnlUtil;

import java.util.List;
import java.util.Map;

public class RadioBoxProperties extends ListFieldProperties {

    public List<CmdItem> tagCmds;
    public String listkey;
    public Boolean checkBox;
    public TagCmdsAlign tagCmdsAlign;
    public Boolean dynLoad;

    public RadioBoxProperties(){

    }

   public RadioBoxProperties(RadioBoxFieldBean bean, ContainerBean containerBean) {
       OgnlUtil.setProperties(JSON.parseObject(JSON.toJSONString(bean), Map.class), this, false, false);
        if (containerBean != null) {
            this.init(containerBean);

        }

    }


    public String getListkey() {
        return listkey;
    }

    public void setListkey(String listkey) {
        this.listkey = listkey;
    }


    public TagCmdsAlign getTagCmdsAlign() {
        return tagCmdsAlign;
    }

    public void setTagCmdsAlign(TagCmdsAlign tagCmdsAlign) {
        this.tagCmdsAlign = tagCmdsAlign;
    }


    public List<CmdItem> getTagCmds() {
        return tagCmds;
    }

    public void setTagCmds(List<CmdItem> tagCmds) {
        this.tagCmds = tagCmds;
    }


    public Boolean getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(Boolean checkBox) {
        this.checkBox = checkBox;
    }
}
