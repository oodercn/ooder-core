package net.ooder.esd.custom.properties;

import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.bean.grid.GridColItemBean;
import net.ooder.esd.dsm.view.field.FieldGridConfig;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.tool.properties.Header;

public class CustomHeader extends Header {


    public CustomHeader(FieldGridConfig info) {
        caption = info.getCaption();
        id = info.getFieldname();
        hidden = info.getColHidden();
        this.enumClass = info.getEnumClass();

        GridColItemBean gridItemBean = info.getGridColItemBean();
        if (gridItemBean != null) {
            if (gridItemBean.getInputType() != null) {
                this.type = gridItemBean.getInputType();
            } else {
                type = ComboInputType.input;
            }
            if (gridItemBean.getHeaderStyle() != null && !gridItemBean.getHeaderStyle().equals("")) {
                this.headerStyle = gridItemBean.getHeaderStyle();
            }
            if (gridItemBean.getHeaderStyle() != null) {
                this.colResizer = gridItemBean.getColResizer();
            }
            if (gridItemBean.getEditable() != null && gridItemBean.getEditable()) {
                this.editable = gridItemBean.getEditable();
            }
            if (gridItemBean.getFlexSize() != null && gridItemBean.getFlexSize()) {
                this.flexSize = gridItemBean.getFlexSize();
            }
            if (gridItemBean.getTitle() != null && !gridItemBean.getTitle().equals("")) {
                this.caption = gridItemBean.getTitle();
            }


            if (gridItemBean.getWidth() != null && !gridItemBean.getWidth().equals("") && !gridItemBean.getWidth().equals("auto")) {
                this.width = gridItemBean.getWidth();
            }

        }
        if (info.getReadonly() != null && info.getReadonly()) {
            this.readonly = info.getReadonly();
        }


        if (type.equals(ComboInputType.auto)) {
            type = ComboInputType.input;
        } else if (type.equals(ComboInputType.password)) {
            hidden = true;
        }

        if (info.getFieldname().toUpperCase().equals("TagCmds".toUpperCase())) {
            this.hidden = true;
        }

        for (TreeListItem listItem : info.getItems()) {
            this.addSelectItem(listItem);
        }
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
