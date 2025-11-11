package net.ooder.esd.tool.properties.item;

import com.alibaba.fastjson.JSON;
import net.ooder.common.JDSException;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

import java.util.Map;

public class ContentBlockItem extends GalleryItem {

    public String datetime;
    public String more;
    public String title;

    public ContentBlockItem() {
    }


    public ContentBlockItem(TreeListItem listItem) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(listItem), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }

    public ContentBlockItem(ContentBlockItem listItem) {
        Map valueMap = JSON.parseObject(JSON.toJSONString(listItem), Map.class);
        OgnlUtil.setProperties(valueMap, this, false, false);
    }


    public ContentBlockItem(Enum enumType) {
        super(enumType);
        OgnlUtil.setProperties(BeanMap.create(enumType), this, false, false);
    }

    public ContentBlockItem(FieldModuleConfig itemConfig) throws JDSException {
        super(itemConfig);
    }

    public ContentBlockItem(String id, String caption, String imageClass) {
        super(id, caption, imageClass);
    }

    public ContentBlockItem(String id, String caption) {
        super(id, caption);
    }

    @Override
    public String getCaption() {
        if (caption == null || caption.equals("")) {
            caption = this.getTitle();
        }
        return caption;
    }


    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
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


