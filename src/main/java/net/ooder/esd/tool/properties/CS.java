package net.ooder.esd.tool.properties;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

public class CS {
    @JSONField(name = "KEY")
    Map<String, Object> KEY;

    @JSONField(name = "LIST")
    Map<String, Object> LIST;

    @JSONField(name = "PANEL")
    Map<String, Object> PANEL;


    @JSONField(name = "CAPTION")
    Map<String, Object> CAPTION;

    @JSONField(name = "BORDER")
    Map<String, Object> BORDER;


    @JSONField(name = "FLAG")
    Map<String, Object> FLAG;


    @JSONField(name = "MOVE")
    Map<String, Object> MOVE;

    @JSONField(name = "TITLE")
    Map<String, Object> TITLE;

    @JSONField(name = "MSGNUM")
    Map<String, Object> MSGNUM;


    @JSONField(name = "MORE")
    Map<String, Object> MORE;


    @JSONField(name = "CMD")
    Map<String, Object> CMD;

    @JSONField(name = "TIEM")
    Map<String, Object> TIEM;

    @JSONField(name = "PERSON")
    Map<String, Object> PERSON;

    @JSONField(name = "PERFORTIME")
    Map<String, Object> PERFORTIME;

    @JSONField(name = "BACKGROUND")
    Map<String, Object> BACKGROUND;


    @JSONField(name = "FRAME")
    Map<String, Object> FRAME;

    @JSONField(name = "RULER")

    Map<String, Object> RULER;
    @JSONField(name = "ICON")
    Map<String, Object> ICON;
    @JSONField(name = "GROUP")
    Map<String, Object> GROUP;
    @JSONField(name = "ITEMS")
    Map<String, Object> ITEMS;
    @JSONField(name = "HANDLER")
    Map<String, Object> HANDLER;
    @JSONField(name = "ITEM")
    Map<String, Object> ITEM;

    @JSONField(name = "ITEMC")
    Map<String, Object> ITEMC;

    @JSONField(name = "SPLIT")
    Map<String, Object> SPLIT;
    @JSONField(name = "BTN")
    Map<String, Object> BTN;
    @JSONField(name = "BOX")
    Map<String, Object> BOX;
    @JSONField(name = "LABEL")
    Map<String, Object> LABEL;
    @JSONField(name = "DROP")
    Map<String, Object> DROP;
    @JSONField(name = "COMMENT")
    Map<String, Object> COMMENT;


    @JSONField(name = "ITEMFRAME")
    Map<String, Object> ITEMFRAME;

    public CS() {

    }

    public Map<String, Object> getTITLE() {
        return TITLE;
    }

    public void setTITLE(Map<String, Object> TITLE) {
        this.TITLE = TITLE;
    }

    public Map<String, Object> getMSGNUM() {
        return MSGNUM;
    }

    public void setMSGNUM(Map<String, Object> MSGNUM) {
        this.MSGNUM = MSGNUM;
    }

    public Map<String, Object> getMORE() {
        return MORE;
    }

    public void setMORE(Map<String, Object> MORE) {
        this.MORE = MORE;
    }

    public Map<String, Object> getLIST() {
        return LIST;
    }

    public void setLIST(Map<String, Object> LIST) {
        this.LIST = LIST;
    }

    public Map<String, Object> getBORDER() {
        return BORDER;
    }

    public void setBORDER(Map<String, Object> BORDER) {
        this.BORDER = BORDER;
    }


    public Map<String, Object> getBACKGROUND() {
        return BACKGROUND;
    }

    public void setBACKGROUND(Map<String, Object> BACKGROUND) {
        this.BACKGROUND = BACKGROUND;
    }


    public Map<String, Object> getCMD() {
        return CMD;
    }

    public void setCMD(Map<String, Object> CMD) {
        this.CMD = CMD;
    }


    public Map<String, Object> getMOVE() {
        return MOVE;
    }

    public void setMOVE(Map<String, Object> MOVE) {
        this.MOVE = MOVE;
    }

