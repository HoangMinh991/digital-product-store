/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.dao;

import com.ivietech.demo.entity.CodeGiftCard;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author HoangMinh
 */
@Repository
public interface CodeGiftCardRepository extends JpaRepository<CodeGiftCard, Long> {

    @Query(
            value = "SELECT * FROM code_gift_card c WHERE c.order_details_id is null and c.product_id = :productId LIMIT :limit",
            nativeQuery = true
    )
    public List<CodeGiftCard> getCode(@Param("productId") long productId, @Param("limit") long limit);

//    //Dem so luong code trong kho
//    @Query(value = "SELECT product_id, count(*) form code_gift_card WHERE enabled = true GROUP BY product_id", nativeQuery = true)
}
