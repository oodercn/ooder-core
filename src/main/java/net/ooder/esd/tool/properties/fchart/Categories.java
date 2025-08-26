package net.ooder.esd.tool.properties.fchart;

import net.ooder.jds.core.esb.util.OgnlUtil;
import net.sf.cglib.beans.BeanMap;

import java.util.List;

public class Categories {

    String font;

    Integer fontsize;

    String fontcolor;

    List<Categorie> category;

    public Categories(){

    }
    public Categories(Enum linenum) {
        OgnlUtil.setProperties(BeanMap.create(linenum), this, false, false);
    }


    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public Integer getFontsize() {
        return fontsize;
    }

    public void setFontsize(Integer fontsize) {
        this.fontsize = fontsize;
    }

    public String getFontcolor() {
        return fontcolor;
    }

    public void setFontcolor(String fontcolor) {
        this.fontcolor = fontcolor;
    }

    public List<Categorie> getCategory() {
        return category;
    }

    public void setCategory(List<Categorie> category) {
        this.category = category;
    }
}
