
package net.ooder.esd.engine.event;

public class EUModuleVersionEvent extends ESDEvent {


    String path;

    public EUModuleVersionEvent(String path, EUModuleVersionEventEnums eventID, String configCode) {
        super(path, null);
        id = eventID;
        this.path = path;
        this.systemCode = configCode;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 取得触发此文件版本的实例
     */
    @Override
    public EUModuleVersionEventEnums getID() {
        return (EUModuleVersionEventEnums) id;
    }

}
