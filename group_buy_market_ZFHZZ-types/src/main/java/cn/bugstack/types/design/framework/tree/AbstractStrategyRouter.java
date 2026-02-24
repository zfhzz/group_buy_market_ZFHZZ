package cn.bugstack.types.design.framework.tree;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractStrategyRouter<T,D,R> implements StrategyHandler<T,D,R>,StrategyMapper<T,D,R> {
    //泛型+变量名
    @Getter
    @Setter
    protected StrategyHandler<T,D,R> defaultStrategyHandler = StrategyHandler.DEFAULT;

    public R router(T requestParameter, D dynamicContext) throws Exception {
        //下一个需要执行的路由信息
        StrategyHandler<T, D, R> strategyHandler = get(requestParameter, dynamicContext);
        if(null != strategyHandler) return strategyHandler.apply(requestParameter, dynamicContext);
        //如果下一个需要执行的对象为空则直接采用default(路由守卫)
        return defaultStrategyHandler.apply(requestParameter, dynamicContext);
    }
}
