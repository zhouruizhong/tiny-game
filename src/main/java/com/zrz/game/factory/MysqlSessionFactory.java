package com.zrz.game.factory;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * mysql 会话工厂
 *
 * @author zrz
 * @date 2021/1/28 14:12
 */
public final class MysqlSessionFactory {

    private static SqlSessionFactory sqlSessionFactory;

    private MysqlSessionFactory(){}

    public static void init(){
        try{
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(
                Resources.getResourceAsStream("mybatis-config.xml")
            );
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    public static SqlSession openSession(){
        if (null == sqlSessionFactory) {
            throw new RuntimeException("sqlSessionFactory 未初始化");
        }
        return sqlSessionFactory.openSession(true);
    }

}