    public Map<String, Object> getFLAG() {
        return FLAG;
    }

    public void setFLAG(Map<String, Object> FLAG) {
        this.FLAG = FLAG;
    }


    public Map<String, Object> getTIEM() {
        return TIEM;
    }

    public void setTIEM(Map<String, Object> TIEM) {
        this.TIEM = TIEM;
    }

    public Map<String, Object> getPERSON() {
        return PERSON;
    }

    public void setPERSON(Map<String, Object> PERSON) {
        this.PERSON = PERSON;
    }

    public Map<String, Object> getPERFORTIME() {
        return PERFORTIME;
    }

    public void setPERFORTIME(Map<String, Object> PERFORTIME) {
        this.PERFORTIME = PERFORTIME;
    }


    public Map<String, Object> getCAPTION() {
        return CAPTION;
    }

    public void setCAPTION(Map<String, Object> CAPTION) {
        this.CAPTION = CAPTION;
    }


    public Map<String, Object> getCOMMENT() {
        return COMMENT;
    }

    public void setCOMMENT(Map<String, Object> COMMENT) {
        this.COMMENT = COMMENT;
    }

    public Map<String, Object> getITEMFRAME() {
        return ITEMFRAME;
    }

    public void setITEMFRAME(Map<String, Object> ITEMFRAME) {
        this.ITEMFRAME = ITEMFRAME;
    }


    public Map<String, Object> getPANEL() {
        return PANEL;
    }

    public void setPANEL(Map<String, Object> PANEL) {
        this.PANEL = PANEL;
    }

    public Map<String, Object> getFRAME() {
        return FRAME;
    }

    public void setFRAME(Map<String, Object> FRAME) {
        this.FRAME = FRAME;
    }


    public Map<String, Object> getKEY() {
        return KEY;
    }

    public void setKEY(Map<String, Object> KEY) {
        this.KEY = KEY;
    }

    public Map<String, Object> getRULER() {
        return RULER;
    }

    public void setRULER(Map<String, Object> RULER) {
        this.RULER = RULER;
    }

    public Map<String, Object> getICON() {
        return ICON;
    }

    public void setICON(Map<String, Object> ICON) {
        this.ICON = ICON;
    }

    public Map<String, Object> getGROUP() {
        return GROUP;
    }

    public void setGROUP(Map<String, Object> GROUP) {
        this.GROUP = GROUP;
    }

    public Map<String, Object> getITEMS() {
        return ITEMS;
    }

    public void setITEMS(Map<String, Object> ITEMS) {
        this.ITEMS = ITEMS;
    }

    public Map<String, Object> getHANDLER() {
        return HANDLER;
    }

    public void setHANDLER(Map<String, Object> HANDLER) {
        this.HANDLER = HANDLER;
    }

    public Map<String, Object> getITEM() {
        return ITEM;
    }

    public Map<String, Object> getITEMC() {
        return ITEMC;
    }

    public void setITEMC(Map<String, Object> ITEMC) {
        this.ITEMC = ITEMC;
    }

    public void setITEM(Map<String, Object> ITEM) {
        this.ITEM = ITEM;
    }

    public Map<String, Object> getSPLIT() {
        return SPLIT;
    }

    public void setSPLIT(Map<String, Object> SPLIT) {
        this.SPLIT = SPLIT;
    }

    public Map<String, Object> getBTN() {
        return BTN;
    }

    public void setBTN(Map<String, Object> BTN) {
        this.BTN = BTN;
    }

    public Map<String, Object> getBOX() {
        return BOX;
    }

    public void setBOX(Map<String, Object> BOX) {
        this.BOX = BOX;
    }

    public Map<String, Object> getLABEL() {
        return LABEL;
    }

    public void setLABEL(Map<String, Object> LABEL) {
        this.LABEL = LABEL;
    }

    public Map<String, Object> getDROP() {
        return DROP;
    }

    public void setDROP(Map<String, Object> DROP) {
        this.DROP = DROP;
    }


}
