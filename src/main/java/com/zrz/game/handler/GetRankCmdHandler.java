package com.zrz.game.handler;

import com.zrz.game.protobuf.GameProtocol;
import com.zrz.game.rank.RankService;
import com.zrz.game.rank.model.RankItem;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;

/**
 * 获取排行榜 处理器
 *
 * @author zrz
 * @date 2021/2/2 11:00
 */
public class GetRankCmdHandler implements ICmdHandler<GameProtocol.GetRankCmd> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GetRankCmdHandler.class);

  @Override
  public void handler(ChannelHandlerContext ctx, GameProtocol.GetRankCmd getRankCmd) {
    if (null == ctx || getRankCmd == null) {
      return;
    }

    RankService.getInstance().getRank((rankItemList) -> {
      if (null == rankItemList) {
        rankItemList = Collections.emptyList();
      }

      GameProtocol.GetRankResult.Builder builder = GameProtocol.GetRankResult.newBuilder();

      for (RankItem rankItem : rankItemList) {
        GameProtocol.GetRankResult.RankItem.Builder rankItemBuilder = GameProtocol.GetRankResult.RankItem.newBuilder();
        rankItemBuilder.setRankId(rankItem.getRankId());
        rankItemBuilder.setUserId(rankItem.getUserId());
        rankItemBuilder.setUserName(rankItem.getUsername());
        rankItemBuilder.setHeroAvatar(rankItem.getHeroAvatar());
        rankItemBuilder.setWin(rankItem.getWin());
        builder.addRankItem(rankItemBuilder);
      }
      GameProtocol.GetRankResult result = builder.build();
      ctx.writeAndFlush(result);

      return null;
    });
  }
}
