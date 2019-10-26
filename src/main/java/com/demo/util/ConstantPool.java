package com.demo.util;

import com.demo.util.internal.ObjectUtil;
import com.demo.util.internal.PlatformDependent;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 常量池？
 * Created by jiangfei on 2019/10/26.
 */
public abstract class ConstantPool<T extends Constant<T>> {

    //map
    private final ConcurrentMap<String, T> constants = PlatformDependent.newConcurrentHashMap();

    //id
    private final AtomicInteger nextId = new AtomicInteger(1);


    public T valueOf(Class<?> firstNameComponent, String secondNameComponent) {
        if (firstNameComponent == null) {
            throw new NullPointerException("firstNameComponent");
        }
        if (secondNameComponent == null) {
            throw new NullPointerException("secondNameComponent");
        }

        return valueOf(firstNameComponent.getName() + '#' + secondNameComponent);
    }

    public T valueOf(String name) {
        checkNotNullAndNotEmpty(name);
        return getOrCreate(name);
    }

    private T getOrCreate(String name) {
        T constant = constants.get(name);
        if (constant == null) {
            //map 不存在
            final T tempConstant = newConstant(nextId(), name);
            constant = constants.putIfAbsent(name, tempConstant);
            if (constant == null) {
                return tempConstant;
            }
        }

        return constant;
    }

    public boolean exists(String name) {
        checkNotNullAndNotEmpty(name);
        return constants.containsKey(name);
    }

    public T newInstance(String name) {
        checkNotNullAndNotEmpty(name);
        return createOrThrow(name);
    }

    private T createOrThrow(String name) {
        T constant = constants.get(name);
        if (constant == null) {
            final T tempConstant = newConstant(nextId(), name);
            constant = constants.putIfAbsent(name, tempConstant);
            if (constant == null) {
                return tempConstant;
            }
        }

        throw new IllegalArgumentException(String.format("'%s' is already in use", name));
    }

    private static String checkNotNullAndNotEmpty(String name) {
        ObjectUtil.checkNotNull(name, "name");

        if (name.isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }

        return name;
    }

    //新建
    protected abstract T newConstant(int id, String name);

    @Deprecated
    public final int nextId() {

        return nextId.getAndIncrement();
    }


}
