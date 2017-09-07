/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.shibernate.test;

import com.conarhco.shibernate.core.BeanInfoHelper;
import java.beans.IntrospectionException;

/**
 *
 * @author Конарх
 */
public class ReviewBeanBeanInfo extends BeanInfoHelper {

    public ReviewBeanBeanInfo() throws IntrospectionException {
        super(ReviewBean.class);
        this.getPropertyDescriptor("review").setBound(true);
    }
}
