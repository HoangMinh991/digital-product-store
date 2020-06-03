package com.ivietech.demo.validation;

import com.ivietech.demo.dto.UpdatePasswordDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class NewPasswordValidator implements Validator {


    @Override
    public boolean supports(Class<?> aClass) {
        return UpdatePasswordDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UpdatePasswordDto updatePasswordDto = (UpdatePasswordDto) o;

        if (updatePasswordDto.getPassword()!= null && updatePasswordDto.getPasswordConfirm()!= null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password","1", "Không được bỏ trống");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "Không được bỏ trống");
       
            if (updatePasswordDto.getPassword().length() < 8 || updatePasswordDto.getPassword().length() > 32) {
                errors.rejectValue("password","1", "Mật khâu phải từ 8 đến 32 kí tự");
            }

            if (!updatePasswordDto.getPasswordConfirm().equals(updatePasswordDto.getPassword())) {
                errors.rejectValue("passwordConfirm","1", "Không trùng pass");
            }
            

        } else {
            errors.rejectValue("password","1", "null");
        }

    }
}
