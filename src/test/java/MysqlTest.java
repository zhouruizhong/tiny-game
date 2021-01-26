import com.zrz.game.GameServer;
import com.zrz.game.factory.AbstractEntityHelper;
import com.zrz.game.factory.EntityHelperFactory;
import com.zrz.game.model.Clsbqy;
import com.zrz.game.utils.EntityHelper;
import javassist.ClassPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class MysqlTest {

  private static final Logger logger = LoggerFactory.getLogger(GameServer.class);

  public static void main(String[] args) throws Exception {
    // 加载驱动
    Class.forName("com.mysql.jdbc.Driver");
    // 数据库连接
    String url = "jdbc:mysql://localhost:3306/zrz?user=root&password=123456";
    // 创建连接
    Connection conn = DriverManager.getConnection(url);
    //
    String sql = "select * from clsbqy";

    PreparedStatement ps = conn.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();

    long start = System.currentTimeMillis();
    while (rs.next()) {
      // 手写代码耗时23ms 左右
      /*Clsbqy clsbqy = new Clsbqy();
      clsbqy.setDate(rs.getString("日期"));
      clsbqy.setNumber(rs.getString("序号"));
      clsbqy.setCompany(rs.getString("企业名称"));
      clsbqy.setCaseId(rs.getString("流水号"));
      clsbqy.setTagNumber(rs.getString("标记号"));
      clsbqy.setIsPrint(rs.getString("是否打印"));
      clsbqy.setFirstResult(rs.getString("初步筛选结果"));
      clsbqy.setIsProblem(rs.getString("是否有问题"));
      clsbqy.setReHandle(rs.getString("需重新处理"));*/
      // 反射耗时42ms 左右

      AbstractEntityHelper helper = EntityHelperFactory.getEntityHelper(Clsbqy.class);
    }

    long end = System.currentTimeMillis();

    ps.close();
    conn.close();
    logger.info("实例化耗时：" + (end - start) + "ms");
  }

  public Object create(java.sql.ResultSet rs) throws Exception {
    Clsbqy clsbqy = new Clsbqy();
    clsbqy.setId(rs.getInt("id"));
    clsbqy.setDate(rs.getString("日期"));
    clsbqy.setNumber(rs.getString("序号"));
    clsbqy.setCompany(rs.getString("企业名称"));
    clsbqy.setCaseId(rs.getString("流水号"));
    clsbqy.setTagNumber(rs.getString("标记号"));
    clsbqy.setIsPrint(rs.getString("是否打印"));
    clsbqy.setFirstResult(rs.getString("初步筛选结果"));
    clsbqy.setIsProblem(rs.getString("是否有问题"));
    clsbqy.setReHandle(rs.getString("需重新处理"));
    clsbqy.setPrintMaterial(rs.getString("11-17打印材料"));
    return clsbqy;
  }
}
