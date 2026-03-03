package cn.bugstack.api;

import cn.bugstack.api.dto.GoodsMarketRequestDTO;
import cn.bugstack.api.dto.GoodsMarketResponseDTO;

import cn.bugstack.api.response.Response;

public interface IMarketIndexService {

    Response<GoodsMarketResponseDTO> queryGroupBuyMarketConfig(GoodsMarketRequestDTO requestDTO);


}
