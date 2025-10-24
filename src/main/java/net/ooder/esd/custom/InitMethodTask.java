package net.ooder.esd.custom;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

public class InitMethodTask<T> implements Callable<T> {

    private final int index;

    private final ESDClass esdClass;

    private final Method method;

    public InitMethodTask(Method method, int index, ESDClass esdClass) {
        this.method = method;
        this.index = index;
        this.esdClass = esdClass;
    }

    public int getIndex() {
        return index;
    }

    public ESDClass getEsdClass() {
        return esdClass;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public T call() {
        CustomMethodInfo fieldInfo = new CustomMethodInfo(method, esdClass);
        return (T) fieldInfo;
    }

}