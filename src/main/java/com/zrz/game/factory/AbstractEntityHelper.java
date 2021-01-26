package com.zrz.game.factory;

import java.sql.ResultSet;

/**
 * 抽象的实体助手
 *
 * @author 周瑞忠
 * @date 2021-1-18 9:19
 */
public abstract class AbstractEntityHelper {

    /**
     * 构建实体
     *
     * @param resultSet 结果集
     * @return 实体对象
     * @throws Exception 异常
     */
    public abstract Object create(ResultSet resultSet) throws Exception;

}
