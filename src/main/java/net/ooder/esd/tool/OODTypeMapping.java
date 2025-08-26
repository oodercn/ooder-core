
package net.ooder.esd.tool;

import net.ooder.annotation.ColType;
import net.ooder.annotation.Enumstype;
import net.ooder.annotation.VFSEnumstype;
import net.ooder.common.database.metadata.ColInfo;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;

import net.ooder.esd.annotation.ui.ComboInputType;
import net.ooder.esd.annotation.ui.ComboType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.web.util.JSONGenUtil;

import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * To change the template for this generated type comment go to Window>Preferences>Java>Code Generation>Code and
 * Comments
 */
public class OODTypeMapping {


    static Class[] customClass = new Class[]{
            String.class,
            Integer.class,
            Date.class,
            LocalDate.class,
            Short.class,
            Long.class,
            Float.class,
            short.class,
            int.class,
            long.class,
            float.class,
            byte.class,
            StringBuffer.class,
            BigInteger.class,
            BigDecimal.class};


    public static ComboInputType getType(Class clazz) {

        if (Enumstype.class.isAssignableFrom(clazz)) {
            return ComboInputType.listbox;
        }
        if (VFSEnumstype.class.isAssignableFrom(clazz)) {
            return ComboInputType.listbox;

        }

        if (clazz.isEnum()) {
            return ComboInputType.combobox;

        }

        if (clazz.isArray() || Collection.class.isAssignableFrom(clazz)) {
            return ComboInputType.combobox;
        }

        if (java.sql.Date.class.isAssignableFrom(clazz)) {
            return ComboInputType.date;
        }
        if (clazz == short.class || clazz == Short.class) {
            return ComboInputType.number;
        }
        if (clazz == boolean.class || clazz == Boolean.class) {
            return ComboInputType.checkbox;
        }

        if (clazz == int.class || clazz == Integer.class) {
            return ComboInputType.number;
        }

        if (clazz == long.class || clazz == Long.class) {
            return ComboInputType.number;
        }

        if (clazz == float.class || clazz == Float.class) {
            return ComboInputType.number;
        }

        if (clazz == BigDecimal.class) {
            return ComboInputType.number;
        }

        if (clazz == BigInteger.class) {
            return ComboInputType.number;
        }

        if (clazz == Date.class) {
            return ComboInputType.date;
        }

        if (clazz == LocalDate.class) {
            return ComboInputType.date;
        }


        if (clazz == java.sql.Date.class) {
            return ComboInputType.date;
        }


        if (clazz == java.sql.Date.class) {
            return ComboInputType.date;
        }

        if (clazz == java.sql.Time.class) {
            return ComboInputType.datetime;
        }

        if (clazz == java.sql.Timestamp.class) {
            return ComboInputType.datetime;
        }


        return ComboInputType.input;


    }

    public static Class guessResultType(ComponentType componentType) {
        Class clazz = String.class;

        switch (componentType) {

            case SVGPAPER:
                clazz = ResultModel.class;
                break;
            case BUTTONVIEWS:
                clazz = ResultModel.class;
                break;
            case GALLERY:
                clazz = ResultModel.class;
                break;
            case TREEVIEW:
                clazz = TreeListResultModel.class;
                break;
            case TREEBAR:
                clazz = TreeListResultModel.class;
                break;
            case TABS:
                clazz = ListResultModel.class;
                break;
            case TREEGRID:
                clazz = ListResultModel.class;
                break;
            case ECHARTS:
                clazz = ListResultModel.class;
                break;
            case TIMER:
                clazz = Date.class;
                break;
            case LIST:
                clazz = List.class;
                break;
            case INPUT:
                clazz = String.class;
                break;
            case COMBOINPUT:
                clazz = String.class;
                break;
            case HIDDENINPUT:
                clazz = String.class;
                break;
            case LINK:
                clazz = String.class;
                break;
            case SLIDER:
                clazz = String.class;
                break;
            case SPAN:
                clazz = String.class;
                break;
            case LABEL:
                clazz = String.class;
                break;
            case ICON:
                clazz = String.class;
                break;
            case RICHEDITOR:
                clazz = StringBuffer.class;
                break;
            case MULTILINES:
                clazz = StringBuffer.class;
                break;
            default:
                clazz = ResultModel.class;
        }

        return clazz;

    }

    public static Class guessResultComboType(ComboType comboType) {
        Class clazz = String.class;
        switch (comboType) {
            case module:
                clazz = ResultModel.class;
                break;
            case list:
                clazz = List.class;
                break;
            case number:
                clazz = Number.class;
                break;
            default:
                clazz = String.class;

        }
        return clazz;

    }


