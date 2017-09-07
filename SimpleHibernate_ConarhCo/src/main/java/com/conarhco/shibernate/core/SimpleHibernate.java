/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.shibernate.core;

import com.conarhco.shibernate.types.DefaultTypeConvertor;
import com.conarhco.shibernate.types.TypeConvertor;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Public service API, which is overriden by all providers
 * @author Конарх
 */
public abstract class SimpleHibernate {
    public static final String PRE = "shibernate.";
    public static final String CFG_PRE = PRE+"config.";
    public static final String BEAN_PRE = PRE+"bean.";

    public SimpleHibernate(){
    }

    /**
     * injects beans into context
     */
    public final void injectBeans(){
        try {
            Collection<String> found = new HashSet<String>();
            for (String name : getConfigurationPropertyNames()) {
                if (name.startsWith(BEAN_PRE)) {
                    String beanName = name.substring(BEAN_PRE.length(), name.indexOf(".", BEAN_PRE.length()));
                    if (!found.contains(beanName)) {
                        found.add(beanName);
                        String beanClassName = getConfigurationProperty(BEAN_PRE + beanName + ".class");
                        String beanTable = getConfigurationProperty(BEAN_PRE + beanName + ".table");
                        //ctx.log("SHibernate: bean found: " + beanName);
                        Class<?> beanClass = Class.forName(beanClassName);
                        BeanInfo info = Introspector.getBeanInfo(beanClass, Object.class);
                        Collection<PropertyDescriptor> pds = getPropertyDescriptors(info);
                        String query = getSelectQuery(pds, beanTable);
                        //System.out.println(query);
                        //Connection c = db.getConnection();
                        Connection c = getConnection();
                        try {
                            Statement st = c.createStatement();
                            ResultSet res = st.executeQuery(query);
                            Collection beans = getBeansFromResultSet(res, pds, info.getBeanDescriptor().getBeanClass(), beanTable);
                            //ctx.setAttribute(beanName, beans);
                            putIntoContext(beanName, beans);
                        } finally {
                            c.close();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }


    private <T> List<T> getBeansFromResultSet(ResultSet res, Collection<PropertyDescriptor> pds, Class<T> beanClass, String beanTable) throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, Exception{
        List<T> beans = new BeanList<T>();//TODO: Заменить стандартный лист своей реализацией, отслеживающей добавление и удаление элементов
        Collection<String> pkNames = new HashSet<String>();
        ResultSet primaryKeys = null;
        while (res.next()) {
            if (primaryKeys == null) {
                Connection c = getConnection();
                try {
                    primaryKeys = c.getMetaData().getPrimaryKeys(null, res.getMetaData().getSchemaName(1), res.getMetaData().getTableName(1));
                    while (primaryKeys.next()) {
                        pkNames.add(primaryKeys.getString("COLUMN_NAME").toLowerCase());
                    }
                } finally {
                    c.close();
                }
            }
            T bean = beanClass.newInstance();
            //injecting initial values to the bean
            Map<String, Class<?>> bounds = new HashMap<String, Class<?>>();
            Map<String, Method> keys = new HashMap<String, Method>();

            for (PropertyDescriptor pd : pds){
                Object val = getTypeConvertor(pd.getPropertyType()).convert(res, pd.getName());
                pd.getWriteMethod().invoke(bean, val);
                if (pd.isBound()){
                    bounds.put(pd.getName(), pd.getPropertyType());
                }
                if(pkNames.contains(pd.getName())){
                    keys.put(pd.getName(), pd.getReadMethod());
                }
            }
            if (!bounds.isEmpty()){
            //Analyzing if bean supports property change
                try {
                    Method addPcl = beanClass.getMethod("addPropertyChangeListener", PropertyChangeListener.class);
                    addPcl.invoke(bean, new UpdatePropertyListener(bounds, keys, beanTable, this));
                } catch (NoSuchMethodException ex) {
                    //Do nothing, no money no honey
                }
            }
            beans.add(bean);
        }
        return beans;
    }

    TypeConvertor getTypeConvertor(Class<?> cl) throws InstantiationException, IllegalAccessException{
        String clName = cl.getName();
        clName = "com.conarhco.shibernate.types.Convertor_" + clName.replace('.', '_');
        try {
            return (TypeConvertor) Class.forName(clName).newInstance();
        } catch (ClassNotFoundException ex) {
            return new DefaultTypeConvertor();
        }
    }

    /**
     * returns list of property descriptors, filtered by annotations
     */
    private Collection<PropertyDescriptor> getPropertyDescriptors(BeanInfo info){
        Collection<PropertyDescriptor> pds = new LinkedHashSet<PropertyDescriptor>();
        for (PropertyDescriptor pd : info.getPropertyDescriptors()){
            boolean add = true;
            Method[] methods = {pd.getReadMethod(), pd.getWriteMethod()};
            for (Method met : methods) {
                if (met != null && met.isAnnotationPresent(IgnoreProperty.class)) {
                    add = false;
                    break;
                }
            }
            if (add) pds.add(pd);
        }
        return pds;
    }

    /**
     * generates select query for bean
     * can be overriden by ServiceProviders to support different SQL dialects
     * currrently tuned to use plain mysql dialect, which is mostly common
     * @param pds
     * @param table
     * @return
     */
    protected String getSelectQuery(Collection<PropertyDescriptor> pds, String table){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        boolean first = true;
        for (PropertyDescriptor pd : pds){
            sb.append((!first ? ", " : "") + pd.getName());
            first = false;
        }
        sb.append(" FROM "+table);
        return sb.toString();
    }

    /**
     * generates update query for bean
     * can be overriden by ServiceProviders to support different SQL dialects
     * currrently tuned to use plain mysql dialect, which is mostly common
     * @param dbId
     * @param updateValues
     * @param table
     * @return
     */
    protected String getUpdateQuery(String table, String col, Collection<String> keys){
        //TODO: Собираем перпаредСтейтмент для апдейта
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(table);
        sb.append(" SET "+col+"=? ");
        sb.append(" WHERE ");
        boolean first = true;
        for (String key:keys){
            sb.append((!first ? " AND " : "") + key);
            first = false;
            sb.append("=?");
        }       
        return sb.toString();
    }

    /**
     * returns configuration property by name
     * @param name
     * @return
     */
    protected abstract String getConfigurationProperty(String name);

    /**
     *  returnbs configuratiuon property names
     * @return
     */
    protected abstract Collection<String> getConfigurationPropertyNames();

    /**
     * return connection to db
     * @return
     */
    protected abstract Connection getConnection() throws Exception;

    /**
     * Puts collection of beans into context
     * @param name
     * @param beans
     */
    protected abstract void putIntoContext(String name, Collection<?> beans);

}
