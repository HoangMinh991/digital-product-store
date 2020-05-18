/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dao;

import com.ivietech.demo.dto.ProductCount;
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

    @Query("SELECT p FROM Product p WHERE p.best = 1")
    public Page<Product> findAllByBestTrue(PageRequest of);

    @Query("SELECT p FROM Product p WHERE p.type.name = ?1")
    public Page<Product> findAllByType(String nametype, PageRequest of);

    @Query("SELECT p FROM Product p WHERE p.platforms.name = ?1")
    public Page<Product> findAllByPlatforms(String namePlatforms, PageRequest of);

    @Query("SELECT p FROM Product p WHERE p.platforms.name = ?2 AND p.type.name=?1")
    public Page<Product> findAllByTypeAndPlatforms(String nametype, String namePlatforms, PageRequest of);

    @Query("SELECT p FROM Product p WHERE p.priceNew > ?1 AND p.priceNew < ?2")
    public Page<Product> findAllByPrice(long priceLow, long priceHigh, PageRequest of);

    @Query("SELECT p FROM Product p WHERE p.name like %?1%")
    public Page<Product> findAllByName(String nameProduct, PageRequest of);

    @Query("SELECT new com.ivietech.demo.dto.ProductCount(o.product, COUNT(o.product) as c) "
            + "FROM OrderDetails  AS o GROUP BY o.product ORDER BY c DESC")
    public Page<ProductCount> findAllByBestSeller(PageRequest of);

}
