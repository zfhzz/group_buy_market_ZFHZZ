package cn.bugstack.domain.trade.adapter.repository;

import cn.bugstack.domain.trade.model.aggregate.GroupBuyOrderAggregate;
import cn.bugstack.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.bugstack.domain.trade.model.entity.MarketPayOrderEntity;
import cn.bugstack.domain.trade.model.valobj.GroupBuyProgressVO;

public interface ITradeRepository {
    MarketPayOrderEntity queryMarketPayOrderEntityByOutTradeNo(String userId, String outTradeNo);
    //将当前的平团人数+1
    MarketPayOrderEntity lockMarketPayOrder(GroupBuyOrderAggregate groupBuyOrderAggregate);
    //拼团id->拼团状态,金额、目标数量，当前数量
    GroupBuyProgressVO queryGroupBuyProgress(String teamId);

    Integer queryOrderCountByActivityId(long activityId, String userId);

    GroupBuyActivityEntity queryGroupBuyActivityByActivityId(long activityId);
}
