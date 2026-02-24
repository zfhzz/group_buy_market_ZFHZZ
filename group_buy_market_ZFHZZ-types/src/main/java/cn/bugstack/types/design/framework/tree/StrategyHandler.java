package cn.bugstack.types.design.framework.tree;
//策略处理器，接收T,结合D来得到R
public interface StrategyHandler<T,D,R>{
    StrategyHandler DEFAULT = (T,D) -> null;

    R apply(T requestParameter,D dynamicContext) throws Exception;

}