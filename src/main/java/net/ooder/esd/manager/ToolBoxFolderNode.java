package net.ooder.esd.manager;

import net.ooder.common.util.StringUtility;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;

import java.util.ArrayList;
import java.util.List;

public class ToolBoxFolderNode {

    public String id;

    public String path;

    public boolean disabled = false;

    public boolean group = false;

    public boolean draggable = true;

    public String key;

    public String cls = "xui.Module";

    public String imageClass = "spafont spa-icon-c-grid";

    public String caption;

    List<ToolBoxFolderNode> sub;


    public ToolBoxFolderNode(Folder folder, boolean isGroup, boolean hasFile) {
        this.id = folder.getPath();
        this.path = folder.getPath();
        this.key = "xui.Class";
        this.group = isGroup;
        this.draggable = false;
        this.caption = folder.getDescrition() == null ? folder.getName() : folder.getDescrition();
        List<Folder> childFolders = folder.getChildrenList();
        for (Folder cfolder : childFolders) {
            this.addSub(new ToolBoxFolderNode((Folder) cfolder, isGroup, hasFile));
        }
        if (hasFile) {
            List<FileInfo> files = folder.getFileList();
            for (FileInfo cfile : files) {
                this.addSub(new ToolBoxFolderNode((FileInfo) cfile));
            }
        }

    }

    public ToolBoxFolderNode(FileInfo fileInfo) {
        this.id = fileInfo.getPath();
        this.path = fileInfo.getPath();
        this.caption = fileInfo.getDescrition() == null ? fileInfo.getName() : fileInfo.getDescrition();
        this.key = StringUtility.replace(path, "/", ".");
        this.draggable = true;
        this.group = false;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public void addSub(ToolBoxFolderNode subNode) {
        if (sub == null) {
            sub = new ArrayList<ToolBoxFolderNode>();
        }
        sub.add(subNode);
    }

    public List<ToolBoxFolderNode> getSub() {
        return sub;
    }

    public void setSub(List<ToolBoxFolderNode> sub) {
        this.sub = sub;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
