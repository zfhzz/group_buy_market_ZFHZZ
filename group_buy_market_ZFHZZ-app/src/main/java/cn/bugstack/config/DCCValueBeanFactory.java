package cn.bugstack.config;

import cn.bugstack.types.annotations.DCCValue;
import cn.bugstack.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class DCCValueBeanFactory implements BeanPostProcessor {
    private static final String BASE_CONFIG_PATH="group_buy_market_dcc_";

    private RedissonClient redissonClient;

    public DCCValueBeanFactory(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
    }

    private final Map<String,Object> dccObject = new HashMap<>();

    @Bean("dccTopic")
    public RTopic dccRedisTopicListener(RedissonClient redissonClient) {
        RTopic topic = redissonClient.getTopic("group_buy_market_dcc");
        topic.addListener(String.class, (charSequence, s) -> {
            String[] split = s.split(Constants.SPLIT);

            // 获取值
            String attribute = split[0];
            String key = BASE_CONFIG_PATH + attribute;
            String value = split[1];

            // 设置值
            RBucket<String> bucket = redissonClient.getBucket(key);
            boolean exists = bucket.isExists();
            if (!exists) return;
            bucket.set(value);
            //如果不存在这个object，则直接返回
            Object objBean = dccObject.get(key);
            if (null == objBean) return;

            Class<?> objBeanClass = objBean.getClass();
            // 检查 objBean 是否是代理对象
            if (AopUtils.isAopProxy(objBean)) {
                // 获取代理对象的目标对象
                objBeanClass = AopUtils.getTargetClass(objBean);
            }

            try {
                // 1. getDeclaredField 方法用于获取指定类中声明的所有字段，包括私有字段、受保护字段和公共字段。
                // 2. getField 方法用于获取指定类中的公共字段，即只能获取到公共访问修饰符（public）的字段。
                Field field = objBeanClass.getDeclaredField(attribute);
                field.setAccessible(true);
                field.set(objBean, value);
                field.setAccessible(false);
                log.info("DCC 节点监听，动态设置值 {} {}", key, value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return topic;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
// 1. 先默认你是房东
        Class<?> targetBeanClass = bean.getClass();
        Object targetBeanObject = bean;

// 2. 检查一下：你是不是中介？
        if (AopUtils.isAopProxy(bean)) {
            // 3. 如果是中介，通过 Spring 工具类把背后的房东（原始类）挖出来
            targetBeanClass = AopUtils.getTargetClass(bean); // 拿到类信息，用于读取注解
            targetBeanObject = AopProxyUtils.getSingletonTarget(bean); // 拿到原始对象，用于赋值
        }
        //获取该类的属性
        Field[] fields = targetBeanClass.getDeclaredFields();
        for(Field field : fields){
            if(!field.isAnnotationPresent(DCCValue.class))
                continue;
            DCCValue dccValue = field.getAnnotation(DCCValue.class);

            String value = dccValue.value();
            if(StringUtils.isBlank(value)){
                throw new RuntimeException("...");
            }
            String[] split = value.split(":");
            String key = BASE_CONFIG_PATH.concat(split[0]);
            String defaultValue = split.length == 2 ? split[1] : null;

            String setValue = defaultValue;

            try{
                if(StringUtils.isBlank(defaultValue)){
                    throw new RuntimeException("...");
                }
                //判断当前的key是否存在，不能存在则直接创建
                RBucket<String> bucket=redissonClient.getBucket(key);
                boolean exists = bucket.isExists();
                if(!exists){
                    bucket.set(defaultValue);
                }
                else{
                    setValue = bucket.get();
                }
                field.setAccessible(true);
                field.set(targetBeanObject,setValue);
                field.setAccessible(false);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
            dccObject.put(key,targetBeanObject);
        }

        return bean;
    }
}
