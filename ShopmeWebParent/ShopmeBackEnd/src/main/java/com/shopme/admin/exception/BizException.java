package com.shopme.admin.exception;

import com.shopme.admin.pojo.response.BaseResponseEnumInterface;

public class BizException extends RuntimeException {

    private final Integer code;

    public BizException(BaseResponseEnumInterface responseEnum, String message) {
        super(message);
        this.code = responseEnum.getCode();
    }

    public BizException(BaseResponseEnumInterface responseEnum) {
        super(responseEnum.getMessage());
        this.code = responseEnum.getCode();
    }

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
