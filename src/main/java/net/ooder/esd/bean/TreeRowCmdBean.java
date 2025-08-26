package net.ooder.esd.bean;

import net.ooder.esd.annotation.TreeRowCmd;
import net.ooder.esd.annotation.menu.TreeRowMenu;
import net.ooder.esd.tool.properties.TreeViewProperties;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.LinkedHashSet;
import java.util.Set;

@AnnotationType(clazz = TreeRowCmd.class)
public class TreeRowCmdBean extends RowCmdBean {


    Set<TreeRowMenu> rowMenu = new LinkedHashSet<>();


    public TreeRowCmdBean(TreeViewProperties properties) {
        AnnotationUtil.fillDefaultValue(TreeRowCmd.class, this);
        this.tagCmdsAlign = properties.getTagCmdsAlign();
    }


    public TreeRowCmdBean() {
        AnnotationUtil.fillDefaultValue(TreeRowCmd.class, this);
    }

    public TreeRowCmdBean(TreeRowCmd annotation) {
        fillData(annotation);
    }

    public TreeRowCmdBean fillData(TreeRowCmd annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }


    public Set<TreeRowMenu> getRowMenu() {
        return rowMenu;
    }

    public void setRowMenu(Set<TreeRowMenu> rowMenu) {
        this.rowMenu = rowMenu;
    }


    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
