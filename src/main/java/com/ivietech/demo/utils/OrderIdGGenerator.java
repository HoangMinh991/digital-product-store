/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.utils;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 *
 * @author HoangMinh
 */
public class OrderIdGGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor ssci, Object o) throws HibernateException {
        String prefix = "DH";
        Connection connection = ssci.connection();

        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT count(*) from Recharge;");
            if (rs.next()) {
                int id = rs.getInt(1)+101;
                String generatedId = prefix + new Integer(id).toString();
                return generatedId;
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }

}
