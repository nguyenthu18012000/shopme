package com.shopme.admin.pojo.response;

import com.shopme.common.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class ListUserResponse {
    public List<User> items;
    public Integer page;
    public Integer pageSize;
    public Integer totalPage;
    public Long totalItems;

}
