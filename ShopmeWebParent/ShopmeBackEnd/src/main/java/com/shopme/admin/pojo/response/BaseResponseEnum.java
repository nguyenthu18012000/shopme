package com.shopme.admin.pojo.response;

public enum BaseResponseEnum implements BaseResponseEnumInterface {
    // Success
    SUCCESS(200, "Successful"),

    // Error
    BAD_REQUEST(400, "Invalid input"),
    UN_AUTHORIZE(401, "Not authorized"),
    NOT_FOUND(404, "Resource not found"),

    SERVER_ERROR(500, "Internal server error");

    private final Integer code;
    private final String message;

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    BaseResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
