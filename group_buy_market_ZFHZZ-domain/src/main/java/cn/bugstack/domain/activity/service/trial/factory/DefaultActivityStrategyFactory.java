package cn.bugstack.domain.activity.service.trial.factory;

import cn.bugstack.domain.activity.model.entity.MarketProductEntity;
import cn.bugstack.domain.activity.model.entity.TrialBalanceEntity;
import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.bugstack.domain.activity.model.valobj.SkuVO;
import cn.bugstack.domain.activity.service.trial.node.RootNode;
import cn.bugstack.types.design.framework.tree.StrategyHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.websocket.server.ServerEndpoint;
import java.util.Stack;

@Service
public class DefaultActivityStrategyFactory {
    //一定要加构造函数
    private final RootNode rootNode;


    public DefaultActivityStrategyFactory(RootNode rootNode){
        this.rootNode=rootNode;
    }
    //获取初始的root结点
    public StrategyHandler<MarketProductEntity,DefaultActivityStrategyFactory.DynamicContext, TrialBalanceEntity> strategyHandler(){
        return rootNode;
    }
    //用于加载动态上下文
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DynamicContext{
        private GroupBuyActivityDiscountVO groupBuyActivityDiscountVO;
        private SkuVO skuVO;
    }
}
