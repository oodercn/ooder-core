
package net.ooder.esd.engine.event;

import net.ooder.common.JDSException;
import net.ooder.common.JDSListener;

public interface EUModuleVersionListener extends JDSListener {


    /**
     * @param event
     * @throws JDSException
     */
    public void lockVersion(EUModuleVersionEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void addFileVersion(EUModuleVersionEvent event) throws JDSException;


    /**
     * @param event
     * @throws JDSException
     */
    public void updateFileVersion(EUModuleVersionEvent event) throws JDSException;

    /**
     * @param event
     * @throws JDSException
     */
    public void deleteFileVersion(EUModuleVersionEvent event) throws JDSException;


    /**
     * 得到系统Code
     *
     * @return
     */
    public String getSystemCode();
}
