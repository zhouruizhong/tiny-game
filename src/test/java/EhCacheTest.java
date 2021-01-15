import com.zrz.game.utils.EhCacheUtils;

public class EhCacheTest {

  public static void main(String[] args) {
    EhCacheUtils.put("test", "test");
    System.out.println(EhCacheUtils.get("test"));
  }
}
