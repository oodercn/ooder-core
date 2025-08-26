package net.ooder.esd.tool.properties.echarts;

import java.util.Map;

public class Feature {
    Map<String,Object> restore;
    Map<String,Object> saveAsImage;
    Map<String,Object> dataView;

    public Map<String, Object> getRestore() {
        return restore;
    }

    public void setRestore(Map<String, Object> restore) {
        this.restore = restore;
    }

    public Map<String, Object> getSaveAsImage() {
        return saveAsImage;
    }

    public void setSaveAsImage(Map<String, Object> saveAsImage) {
        this.saveAsImage = saveAsImage;
    }

    public Map<String, Object> getDataView() {
        return dataView;
    }

    public void setDataView(Map<String, Object> dataView) {
        this.dataView = dataView;
    }
}
