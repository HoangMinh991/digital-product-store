package com.ivietech.demo.dto;


public class UpdatePasswordDto {

    private String password;

    
    private String passwordConfirm;

    public UpdatePasswordDto() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    

}
