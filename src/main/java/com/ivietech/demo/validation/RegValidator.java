package com.ivietech.demo.validation;

import com.ivietech.demo.dao.UserRepository;
import com.ivietech.demo.dto.UserRegistrationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RegValidator implements Validator {

    @Autowired
    private UserRepository userService;

    @Autowired
    EmailValidator emailValidator;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegistrationDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRegistrationDto userDto = (UserRegistrationDto) o;

        if (userDto.getUserName() != null && userDto.getEmail() != null
                && userDto.getPassword() != null && userDto.getPasswordConfirm() != null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName","1", "Không được bỏ trống");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password","1", "Không được bỏ trống");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "1","Không được bỏ trống");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email","1", "Không được bỏ trống");
         
            if (userDto.getUserName().length() < 6 || userDto.getUserName().length() > 32) {
                errors.rejectValue("userName","1", "Tên từ 6 đến 32");
               
            }
            if (userService.findByUserName(userDto.getUserName()) != null) {
                errors.rejectValue("userName","1", "Trùng tên");
            }
            if (userService.findByEmail(userDto.getEmail()) != null) {
                errors.rejectValue("email","1", "Trùng mail");
            }
            
            if (userDto.getPassword().length() < 8 || userDto.getPassword().length() > 32) {
                errors.rejectValue("password","1", "Mật khâu phải từ 8 đến 32 kí tự");
            }

            if (!emailValidator.validateEmail(userDto.getEmail())) {
                errors.rejectValue("email","1", "Không phải là mail");
            }

            if (!userDto.getPasswordConfirm().equals(userDto.getPassword())) {
                errors.rejectValue("passwordConfirm","1", "Không trùng pass");
            }

            
        } else {
            errors.rejectValue("userName", "null");
        }

    }
}
