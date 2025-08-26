package net.ooder.esd.engine.event;


public class EUModuleObjectEvent extends ESDEvent {


    private String hash;

    /**
     * FileEvent
     *
     * @param eventID
     */
    public EUModuleObjectEvent(String hash, EUModuleObjectEventEnums eventID, String sysCode) {
        super(hash, null);
        id = eventID;
        this.systemCode = sysCode;
    }


    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }


    @Override
    public EUModuleObjectEventEnums getID() {
        return (EUModuleObjectEventEnums) id;
    }

    /**
     * 取得触发此文件事件的实例
     */
    public String getFilePath() {
        return (String) getSource();
    }


}
