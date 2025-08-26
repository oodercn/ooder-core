package net.ooder.esd.tool.component;

import net.ooder.esd.annotation.event.FileUploadEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.FileUploadProperties;
public class FileUploadComponent extends FieldComponent<FileUploadProperties, FileUploadEventEnum> {

    public FileUploadComponent addAction(FileUploadEventEnum eventKey, Action<FileUploadEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public FileUploadComponent(String alias) {
        super(ComponentType.FILEUPLOAD, alias);
       this.setProperties( new FileUploadProperties());
    }

    public FileUploadComponent(String alias, FileUploadProperties properties) {
        super(ComponentType.FILEUPLOAD, alias);
        this.setProperties(properties);
    }

    public FileUploadComponent() {
        super(ComponentType.FILEUPLOAD);
        this.setProperties( new FileUploadProperties());
    }
}
