package net.ooder.esd.engine.event;

import net.ooder.common.JDSListener;
import net.ooder.vfs.VFSException;

public interface EIPackageListener extends JDSListener {

    /**
     * @param event
     * @throws VFSException
     */
    public void create(PackageEvent event) throws VFSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void lock(PackageEvent event) throws VFSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void beforReName(PackageEvent event) throws VFSException;


    /**
     * @param event
     * @throws VFSException
     */
    public void reNameEnd(PackageEvent event) throws VFSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void beforDelete(PackageEvent event) throws VFSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void deleteing(PackageEvent event) throws VFSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void deleteEnd(PackageEvent event) throws VFSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void save(PackageEvent event) throws VFSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void beforCopy(PackageEvent event) throws VFSException;


    /**
     * @param event
     * @throws VFSException
     */
    public void copying(PackageEvent event) throws VFSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void copyEnd(PackageEvent event) throws VFSException;


    /**
     * @param event
     * @throws VFSException
     */
    public void beforMove(PackageEvent event) throws VFSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void moving(PackageEvent event) throws VFSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void moveEnd(PackageEvent event) throws VFSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void beforClean(PackageEvent event) throws VFSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void cleanEnd(PackageEvent event) throws VFSException;

    /**
     * @param event
     * @throws VFSException
     */
    public void restore(PackageEvent event) throws VFSException;


    /**
     * 得到系统Code
     *
     * @return
     */
    public String getSystemCode();

}
