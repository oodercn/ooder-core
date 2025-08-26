/**
 * $RCSfile: VFSEvent.java,v $
 * $Revision: 1.1 $
 * $Date: 2014/07/08 00:25:43 $
 * <p>
 * Copyright (C) 2003 itjds, Inc. All rights reserved.
 * <p>
 * This software is the proprietary information of itjds, Inc.
 * Use is subject to license terms.
 */
package net.ooder.esd.engine.event;

import net.ooder.common.JDSEvent;
import net.ooder.common.JDSListener;
import net.ooder.esd.engine.ESDClient;

/**
 * <p>
 * Title: VFS虚拟文件管理系统
 * </p>
 * <p>
 * Description: VFS虚拟文件内所有事件的基类，继承自java.util.EventObject
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: www.justdos.net
 * </p>
 *
 * @author wenzhangli
 * @version 2.0
 */
public abstract class ESDEvent<T> extends JDSEvent {

    private JDSListener listener;

    public ESDEvent(T source) {
        super(source);

    }

    public ESDEvent(T source, JDSListener listener) {
        super(source);
        this.listener = listener;
    }

    protected String expression;

    protected ESDClient client = null;

    /**
     * 设置发生事件时的VFSClientService对象！
     *
     * @param client
     */
    public void setClientService(ESDClient client) {
        this.client = client;
    }

    /**
     * 取得发生事件时的VFSClientService对象！
     *
     * @return
     */
    public ESDClient getClientService() {
        return client;
    }

}
