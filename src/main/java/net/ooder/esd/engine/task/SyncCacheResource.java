package net.ooder.esd.engine.task;

import net.ooder.common.JDSException;
import net.ooder.context.JDSActionContext;
import net.ooder.context.JDSContext;
import net.ooder.server.context.MinServerActionContextImpl;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.FileObject;
import net.ooder.vfs.FileVersion;
import net.ooder.vfs.Folder;
import net.ooder.vfs.ct.CtVfsFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class SyncCacheResource implements Callable<Boolean> {

    private final MinServerActionContextImpl autoruncontext;
    private final Folder folder;

    public SyncCacheResource(Folder folder) {
        this.folder = folder;
        JDSContext context = JDSActionContext.getActionContext();
        this.autoruncontext = new MinServerActionContextImpl(context.getHttpRequest(),context.getOgnlContext());
        autoruncontext.setParamMap(context.getContext());
        if (context.getSessionId() != null) {
            autoruncontext.setSessionId(context.getSessionId());
            autoruncontext.getSession().put("sessionHandle", context.getSession().get("sessionHandle"));
        }
        autoruncontext.setSessionMap(context.getSession());

    }

    @Override
    public Boolean call() {
        try {
            List<Folder> folderList = folder.getChildrenRecursivelyList();
            List<FileInfo> fileInfos = folder.getFileListRecursively();
            Set<String> versionIds = new HashSet<>();
            Set<String> objIds = new HashSet<>();
            for (FileInfo fileInfo : fileInfos) {
                versionIds.add(fileInfo.getCurrentVersonId());
                objIds.add(fileInfo.getCurrentVersonFileHash());
            }
            List<FileVersion> versions = CtVfsFactory.getCtVfsService().loadVersionByIds(versionIds);
            List<FileObject> objectList = CtVfsFactory.getCtVfsService().loadObjects(objIds);
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return true;

    }
}
