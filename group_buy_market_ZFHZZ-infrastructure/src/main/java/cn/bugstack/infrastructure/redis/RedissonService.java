package cn.bugstack.infrastructure.redis;

import org.redisson.api.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Redis 服务实现类 - 基于 Redisson 框架
 * 作用：提供分布式环境下的缓存、计数器、锁、队列等核心功能
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 */
@Service("redissonService")
public class RedissonService implements IRedisService {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 【String/Object 存储】设置键值
     */
    public <T> void setValue(String key, T value) {
        redissonClient.<T>getBucket(key).set(value);
    }

    /**
     * 【String/Object 存储】设置键值及过期时间（毫秒）
     */
    @Override
    public <T> void setValue(String key, T value, long expired) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value, Duration.ofMillis(expired));
    }

    /**
     * 【String/Object 获取】获取键值
     */
    public <T> T getValue(String key) {
        return redissonClient.<T>getBucket(key).get();
    }

    /**
     * 【队列】获取普通队列
     */
    @Override
    public <T> RQueue<T> getQueue(String key) {
        return redissonClient.getQueue(key);
    }

    /**
     * 【队列】获取阻塞队列
     */
    @Override
    public <T> RBlockingQueue<T> getBlockingQueue(String key) {
        return redissonClient.getBlockingQueue(key);
    }

    /**
     * 【队列】获取延迟队列
     */
    @Override
    public <T> RDelayedQueue<T> getDelayedQueue(RBlockingQueue<T> rBlockingQueue) {
        return redissonClient.getDelayedQueue(rBlockingQueue);
    }

    /**
     * 【原子量】设置长整型数值
     */
    @Override
    public void setAtomicLong(String key, long value) {
        redissonClient.getAtomicLong(key).set(value);
    }

    /**
     * 【原子量】获取长整型数值
     */
    @Override
    public Long getAtomicLong(String key) {
        return redissonClient.getAtomicLong(key).get();
    }

    /**
     * 【计数器】自增 1 并返回新值
     */
    @Override
    public long incr(String key) {
        return redissonClient.getAtomicLong(key).incrementAndGet();
    }

    /**
     * 【计数器】增加指定数值并返回新值
     */
    @Override
    public long incrBy(String key, long delta) {
        return redissonClient.getAtomicLong(key).addAndGet(delta);
    }

    /**
     * 【计数器】自减 1 并返回新值
     */
    @Override
    public long decr(String key) {
        return redissonClient.getAtomicLong(key).decrementAndGet();
    }

    /**
     * 【计数器】减少指定数值并返回新值
     */
    @Override
    public long decrBy(String key, long delta) {
        return redissonClient.getAtomicLong(key).addAndGet(-delta);
    }

    /**
     * 【通用】删除 Key
     */
    @Override
    public void remove(String key) {
        redissonClient.getBucket(key).delete();
    }

    /**
     * 【通用】检查 Key 是否存在
     */
    @Override
    public boolean isExists(String key) {
        return redissonClient.getBucket(key).isExists();
    }

    /**
     * 【集合 Set】添加元素
     */
    public void addToSet(String key, String value) {
        RSet<String> set = redissonClient.getSet(key);
        set.add(value);
    }

    /**
     * 【集合 Set】判断元素是否在集合中
     */
    public boolean isSetMember(String key, String value) {
        RSet<String> set = redissonClient.getSet(key);
        return set.contains(value);
    }

    /**
     * 【列表 List】添加元素
     */
    public void addToList(String key, String value) {
        RList<String> list = redissonClient.getList(key);
        list.add(value);
    }

    /**
     * 【列表 List】获取指定下标元素
     */
    public String getFromList(String key, int index) {
        RList<String> list = redissonClient.getList(key);
        return list.get(index);
    }

    /**
     * 【哈希 Map】获取整个 Map 对象
     */
    @Override
    public <K, V> RMap<K, V> getMap(String key) {
        return redissonClient.getMap(key);
    }

    /**
     * 【哈希 Map】向 Map 写入键值对
     */
    public void addToMap(String key, String field, String value) {
        RMap<String, String> map = redissonClient.getMap(key);
        map.put(field, value);
    }

    /**
     * 【哈希 Map】获取 Map 里的特定字符串值
     */
    public String getFromMap(String key, String field) {
        RMap<String, String> map = redissonClient.getMap(key);
        return map.get(field);
    }

    /**
     * 【哈希 Map】获取 Map 里的泛型对象值
     */
    @Override
    public <K, V> V getFromMap(String key, K field) {
        return redissonClient.<K, V>getMap(key).get(field);
    }

    /**
     * 【有序集合 ZSet】添加元素
     */
    public void addToSortedSet(String key, String value) {
        RSortedSet<String> sortedSet = redissonClient.getSortedSet(key);
        sortedSet.add(value);
    }

    /**
     * 【分布式锁】获取可重入锁
     */
    @Override
    public RLock getLock(String key) {
        return redissonClient.getLock(key);
    }

    /**
     * 【分布式锁】获取公平锁
     */
    @Override
    public RLock getFairLock(String key) {
        return redissonClient.getFairLock(key);
    }

    /**
     * 【分布式锁】获取读写锁
     */
    @Override
    public RReadWriteLock getReadWriteLock(String key) {
        return redissonClient.getReadWriteLock(key);
    }

    /**
     * 【信号量】获取信号量（限流）
     */
    @Override
    public RSemaphore getSemaphore(String key) {
        return redissonClient.getSemaphore(key);
    }

    /**
     * 【信号量】获取带过期时间的信号量
     */
    @Override
    public RPermitExpirableSemaphore getPermitExpirableSemaphore(String key) {
        return redissonClient.getPermitExpirableSemaphore(key);
    }

    /**
     * 【同步辅助】获取闭锁（倒计时器）
     */
    @Override
    public RCountDownLatch getCountDownLatch(String key) {
        return redissonClient.getCountDownLatch(key);
    }

    /**
     * 【过滤器】获取布隆过滤器（判断海量数据是否存在）
     */
    @Override
    public <T> RBloomFilter<T> getBloomFilter(String key) {
        return redissonClient.getBloomFilter(key);
    }

    /**
     * 【原子操作】尝试设置值（如果 Key 不存在则成功）
     */
    @Override
    public Boolean setNx(String key) {
        return redissonClient.getBucket(key).trySet("lock");
    }

    /**
     * 【原子操作】尝试设置值并带过期时间
     */
    @Override
    public Boolean setNx(String key, long expired, TimeUnit timeUnit) {
        return redissonClient.getBucket(key).trySet("lock", expired, timeUnit);
    }

    /**
     * 【位图】获取 BitSet
     */
    @Override
    public RBitSet getBitSet(String key) {
        return redissonClient.getBitSet(key);
    }

}