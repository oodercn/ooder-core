package net.ooder.esd.tool.properties;


import net.ooder.esd.annotation.event.APIEvent;
import net.ooder.esd.annotation.ui.UrlPath;

import java.util.List;

public interface APIExtparams {


    public List<? extends APIEvent> getEvents();

    public Boolean getQueryAsync();

    public Boolean isAutoRun();

    public Boolean isAllform();

    public List<? extends UrlPath> getRequestDataSource();

    public List<? extends UrlPath> getResponseDataTarget();

    public List<? extends UrlPath> getResponseCallback();
}
