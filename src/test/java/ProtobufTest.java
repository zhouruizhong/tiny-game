
import org.junit.Test;

import java.io.*;

/**
 * @author zrz
 */
public class ProtobufTest {

    /*PersonModel.Person.Builder builder = PersonModel.Person.newBuilder();
    PersonModel.Person person = builder.build();

    @Test
    public void test1() throws InvalidProtocolBufferException {
        builder.setId(2);
        builder.setName("李四");
        builder.setAge("25");
        // 第一种序列化
        //序列化
        byte[] data = person.toByteArray();
        //反序列化
        PersonModel.Person result = PersonModel.Person.parseFrom(data);
        System.out.println(result.getName());
    }

    @Test
    public void test2() throws IOException {
        // 第二种序列化，粘包，将一个或者多个protobuf对象字节写入stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 生成一个由，[字节长度][字节数据]组成的package,特别适合RPC场景
        person.writeDelimitedTo(byteArrayOutputStream);
        // 反序列化，从stream 中读取一个或者多个protobuf字节对象
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        PersonModel.Person result = PersonModel.Person.parseDelimitedFrom(byteArrayInputStream);
        System.out.println(result.getName());
    }

    public void test3() throws IOException {
        // 第三种序列化，写入文件或者socket
        FileOutputStream fileOutputStream = new FileOutputStream(new File("e:/read.txt"));
        person.writeTo(fileOutputStream);
        fileOutputStream.close();

        FileInputStream fileInputStream = new FileInputStream(new File("/test.dt"));
        PersonModel.Person result = PersonModel.Person.parseFrom(fileInputStream);
        System.out.println(result);
    }*/

}
