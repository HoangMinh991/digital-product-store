package com.ivietech.demo.service;

import com.ivietech.demo.dto.UserDto;
import com.ivietech.demo.entity.Balance;
import com.ivietech.demo.entity.User;
import com.ivietech.demo.entity.VerificationToken;




public interface IUserService {

    User registerNewUserAccount(UserDto accountDto);

    void saveRegisteredUser(User user);
   
    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);
    
    void changeUserPassword(User user, String password);
    
    boolean checkIfValidOldPassword(final User user, final String oldPassword);
       
    void changeMobilePhone(User user, String steamId);
    
    
    
}
