package com.zrz.game.rank;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.sun.javafx.runtime.async.AsyncOperation;
import com.zrz.game.processor.AsyncOperationProcessor;
import com.zrz.game.processor.IAsyncOperation;
import com.zrz.game.rank.model.RankItem;
import com.zrz.game.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * 排行榜服务
 * @author zrz
 * @date 2021/2/2 14:49
 */
public final class RankService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RankService.class);

    private static final RankService INSTANCE = new RankService();

    private RankService(){}

    public static RankService getInstance(){
        return INSTANCE;
    }

    /**
     * 获取排名列表
     * @param callback 回调函数
     */
    public void getRank(Function<List<RankItem>, Void> callback){
        IAsyncOperation async = new AsyncGetRank() {
            @Override
            public void doFinish() {
                callback.apply(this.getRankItemList());
            }
        };

        AsyncOperationProcessor.getInstance().process(async);

    }

    /**
     * 刷新排行榜
     * @param loseId 输家id
     * @param winnerId 赢家id
     */
    public void refreshRank(int loseId, int winnerId){
        try (Jedis redis = RedisUtil.getJedis()) {
            redis.hincrBy("User_" + winnerId, "Win", 1);
            redis.hincrBy("User_" + loseId, "Lose", 1);

            String win = redis.hget("User_" + winnerId, "Win");
            redis.zadd("Rank", Double.parseDouble(win), String.valueOf(winnerId));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 异步方式获取排名
     */
    public static class AsyncGetRank implements IAsyncOperation {

        /**
         * 排名列表
         */
        private List<RankItem> rankItems = null;

        /**
         * 获取排名列表
         * @return 排名列表
         */
        public List<RankItem> getRankItemList() {
            return rankItems;
        }

        @Override
        public void doAsync() {
            try(Jedis redis = RedisUtil.getJedis()) {
                Set<Tuple> valSet = redis.zrevrangeWithScores("Rank", 0,9);

                List<RankItem> rankItemList = new ArrayList<>(10);
                int rankId = 0;

                for (Tuple t : valSet) {
                    String userId = t.getElement();
                    double score = t.getScore();

                    String json = redis.hget("User_" + userId, "BasicInfo");
                    if ( StringUtils.isBlank(json)) {
                        continue;
                    }

                    RankItem rankItem = JSONObject.parseObject(json, RankItem.class);
                    rankItem.setRankId(++ rankId);
                    rankItem.setWin((int) score);
                    rankItemList.add(rankItem);
                }
                rankItems = rankItemList;
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
