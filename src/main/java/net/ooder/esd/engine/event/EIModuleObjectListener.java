package net.ooder.esd.engine.event;

import net.ooder.common.JDSException;
import net.ooder.common.JDSListener;

/**
 * <p>
 * Title: VFS文件管理系统
 * </p>
 * <p>
 * Description: 核心文件事件监听器接口
 * </p>
 * <p>
 * Copyright: Copyright (c) 2020
 * </p>
 * <p>
 * Company: www.justdos.net
 * </p>
 *
 * @author wenzhangli
 * @version 4.0
 */
public interface EIModuleObjectListener extends JDSListener {


    /**
     * @param event
     * @throws JDSException
     */
    public void befaultUpLoad(EUModuleObjectEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void upLoading(EUModuleObjectEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void upLoadEnd(EUModuleObjectEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void upLoadError(EUModuleObjectEvent event) throws JDSException;


    /**
     * @param event
     * @throws JDSException
     */
    public void share(EUModuleObjectEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void append(EUModuleObjectEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void beforDownLoad(EUModuleObjectEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void downLoading(EUModuleObjectEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void downLoadEnd(EUModuleObjectEvent event) throws JDSException;


    /**
     * 得到系统Code
     *
     * @return
     */
    public String getSystemCode();

}
