/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.shibernate.ee;

import com.conarhco.shibernate.core.SimpleHibernate;
import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.util.Collection;
import java.util.Collections;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

/**
 * @author Конарх
 */
class SimpleHibernateEEServiceProvider extends SimpleHibernate{
    private ServletContext ctx;
    private DataSource ds = null;

    public SimpleHibernateEEServiceProvider(ServletContext ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String getConfigurationProperty(String name) {
        return ctx.getInitParameter(name);
    }

    @Override
    protected Collection<String> getConfigurationPropertyNames() {
        return Collections.list(ctx.getInitParameterNames());
    }

    @Override
    protected Connection getConnection() throws Exception{
        if (ds == null) {
            //Getting JNDI source name
            String jndi = this.getConfigurationProperty(CFG_PRE + "jndi");
            InitialContext init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/" + jndi);
            ctx.log("SHibernate: using data source " + jndi);
        }
        return ds.getConnection();
    }

    @Override
    protected void putIntoContext(String name, Collection<?> beans) {
        ctx.setAttribute(name, beans);
        ctx.log("SHibernate: bean found: " + name);
    }

}
