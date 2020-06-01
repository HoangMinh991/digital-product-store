package com.ivietech.demo.validation;


import com.ivietech.demo.dao.UserRepository;
import com.ivietech.demo.dto.NewPassDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ChangePasswordValidator implements Validator {

    @Autowired
    private UserRepository userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return NewPassDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        NewPassDto newPassDto = (NewPassDto) o;

        if (newPassDto.getOddPassword() != null && newPassDto.getNewPassword() != null
                && newPassDto.getNewPasswordConfirm() != null) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "oddPassword", "Không được bỏ trống");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "Không được bỏ trống");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPasswordConfirm", "Không được bỏ trống");
            if (newPassDto.getNewPassword().length() < 8 || newPassDto.getNewPassword().length() > 32) {
                errors.rejectValue("newPassword", "Mật khâu phải từ 8 đến 32 kí tự");
            }
            if (newPassDto.getOddPassword().length() < 8 || newPassDto.getOddPassword().length() > 32) {
                errors.rejectValue("oddPassword", "Mật khâu phải từ 8 đến 32 kí tự");
            }

            if (!newPassDto.getNewPasswordConfirm().equals(newPassDto.getNewPassword())) {
                errors.rejectValue("newPasswordConfirm", "Không trùng pass");
            }

        } else {
            errors.rejectValue("oddPassword", "null");
        }

    }
}
