package net.ooder.esd.custom;

import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.bean.*;
import net.ooder.esd.bean.field.CustomFieldBean;
import net.ooder.esd.bean.field.FieldBean;
import net.ooder.esd.bean.field.SearchFieldBean;
import net.ooder.esd.bean.field.LabelBean;
import net.ooder.esd.bean.field.combo.ComboBoxBean;
import net.ooder.esd.bean.grid.GridColItemBean;
import java.lang.reflect.Type;

public interface ESDField<M , N extends ComboBoxBean> {

    @CustomAnnotation(hidden = true, uid = true)
    ESDClass getESDClass();

    @CustomAnnotation(hidden = true, uid = true)
    String getId();

    @CustomAnnotation(hidden = true, uid = true)
    String getFieldName();

    @CustomAnnotation(caption = "返回真实类型")
    Class getReturnType();

    @CustomAnnotation(caption = "返回泛型类型")
    Type getGenericType();

    @CustomAnnotation(caption = "字段名称")
    String getName();

    @CustomAnnotation(caption = "表达式")
    String getExpression();

    @CustomAnnotation(caption = "目标框架")
    String getTarget();

    @CustomAnnotation(caption = "是否隐藏")
    boolean isHidden();

    @CustomAnnotation(caption = "是否主键")
    boolean isUid();

    @CustomAnnotation(caption = "是否显示值")
    boolean isCaption();

    @CustomAnnotation(caption = "父级主键")
    boolean isPid();

    @CustomAnnotation(caption = "是否只读")
    boolean isReadonly();

    @CustomAnnotation(caption = "是否禁用")
    boolean isDisabled();

    @CustomAnnotation(caption = "是否序列化")
    boolean isSerialize();

    @CustomAnnotation(caption = "分隔符")
    boolean isSplit();


    @CustomAnnotation(caption = "Icon")
    String getImageClass();

    @CustomAnnotation(caption = "当前值")
    Object getValue();

    @CustomAnnotation(caption = "描述")
    String getDesc();


    @CustomAnnotation(caption = "模板ID")
    String getProjectVersionName();

    @CustomAnnotation(caption = "领域模板ID")
    String getDomainId();

    @CustomAnnotation(caption = "宽")
    String getWidth();

    @CustomAnnotation(caption = "高")
    String getHeight();

    @CustomAnnotation(caption = "显示值")
    String getCaption();

    @CustomAnnotation(caption = "关联类型")
    CustomRefBean getRefBean();

    @CustomAnnotation(caption = "列表配置")
    GridColItemBean getGridColItemBean();


    @CustomAnnotation(caption = "布局信息")
    CustomLayoutItemBean getLayoutItemBean();


    @CustomAnnotation(caption = "右键菜单")
    RightContextMenuBean getContextMenuBean();


    @CustomAnnotation(caption = "表单配置")
    FieldBean getFieldBean();

    @CustomAnnotation(caption = "通用配置")
    CustomFieldBean getCustomBean();

    @CustomAnnotation(caption = "禁用配置")
    DisabledBean getDisabledBean();

    @CustomAnnotation(caption = "提示配置")
    TipsBean getTipsBean();

    @CustomAnnotation(caption = "检索条件")
    SearchFieldBean getSearchFieldBean();

    @CustomAnnotation(caption = "标签配置")
    LabelBean getLabelBean();

    @CustomAnnotation(caption = "停靠设定")
    DockBean getDockBean();

    @CustomAnnotation(caption = "容器配置")
    ContainerBean getContainerBean();

    @CustomAnnotation(caption = "高级类型")
    ComponentType getComponentType();

    String[] getEnums();

    Class<? extends Enum>  getEnumClass();

    M getWidgetConfig();

    N getComboConfig();
}
