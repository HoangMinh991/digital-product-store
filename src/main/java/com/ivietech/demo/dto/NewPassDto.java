/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dto;

/**
 *
 * @author minhh
 */
public class NewPassDto {
    
    private String oddPassword;
    private String newPassword;
    private String newPasswordConfirm;

    public NewPassDto() {
    }

    public String getOddPassword() {
        return oddPassword;
    }

    public void setOddPassword(String oddPassword) {
        this.oddPassword = oddPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return newPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        this.newPasswordConfirm = newPasswordConfirm;
    }

   
    
}
