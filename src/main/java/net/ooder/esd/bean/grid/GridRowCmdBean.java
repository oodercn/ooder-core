package net.ooder.esd.bean.grid;

import net.ooder.esd.annotation.GridRowCmd;
import net.ooder.esd.annotation.MGridRowCmd;
import net.ooder.esd.annotation.ui.TreeModeType;
import net.ooder.esd.annotation.action.ActiveModeType;
import net.ooder.esd.annotation.action.EditModeType;
import net.ooder.esd.annotation.action.HotRowModeType;
import net.ooder.esd.annotation.menu.GridRowMenu;
import net.ooder.esd.annotation.ui.CmdTPosType;
import net.ooder.esd.bean.RowCmdBean;
import net.ooder.esd.tool.properties.GridProperties;
import net.ooder.esd.tool.properties.item.CmdItem;
import net.ooder.annotation.AnnotationType;
import net.ooder.web.util.AnnotationUtil;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@AnnotationType(clazz = GridRowCmd.class)
public class GridRowCmdBean extends RowCmdBean {


    CmdTPosType pos;

    EditModeType editMode;

    ActiveModeType activeMode;

    TreeModeType treeMode;

    HotRowModeType hotRowMode;

    Set<GridRowMenu> rowMenu = new LinkedHashSet<>();


    public void update(GridProperties gridProperties) {
        this.activeMode = gridProperties.getActiveMode();
        this.editMode = gridProperties.getEditMode();
        this.hotRowMode = gridProperties.getHotRowMode();
        this.treeMode = gridProperties.getTreeMode();
        this.setCaption(gridProperties.getHeaderTail());
        List<CmdItem> buttomItems = gridProperties.getTagCmds();
        if (buttomItems != null) {
            for (CmdItem cmdItem : buttomItems) {
                if (cmdItem.getId().indexOf("_") > -1) {
                    String menuId = cmdItem.getId().split("_")[0];
                    GridRowMenu gridRowMenu = GridRowMenu.valueOf(menuId);
                    if (gridRowMenu != null) {
                        rowMenu.add(gridRowMenu);
                    }
                }
            }

        }


    }


    public GridRowCmdBean(GridProperties gridProperties) {
        AnnotationUtil.fillDefaultValue(GridRowCmd.class, this);
        this.update(gridProperties);
    }


    public GridRowCmdBean() {
        AnnotationUtil.fillDefaultValue(GridRowCmd.class, this);
    }

    public GridRowCmdBean(GridRowCmd annotation) {
        fillData(annotation);
    }

    public GridRowCmdBean(MGridRowCmd annotation) {
        AnnotationUtil.fillBean(annotation, this);
    }

    public GridRowCmdBean fillData(GridRowCmd annotation) {
        return AnnotationUtil.fillBean(annotation, this);
    }

    public EditModeType getEditMode() {
        return editMode;
    }

    public TreeModeType getTreeMode() {
        return treeMode;
    }

    public void setTreeMode(TreeModeType treeMode) {
        this.treeMode = treeMode;
    }

    public void setEditMode(EditModeType editMode) {
        this.editMode = editMode;
    }

    public ActiveModeType getActiveMode() {
        return activeMode;
    }

    public void setActiveMode(ActiveModeType activeMode) {
        this.activeMode = activeMode;
    }

    public HotRowModeType getHotRowMode() {
        return hotRowMode;
    }

    public void setHotRowMode(HotRowModeType hotRowMode) {
        this.hotRowMode = hotRowMode;
    }

    public CmdTPosType getPos() {
        return pos;
    }

    public void setPos(CmdTPosType pos) {
        this.pos = pos;
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
