package com.shopme.admin.pojo.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserLoginRequest {

    @NotEmpty(message = "email is required")
    private String email;

    @NotEmpty(message = "password is required")
    private String password;

    public @NotEmpty(message = "email is required") String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty(message = "email is required") String email) {
        this.email = email;
    }

    public @NotEmpty(message = "password is required") String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty(message = "password is required") String password) {
        this.password = password;
    }
}
