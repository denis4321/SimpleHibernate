/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.shibernate.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

/**
 *
 * @author Конарх
 */
public class Convertor_java_util_Date implements TypeConvertor<java.util.Date>{

    public java.util.Date convert(ResultSet res, String field) throws SQLException {
        //System.out.println("Type convertor called");
        ResultSetMetaData meta = res.getMetaData();
        int index = res.findColumn(field);
        int type = meta.getColumnType(index);
        switch (type){
            case Types.DATE:
                return res.getDate(field);
            case Types.TIME:
                return res.getTime(field);
            default:
                throw new IllegalArgumentException(field+" SQL type must be Date or Time");
        }
    }

    public void convert(PreparedStatement st, int index, Date value) throws SQLException {
        //TODO: Реализовать
    }

}
