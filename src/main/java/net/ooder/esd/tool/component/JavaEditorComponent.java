package net.ooder.esd.tool.component;


import net.ooder.esd.annotation.event.FieldEventEnum;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.tool.properties.Action;
import net.ooder.esd.tool.properties.form.CodeEditorProperties;

public class JavaEditorComponent extends FieldComponent<CodeEditorProperties, FieldEventEnum> {


    public JavaEditorComponent(String alias) {
        super(ComponentType.JAVAEDITOR, alias);
        this.setProperties( new CodeEditorProperties());
    }

    public JavaEditorComponent(String alias, CodeEditorProperties properties) {
        super(ComponentType.JAVAEDITOR, alias);
        this.setProperties(properties);
    }

    public JavaEditorComponent addAction(Action<FieldEventEnum> action) {
        super.addAction(action);
        return this;
    }

    public JavaEditorComponent() {
        super(ComponentType.JAVAEDITOR);
        this.setProperties( new CodeEditorProperties());
    }


}
