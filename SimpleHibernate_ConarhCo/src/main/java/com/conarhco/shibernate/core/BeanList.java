/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.conarhco.shibernate.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * List implmementation with bean add/remove monitoring support
 * TODO: Реализовать все методы
 * TODO: Реализовать отслеживание через пцс (проперти адд и ремув)
 * @author Конарх
 */
class BeanList<T> implements List<T>{
    private List<T> data = new ArrayList<T>();

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Iterator<T> iterator() {
        return data.iterator();
    }

    public Object[] toArray() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean add(T e) {
        //TODO: Вот здесь отслеживается добавление нового бина и вносятся изменния в базу
        return data.add(e);
    }

    public boolean remove(Object o) {
        //TODO: Вот здесь отслеживается удаление нового бина и вносятся изменния в базу
        boolean res = data.remove(o);
        if (res){

        }
        return res;
    }

    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addAll(Collection<? extends T> c) {
        //TODO: работает через адд
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public T get(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public T set(int index, T element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void add(int index, T element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public T remove(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
