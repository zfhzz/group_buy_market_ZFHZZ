package cn.bugstack.domain.trade.service.lock.filter;

import cn.bugstack.domain.trade.adapter.repository.ITradeRepository;
import cn.bugstack.domain.trade.model.entity.GroupBuyActivityEntity;
import cn.bugstack.domain.trade.model.entity.TradeLockCommandEntity;
import cn.bugstack.domain.trade.model.entity.TradeLockFilterBackEntity;
import cn.bugstack.domain.trade.service.lock.factory.TradeRuleFilterFactory;
import cn.bugstack.types.design.framework.link.model2.handler.ILogicHandler;
import cn.bugstack.types.enums.ResponseCode;
import cn.bugstack.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserTaskLimitRuleFilter implements ILogicHandler<TradeLockCommandEntity, TradeRuleFilterFactory.DynamicContext, TradeLockFilterBackEntity> {

    @Resource
    private ITradeRepository repository;

    @Override
    public TradeLockFilterBackEntity apply(TradeLockCommandEntity requestParameter, TradeRuleFilterFactory.DynamicContext dynamicContext) throws Exception {
        log.info("交易规则过滤-用户参与次数校验{} activityId:{}", requestParameter.getUserId(), requestParameter.getActivityId());

        GroupBuyActivityEntity groupBuyActivity = dynamicContext.getGroupBuyActivity();

        // 查询用户在一个拼团活动上参与的次数
        Integer count = repository.queryOrderCountByActivityId(requestParameter.getActivityId(), requestParameter.getUserId());

        //如果当前的交易次数不为空，且该用户参与拼图的次数已经达到上线
        if(groupBuyActivity.getTakeLimitCount()!=null &&  count>=groupBuyActivity.getTakeLimitCount()){
            log.info("用户参与次数校验，已达可参与上限 activityId:{}", requestParameter.getActivityId());
            throw new AppException(ResponseCode.E0103.getCode());
        }
        return TradeLockFilterBackEntity.builder()
                .userTaskOrderCount(count)
                .build();
    }
}
