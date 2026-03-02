package cn.bugstack.infrastructure.adapter.port;

import cn.bugstack.domain.trade.adapter.port.ITradePort;
import cn.bugstack.domain.trade.model.entity.NotifyTaskEntity;
import cn.bugstack.infrastructure.gateway.GroupBuyNotifyService;
import cn.bugstack.infrastructure.redis.IRedisService;
import cn.bugstack.types.enums.NotifyTaskHTTPEnumVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TradePort implements ITradePort {

    @Resource
    private GroupBuyNotifyService groupBuyNotifyService;

    //需要加上一个分布式锁进行资源抢占
    @Resource
    private IRedisService redisService;

    @Override
    public String groupBuyNotify(NotifyTaskEntity notifyTask) throws Exception {
        RLock lock = redisService.getLock(notifyTask.lockKey());
        try {
            // 2. 尝试抢占任务（抢锁）。参数：等待3秒，不自动续约，单位秒
            // 如果抢不到，说明别的服务器已经在处理这个任务了
            // group-buy-market 拼团服务端会被部署到多台应用服务器上，那么就会有很多任务一起执行。这个时候要进行抢占，避免被多次执行
            if (lock.tryLock(3, 0, TimeUnit.SECONDS)) {
                try {
                    // 无效的 notifyUrl 则直接返回成功
                    if (StringUtils.isBlank(notifyTask.getNotifyUrl()) || "暂无".equals(notifyTask.getNotifyUrl())) {
                        return NotifyTaskHTTPEnumVO.SUCCESS.getCode();
                    }
                    //执行真正的http转发
                    return groupBuyNotifyService.groupBuyNotify(notifyTask.getNotifyUrl(), notifyTask.getParameterJson());
                } finally {
                    // 释放锁。必须判断是当前线程持有的锁才释放，防止误解锁
                    if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }
            //抢锁失败，返回 NULL。外层扫描任务的逻辑会认为没处理，下次再试
            return NotifyTaskHTTPEnumVO.NULL.getCode();
        } catch (Exception e) {
            //异常处理。如果中断了，恢复中断状态，返回 NULL 触发重试
            Thread.currentThread().interrupt();
            return NotifyTaskHTTPEnumVO.NULL.getCode();
        }
    }
}
