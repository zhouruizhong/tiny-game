
import com.google.protobuf.InvalidProtocolBufferException;
import com.zrz.game.model.Person;
import com.zrz.game.protobuf.PersonModel;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author zrz
 */
public class ProtobufTest {

    @Test
    public void test() throws InvalidProtocolBufferException {
        PersonModel.Person.Builder builder = PersonModel.Person.newBuilder();
        builder.setId(2);
        builder.setName("李四");
        builder.setAge("25");

        PersonModel.Person person = builder.build();
        //序列化
        byte[] data = person.toByteArray();
        //反序列化
        PersonModel.Person result = PersonModel.Person.parseFrom(data);
        System.out.println(result.getName());
    }
}
