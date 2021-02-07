package com.zrz.game.utils;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.*;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author zrz
 * @date 2021/2/4 17:22
 */
public class RabbitMqUtils {

    private static Logger logger = LoggerFactory.getLogger(RabbitMqUtils.class);

    private ConnectionFactory factory;
    private Connection connection ;
    private Channel channel ;


    /**
     * 初始化RabbitMq连接工具类
     * @param host          主机
     * @param port          端口
     * @param userName      用户名
     * @param password      密码
     * @param virtualHost   虚拟主机
     * @throws IOException
     * @throws TimeoutException
     */
    public RabbitMqUtils(String host, int port, String  userName, String password, String virtualHost) throws IOException, TimeoutException {
        this.factory = this.initConnectionFactory(host, port, userName, password, virtualHost);
        // 创建与RabbitMQ服务器的TCP连接
        this.connection = connection == null? this.factory.newConnection() : this.connection;
        this.channel = this.channel == null? this.connection.createChannel() : this.channel;
    }

    /**
     * 初始化rabbitMq服务配置
     * @param host          主机
     * @param port          端口
     * @param userName      用户名
     * @param password      密码
     * @param virtualHost   虚拟主机
     * @return
     */
    private ConnectionFactory initConnectionFactory(String host, int port, String  userName, String password, String virtualHost){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(userName);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        return factory;
    }

    /**
     * 绑定队列
     * @param exchangeName      交换机名
     * @param queueName         队列名
     * @param routingKey        路由KEY
     * @param type              消息模式：FANOUT|TOPIC|DIRECT
     * @param durable           是否持久化
     * @param autoDelete        是否自动删除队列
     * @throws IOException
     */
    private void queueBind(String exchangeName, String queueName, String routingKey,BuiltinExchangeType type, boolean durable, boolean autoDelete) throws IOException{
        // 声明交换机类型：交换机，类型，持久化
        this.channel.exchangeDeclare(exchangeName, type, durable);
        if (queueName != null) {
            if (type != BuiltinExchangeType.DIRECT) {
                // 声明默认的队列：队列，持久化，声明独占队列（仅限于此连接），自动删除队列，队列的其他属性
                this.channel.queueDeclare(queueName, durable, false, autoDelete, null);
            }
            // 将队列与交换机绑定
            this.channel.queueBind(queueName, exchangeName, routingKey);
        }
    }

    /**
     * 发送消息
     * @param exchangeName      交换机名
     * @param queneName         队列名
     * @param routingKey        路由KEY
     * @param type              消息模式：FANOUT|TOPIC|DIRECT
     * @param msg               消息
     * @return
     */
    public boolean sendMq(String exchangeName, String queneName, String routingKey, BuiltinExchangeType type,  String msg) {
        try {
            this.queueBind(exchangeName, queneName, routingKey, type, true, true);
            this.channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());
        } catch (Exception e) {
            logger.error("error", e);
            return false;
        }
        return true;
    }

    /**
     * 拉取队列消息
     * @param exchangeName      交换机名
     * @param queneName         队列名
     * @param routingKey        路由KEY
     * @param type              消息模式：FANOUT|TOPIC|DIRECT
     * @param headerInterface   回调实现
     * @throws IOException
     */
    public void pullMq(String exchangeName, String queneName, String routingKey, BuiltinExchangeType type, HeaderInterface headerInterface) throws IOException{
        if (queneName == null){
            queneName = (type == BuiltinExchangeType.DIRECT) ? this.channel.queueDeclare().getQueue(): queneName;
        }
        String newsQueueName = queneName;
        this.queueBind(exchangeName, newsQueueName, routingKey, type, true, true);
        // 创建接收客户端，当有消息，则直接回调handleDelivery方法
        Consumer consumer = new DefaultConsumer(this.channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                logger.info("exchang:{}, routingKey:{}, queueName:{}, message:{}", envelope.getExchange(), envelope.getRoutingKey(),newsQueueName, message);
                headerInterface.execute(consumerTag, body);
            }
        };
        // channel绑定队列、消费者，autoAck为true表示一旦收到消息则自动回复确认消息
        this.channel.basicConsume(newsQueueName, true, consumer);
    }

    /**
     * 关闭连接通道
     * @throws IOException
     * @throws TimeoutException
     */
    public void close() throws IOException, TimeoutException {
        if (this.channel != null) {
            this.channel.close();
            this.channel = null;
        }
        if (connection != null) {
            this.connection.close();
            this.connection = null;
        }
        this.factory = null;
    }

    /**
     * 函数式回调接口
     */
    @FunctionalInterface
    interface HeaderInterface{
        void execute(String consumerTag, byte[] body) throws IOException ;
    }

    /**
     * 测试入口
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String [] words = new String[]{"props","student","build","name","execute"};
        RabbitMqUtils rabbitMqUtils2 = new RabbitMqUtils("192.68.58.163", 5672, "lilo", "lilo123", "/lilo");
        int i =0;

        //FANOUT模式不需要routingKey，因此routingKey传空字符串（不要设置成null）

        JSONObject object = new JSONObject();
        object.put("docId", "21020400131");
        object.put("status", "2");
        object.put("messageId", UUID.randomUUID());
        object.put("constantValue", "通知材料");
        object.put("caseid", "21020400131");
        object.put("CDPFTYPE", "2");
        rabbitMqUtils2.sendMq("amq.fanout", "ZSWYDJ", "ZSWYDJ", BuiltinExchangeType.FANOUT,object.toJSONString());

        System.out.println("发送结束");

        /*System.out.println("接收fanout消息");
        rabbitMqUtils2.pullMq("amq.fanout", "test1", "", BuiltinExchangeType.FANOUT, (record, body) -> {
            String message = new String(body, "UTF-8");
            System.out.println(message);
        });*/

//        while (i < words.length) {
//            rabbitMqUtils2.sendMq("amq.topic", "test-topic", "topic-key", BuiltinExchangeType.TOPIC, words[i] + "," + RandomUtils.nextInt(1,100));
//            i++;
//        }
//        System.out.println("发送topic结束");
//
//        System.out.println("接收topic消息");
//        rabbitMqUtils2.pullMq("amq.topic", "test-topic", "topic-key", BuiltinExchangeType.TOPIC, (record, body) -> {
//            String message = new String(body, "UTF-8");
//            System.out.println(message);
//        });

        //生产者和消费者，分开两个测试类执行，否则会导制队列绑定失败
//        while (i < words.length) {
//            rabbitMqUtils2.sendMq("amq.direct", null,  "direct-key", BuiltinExchangeType.DIRECT, words[i] + "," + RandomUtils.nextInt(1,100));
//            i++;
//        }
//        System.out.println("发送direct结束");

//        System.out.println("接收direct消息");
//        rabbitMqUtils2.pullMq("amq.direct", null,  "direct-key", BuiltinExchangeType.DIRECT, (record, body) -> {
//            String message = new String(body, "UTF-8");
//            System.out.println(message);
//        });

        rabbitMqUtils2.close();
    }
}
