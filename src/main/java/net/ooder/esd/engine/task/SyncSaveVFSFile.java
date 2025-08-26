package net.ooder.esd.engine.task;

import net.ooder.vfs.VFSConstants;
import net.ooder.vfs.ct.CtVfsFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SyncSaveVFSFile<T extends Boolean> implements Runnable {
    public static Log logger = LogFactory.getLog(SyncSaveVFSFile.class);
    String json;
    String path;

    public SyncSaveVFSFile(String json, String path) {
        this.json = json;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            CtVfsFactory.getCtVfsService().saveFileAsContent(path, json, VFSConstants.Default_Encoding);
        } catch (Throwable e) {
            logger.error(e);
        }
    }


}