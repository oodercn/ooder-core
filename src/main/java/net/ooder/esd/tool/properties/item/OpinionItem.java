package net.ooder.esd.tool.properties.item;

import net.ooder.common.JDSException;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.dsm.view.field.FieldModuleConfig;

public class OpinionItem extends GalleryItem {

    public String content;
    public String creatorName;
    public String createDateStr;


    public OpinionItem() {
    }


    public OpinionItem(Enum enumType) {
        super(enumType);
    }

    public OpinionItem(TreeListItem treeListItem) {
        super(treeListItem);
    }


    public OpinionItem(FieldModuleConfig itemConfig) throws JDSException {
        super(itemConfig);

    }


    public OpinionItem(String id, String caption, String imageClass) {
        super(id, caption, imageClass);
    }

    public OpinionItem(String id, String caption) {
        super(id, caption);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }
}
