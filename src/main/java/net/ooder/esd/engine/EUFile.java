package net.ooder.esd.engine;

import net.ooder.esd.annotation.ui.EUFileType;

public class EUFile {

    EUFileType filetype;

    String name;

    String path;


    public EUFileType getFiletype() {
        return filetype;
    }

    public void setFiletype(EUFileType filetype) {
        this.filetype = filetype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
