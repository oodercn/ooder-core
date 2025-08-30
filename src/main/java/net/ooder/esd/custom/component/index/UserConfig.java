package net.ooder.esd.custom.component.index;

import net.ooder.esd.annotation.ui.Dock;
import net.ooder.esd.tool.component.DivComponent;
import net.ooder.esd.tool.component.LabelComponent;
import net.ooder.esd.tool.properties.DivProperties;
import net.ooder.esd.tool.properties.form.LabelProperties;

import net.ooder.jds.core.esb.EsbUtil;
import net.ooder.org.Person;
import net.ooder.server.JDSClientService;
import net.ooder.server.JDSServer;
import net.ooder.server.OrgManagerFactory;

public class UserConfig extends DivComponent {

    public UserConfig() {
        super("userInfo");
        DivProperties divProperties = (DivProperties) this.getProperties();
        divProperties.setDock(Dock.none);
        divProperties.setHoverPop("dynSvgMenu");
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
        logoProperties.setCaption(getCurrPerson().getName());
        logoProperties.setFontColor("#FFFFFF");
        return logoLabelCom;
    }

    Person getCurrPerson() {
        Person person = null;
        try {
            String userId = JDSServer.getInstance().getAdminUser().getId();
            JDSClientService clientService = (JDSClientService) EsbUtil.parExpression("$JDSC");
            if (clientService != null && clientService.getConnectInfo() != null) {
                userId = clientService.getConnectInfo().getUserID();
            }
            person = OrgManagerFactory.getOrgManager().getPersonByID(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return person;
    }


    LabelComponent getToggle() {
        LabelComponent logoLabelCom = new LabelComponent();
        logoLabelCom.setAlias("toggle");
        LabelProperties logoProperties = logoLabelCom.getProperties();
        logoProperties.setTabindex(4);
        logoProperties.setPosition("static");
        logoProperties.setCaption("");
        logoProperties.setImageClass("fa-solid fa-sort-amount-down");
        logoProperties.setFontColor("#FFFFFF");
        return logoLabelCom;
    }


}