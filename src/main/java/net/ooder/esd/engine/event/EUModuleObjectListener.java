package net.ooder.esd.engine.event;

import net.ooder.common.JDSException;
import net.ooder.common.JDSListener;
import net.ooder.vfs.VFSException;


public interface EUModuleObjectListener extends JDSListener {


    /**
     * @param event
     * @throws VFSException
     */
    public void befaultUpLoad(EUModuleObjectEvent event) throws JDSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void upLoading(EUModuleObjectEvent event) throws JDSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void upLoadEnd(EUModuleObjectEvent event) throws JDSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void upLoadError(EUModuleObjectEvent event) throws JDSException;


    /**
     * @param event
     * @throws VFSException
     */
    public void share(EUModuleObjectEvent event) throws JDSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void append(EUModuleObjectEvent event) throws JDSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void beforDownLoad(EUModuleObjectEvent event) throws VFSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void downLoading(EUModuleObjectEvent event) throws JDSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void downLoadEnd(EUModuleObjectEvent event) throws JDSException;


    /**
     * 得到系统Code
     *
     * @return
     */
    public String getSystemCode();

}
