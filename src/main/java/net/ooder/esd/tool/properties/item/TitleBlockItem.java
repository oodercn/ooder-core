package net.ooder.esd.tool.properties.item;

import com.alibaba.fastjson.JSON;
import net.ooder.common.JDSException;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.bean.gallery.GalleryItemBean;
import net.ooder.esd.bean.gallery.TitleBlockItemBean;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

import java.util.Map;

public class TitleBlockItem extends GalleryItem {

    public String msgnum;
    public String more;
    public String title;

    public TitleBlockItem() {
    }


    public TitleBlockItem(TitleBlockItemBean titleBlockItemBean) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(titleBlockItemBean), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);

    }



    public TitleBlockItem(TreeListItem listItem) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(listItem), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public TitleBlockItem(Enum enumType) {
        super(enumType);
        OgnlUtil.setProperties(BeanMap.create(enumType), this, false, false);
    }

    public TitleBlockItem(FieldModuleConfig itemConfig) throws JDSException {
        super(itemConfig);
    }

    public TitleBlockItem(String id, String caption, String imageClass) {
        super(id, caption, imageClass);
    }

    public TitleBlockItem(String id, String caption) {
        super(id, caption);
    }

    @Override
    public String getCaption() {
        if (caption == null || caption.equals("")) {
            caption = this.getTitle();
        }
        return caption;
    }


    public String getMsgnum() {
        return msgnum;
    }

    public void setMsgnum(String msgnum) {
        this.msgnum = msgnum;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
