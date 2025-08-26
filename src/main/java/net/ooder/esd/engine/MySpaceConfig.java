package net.ooder.esd.engine;

import net.ooder.config.CApplication;

public class MySpaceConfig {






    public CApplication application;

    public String topOrgId;

    public String fontPath = "resource/font/";

    public MySpaceConfig() {
        super();
    }

    public String getFontPath() {
        return fontPath;
    }

    public void setFontPath(String fontPath) {
        this.fontPath = fontPath;
    }


    public String getTopOrgId() {
        return topOrgId;
    }


    public void setTopOrgId(String topOrgId) {
        this.topOrgId = topOrgId;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


    @Override
    public String toString() {
        return super.toString();
    }


    public CApplication getApplication() {
        return application;
    }

    public void setApplication(CApplication application) {
        this.application = application;
    }


}
