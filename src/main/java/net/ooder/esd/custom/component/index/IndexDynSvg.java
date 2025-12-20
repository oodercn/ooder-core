package net.ooder.esd.custom.component.index;


import net.ooder.esd.annotation.ui.OverflowType;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.bean.TreeListItem;
import net.ooder.esd.tool.component.ListComponent;
import net.ooder.esd.tool.component.SVGPaperComponent;
import net.ooder.esd.tool.component.SVGPathComponent;
import net.ooder.esd.tool.properties.list.ListFieldProperties;
import net.ooder.esd.tool.properties.svg.SVGPaperProperties;
import net.ooder.esd.tool.properties.svg.comb.path.PathAttr;
import net.ooder.esd.tool.properties.svg.comb.path.PathProperties;

import java.util.ArrayList;
import java.util.List;


public class IndexDynSvg extends SVGPaperComponent {

    SVGPathComponent svgPathComponent;

    ListComponent menu;

    public IndexDynSvg() {
        this.alias = "dynSvgMenu";
        SVGPaperProperties logoProperties = this.getProperties();
        logoProperties.setLeft("13.6em");
        logoProperties.setWidth("11.25em");
        logoProperties.setTop("3em");
        logoProperties.setHeight("1.375em");
        logoProperties.setzIndex(1002);
        logoProperties.setGraphicZIndex(2);
        logoProperties.setOverflow(OverflowType.visible);

        svgPathComponent = this.getSvgPathComponent();
        this.addChildren(svgPathComponent);
        this.menu = getDynMenu();
        this.addChildren(menu);

    }

    public ListComponent getDynMenu() {
        ListComponent listComponent = new ListComponent();
        ListFieldProperties listProperty = listComponent.getProperties();
        listProperty.setDirtyMark(false);
        List<TreeListItem> uiItems = new ArrayList<>();
        TreeListItem profileItem = new TreeListItem<>();
        profileItem.setCaption("配置");
        profileItem.setId("profile");
        profileItem.setImageClass("ri-settings-3-line");
        uiItems.add(profileItem);
        TreeListItem accountItem = new TreeListItem();
        accountItem.setCaption("账户");
        accountItem.setId("account");
        accountItem.setImageClass("ri-user-settings-line");
        uiItems.add(accountItem);

        TreeListItem logoutItem = new TreeListItem();
        logoutItem.setCaption("退出");
        logoutItem.setId("logout");
        logoutItem.setImageClass("ri-logout-box-line");
        uiItems.add(logoutItem);
        listProperty.setLeft("0em");
        listProperty.setTop("1.5em");
        listProperty.setWidth("9.85em");
        listProperty.setHeight("auto");
        listProperty.setSelMode(SelModeType.none);
        listProperty.setValue("");
        listProperty.setItems(uiItems);
        return listComponent;
    }

    public SVGPathComponent getSvgPathComponent() {
        SVGPathComponent pathComponent = new SVGPathComponent();
        PathProperties pathProperties = pathComponent.getProperties();
        PathAttr pathAttr = new PathAttr();
        pathAttr.setPath("M,21,21L,28,1L,51,21");
        pathAttr.setFill("#ffffff");
        pathAttr.setStroke("#B6B6B6");
        pathProperties.setAttr(pathAttr);
        return pathComponent;

    }


}
