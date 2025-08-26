package net.ooder.esd.custom.service;

import net.ooder.config.ListResultModel;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.tool.properties.fchart.Categories;
import net.ooder.esd.tool.properties.fchart.DataSet;
import net.ooder.esd.tool.properties.fchart.RawData;
import net.ooder.esd.tool.properties.fchart.TrendLines;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path = "/form/custom/field/fchart/")
public class CustomFChartService {


    @RequestMapping(value = "getCategories")
    @APIEventAnnotation(customRequestData = RequestPathEnum.CURRFORM)
    @ResponseBody
    public ListResultModel<List<Categories>> getCategories() {
        return new ListResultModel<>();
    }

    @RequestMapping(value = "getDataset")
    @APIEventAnnotation(customRequestData = RequestPathEnum.CURRFORM)
    @ResponseBody
    public ListResultModel<List<DataSet>> getDataset() {
        return new ListResultModel<>();
    }


    @RequestMapping(value = "getData")
    @APIEventAnnotation(customRequestData = RequestPathEnum.CURRFORM)
    @ResponseBody
    public ListResultModel<List<RawData>> getData() {
        return new ListResultModel<>();
    }

    @RequestMapping(value = "getTrendlines")
    @APIEventAnnotation(customRequestData = RequestPathEnum.CURRFORM)
    @ResponseBody
    public ListResultModel<List<TrendLines>> getTrendlines() {
        return new ListResultModel<>();
    }

}
