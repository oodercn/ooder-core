package net.ooder.esd.bean.data;

import net.ooder.esd.annotation.CustomClass;
import net.ooder.esd.annotation.ui.CustomViewType;
import net.ooder.esd.annotation.view.EChartViewAnnotation;
import net.ooder.esd.annotation.ui.ModuleViewType;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.annotation.AnnotationType;
import net.ooder.esd.custom.component.FullEChartsComponent;
import net.ooder.web.util.AnnotationUtil;
@CustomClass(clazz = FullEChartsComponent.class,
        viewType = CustomViewType.LISTMODULE,
        moduleType = ModuleViewType.ECHARTCONFIG
)
@AnnotationType(clazz = EChartViewAnnotation.class)
public class CustomEChartDataBean extends CustomDataBean {


    ModuleViewType moduleViewType = ModuleViewType.CHARTCONFIG;

    String JSONUrl;

    String XMLUrl;

    String categoriesUrl;

    String datasetUrl;

    String dataUrl;

    String trendlinesUrl;


    public CustomEChartDataBean() {

    }


    public CustomEChartDataBean(MethodConfig methodConfig) {
        super(methodConfig);
        EChartViewAnnotation formAnnotation = AnnotationUtil.getMethodAnnotation(methodConfig.getMethod(), EChartViewAnnotation.class);
        if (formAnnotation != null) {
            if (dataUrl == null || dataUrl.equals("")) {
                dataUrl = formAnnotation.dataUrl();
            }

            if (JSONUrl == null || JSONUrl.equals("")) {
                JSONUrl = formAnnotation.JSONUrl();
            }

            if (XMLUrl == null || XMLUrl.equals("")) {
                XMLUrl = formAnnotation.XMLUrl();
            }

            if (categoriesUrl == null || categoriesUrl.equals("")) {
                categoriesUrl = formAnnotation.categoriesUrl();
            }
            if (datasetUrl == null || datasetUrl.equals("")) {
                datasetUrl = formAnnotation.datasetUrl();
            }
            if (trendlinesUrl == null || trendlinesUrl.equals("")) {
                trendlinesUrl = formAnnotation.trendlinesUrl();
            }
        }
    }


    @Override
    public String getDataUrl() {
        return dataUrl;
    }

    @Override
    public Boolean getCache() {
        return false;
    }

    public String getJSONUrl() {
        return JSONUrl;
    }

    public void setJSONUrl(String JSONUrl) {
        this.JSONUrl = JSONUrl;
    }

    public String getXMLUrl() {
        return XMLUrl;
    }

    public void setXMLUrl(String XMLUrl) {
        this.XMLUrl = XMLUrl;
    }

    public String getCategoriesUrl() {
        return categoriesUrl;
    }

    public void setCategoriesUrl(String categoriesUrl) {
        this.categoriesUrl = categoriesUrl;
    }

    public String getDatasetUrl() {
        return datasetUrl;
    }

    public void setDatasetUrl(String datasetUrl) {
        this.datasetUrl = datasetUrl;
    }

    public String getTrendlinesUrl() {
        return trendlinesUrl;
    }

    public void setTrendlinesUrl(String trendlinesUrl) {
        this.trendlinesUrl = trendlinesUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    @Override
    public ModuleViewType getModuleViewType() {
        return moduleViewType;
    }

    public void setModuleViewType(ModuleViewType moduleViewType) {
        this.moduleViewType = moduleViewType;
    }

    public String toAnnotationStr() {
        return AnnotationUtil.toAnnotationStr(this);
    }
}
