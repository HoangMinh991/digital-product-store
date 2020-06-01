package com.ivietech.demo.validation;


import com.ivietech.demo.dao.PasswordResetTokenRepository;
import com.ivietech.demo.dao.UserRepository;
import com.ivietech.demo.entity.PasswordResetToken;
import com.ivietech.demo.entity.User;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetValidator {

    @Autowired
    private UserRepository userService;

    @Autowired
    EmailValidator emailValidator;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    public boolean validate(String email) {
        if (email == null) {
            return false;
        }
        if (!emailValidator.validateEmail(email)) {
            return false;
        }
        User user = userService.findByEmail(email);
        if (user == null) {
            return false;
        } else {
            List<PasswordResetToken> list = passwordResetTokenRepository.findByUser(user);
            if (list == null) {
                return true;
            } else {
                Calendar cal = Calendar.getInstance();
                for (int i = 0; i < list.size(); i++) {
                    PasswordResetToken passToken = list.get(i);
                    if ((passToken.getExpiryDate()
                            .getTime() - cal.getTime()
                                    .getTime()) > 0) {
                        return false;
                    }
                }
            }
        }
        return true;

    }
}
