package cn.bugstack.domain.activity.service.discount;

import cn.bugstack.domain.activity.adapter.repository.IActivityRepository;
import cn.bugstack.domain.activity.model.valobj.DiscountTypeEnum;
import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Slf4j
public abstract class AbstractDiscountCalculateService implements IDiscountCalculateService{

    @Resource
    protected IActivityRepository repository;

    @Override
    public BigDecimal calculate(String userId, BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount){
        //人群标签过滤
        if(DiscountTypeEnum.TAG.equals(groupBuyDiscount.getDiscountType())){
            boolean isCrowdRange =  filterTagId(userId,groupBuyDiscount.getTagId());
            if(!isCrowdRange) {
                log.info("折扣优惠计算拦截,userId:{}",userId);
                return originalPrice;
            }
        }
        return doCalculate(originalPrice,groupBuyDiscount);
    }

    // 人群过滤 - 限定人群优惠，用于判断是否属于优惠折扣人群
    private boolean filterTagId(String userId, String tagId) {
        return repository.isTagCrowdRange(tagId,userId);
    }

    protected abstract BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount);
}
