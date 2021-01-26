import com.zrz.game.utils.EhCacheUtils;

public class EhCacheTest {

  public static void main(String[] args) {
    EhCacheUtils.put("test", "123456789");
    System.out.println(EhCacheUtils.get("test"));
  }
}
