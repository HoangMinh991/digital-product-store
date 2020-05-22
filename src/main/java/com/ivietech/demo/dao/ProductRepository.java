/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dao;

import com.ivietech.demo.dto.ProductCount;
import com.ivietech.demo.dto.ProductDto;
import com.ivietech.demo.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HoangMinh
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT new com.ivietech.demo.dto.ProductDto(p.id, p.img, p.name, p.priceNew, p.priceOld, COUNT(c.code)) "
            + "FROM Product AS p LEFT JOIN p.listCodeGiftCard AS c WHERE p.best = 1 GROUP BY p.id")
    public Page<ProductDto> findAllByBestTrue(PageRequest of);

    @Query("SELECT new com.ivietech.demo.dto.ProductDto(p.id, p.img, p.name, p.priceNew, p.priceOld, COUNT(c.code)) "
            + "FROM Product AS p LEFT JOIN p.listCodeGiftCard AS c WHERE p.type.name=?1 GROUP BY p.id")
    public Page<ProductDto> findAllByType(String nametype, PageRequest of);

    @Query("SELECT new com.ivietech.demo.dto.ProductDto(p.id, p.img, p.name, p.priceNew, p.priceOld, COUNT(c.code)) "
            + "FROM Product AS p LEFT JOIN p.listCodeGiftCard AS c WHERE p.platforms.name=?1 GROUP BY p.id")
    public Page<ProductDto> findAllByPlatforms(String namePlatforms, PageRequest of);

    @Query("SELECT new com.ivietech.demo.dto.ProductDto(p.id, p.img, p.name, p.priceNew, p.priceOld, COUNT(c.code)) "
            + "FROM Product AS p LEFT JOIN p.listCodeGiftCard AS c WHERE p.platforms.name = ?2 AND p.type.name=?1 GROUP BY p.id")
    public Page<ProductDto> findAllByTypeAndPlatforms(String nametype, String namePlatforms, PageRequest of);

    @Query("SELECT new com.ivietech.demo.dto.ProductDto(p.id, p.img, p.name, p.priceNew, p.priceOld, COUNT(c.code)) "
            + "FROM Product AS p LEFT JOIN p.listCodeGiftCard AS c WHERE p.priceNew > ?1 AND p.priceNew < ?2 GROUP BY p.id")
    public Page<ProductDto> findAllByPrice(long priceLow, long priceHigh, PageRequest of);

    @Query("SELECT new com.ivietech.demo.dto.ProductDto(p.id, p.img, p.name, p.priceNew, p.priceOld, COUNT(c.code)) "
            + "FROM Product AS p LEFT JOIN p.listCodeGiftCard AS c WHERE p.name like %?1%")
    public Page<ProductDto> findAllByName(String nameProduct, PageRequest of);

   

    @Query("SELECT new com.ivietech.demo.dto.ProductDto(p.id, p.img, p.name, p.priceNew, p.priceOld, COUNT(c.code)) "
            + "FROM Product AS p LEFT JOIN p.listCodeGiftCard AS c WHERE p.type.name=?1 GROUP BY p.id")
    public Page<ProductDto> findAllDto(String nameType, PageRequest of);

}
