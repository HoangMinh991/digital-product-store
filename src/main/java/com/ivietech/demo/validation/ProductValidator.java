/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.validation;

import com.ivietech.demo.dao.PlaformRepository;
import com.ivietech.demo.dao.TypeRepository;
import com.ivietech.demo.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author HoangMinh
 */
@Component
public class ProductValidator implements Validator {
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private PlaformRepository plaformRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return ProductDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProductDto productDto = (ProductDto) o;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "1", "Không được bỏ trống");
        if (productDto.getPriceNew() == 0 && productDto.getPriceOld() == 0) {
             errors.rejectValue("priceNew","1", "Giá không thể bằng 0");
        }
        if (typeRepository.findByName(productDto.getTypeName())==null) {
            errors.rejectValue("typeName","1", "Không tìm thấy loại hình");
        }
        if (plaformRepository.findByName(productDto.getPlatformsName())==null) {
            errors.rejectValue("platformsName","1", "Không tìm thấy nền tảng");
        }
    }

}
