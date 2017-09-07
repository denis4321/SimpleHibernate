/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.shibernate.ee;

import com.conarhco.shibernate.core.SimpleHibernate;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Конарх
 */
public class SimpleHibernateServletContextListener implements ServletContextListener{
    private SimpleHibernate engine = null;

    public void contextInitialized(ServletContextEvent sce) {
        engine = new SimpleHibernateEEServiceProvider(sce.getServletContext());
        engine.injectBeans();
    }

    public void contextDestroyed(ServletContextEvent sce) {

    }

}
