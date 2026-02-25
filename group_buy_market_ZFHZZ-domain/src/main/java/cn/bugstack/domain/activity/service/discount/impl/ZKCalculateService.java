package cn.bugstack.domain.activity.service.discount.impl;

import cn.bugstack.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.bugstack.domain.activity.service.discount.AbstractDiscountCalculateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

//折扣
@Service("ZK")
@Slf4j
public class ZKCalculateService extends AbstractDiscountCalculateService {
    @Override
    protected BigDecimal doCalculate(BigDecimal originalPrice, GroupBuyActivityDiscountVO.GroupBuyDiscount groupBuyDiscount) {
        String marketExpr=groupBuyDiscount.getMarketExpr();

        BigDecimal deductionPrice = originalPrice.multiply(new BigDecimal(marketExpr));

        if(deductionPrice.compareTo(BigDecimal.ZERO) < 0)
            return new BigDecimal("0.01");

        return deductionPrice;
    }
}
