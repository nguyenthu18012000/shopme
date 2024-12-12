package com.shopme.admin.pojo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginResponse {
    @JsonProperty(value = "access_token")
    private String accessToken;

    private Integer userId;

    private String errorCode;

    private String message;
}
