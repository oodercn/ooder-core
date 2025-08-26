package net.ooder.esd.tool.properties;



import net.ooder.annotation.Enumstype;
import net.ooder.esd.annotation.CallBackPathAnnotation;
import net.ooder.esd.annotation.RequestPathAnnotation;
import net.ooder.esd.annotation.ResponsePathAnnotation;
import net.ooder.esd.annotation.ui.UrlPath;
import net.ooder.esd.bean.CallBackPathBean;
import net.ooder.esd.bean.RequestPathBean;
import net.ooder.esd.bean.ResponsePathBean;

public class UrlPathData<T extends Enumstype> implements UrlPath<T> {
    T type;
    String name = "";
    String path = "";

    public UrlPathData() {

    }


    public UrlPathData(ResponsePathBean responsePathBean) {
        this.name = responsePathBean.getParamsname();
        this.type = (T) responsePathBean.getType();
        this.path = responsePathBean.getPath();
    }

    public UrlPathData(RequestPathBean requestPathBean) {
        this.name = requestPathBean.getParamsname();
        this.type = (T) requestPathBean.getType();
        this.path = requestPathBean.getPath();
    }

    public UrlPathData(CallBackPathBean callBackPathBean) {
        this.name = callBackPathBean.getParamsname();
        this.type = (T) callBackPathBean.getType();
        this.path = callBackPathBean.getPath();
    }


    public UrlPathData(UrlPath urlPathEnum) {
        this.name = urlPathEnum.getName();
        this.type = (T) urlPathEnum.getType();
        this.path = urlPathEnum.getPath();
    }


    public UrlPathData(RequestPathAnnotation urlPathEnum) {
        this.name = urlPathEnum.paramsname();
        this.type = (T) urlPathEnum.type();
        this.path = urlPathEnum.path();
    }

    public UrlPathData(ResponsePathAnnotation urlPathEnum) {
        this.name = urlPathEnum.paramsname();
        this.type = (T) urlPathEnum.type();
        this.path = urlPathEnum.path();
    }

    public UrlPathData(CallBackPathAnnotation urlPathEnum) {
        this.name = urlPathEnum.paramsname();
        this.type = (T) urlPathEnum.type();
        this.path = urlPathEnum.path();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof UrlPath) {
            UrlPath o = (UrlPath) obj;
            if (o.getName() != null && this.name != null && o.getName().equals(this.name)) {
                return true;
            }
        }
        return super.equals(obj);
    }


    public UrlPathData(String name, T type, String path) {
        this.type = type;
        this.name = name;
        this.path = path;
    }

    public T getType() {
        return type;
    }

    public void setType(T type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
