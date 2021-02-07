package com.zrz.game;

import com.alibaba.fastjson.JSON;
import com.zrz.game.mq.VictorMsg;
import com.zrz.game.rank.RankService;
import com.zrz.game.utils.RocketMqUtils;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 排行榜主程序
 *
 * @author zrz
 * @date 2021/2/3 22:47
 */
public class RankApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(RankApp.class);

    public static void main(String[] args) throws Exception {
        // tag="pushA || pushB || pushC" 用||隔开表示多个tag消息都可以接收,null或*表示主题所有队列，但是发送MQ一条消息只能有一个tag标签
        RocketMqUtils rocketMqUtils = new RocketMqUtils("192.168.1.37:9876", "test", "die",3000);

    // 消费消息,如果多个消费者线程（应用）consumerGroup相同，则天然具备了负载均衡消费topic消息能力
    rocketMqUtils.pullMq(
        "consumerGroup",
        (message) -> {
          String messageBody = new String(message.getBody(), RemotingHelper.DEFAULT_CHARSET);
          LOGGER.info("消费响应：tag : " + message.getTags() + ",  msgBody : " + messageBody); // 输出消息内容
          VictorMsg victorMsg = JSON.parseObject(messageBody, VictorMsg.class);
          RankService.getInstance().refreshRank(victorMsg.getLoseId(), victorMsg.getWinnerId());
        },
        true,
        false);
    }
}
