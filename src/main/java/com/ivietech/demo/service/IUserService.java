package com.ivietech.demo.service;

import com.ivietech.demo.dto.UserRegistrationDto;
import com.ivietech.demo.entity.Balance;
import com.ivietech.demo.entity.User;
import com.ivietech.demo.entity.VerificationToken;




public interface IUserService {

    User registerNewUserAccount(UserRegistrationDto userRegistrationDto);

    void saveRegisteredUser(User user);
   
    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);
    
    void changeUserPassword(User user, String password);
    
    boolean checkIfValidOldPassword(final User user, final String oldPassword);
       
    void changeMobilePhone(User user, String steamId);
    
    void createPasswordResetTokenForUser(User user, String token);
    
    
    
    
}
