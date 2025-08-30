package net.ooder.esd.custom.component.index;

import net.ooder.esd.annotation.ui.BorderType;
import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.SelModeType;
import net.ooder.esd.tool.component.DivComponent;
import net.ooder.esd.tool.component.GalleryComponent;
import net.ooder.esd.tool.component.IconComponent;
import net.ooder.esd.tool.component.LabelComponent;
import net.ooder.esd.tool.properties.CS;
import net.ooder.esd.tool.properties.DivProperties;
import net.ooder.esd.tool.properties.GalleryProperties;
import net.ooder.esd.tool.properties.IconProperties;
import net.ooder.esd.tool.properties.form.LabelProperties;
import net.ooder.esd.tool.properties.item.GalleryItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndexMainBarComponent extends DivComponent {


    public IndexMainBarComponent() {
        super("leftCenterBar");
        DivProperties divProperties =  this.getProperties();
        divProperties.setHeight("4em");
        divProperties.setDock(Dock.top);
        divProperties.setzIndex(10);
        divProperties.setPanelBgClr("#3498DB");
        this.addChildren(getUserIcon());
        this.addChildren(getGalleryMenu());
        this.addChildren(new UserConfigComponent());
    }

    IconComponent getUserIcon() {
        IconComponent<IconProperties, ?> logoLabelCom = new IconComponent();
        logoLabelCom.setAlias("userIcon");
        IconProperties logoProperties = logoLabelCom.getProperties();
        logoProperties.setLeft("10em");
        logoProperties.setTop("0.75em");
        logoProperties.setWidth("3em");
        logoProperties.setLeft("10em");
        logoProperties.setHeight("2.2em");
        logoProperties.setIconFontSize("2em");
        logoProperties.setImageClass("fas fa-sign-in-alt");
        return logoLabelCom;
    }


    GalleryComponent getGalleryMenu() {
        GalleryComponent galleryComponent = new GalleryComponent();
        galleryComponent.setAlias("GalleryMenu");
        GalleryProperties logoProperties = galleryComponent.getProperties();
        logoProperties.setTop("0.68em");
        logoProperties.setWidth("16em");
        logoProperties.setHeight("5em");
        logoProperties.setRight("0em");
        logoProperties.setIconOnly(true);
        logoProperties.setSelMode(SelModeType.none);
        logoProperties.setBorderType(BorderType.none);

        List<GalleryItem> itemList = new ArrayList<>();
        GalleryItem searchItem = new GalleryItem();
        searchItem.setCaption("查询");
        searchItem.setId("search");
        searchItem.setImageClass("fas fa-search");

        GalleryItem taskItem = new GalleryItem();
        taskItem.setCaption("任务");
        taskItem.setId("task");
        taskItem.setImageClass("fas fa-file-alt");
        taskItem.setFlagText("1");


        GalleryItem msgItem = new GalleryItem();
        msgItem.setCaption("消息");
        msgItem.setId("msg");
        msgItem.setImageClass("fas fa-bell");
        msgItem.setFlagText("8");
        itemList.add(searchItem);
        itemList.add(taskItem);
        itemList.add(msgItem);
        logoProperties.setItems(itemList);

        net.ooder.esd.tool.properties.CS cs = new net.ooder.esd.tool.properties.CS ();
        HashMap flagMap = new HashMap();
        flagMap.put("margin", "-12px -48px 0px 0px");
        flagMap.put("font-size", "8px");

        cs.setFLAG(flagMap);

        HashMap iconMap = new HashMap();
        iconMap.put("font-size", "3em");
        cs.setICON(iconMap);

        HashMap itemMap = new HashMap();
        itemMap.put("background-color", "transparent");
        cs.setITEM(itemMap);

        HashMap itemsMap = new HashMap();
        itemsMap.put("background-color", "transparent");
        itemsMap.put("overflow", "hidden");
        cs.setITEMS(itemsMap);

        galleryComponent.setCS(cs);

        return galleryComponent;
    }


    LabelComponent getLogoText() {
        LabelComponent logoLabelCom = new LabelComponent();
        logoLabelCom.setAlias("logoText");
        LabelProperties logoProperties = logoLabelCom.getProperties();
        logoProperties.setLeft("4.5em");
        logoProperties.setWidth("3.85em");
        logoProperties.setHeight("1.2em");
        logoProperties.setFontSize("18px");
        logoProperties.setFontColor("#FFFFFF");
        logoProperties.setCaption("@{currPerson.name}");
        logoProperties.sethAlign(HAlignType.center);
        return logoLabelCom;
    }


    class UserConfigComponent extends DivComponent {
        public UserConfigComponent() {
            super("userInfo");
            DivProperties divProperties = (DivProperties) this.getProperties();
            divProperties.setDock(Dock.none);
            divProperties.setLeft("13.5em");
            divProperties.setTop("0.75em");
            divProperties.setHeight("2.5em");
            divProperties.setWidth("8.75em");
            this.addChildren(getUserName());
            this.addChildren(getToggle());

        }


        LabelComponent getUserName() {
            LabelComponent logoLabelCom = new LabelComponent();
            logoLabelCom.setAlias("userName");
            LabelProperties logoProperties = logoLabelCom.getProperties();
            logoProperties.setTabindex(3);
            logoProperties.setPosition("static");
            logoProperties.setCaption("管理员");
            logoProperties.setFontColor("#FFFFFF");
            return logoLabelCom;
        }


        LabelComponent getToggle() {
            LabelComponent logoLabelCom = new LabelComponent();
            logoLabelCom.setAlias("toggle");
            LabelProperties logoProperties = logoLabelCom.getProperties();
            logoProperties.setTabindex(4);
            logoProperties.setPosition("static");
            logoProperties.setCaption("");
            logoProperties.setImageClass("fas fa-sort-amount-down");
            logoProperties.setFontColor("#FFFFFF");
            return logoLabelCom;
        }


    }

}
