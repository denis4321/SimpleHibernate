/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.shibernate.core;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Helper class for easy implementation of  BeanInfo
 * @author Конарх
 */
public class BeanInfoHelper extends SimpleBeanInfo {
    BeanDescriptor bd;
    private Collection<PropertyDescriptor> pds = new LinkedHashSet<PropertyDescriptor>();

    /*public BeanInfoHelper(){
        //TODO: Denis: определи автоматически имя класса бина по имени бинИнфо
    }*/

    public BeanInfoHelper(Class<?> beanClass) throws IntrospectionException{
        bd  = new BeanDescriptor(beanClass);
        BeanInfo biReflect = Introspector.getBeanInfo(beanClass, Introspector.IGNORE_IMMEDIATE_BEANINFO);
        pds.addAll(Arrays.asList(biReflect.getPropertyDescriptors()));
        for (PropertyDescriptor pd : pds){
            pd.setBound(false);
            pd.setConstrained(false);
        }
        removePropertyDescriptor(getPropertyDescriptor("class"));
    }

    //TODO: Я не уверен что этот метод нужен
    protected final void addPropertyDescriptor(PropertyDescriptor pd){
        pds.add(pd);
    }

    protected final void removePropertyDescriptor(PropertyDescriptor pd){
        pds.remove(pd);
    }

    protected final PropertyDescriptor getPropertyDescriptor(String name){
        for (PropertyDescriptor pd : pds){
            if (pd.getName().equals(name)) return pd;
        }
        throw new IllegalArgumentException("No such property: "+name);
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
        return bd;
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return pds.toArray(new PropertyDescriptor[pds.size()]);
    }

}
