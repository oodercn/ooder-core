package net.ooder.esd.bean.gallery;

import net.ooder.esd.bean.gallery.IGalleryItem;

public interface IContentBlockItem extends IGalleryItem {

    public String getCaption();

    public String getMsgnum();

    public void setMsgnum(String msgnum);

    public String getMore();

    public void setMore(String more);

    public String getTitle();

    public void setTitle(String title);

}
