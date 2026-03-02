package cn.bugstack.domain.trade.service.lock.factory;

import cn.bugstack.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.bugstack.domain.trade.model.entity.TradeLockCommandEntity;
import cn.bugstack.domain.trade.model.entity.TradeLockFilterBackEntity;
import cn.bugstack.domain.trade.service.lock.filter.ActivityUsabilityRuleFilter;
import cn.bugstack.domain.trade.service.lock.filter.UserTaskLimitRuleFilter;
import cn.bugstack.types.design.framework.link.model2.LinkArmory;
import cn.bugstack.types.design.framework.link.model2.chain.BusinessLinkedList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TradeRuleFilterFactory {

    @Bean("tradeRuleFilter")
    public BusinessLinkedList<TradeLockCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeLockFilterBackEntity> tradeRuleFilter(ActivityUsabilityRuleFilter activityUsabilityRuleFilter, UserTaskLimitRuleFilter userTakeLimitRuleFilter) {
        // 组装链
        LinkArmory<TradeLockCommandEntity, DynamicContext, TradeLockFilterBackEntity> linkArmory =
                new LinkArmory<>("交易规则过滤链", activityUsabilityRuleFilter, userTakeLimitRuleFilter);

        // 链对象
        return linkArmory.getLogicLink();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext{
        private GroupBuyActivityEntity groupBuyActivity;
    }
}
