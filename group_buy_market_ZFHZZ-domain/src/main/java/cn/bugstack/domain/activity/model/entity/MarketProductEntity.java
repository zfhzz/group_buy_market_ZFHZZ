package cn.bugstack.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//商品信息
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarketProductEntity {
    private String UserId;
    private String goodsId;
    private String source;
    private String channel;
}
