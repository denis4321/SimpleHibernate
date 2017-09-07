/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.shibernate.test;

import com.conarhco.shibernate.core.IgnoreProperty;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Time;

/**
 * Just a sample bean
 * @author Конарх
 */

public class ReviewBean {
    private String name = null;
    private String review = null;
    private int id = -1;
    private int somefield;
    private java.sql.Time time;
    private java.util.Date date;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public ReviewBean(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        String old = this.review;
        this.review = review;
        pcs.firePropertyChange("review", old, this.review);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    @IgnoreProperty
    public int getSomefield() {
        return somefield;
    }

    @IgnoreProperty
    public void setSomefield(int somefield) {
        this.somefield = somefield;
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        return super.toString()+", id="+id;
    }


    
}
