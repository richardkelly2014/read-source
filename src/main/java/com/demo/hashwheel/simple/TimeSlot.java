package com.demo.hashwheel.simple;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by jiangfei on 2019/10/18.
 */
public class TimeSlot<E> {

    /**
     * 时间槽id
     */
    private int id;

    private Set<E> elements = new ConcurrentSkipListSet<E>();

    public TimeSlot(int id) {
        this.id = id;
    }

    public void add(E e) {
        elements.add(e);
    }

    public boolean remove(E e) {
        return elements.remove(e);
    }

    public Set<E> getElements() {
        return elements;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        TimeSlot timeSlot = (TimeSlot) obj;
        if (timeSlot.id != id) {
            return false;
        }
        return true;
    }
}
