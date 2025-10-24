package net.ooder.esd.custom;

import java.lang.reflect.Field;
import java.util.concurrent.Callable;

public class InitFieldTask<T> implements Callable<T> {

    private final int index;

    private final ESDClass esdClass;

    private final Field field;

    public InitFieldTask(Field field, int index, ESDClass esdClass) {
        this.field = field;
        this.index = index;
        this.esdClass = esdClass;
    }

    public int getIndex() {
        return index;
    }

    public ESDClass getEsdClass() {
        return esdClass;
    }

    public Field getField() {
        return field;
    }

    @Override
    public T call() {
        CustomFieldInfo fieldInfo = new CustomFieldInfo(field, index, esdClass);
        return (T) fieldInfo;
    }

}