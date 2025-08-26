package net.ooder.esd.engine.event;

import net.ooder.common.JDSException;
import net.ooder.common.JDSListener;

public interface EIModuleListener extends JDSListener {

    /**
     * @param event
     * @throws JDSException
     */
    public void beforCopy(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void create(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void upLoadError(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void upLoadEnd(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void upLoading(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void beforUpLoad(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void beforDownLoad(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void downLoading(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void downLoadEnd(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void beforeReName(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void reNameEnd(EUModuleEvent event) throws JDSException;


    /**
     * @param event
     * @throws JDSException
     */
    public void reStore(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void share(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void clear(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void open(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void send(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void deleteEnd(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void beforDelete(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void moveEnd(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void beforMove(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void updateEnd(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void beforUpdate(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void save(EUModuleEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void copyEnd(EUModuleEvent event) throws JDSException;


    /**
     * 得到系统Code
     *
     * @return
     */
    public String getSystemCode();

}
