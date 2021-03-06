/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.struts.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * リストのラッパーです。
 * 
 * @author higa
 * 
 */
@SuppressWarnings("unchecked")
public class ListWrapper implements List {

    /**
     * オリジナルのリストです。
     */
    protected List list;

    /**
     * インスタンスを構築します。
     * 
     * @param list
     *            リスト
     */
    public ListWrapper(List list) {
        this.list = list;
    }

    public boolean add(Object o) {
        return list.add(o);
    }

    public void add(int index, Object element) {
        list.add(index, element);
    }

    public boolean addAll(Collection c) {
        return list.addAll(c);
    }

    public boolean addAll(int index, Collection c) {
        return list.addAll(index, c);
    }

    public void clear() {
        list.clear();
    }

    public boolean contains(Object o) {
        return list.contains(o);
    }

    public boolean containsAll(Collection c) {
        return list.containsAll(c);
    }

    public Object get(int index) {
        return WrapperUtil.convert(list.get(index));
    }

    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public Iterator iterator() {
        return new IteratorWrapper(list.iterator());
    }

    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    public ListIterator listIterator() {
        return new ListIteratorWrapper(list.listIterator());
    }

    public ListIterator listIterator(int index) {
        return new ListIteratorWrapper(list.listIterator(index));
    }

    public boolean remove(Object o) {
        return list.remove(o);
    }

    public Object remove(int index) {
        return list.remove(index);
    }

    public boolean removeAll(Collection c) {
        return list.removeAll(c);
    }

    public boolean retainAll(Collection c) {
        return list.retainAll(c);
    }

    public Object set(int index, Object element) {
        return list.set(index, element);
    }

    public int size() {
        return list.size();
    }

    public List subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    public Object[] toArray() {
        Object[] array = new Object[size()];
        int i = 0;
        for (Iterator ite = iterator(); ite.hasNext();) {
            array[i++] = ite.next();
        }
        return array;
    }

    public Object[] toArray(Object[] a) {
        return toArray();
    }

    @Override
    public String toString() {
        return list.toString();
    }
}