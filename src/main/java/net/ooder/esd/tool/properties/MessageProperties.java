package net.ooder.esd.tool.properties;

import net.ooder.esd.tool.properties.list.DataProperties;

public class MessageProperties extends DataProperties {
    public String dataBinder;
    public String dataField;
    public String msgType;
    public String recipientType;
    public Boolean asynReceive;

    public Boolean isAsynReceive() {
        return asynReceive;
    }

    public void setAsynReceive(Boolean asynReceive) {
        this.asynReceive = asynReceive;
    }


    public String getDataBinder() {
        return dataBinder;
    }

    public void setDataBinder(String dataBinder) {
        this.dataBinder = dataBinder;
    }

    public String getDataField() {
        return dataField;
    }

    public void setDataField(String dataField) {
        this.dataField = dataField;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }
}
