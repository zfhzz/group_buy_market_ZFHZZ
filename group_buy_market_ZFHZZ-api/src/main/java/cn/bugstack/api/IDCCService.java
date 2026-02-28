package cn.bugstack.api;

import cn.bugstack.api.response.Response;

public interface IDCCService {

    public Response<Boolean> updateConifg(String key, String value);

}
