package cn.bugstack.domain.trade.service;

import cn.bugstack.domain.trade.model.entity.TradePaySettlementEntity;
import cn.bugstack.domain.trade.model.entity.TradePaySuccessEntity;

//拼团结算
public interface ITradeSettlementOrderService {
    TradePaySettlementEntity settlementMarketPayOrder(TradePaySuccessEntity tradePaySuccessEntity) throws Exception;
}
