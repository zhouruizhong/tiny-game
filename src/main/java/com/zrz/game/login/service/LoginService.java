package com.zrz.game.login.service;

import com.zrz.game.factory.MysqlSessionFactory;
import com.zrz.game.login.dao.IUserDao;
import com.zrz.game.login.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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

    public UserModel login(String username, String password){
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return null;
        }

        logger.info("当前线程 = {}", Thread.currentThread().getName());

        UserModel userModel;
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
            return userModel;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
