package cn.bugstack.types.design.framework.tree;
//策略映射器,接收策略跳转到哪一步
public interface StrategyMapper<T,D,R> {
    StrategyHandler<T,D,R> get(T requestParameter,D dynamicConetxt);

}
