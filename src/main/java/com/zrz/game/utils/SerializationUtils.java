package com.zrz.game.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 序列化工具类
 *
 * @author zrz
 * @date 2021/1/25 15:30
 */
public class SerializationUtils {

  public static byte[] serialize(Object object) {
    ObjectOutputStream oos = null;
    ByteArrayOutputStream baos = null;
    try {
      // 序列化
      baos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(baos);
      oos.writeObject(object);
      return baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close(oos);
      close(baos);
    }
    return null;
  }

  public static Object unserialize(byte[] bytes) {
    ByteArrayInputStream bais = null;
    ObjectInputStream ois = null;
    try {
      // 反序列化
      bais = new ByteArrayInputStream(bytes);
      ois = new ObjectInputStream(bais);
      return ois.readObject();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close(bais);
      close(ois);
    }
    return null;
  }

  /**
   * 序列化 list 集合
   *
   * @param list
   * @return
   */
  public static <T> byte[] serializeList(List<T> list) {

    if (list == null || list.size() <= 0) {
      return null;
    }
    ObjectOutputStream oos = null;
    ByteArrayOutputStream baos = null;
    byte[] bytes = null;
    try {
      baos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(baos);
      for (Object obj : list) {
        oos.writeObject(obj);
      }
      bytes = baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close(oos);
      close(baos);
    }
    return bytes;
  }

  /**
   * 反序列化 list 集合
   *
   * @param bytes
   * @return
   */
  public static <T> List<T> unserializeList(byte[] bytes) {
    if (bytes == null) {
      return null;
    }

    List<T> list = new ArrayList<>();
    ByteArrayInputStream bais = null;
    ObjectInputStream ois = null;
    try {
      // 反序列化
      bais = new ByteArrayInputStream(bytes);
      ois = new ObjectInputStream(bais);
      while (bais.available() > 0) {
        T obj = (T)ois.readObject();
        if (obj == null) {
          break;
        }
        list.add(obj);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      close(bais);
      close(ois);
    }
    return list;
  }

  /**
   * 关闭io流对象
   *
   * @param closeable 流
   */
  public static void close(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }


}
