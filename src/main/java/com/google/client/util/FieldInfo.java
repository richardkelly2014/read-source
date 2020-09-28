package com.google.client.util;

import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class FieldInfo {

    private static final Map<Field, FieldInfo> CACHE = new WeakHashMap<Field, FieldInfo>();

    public static FieldInfo of(Enum<?> enumValue) {
        try {
            FieldInfo result = FieldInfo.of(enumValue.getClass().getField(enumValue.name()));
            Preconditions.checkArgument(
                    result != null, "enum constant missing @Value or @NullValue annotation: %s", enumValue);
            return result;
        } catch (NoSuchFieldException e) {
            // not possible
            throw new RuntimeException(e);
        }
    }

    public static FieldInfo of(Field field) {
        if (field == null) {
            return null;
        }
        synchronized (CACHE) {
            FieldInfo fieldInfo = CACHE.get(field);

            //是不是枚举
            boolean isEnumContant = field.isEnumConstant();

            if (fieldInfo == null && (isEnumContant || !Modifier.isStatic(field.getModifiers()))) {
                String fieldName;
                if (isEnumContant) {
                    // check for @Value annotation
                    Value value = field.getAnnotation(Value.class);
                    if (value != null) {
                        fieldName = value.value();
                    } else {
                        // check for @NullValue annotation
                        NullValue nullValue = field.getAnnotation(NullValue.class);
                        if (nullValue != null) {
                            fieldName = null;
                        } else {
                            // else ignore
                            return null;
                        }
                    }
                } else {
                    // check for @Key annotation
                    Key key = field.getAnnotation(Key.class);
                    if (key == null) {
                        // else ignore
                        return null;
                    }
                    fieldName = key.value();
                    field.setAccessible(true);
                }
                if ("##default".equals(fieldName)) {
                    fieldName = field.getName();
                }
                fieldInfo = new FieldInfo(field, fieldName);
                CACHE.put(field, fieldInfo);
            }
            return fieldInfo;
        }
    }

    /** Whether the field class is "primitive" as defined by {@link Data#isPrimitive(Type)}. */
    private final boolean isPrimitive;

    /** Field. */
    private final Field field;

    /** Setters Method for field */
    private final Method[]setters;

    private final String name;

    FieldInfo(Field field, String name) {
        this.field = field;
        this.name = name == null ? null : name.intern();
        isPrimitive = Data.isPrimitive(getType());
        this.setters = settersMethodForField(field);
    }

    private Method[] settersMethodForField(Field field) {
        List<Method> methods = new ArrayList<Method>();
        for (Method method : field.getDeclaringClass().getDeclaredMethods()) {
            if (Ascii.toLowerCase(method.getName()).equals("set" + field.getName().toLowerCase())
                    && method.getParameterTypes().length == 1) {
                methods.add(method);
            }
        }
        return methods.toArray(new Method[0]);
    }

    public Field getField() {
        return field;
    }

    public String getName() {
        return name;
    }

    /**
     * Returns the field's type.
     *
     * @since 1.4
     */
    public Class<?> getType() {
        return field.getType();
    }

    public Type getGenericType() {
        return field.getGenericType();
    }

    public boolean isFinal() {
        return Modifier.isFinal(field.getModifiers());
    }

    public boolean isPrimitive() {
        return isPrimitive;
    }


    /**
     * Returns the value of the field in the given object instance using reflection.
     */
    public Object getValue(Object obj) {
        return getFieldValue(field, obj);
    }

    /**
     * Sets to the given value of the field in the given object instance using reflection.
     * <p>
     * If the field is final, it checks that value being set is identical to the existing value.
     */
    public void setValue(Object obj, Object value) {
        if (setters.length > 0) {
            for (Method method : setters) {
                if (value == null || method.getParameterTypes()[0].isAssignableFrom(value.getClass())) {
                    try {
                        method.invoke(obj, value);
                        return;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        // try to set field directly
                    }
                }
            }
        }
        setFieldValue(field, obj, value);
    }

    /** Returns the class information of the field's declaring class. */
    public ClassInfo getClassInfo() {
        return ClassInfo.of(field.getDeclaringClass());
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> T enumValue() {
        return Enum.valueOf((Class<T>) field.getDeclaringClass(), field.getName());
    }

    /**
     * Returns the value of the given field in the given object instance using reflection.
     */
    public static Object getFieldValue(Field field, Object obj) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Sets to the given value of the given field in the given object instance using reflection.
     * <p>
     * If the field is final, it checks that value being set is identical to the existing value.
     */
    public static void setFieldValue(Field field, Object obj, Object value) {
        if (Modifier.isFinal(field.getModifiers())) {
            Object finalValue = getFieldValue(field, obj);
            if (value == null ? finalValue != null : !value.equals(finalValue)) {
                throw new IllegalArgumentException(
                        "expected final value <" + finalValue + "> but was <" + value + "> on "
                                + field.getName() + " field in " + obj.getClass().getName());
            }
        } else {
            try {
                field.set(obj, value);
            } catch (SecurityException e) {
                throw new IllegalArgumentException(e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
