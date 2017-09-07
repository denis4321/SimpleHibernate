/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.shibernate.core;
import com.conarhco.shibernate.types.TypeConvertor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

/**
 * Monitors updates of bean properties and updates database accordingly
 * @author Конарх
 */
class UpdatePropertyListener implements PropertyChangeListener{
    private Map<String, Class<?>> properties;
    private SimpleHibernate engine;
    private Map<String, Method> keys;
    private String table;

    public UpdatePropertyListener(Map<String, Class<?>> properties, Map<String, Method> keys, String table, SimpleHibernate engine) {
        this.properties = properties;
        this.engine = engine;
        this.keys = keys;
        this.table = table;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (properties.containsKey(evt.getPropertyName())){
            if (keys.isEmpty()) throw new IllegalStateException("No key fields are set to bean "+evt.getSource());
            try{
                Connection c = engine.getConnection();
                try{
                    PreparedStatement st = c.prepareStatement(engine.getUpdateQuery(table, evt.getPropertyName(), keys.keySet()));
                    TypeConvertor tc = engine.getTypeConvertor(properties.get(evt.getPropertyName()));
                    tc.convert(st, 1, evt.getNewValue());
                    int i=1;
                    for(String s:keys.keySet()){
                        i++;
                        Object val = keys.get(s).invoke(evt.getSource());
                        tc = engine.getTypeConvertor(val.getClass());
                        tc.convert(st, i, val);
                    }
                    System.out.println(st);
                    st.executeUpdate();
                }finally {
                    c.close();
                }
            } catch (Exception ex){
                throw new IllegalStateException("DB upddate exception: "+ex);
            }
            //System.out.println(evt.getSource()+": bean property changed: "+evt.getPropertyName()+" = "+evt.getNewValue());
        }
    }

}
