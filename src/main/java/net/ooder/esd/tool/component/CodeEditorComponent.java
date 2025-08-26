package net.ooder.esd.tool.component;

import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.CodeEditorProperties;

public class CodeEditorComponent extends FieldComponent<CodeEditorProperties, FieldEventEnum> {

    public static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CodeEditorComponent.class);


    public CodeEditorComponent(String alias) {
        super(ComponentType.CODEEDITOR, alias);
        this.setProperties( new CodeEditorProperties());
    }

    public CodeEditorComponent(String alias, CodeEditorProperties properties) {
        super(ComponentType.CODEEDITOR, alias);
        this.setProperties(properties);
    }

    public CodeEditorComponent addAction(Action<FieldEventEnum> action) {
        super.addAction( action);
        return this;
    }

    public CodeEditorComponent() {
        super(ComponentType.CODEEDITOR);
        this.setProperties(new CodeEditorProperties());
    }


}
