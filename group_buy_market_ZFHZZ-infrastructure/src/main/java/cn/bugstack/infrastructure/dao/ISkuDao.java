package cn.bugstack.infrastructure.dao;


import cn.bugstack.infrastructure.dao.po.GroupBuyActivity;
import cn.bugstack.infrastructure.dao.po.Sku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISkuDao {
    //查询折扣配置
    Sku querySkuByGoodsId(String goodsId);
}
