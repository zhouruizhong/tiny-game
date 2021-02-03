package com.zrz.game.login.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zrz.game.factory.MysqlSessionFactory;
import com.zrz.game.login.model.UserModel;
import com.zrz.game.processor.AsyncOperationProcessor;
import com.zrz.game.processor.IAsyncOperation;
import com.zrz.game.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.function.Function;

/**
 * 登录
 * @author zrz
 * @date 2021/1/28 14:25
 */
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    private static final LoginService INSTANCE = new LoginService();

    private LoginService(){}

    public static LoginService getInstance(){
        return INSTANCE;
    }

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @param callback 回调函数
     */
    public void login(String username, String password, Function<UserModel, Void> callback){
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return;
        }

        logger.info("当前线程 = {}", Thread.currentThread().getName());

        IAsyncOperation asyncOperation = new AsyncGetUserByName(username, password) {

            @Override
            public void doFinish() {
                callback.apply(this.getUserModel());
            }
        };
        AsyncOperationProcessor.getInstance().process(asyncOperation);
    }

    private void updateUserBasicInfoInRedis(UserModel model){
        if (null == model) {
            return;
        }

        try (Jedis redis = RedisUtil.getJedis()) {
            Object object = JSON.toJSON(model);
            redis.hset("User_" + model.getUserId(), "BasicInfo", object.toString());
        } catch (Exception e) {
          logger.error(e.getMessage(), e);
        }
    }

    /**
     * 异步获取用户
     */
    public static class AsyncGetUserByName implements IAsyncOperation{
        private final String username;
        private final String password;
        private UserModel userModel = null;

        public AsyncGetUserByName(String username, String password){
            this.username = username;
            this.password = password;
        }

        public UserModel getUserModel(){
            return userModel;
        }

        @Override
        public int bindId() {
            return username.charAt(username.length() -1);
        }

        @Override
        public void doAsync() {
            try(SqlSession sqlSession = MysqlSessionFactory.openSession()) {
                //IUserDao dao = sqlSession.getMapper(IUserDao.class);
                List<UserModel> userList = sqlSession.selectList("getUserByName", username);
                //List<UserModel> userList = dao.getUserByName(username);
                if (null != userList && !userList.isEmpty()) {
                    userModel = userList.get(0);
                    if (!password.equals(userModel.getPassword())) {
                        logger.error("密码错误，username = {}", username);
                        throw new RuntimeException("密码错误");
                    }
                } else {
                    userModel = new UserModel();
                    userModel.setUserName(username);
                    userModel.setPassword(password);
                    userModel.setHeroAvatar("Hero_Shaman");

                    sqlSession.insert("insert", userModel);
                    //dao.insert(userModel);
                }

                LoginService.getInstance().updateUserBasicInfoInRedis(userModel);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
