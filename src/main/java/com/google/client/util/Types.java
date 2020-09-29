package com.google.client.util;

import com.google.common.base.Preconditions;

import java.lang.reflect.*;
import java.util.*;

public class Types {


    public static ParameterizedType getSuperParameterizedType(Type type, Class<?> superClass) {
        if (type instanceof Class<?> || type instanceof ParameterizedType) {
            outer:
            while (type != null && type != Object.class) {
                Class<?> rawType;
                if (type instanceof Class<?>) {
                    // type is a class
                    rawType = (Class<?>) type;
                } else {
                    // current is a parameterized type
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    rawType = getRawClass(parameterizedType);
                    // check if found Collection
                    if (rawType == superClass) {
                        // return the actual collection parameter
                        return parameterizedType;
                    }
                    if (superClass.isInterface()) {
                        for (Type interfaceType : rawType.getGenericInterfaces()) {
                            // interface type is class or parameterized type
                            Class<?> interfaceClass =
                                    interfaceType instanceof Class<?> ? (Class<?>) interfaceType : getRawClass(
                                            (ParameterizedType) interfaceType);
                            if (superClass.isAssignableFrom(interfaceClass)) {
                                type = interfaceType;
                                continue outer;
                            }
                        }
                    }
                }
                // move on to the super class
                type = rawType.getGenericSuperclass();
            }
        }
        return null;
    }

    public static boolean isAssignableToOrFrom(Class<?> classToCheck, Class<?> anotherClass) {
        return classToCheck.isAssignableFrom(anotherClass)
                || anotherClass.isAssignableFrom(classToCheck);
    }



