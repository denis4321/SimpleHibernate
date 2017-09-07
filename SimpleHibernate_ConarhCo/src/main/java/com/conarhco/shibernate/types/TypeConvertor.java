/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.shibernate.types;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * gets value of correct type from result set based on field java type
 * TODO: Возможно все тайпКоныертеры есть смысл реализовать в виде синглетонов или кешировать
 * @author Конарх
 */
public interface TypeConvertor<T> {

    public T convert(ResultSet res, String field) throws SQLException;

    public void convert(PreparedStatement st, int index, T value) throws SQLException;
}
