package net.ooder.esd.engine.event;

/**
 * <p>
 * Title: VFS管理系统
 * </p>
 * <p>
 * Description: 核心文件事件
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company: www.justdos.net
 * </p>
 * 
 * @author wenzhangli
 * @version 2.0
 */
@SuppressWarnings("all")
public class EUModuleEvent extends ESDEvent {




    /**
     * FileEvent
     * 
     * @param path
     * @param eventID
     */
    public EUModuleEvent(String path, EUModuleEventEnums eventID, String sysCode) {
	super(path, null);
	id = eventID;
	this.systemCode = sysCode;
    }

    @Override
    public EUModuleEventEnums getID() {
	return (EUModuleEventEnums) id;
    }

    /**
     * 取得触发此文件事件的实例
     */
    public String getFilePath() {
	return (String) getSource();
    }


}