    public static <T> T newInstance(Class<T> clazz) {
        // TODO(yanivi): investigate "sneaky" options for allocating the class that GSON uses, like
        // setting the constructor to be accessible, or possibly provide a factory method of a special
        // name
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException e) {
            throw handleExceptionForNewInstance(e, clazz);
        } catch (InstantiationException e) {
            throw handleExceptionForNewInstance(e, clazz);
        }
    }

    private static IllegalArgumentException handleExceptionForNewInstance(
            Exception e, Class<?> clazz) {
        StringBuilder buf =
                new StringBuilder("unable to create new instance of class ").append(clazz.getName());
        ArrayList<String> reasons = new ArrayList<String>();
        if (clazz.isArray()) {
            reasons.add("because it is an array");
        } else if (clazz.isPrimitive()) {
            reasons.add("because it is primitive");
        } else if (clazz == Void.class) {
            reasons.add("because it is void");
        } else {
            if (Modifier.isInterface(clazz.getModifiers())) {
                reasons.add("because it is an interface");
            } else if (Modifier.isAbstract(clazz.getModifiers())) {
                reasons.add("because it is abstract");
            }
            if (clazz.getEnclosingClass() != null && !Modifier.isStatic(clazz.getModifiers())) {
                reasons.add("because it is not static");
            }
            // we don't know what visibility is necessary, but we can give a hint
            if (!Modifier.isPublic(clazz.getModifiers())) {
                reasons.add("possibly because it is not public");
            } else {
                try {
                    clazz.getConstructor();
                } catch (NoSuchMethodException e1) {
                    reasons.add("because it has no accessible default constructor");
                }
            }
        }
        // append reasons
        boolean and = false;
        for (String reason : reasons) {
            if (and) {
                buf.append(" and");
            } else {
                and = true;
            }
            buf.append(" ").append(reason);
        }
        return new IllegalArgumentException(buf.toString(), e);
    }

    public static boolean isArray(Type type) {
        return type instanceof GenericArrayType || type instanceof Class<?>
                && ((Class<?>) type).isArray();
    }

    /**
     * Returns the component type of the given array type, assuming {@link #isArray(Type)}.
     *
     * <p>
     * Return type will either be class, parameterized type, generic array type, or type variable, but
     * not a wildcard type.
     * </p>
     *
     * @throws ClassCastException if {@link #isArray(Type)} is false
     */
    public static Type getArrayComponentType(Type array) {
        return array instanceof GenericArrayType ? ((GenericArrayType) array).getGenericComponentType()
                : ((Class<?>) array).getComponentType();
    }


    public static Class<?> getRawClass(ParameterizedType parameterType) {
        return (Class<?>) parameterType.getRawType();
    }


    public static Type getBound(WildcardType wildcardType) {
        Type[] lowerBounds = wildcardType.getLowerBounds();
        if (lowerBounds.length != 0) {
            return lowerBounds[0];
        }
        return wildcardType.getUpperBounds()[0];
    }

    public static Type resolveTypeVariable(List<Type> context, TypeVariable<?> typeVariable) {
        // determine where the type variable was declared
        GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
        if (genericDeclaration instanceof Class<?>) {
            Class<?> rawGenericDeclaration = (Class<?>) genericDeclaration;
            // check if the context extends that declaration
            int contextIndex = context.size();
            ParameterizedType parameterizedType = null;
            while (parameterizedType == null && --contextIndex >= 0) {
                parameterizedType =
                        getSuperParameterizedType(context.get(contextIndex), rawGenericDeclaration);
            }
            if (parameterizedType != null) {
                // find the type variable's index in the declaration's type parameters
                TypeVariable<?>[] typeParameters = genericDeclaration.getTypeParameters();
                int index = 0;
                for (; index < typeParameters.length; index++) {
                    TypeVariable<?> typeParameter = typeParameters[index];
                    if (typeParameter.equals(typeVariable)) {
                        break;
                    }
                }
                // use that index to get the actual type argument
                Type result = parameterizedType.getActualTypeArguments()[index];
                if (result instanceof TypeVariable<?>) {
                    // attempt to resolve type variable
                    Type resolve = resolveTypeVariable(context, (TypeVariable<?>) result);
                    if (resolve != null) {
                        return resolve;
                    }
                    // partially resolved type variable is okay
                }
                return result;
            }
        }
        return null;
    }

    public static Class<?> getRawArrayComponentType(List<Type> context, Type componentType) {
        if (componentType instanceof TypeVariable<?>) {
            componentType = Types.resolveTypeVariable(context, (TypeVariable<?>) componentType);
        }
        if (componentType instanceof GenericArrayType) {
            Class<?> raw = getRawArrayComponentType(context, Types.getArrayComponentType(componentType));
            return Array.newInstance(raw, 0).getClass();
        }
        if (componentType instanceof Class<?>) {
            return (Class<?>) componentType;
        }
        if (componentType instanceof ParameterizedType) {
            return Types.getRawClass((ParameterizedType) componentType);
        }
        Preconditions.checkArgument(
                componentType == null, "wildcard type is not supported: %s", componentType);
        return Object.class;
    }

    public static Type getIterableParameter(Type iterableType) {
        return getActualParameterAtPosition(iterableType, Iterable.class, 0);
    }

    public static Type getMapValueParameter(Type mapType) {
        return getActualParameterAtPosition(mapType, Map.class, 1);
    }

    private static Type getActualParameterAtPosition(Type type, Class<?> superClass, int position) {
        ParameterizedType parameterizedType = Types.getSuperParameterizedType(type, superClass);
        if (parameterizedType == null) {
            return null;
        }
        Type valueType = parameterizedType.getActualTypeArguments()[position];
        // this is normally a type variable, except in the case where the class of iterableType is
        // superClass, e.g. Iterable<String>
        if (valueType instanceof TypeVariable<?>) {
            Type resolve = Types.resolveTypeVariable(Arrays.asList(type), (TypeVariable<?>) valueType);
            if (resolve != null) {
                return resolve;
            }
        }
        return valueType;
    }

    public static <T> Iterable<T> iterableOf(final Object value) {
        if (value instanceof Iterable<?>) {
            return (Iterable<T>) value;
        }
        Class<?> valueClass = value.getClass();
        Preconditions.checkArgument(valueClass.isArray(), "not an array or Iterable: %s", valueClass);
        Class<?> subClass = valueClass.getComponentType();
        if (!subClass.isPrimitive()) {
            return Arrays.<T>asList((T[]) value);
        }
        return new Iterable<T>() {

            public Iterator<T> iterator() {
                return new Iterator<T>() {

                    final int length = Array.getLength(value);
                    int index = 0;

                    public boolean hasNext() {
                        return index < length;
                    }

                    public T next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return (T) Array.get(value, index++);
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }


    public static Object toArray(Collection<?> collection, Class<?> componentType) {
        if (componentType.isPrimitive()) {
            Object array = Array.newInstance(componentType, collection.size());
            int index = 0;
            for (Object value : collection) {
                Array.set(array, index++, value);
            }
            return array;
        }
        return collection.toArray((Object[]) Array.newInstance(componentType, collection.size()));
    }

}
