package net.ooder.esd.manager.plugins.font.node;


import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.JDSException;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;

import java.util.ArrayList;
import java.util.List;


public class FontConfig {
    String id;
    String caption;
    String projectName;
    String name;
    String imageClass;
    @JSONField(serialize = false)
    String file;
    String code;
    Boolean iniFold = false;

    String description;
    List<ALFont> glyphs;

    @JSONField(serialize = false)
    List<String> listCachePath = new ArrayList<>();


    List<FontConfig> sub;
    @JSONField(serialize = false)
    List<IFontMenu> subItems = new ArrayList<>();
    @JSONField(serialize = false)
    IFontMenu defaultFont;

    public FontConfig(IFontMenu menu) {

        this.id = menu.getId();
        this.caption = menu.getCaption();
        this.name = menu.getCaption();
        this.subItems = menu.getSubItems();
        this.defaultFont = menu.getDefaultFont();
        this.imageClass = menu.getImageClass();
        FontConfig defaultConfig = this.getDefaultFontConfig();
        if (defaultConfig != null) {
            this.cssPrefixText = defaultConfig.getCssPrefixText();
            this.fontfamily = defaultConfig.getFontfamily();
        }
    }

    @JSONField(serialize = false)
    FontConfig getDefaultFontConfig() {
        FontConfig defaultConfig = null;
        try {
            if (defaultFont != null) {
                defaultConfig = this.getClient().getFont(projectName,defaultFont.getId());
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return defaultConfig;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<IFontMenu> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<IFontMenu> subItems) {
        this.subItems = subItems;
    }


    @JSONField(name = "font_family")
    String fontfamily;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ALFont> getGlyphs() {
        if (glyphs == null || glyphs.isEmpty()) {
            FontConfig defaultConfig = this.getDefaultFontConfig();
            if (defaultConfig != null) {
                glyphs = new ArrayList<ALFont>();
                for (ALFont font : defaultConfig.getGlyphs()) {
                    glyphs.add(font.clone());
                }
            }
        }
        return glyphs;
    }


    public List<String> getListCachePath() {
        return listCachePath;
    }

    public void setListCachePath(List<String> listCachePath) {
        this.listCachePath = listCachePath;
    }

    public void setGlyphs(List<ALFont> glyphs) {
        this.glyphs = glyphs;
    }

    public String getFontfamily() {

        FontConfig defaultConfig = this.getDefaultFontConfig();
        if (defaultConfig != null) {
            this.fontfamily = defaultConfig.getFontfamily();
        }

        return fontfamily;
    }

    public void setFontfamily(String fontfamily) {
        this.fontfamily = fontfamily;
    }

    public String getCssPrefixText() {
        FontConfig defaultConfig = this.getDefaultFontConfig();
        if (defaultConfig != null) {
            this.cssPrefixText = defaultConfig.getCssPrefixText();
        }
        return cssPrefixText;
    }

    public void setCssPrefixText(String cssPrefixText) {
        this.cssPrefixText = cssPrefixText;
    }

    @JSONField(name = "css_prefix_text")
    String cssPrefixText;

    public IFontMenu getDefaultFont() {
        return defaultFont;
    }

    public void setDefaultFont(IFontMenu defaultFont) {
        this.defaultFont = defaultFont;
    }


    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public List<FontConfig> getSub() {
        if (this.subItems != null && !subItems.isEmpty()) {
            sub = new ArrayList<FontConfig>();
            for (IFontMenu fontMenu : subItems) {
                FontConfig config = null;
                try {
                    config = getClient().getFont(projectName,fontMenu.getId());
                } catch (JDSException e) {
                    e.printStackTrace();
                }

                if (config != null) {
                    config.setImageClass(fontMenu.getImageClass());
                    sub.add(config);

                } else {
                    sub.add(new FontConfig(fontMenu));
                }

            }
        }
        return sub;
    }

    public void setSub(List<FontConfig> sub) {
        this.sub = sub;
    }

    public FontConfig() {

    }

    public FontConfig(String id, String caption, String name) {
        this.id = id;
        this.caption = caption;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        if (caption == null || caption.equals("")) {
            caption = this.name;
        }
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Boolean getIniFold() {
        return iniFold;
    }

    public void setIniFold(Boolean iniFold) {
        this.iniFold = iniFold;
    }

    @JSONField(serialize = false)
    public ESDClient getClient() throws JDSException {
        return ESDFacrory.getAdminESDClient();
    }


}