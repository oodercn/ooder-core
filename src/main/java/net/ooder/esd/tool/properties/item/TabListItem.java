package net.ooder.esd.tool.properties.item;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.TypeUtils;
import net.ooder.annotation.Enumstype;
import net.ooder.annotation.IconEnumstype;
import net.ooder.common.JDSConstants;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.util.ClassUtility;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.field.TabItem;
import net.ooder.esd.annotation.ui.*;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.util.json.BindClassArrDeserializer;
import net.ooder.esd.util.json.BindClassDeserializer;
import net.ooder.esd.util.json.DefaultTabItem;
import net.ooder.esd.util.json.EnumsSerializer;
import net.ooder.jds.core.esb.util.OgnlUtil;
import net.ooder.web.RequestParamBean;
import net.sf.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;

public class TabListItem<T extends Enum> extends UIItem<T> {
    protected static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, TabListItem.class);
    public String image;
    public Boolean closeBtn;
    public Boolean popBtn;
    public String euClassName;
    public String methodName;
    public BorderType borderType;
    public Boolean activeLast;
    public ComboInputType type;
    @JSONField(deserializeUsing = BindClassArrDeserializer.class)
    public Class[] bindClass;

    @JSONField(deserializeUsing = BindClassDeserializer.class)
    public Class entityClass;

    public String bindClassName;

    public DefaultTabItem tabItem;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public IconColorEnum iconColor;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public ItemColorEnum itemColor;
    @JSONField(serializeUsing = EnumsSerializer.class, deserializeUsing = EnumsSerializer.class)
    public FontColorEnum fontColor;


    public TabListItem() {

    }

    public TabListItem(String id, String caption) {
        this.id = id;
        this.caption = caption;
    }

    public TabListItem(String id, String caption, String imgClass) {
        this.id = id;
        this.caption = caption;
        this.imageClass = imgClass;
    }

    public TabListItem(String id, String caption, String imageClass, String tips, ComboInputType type) {
        super(id, caption, imageClass, tips, new HashMap<>());
        this.type = type;
    }

    public TabListItem(String id, String caption, String imageClass, String tips, Map<String, Object> params) {
        super(id, caption, imageClass, tips, params);
    }

    public TabListItem(T enumType) {
        super(enumType);
        if (enumType instanceof TabItem) {
            this.tabItem = new DefaultTabItem((TabItem) enumType);
        }

        OgnlUtil.setProperties(BeanMap.create(enumType), this, false, false);
        if (enumType instanceof IconEnumstype) {
            if (id == null) {
                this.id = ((IconEnumstype) enumType).getType();
            }
            if (caption == null) {
                this.caption = ((IconEnumstype) enumType).getName();
            }
            this.imageClass = ((IconEnumstype) enumType).getImageClass();
        } else if (enumType instanceof Enumstype) {
            if (id == null) {
                this.id = ((Enumstype) enumType).getType();
            }
            if (caption == null) {
                this.caption = ((Enumstype) enumType).getName();
            }
        }
    }


    public Class[] getBindClass() {
        if (bindClass == null || bindClass.length == 0) {
            bindClass = new Class[]{};
            if (this.getBindClassName() != null && !this.getBindClassName().equals("") && !this.getBindClassName().equals(Void.class.getName()) && !getBindClassName().equals(Enum.class.getName())) {
                try {
                    Class uibingClass = ClassUtility.loadClass(this.getBindClassName());
                    bindClass = new Class[]{uibingClass};
                } catch (ClassNotFoundException e) {
                    logger.error(e);
                    //  e.printStackTrace();
                }
            }

            if (bindClass.length == 0 && this.getEuClassName() != null && !this.getEuClassName().equals("")) {
                EUModule module = null;
                try {
                    JDSActionContext.getActionContext().getContext().putAll(this.getTagVar());
                    module = ESDFacrory.getAdminESDClient().getModule(this.getEuClassName(), null);
                    if (module == null) {
                        module = CustomViewFactory.getInstance().getView(this.getEuClassName(), null);
                    }
                    if (module != null && module.getSourceClassName() != null) {
                        Class uibingClass = ClassUtility.loadClass(module.getSourceClassName());
                        bindClass = new Class[]{uibingClass};
                    }
                } catch (Exception e) {
                    logger.error(e);
                    //   e.printStackTrace();
                }

            }


            if (bindClass.length == 0 && this.getEntityClass() != null) {
                bindClass = new Class[]{this.getEntityClass()};
            }

        }
        return bindClass;
    }

    public String getBindClassName() {
        if (bindClassName == null && bindClass != null && bindClass.length > 0 && bindClass[0] != null) {
            bindClassName = bindClass[0].getName();
        }
        return bindClassName;
    }

    public void setBindClassName(String bindClassName) {
        this.bindClassName = bindClassName;
    }

    public void setBindClass(Class[] bindClass) {
        this.bindClass = bindClass;
    }


    public DefaultTabItem getTabItem() {
        return tabItem;
    }

    public void setTabItem(DefaultTabItem tabItem) {
        this.tabItem = tabItem;
    }


    public Map<String, Object> addTagVar(String name, Object value) {
        if (tagVar == null) {
            tagVar = new HashMap<>();
        }
        tagVar.put(name, value);
        return tagVar;
    }


    public void fillParams(RequestParamBean[] requestParamBeans, Map valueMap) {
        if (valueMap == null) {
            valueMap = JDSActionContext.getActionContext().getContext();
        }
        for (RequestParamBean paramBean : requestParamBeans) {
            Object obj = valueMap.get(paramBean.getParamName());
            if (obj != null && !obj.equals("")) {
                addTagVar(paramBean.getParamName(), obj);
            } else {
                Object value = TypeUtils.cast(JDSActionContext.getActionContext().getParams(paramBean.getParamName()), paramBean.getParamClass(), null);
                addTagVar(paramBean.getParamName(), value);
            }

        }
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public IconColorEnum getIconColor() {
        return iconColor;
    }

    public void setIconColor(IconColorEnum iconColor) {
        this.iconColor = iconColor;
    }

    public ItemColorEnum getItemColor() {
        return itemColor;
    }

    public void setItemColor(ItemColorEnum itemColor) {
        this.itemColor = itemColor;
    }

    public FontColorEnum getFontColor() {
        return fontColor;
    }

    public void setFontColor(FontColorEnum fontColor) {
        this.fontColor = fontColor;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    public String getItemClass() {
        return itemClass;
    }

    public void setItemClass(String itemClass) {
        this.itemClass = itemClass;
    }

    public String getItemStyle() {
        return itemStyle;
    }

    public void setItemStyle(String itemStyle) {
        this.itemStyle = itemStyle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ComboInputType getType() {
        return type;
    }

    public void setType(ComboInputType type) {
        this.type = type;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Boolean getCloseBtn() {
        return closeBtn;
    }

    public void setCloseBtn(Boolean closeBtn) {
        this.closeBtn = closeBtn;
    }

    public Boolean getPopBtn() {
        return popBtn;
    }

    public void setPopBtn(Boolean popBtn) {
        this.popBtn = popBtn;
    }

    public String getEuClassName() {
        return euClassName;
    }

    public void setEuClassName(String euClassName) {
        this.euClassName = euClassName;
    }

    public BorderType getBorderType() {
        return borderType;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public Boolean getActiveLast() {
        return activeLast;
    }

    public void setActiveLast(Boolean activeLast) {
        this.activeLast = activeLast;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }


    public String toEnumsStr() {
        StringBuffer enumBuffer = new StringBuffer();
        if (this.getBindClass() != null && this.getBindClass().length > 0) {
            for (Class clazz : this.getBindClass()) {
                enumBuffer.append(clazz.getName() + ".class,");
            }
//        } else if (this.getBindClassName() != null && !this.getBindClassName().equals("")&& !this.getBindClassName().equals(Void.class.getName()) && !getBindClassName().equals(Enum.class.getName())) {
//            enumBuffer.append(this.getBindClassName() + ".class");
        } else {
            enumBuffer.append("null");
        }
        String enumStr = enumBuffer.toString();
        if (enumStr.endsWith(",")) {
            enumStr = enumStr.substring(0, enumStr.length() - 1);
        }
        return enumStr;
    }


}
