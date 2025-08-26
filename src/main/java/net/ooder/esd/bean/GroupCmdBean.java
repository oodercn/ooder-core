package net.ooder.esd.bean;

import net.ooder.esd.annotation.GroupCmd;
import net.ooder.esd.annotation.menu.GridRowMenu;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.LinkedHashSet;
import java.util.Set;

@AnnotationType(clazz = GroupCmd.class)
public class GroupCmdBean extends RowCmdBean {



    Set<GridRowMenu> rowMenu = new LinkedHashSet<>();


    public GroupCmdBean() {
        AnnotationUtil.fillDefaultValue(GroupCmd.class, this);
    }

    public GroupCmdBean(GroupCmd annotation) {
        fillData(annotation);
    }

    public GroupCmdBean fillData(GroupCmd annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }



    public Set<GridRowMenu> getRowMenu() {
        return rowMenu;
    }

    public void setRowMenu(Set<GridRowMenu> rowMenu) {
        this.rowMenu = rowMenu;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