    public static ColType guessInputDBType(ComboInputType type) {
        ColType colType = ColType.VARCHAR2;
        switch (type) {
            case number:
                colType = ColType.FLOAT;
                break;
            case datetime:
                colType = ColType.DATETIME;
                break;
            case date:
                colType = ColType.DATE;
                break;
            case time:
                colType = ColType.INT;
                break;
            case checkbox:
                colType = ColType.BOOLEAN;
                break;
            case spin:
                colType = ColType.INT;
                break;
            case currency:
                colType = ColType.FLOAT;
                break;

        }
        return colType;
    }


    public static ColType guessComponentDBType(ComponentType type) {
        ColType colType = ColType.VARCHAR2;
        switch (type) {
            case CHECKBOX:
                colType = ColType.BOOLEAN;
                break;
            case RICHEDITOR:
                colType = ColType.TEXT;
                break;
            case DATEPICKER:
                colType = ColType.DATETIME;
                break;
            case JAVAEDITOR:
                colType = ColType.TEXT;
                break;
            case CODEEDITOR:
                colType = ColType.TEXT;
                break;
            case FILEUPLOAD:
                colType = ColType.BLOB;
                break;
        }
        return colType;

    }


    public static Class genType(ComboInputType type) {
        Class clazz = String.class;
        switch (type) {
            case number:
                clazz = Integer.class;
                break;
            case datetime:
                clazz = Date.class;
                break;
            case date:
                clazz = Date.class;
                break;
            case checkbox:
                clazz = Boolean.class;
                break;
            case spin:
                clazz = Integer.class;
                break;
            case currency:
                clazz = Long.class;
                break;

        }

        return clazz;


    }



