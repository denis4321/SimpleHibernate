/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.shibernate.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Конарх
 */
public class DefaultTypeConvertor implements TypeConvertor{

    public Object convert(ResultSet res, String field) throws SQLException {
        return res.getObject(field);
    }

    public void convert(PreparedStatement st, int index, Object value) throws SQLException {
        st.setObject(index, value);
    }

}
