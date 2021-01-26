package com.zrz.game.factory;

import com.zrz.game.annotation.Column;
import javassist.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.ResultSet;

public final class EntityHelperFactory {

    private EntityHelperFactory(){

    }

    public static AbstractEntityHelper getEntityHelper(Class<?> entityClazz) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException, IOException {
        if (null == entityClazz) {
            return null;
        }

        ClassPool pool = ClassPool.getDefault();
        pool.appendSystemPath();

        pool.importPackage(ResultSet.class.getPackage().getName());
        pool.importPackage(entityClazz.getName());

        // 获取 AbstractEntityHelper 类
        CtClass clazzHelper = pool.getCtClass(AbstractEntityHelper.class.getName());
        // 要创建的助手类名称
        String helperClazzName = entityClazz.getName() + "Helper";

        // 创建XxxEntityHelper 继承 AbstractEntityHelper
        CtClass cc = pool.makeClass(helperClazzName, clazzHelper);

        // 创建构造器
        CtConstructor constructor = new CtConstructor(new CtClass[0], cc);
        constructor.setBody("{}");
        cc.addConstructor(constructor);

        String clazzName = entityClazz.getSimpleName();
        // 创建方法
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("public Object create(java.sql.ResultSet rs) throws Exception {\n");
        stringBuilder.append(clazzName).append(" ");
        stringBuilder.append(clazzName.toLowerCase()).append(" = new ");
        stringBuilder.append(clazzName).append("(); \n");

        Field[] fields = entityClazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if (null == column) {
                continue;
            }

            String first = field.getName().substring(0,1);
            String last = field.getName().substring(1);

            String columnName = column.name();
            if (field.getType() == Integer.class) {
                stringBuilder.append(clazzName.toLowerCase()).append(".");
                stringBuilder.append("set");
                stringBuilder.append(first.toUpperCase());
                stringBuilder.append(last);
                stringBuilder.append("(");
                stringBuilder.append("rs.getInt(\"");
                stringBuilder.append(columnName);
                stringBuilder.append("\")); \n");
            } else if (field.getType().equals(String.class)) {
                stringBuilder.append(clazzName.toLowerCase()).append(".");
                stringBuilder.append("set");
                stringBuilder.append(first.toUpperCase());
                stringBuilder.append(last);
                stringBuilder.append("(");
                stringBuilder.append("rs.getString(\"");
                stringBuilder.append(columnName);
                stringBuilder.append("\")); \n");
            }
        }

        stringBuilder.append("return ").append(clazzName.toLowerCase()).append(";\n");
        stringBuilder.append("}");

        CtMethod ctMethod = CtMethod.make(stringBuilder.toString(), cc);
        cc.addMethod(ctMethod);

        cc.writeFile("d://zrz");
        cc.defrost();

        Class<?> javaClazz = cc.toClass();
        Object helpImpl = javaClazz.newInstance();
        return (AbstractEntityHelper) helpImpl;
    }
}