    public static ComponentType getComponentType(Class clazz) {
        if (clazz != null && (clazz.isArray() || Collection.class.isAssignableFrom(clazz))) {
            if (Arrays.asList(customClass).contains(clazz) || clazz.equals(clazz)) {
                return ComponentType.LIST;
            }
        }

        if (java.sql.Date.class.isAssignableFrom(clazz)) {
            return ComponentType.COMBOINPUT;

        }
        if (clazz.equals(short.class) || clazz.equals(Short.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
            return ComponentType.CHECKBOX;
        }

        if (clazz.equals(long.class) || clazz.equals(Long.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(float.class) || clazz.equals(Float.class)) {
            return ComponentType.COMBOINPUT;
        }


        if (clazz.equals(BigDecimal.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(BigInteger.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(Date.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(java.sql.Date.class)) {
            return ComponentType.COMBOINPUT;
        }


        if (clazz.equals(java.sql.Date.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(java.sql.Time.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(Reader.class)) {
            return ComponentType.RICHEDITOR;
        }
        if (clazz.equals(StringBuffer.class)) {
            return ComponentType.RICHEDITOR;
        }

        return ComponentType.INPUT;

    }

//    public static Class genComponentType(ComponentType type) {
//
//        Class clazz = String.class;
//        switch (type) {
//            case List:
//                clazz = List.class;
//                break;
//            case Group:
//                clazz = ResultModel.class;
//                break;
//            case RichEditor:
//                clazz = StringBuffer.class;
//                break;
//
//        }
//
//        return clazz;
//
//    }

    public static ComponentType getComponentType(Class typeClass, Type type) {

        Class clazz = JSONGenUtil.getInnerType(type);

        if (Enumstype.class.isAssignableFrom(clazz) || clazz.isEnum()) {
            return ComponentType.COMBOINPUT;
        }

        if (typeClass != null && (typeClass.isArray() || Collection.class.isAssignableFrom(typeClass))) {
            if (Arrays.asList(customClass).contains(clazz) || typeClass.equals(clazz)) {
                return ComponentType.LIST;
            }
        }

        if (java.sql.Date.class.isAssignableFrom(clazz)) {
            return ComponentType.COMBOINPUT;

        }
        if (clazz.equals(short.class) || clazz.equals(Short.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
            return ComponentType.CHECKBOX;
        }

        if (clazz.equals(long.class) || clazz.equals(Long.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(float.class) || clazz.equals(Float.class)) {
            return ComponentType.COMBOINPUT;
        }


        if (clazz.equals(BigDecimal.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(BigInteger.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(Date.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(java.sql.Date.class)) {
            return ComponentType.COMBOINPUT;
        }


        if (clazz.equals(java.sql.Date.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(java.sql.Time.class)) {
            return ComponentType.COMBOINPUT;
        }

        if (clazz.equals(Reader.class)) {
            return ComponentType.RICHEDITOR;
        }
        if (clazz.equals(StringBuffer.class)) {
            return ComponentType.RICHEDITOR;
        }

//        if (EsbBeanFactory.findClass(clazz.getName()) != null) {
//            return ComponentType.Module;
//        }

        return ComponentType.INPUT;


    }


    public static ComponentType getComponentType(ColInfo field) {
        int type = field.getDataType();
        ComponentType mappedType = null;
        ColType colType = field.getColType();
        switch (colType) {
            case ENUM:
                mappedType = ComponentType.COMBOINPUT;
                break;
            case INT:
                mappedType = ComponentType.COMBOINPUT;
                break;
            case INTEGER:
                mappedType = ComponentType.COMBOINPUT;
                break;
            case BIGINT:
                mappedType = ComponentType.COMBOINPUT;
                break;
            case TEXT:
                mappedType = ComponentType.MULTILINES;
                break;
            case BOOLEAN:
                mappedType = ComponentType.CHECKBOX;
                break;
            case DATETIME:
                mappedType = ComponentType.COMBOINPUT;
                break;
            case DATE:
                mappedType = ComponentType.COMBOINPUT;
                break;
            case TIME:
                mappedType = ComponentType.COMBOINPUT;
                break;
            case TIMESTAMP:
                mappedType = ComponentType.COMBOINPUT;
                break;
            case FLOAT:
                mappedType = ComponentType.COMBOINPUT;
                break;
            case JSON:
                mappedType = ComponentType.RICHEDITOR;
                break;
            case SET:
                mappedType = ComponentType.MULTILINES;
                break;
            default:
                mappedType = ComponentType.INPUT;
        }
        if (mappedType.equals(ComboInputType.input)) {
            switch (type) {
                case Types.ARRAY:
                    mappedType = ComponentType.COMBOINPUT;//"ood.UI.Input";
                    break;
                case Types.BIGINT:
                    mappedType = ComponentType.COMBOINPUT;//"ood.UI.ComboInput";
                    break;
                case Types.BINARY:
                    mappedType = ComponentType.COMBOINPUT;
                    //   mappedType = "java.io.InputStream";
                    break;
                case Types.BIT:
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                case Types.BLOB:
                    mappedType = ComponentType.CHECKBOX;
                    break;
                case 16: // Types.BOOLEAN
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                case Types.CHAR:
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                case Types.CLOB:
                    mappedType = ComponentType.RICHEDITOR;
                    break;
                case Types.DATE:
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                case Types.DECIMAL:
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                case Types.DOUBLE:
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                case Types.FLOAT:
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                case Types.INTEGER:
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                case Types.LONGVARBINARY:
                    mappedType = ComponentType.RICHEDITOR;
                    break;
                case Types.LONGNVARCHAR:
                    mappedType = ComponentType.RICHEDITOR;
                    break;
                case Types.LONGVARCHAR:
                    mappedType = ComponentType.RICHEDITOR;
                    break;
                case Types.NUMERIC:
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                case Types.REAL:
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                case Types.SMALLINT:
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                case Types.TIME:
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                case Types.TIMESTAMP:
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                case Types.TINYINT:
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                case Types.VARBINARY:
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                case Types.VARCHAR:
                    mappedType = ComponentType.COMBOINPUT;
                    break;
                default:
                    mappedType = ComponentType.INPUT;
            }
        }

        return mappedType;
    }

    public static ComboInputType getType(ColInfo field) {
        ComboInputType mappedType = null;
        int type = field.getDataType();
        ColType colType = field.getColType();
        switch (colType) {
            case ENUM:
                mappedType = ComboInputType.listbox;
                break;
            case INTEGER:
                mappedType = ComboInputType.spin;
                break;
            case BIGINT:
                mappedType = ComboInputType.number;
                break;
            case TEXT:
                mappedType = ComboInputType.text;
                break;
            case BOOLEAN:
                mappedType = ComboInputType.checkbox;
                break;
            case DATETIME:
                mappedType = ComboInputType.datetime;
                break;
            case DATE:
                mappedType = ComboInputType.date;
                break;
            case TIME:
                mappedType = ComboInputType.time;
                break;
            case TIMESTAMP:
                mappedType = ComboInputType.datetime;
                break;
            case FLOAT:
                mappedType = ComboInputType.number;
                break;
            case JSON:
                mappedType = ComboInputType.text;
                break;
            case SET:
                mappedType = ComboInputType.text;
                break;
            default:
                mappedType = ComboInputType.input;
        }

        if (mappedType.equals(ComboInputType.input)) {
            switch (type) {

                case Types.ARRAY:
                    mappedType = ComboInputType.input;
                    break;
                case Types.BIGINT:
                    mappedType = ComboInputType.number;
                    break;
                case Types.BINARY:
                    mappedType = ComboInputType.input;
                    //   mappedType = "java.io.InputStream";
                    break;
                case Types.BIT:
                    mappedType = ComboInputType.input;
                    break;
                case Types.BLOB:
                    mappedType = ComboInputType.file;
                    break;
                case Types.BOOLEAN:
                    mappedType = ComboInputType.listbox;
                    break;
                case Types.CHAR:
                    mappedType = ComboInputType.input;
                    break;
                case Types.CLOB:
                    mappedType = ComboInputType.input;
                    break;
                case Types.DATE:
                    mappedType = ComboInputType.date;
                    break;

                case Types.DECIMAL:
                    mappedType = ComboInputType.number;
                    break;
                case Types.DOUBLE:
                    mappedType = ComboInputType.number;
                    break;
                case Types.FLOAT:
                    mappedType = ComboInputType.number;
                    break;
                case Types.INTEGER:
                    mappedType = ComboInputType.number;
                    break;
                case Types.LONGVARBINARY:
                    mappedType = ComboInputType.input;
                    break;
                case Types.LONGVARCHAR:
                    mappedType = ComboInputType.input;
                    break;
                case Types.NUMERIC:
                    mappedType = ComboInputType.number;
                    break;
                case Types.REAL:
                    mappedType = ComboInputType.number;
                    break;
                case Types.SMALLINT:
                    mappedType = ComboInputType.number;
                    break;
                case Types.TIME:
                    mappedType = ComboInputType.datetime;
                    break;
                case Types.TIMESTAMP:
                    mappedType = ComboInputType.datetime;
                    break;
                case Types.TINYINT:
                    mappedType = ComboInputType.number;
                    break;
                case Types.VARBINARY:
                    mappedType = ComboInputType.input;
                    break;
                case Types.VARCHAR:
                    mappedType = ComboInputType.input;
                    break;
                default:
                    mappedType = ComboInputType.input;
            }
        }


        return mappedType;
    }


    public static ComboInputType getComboInputType(ColInfo field) {
        int type = field.getDataType();
        ComboInputType mappedType = null;
        int fractions = field.getFractions();

        boolean primitive = false;
        switch (type) {

            case Types.ARRAY:
                mappedType = ComboInputType.input;
                break;
            case Types.BIGINT:
                mappedType = ComboInputType.number;
                break;
            case Types.BINARY:
                mappedType = ComboInputType.input;
                //   mappedType = "java.io.InputStream";
                break;
            case Types.BIT:
                mappedType = ComboInputType.input;
                break;
            case Types.BLOB:
                mappedType = ComboInputType.getter;
                break;
            case 16: // Types.BOOLEAN
                mappedType = ComboInputType.listbox;
                break;
            case Types.CHAR:
                mappedType = ComboInputType.input;
                break;
            case Types.CLOB:
                mappedType = ComboInputType.input;
                break;
            case Types.DATE:
                mappedType = ComboInputType.date;
                break;

            case Types.DECIMAL:
                mappedType = ComboInputType.number;
                break;
            case Types.DOUBLE:
                mappedType = ComboInputType.number;
                break;
            case Types.FLOAT:
                mappedType = ComboInputType.number;
                break;
            case Types.INTEGER:
                mappedType = ComboInputType.number;
                break;
            case Types.LONGVARBINARY:
                mappedType = ComboInputType.input;
                break;
            case Types.LONGVARCHAR:
                mappedType = ComboInputType.input;
                break;
            case Types.NUMERIC:
                mappedType = ComboInputType.number;
                break;
            case Types.REAL:
                mappedType = ComboInputType.number;
                break;
            case Types.SMALLINT:
                mappedType = ComboInputType.number;
                break;
            case Types.TIME:
                mappedType = ComboInputType.datetime;
                break;
            case Types.TIMESTAMP:
                mappedType = ComboInputType.datetime;
                break;
            case Types.TINYINT:
                mappedType = ComboInputType.number;
                break;
            case Types.VARBINARY:
                mappedType = ComboInputType.input;
                break;
            case Types.VARCHAR:
                mappedType = ComboInputType.input;
                break;
            default:
                mappedType = ComboInputType.input;
        }
        return mappedType;
    }

}
