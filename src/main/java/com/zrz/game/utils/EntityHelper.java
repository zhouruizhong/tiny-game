package com.zrz.game.utils;

import com.zrz.game.annotation.Column;
import com.zrz.game.model.Clsbqy;

import java.lang.reflect.Field;
import java.sql.ResultSet;

/**
 * @author 52844
 */
public class EntityHelper {

    public static <T> T create(Class<T> clazz, ResultSet rs) throws Exception{
        if (null == rs) {
            return null;
        }
        Object object = clazz.newInstance();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if (null == column) {
                continue;
            }
            String columnName = column.name();
            Object columnValue = rs.getObject(columnName);
            if (null == columnValue) {
                continue;
            }
            field.set(object, columnValue);
        }
        return (T) object;
    }
}
