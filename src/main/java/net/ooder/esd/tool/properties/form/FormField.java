package net.ooder.esd.tool.properties.form;

import java.util.Map;

public interface FormField {


    public String getTag();

    public void setTag(String tag);

    public Map<String, Object> getTagVar();

    public void setTagVar(Map<String, Object> tagVar);

    public Object getValue();

    public void setValue(Object value);

    public String getName();

    public void setName(String name);

    public Boolean getFormField();
}
