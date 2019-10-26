package com.demo.util;

/**
 * 属性key
 * Created by jiangfei on 2019/10/26.
 */
public final class AttributeKey<T> extends AbstractConstant<AttributeKey<T>> {

    private static final ConstantPool<AttributeKey<Object>> pool = new ConstantPool<AttributeKey<Object>>() {
        @Override
        protected AttributeKey<Object> newConstant(int id, String name) {
            return new AttributeKey<Object>(id, name);
        }
    };

    public static <T> AttributeKey<T> valueOf(String name) {

        return (AttributeKey<T>) pool.valueOf(name);
    }

    public static boolean exists(String name) {

        return pool.exists(name);
    }

    public static <T> AttributeKey<T> newInstance(String name) {

        return (AttributeKey<T>) pool.newInstance(name);
    }

    public static <T> AttributeKey<T> valueOf(Class<?> firstNameComponent, String secondNameComponent) {
        return (AttributeKey<T>) pool.valueOf(firstNameComponent, secondNameComponent);
    }

    private AttributeKey(int id, String name) {

        super(id, name);
    }
}
