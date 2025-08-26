package net.ooder.esd.dsm.view.field;

import net.ooder.annotation.CustomBean;
import net.ooder.esd.bean.CustomRefBean;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.custom.ESDField;

public interface ESDFieldConfig<M, N extends ComboBoxBean> extends CustomBean {

    public String getId();


    public String getSourceClassName();

    public String getViewClassName();

    //public String getServiceClassName();

    public String getFieldname();

    public void setFieldname(String fieldname);

    public CustomFieldBean getCustomBean();

    public void setCustomBean(CustomFieldBean customBean);

    public String getMethodName();

    public String getDomainId();

    public ESDField<M, N> getEsdField();

    public Boolean getSerialize();

    public Boolean getColHidden();

    public CustomRefBean getRefBean();

    public Boolean getPid();

    public Boolean getUid();

    public Class<? extends ESDFieldConfig> getClazz();

    public Boolean getCaptionField();

    public void setDomainId(String domainId);


}
