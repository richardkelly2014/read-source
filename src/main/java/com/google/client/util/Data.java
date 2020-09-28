package com.google.client.util;

import com.google.common.base.Preconditions;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Data {

    /** The single instance of the magic null object for a {@link Boolean}. */
    public static final Boolean NULL_BOOLEAN = new Boolean(true);

    /** The single instance of the magic null object for a {@link String}. */
    public static final String NULL_STRING = new String();

    /** The single instance of the magic null object for a {@link Character}. */
    public static final Character NULL_CHARACTER = new Character((char) 0);

    /** The single instance of the magic null object for a {@link Byte}. */
    public static final Byte NULL_BYTE = new Byte((byte) 0);

    /** The single instance of the magic null object for a {@link Short}. */
    public static final Short NULL_SHORT = new Short((short) 0);

    /** The single instance of the magic null object for a {@link Integer}. */
    public static final Integer NULL_INTEGER = new Integer(0);

    /** The single instance of the magic null object for a {@link Float}. */
    public static final Float NULL_FLOAT = new Float(0);

    /** The single instance of the magic null object for a {@link Long}. */
    public static final Long NULL_LONG = new Long(0);

    /** The single instance of the magic null object for a {@link Double}. */
    public static final Double NULL_DOUBLE = new Double(0);

    /** The single instance of the magic null object for a {@link BigInteger}. */
    public static final BigInteger NULL_BIG_INTEGER = new BigInteger("0");

    /** The single instance of the magic null object for a {@link BigDecimal}. */
    public static final BigDecimal NULL_BIG_DECIMAL = new BigDecimal("0");

    /** The single instance of the magic null object for a {@link DateTime}. */
    public static final DateTime NULL_DATE_TIME = new DateTime(0);


    private static final ConcurrentHashMap<Class<?>, Object> NULL_CACHE =
            new ConcurrentHashMap<Class<?>, Object>();
    static {
        // special cases for some primitives
        NULL_CACHE.put(Boolean.class, NULL_BOOLEAN);
        NULL_CACHE.put(String.class, NULL_STRING);
        NULL_CACHE.put(Character.class, NULL_CHARACTER);
        NULL_CACHE.put(Byte.class, NULL_BYTE);
        NULL_CACHE.put(Short.class, NULL_SHORT);
        NULL_CACHE.put(Integer.class, NULL_INTEGER);
        NULL_CACHE.put(Float.class, NULL_FLOAT);
        NULL_CACHE.put(Long.class, NULL_LONG);
        NULL_CACHE.put(Double.class, NULL_DOUBLE);
        NULL_CACHE.put(BigInteger.class, NULL_BIG_INTEGER);
        NULL_CACHE.put(BigDecimal.class, NULL_BIG_DECIMAL);
        NULL_CACHE.put(DateTime.class, NULL_DATE_TIME);
    }

    public static <T> T nullOf(Class<T> objClass) {
        Object result = NULL_CACHE.get(objClass);
        if (result == null) {
            synchronized (NULL_CACHE) {
                result = NULL_CACHE.get(objClass);
                if (result == null) {
                    if (objClass.isArray()) {
                        // arrays are special because we need to compute both the dimension and component type
                        int dims = 0;
                        Class<?> componentType = objClass;
                        do {
                            componentType = componentType.getComponentType();
                            dims++;
                        } while (componentType.isArray());
                        result = Array.newInstance(componentType, new int[dims]);
                    } else if (objClass.isEnum()) {
                        // enum requires look for constant with @NullValue
                        FieldInfo fieldInfo = ClassInfo.of(objClass).getFieldInfo(null);
                        Preconditions.checkNotNull(
                                fieldInfo, "enum missing constant with @NullValue annotation: %s", objClass);
                        @SuppressWarnings({"unchecked", "rawtypes"})
                        Enum e = fieldInfo.<Enum>enumValue();
                        result = e;
                    } else {
                        // other classes are simpler
                        result = Types.newInstance(objClass);
                    }
                    NULL_CACHE.put(objClass, result);
                }
            }
        }
        @SuppressWarnings("unchecked")
        T tResult = (T) result;
        return tResult;
    }

    public static boolean isNull(Object object) {
        // don't call nullOf because will throw IllegalArgumentException if cannot create instance
        return object != null && object == NULL_CACHE.get(object.getClass());
    }

    public static Map<String, Object> mapOf(Object data) {
        if (data == null || isNull(data)) {
            return Collections.emptyMap();
        }
        if (data instanceof Map<?, ?>) {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = (Map<String, Object>) data;
            return result;
        }
        Map<String, Object> result = new DataMap(data, false);
        return result;
    }


    public static <T> T clone(T data) {
        // don't need to clone primitive
        if (data == null || Data.isPrimitive(data.getClass())) {
            return data;
        }
        if (data instanceof GenericData) {
            return (T) ((GenericData) data).clone();
        }
        T copy;
        Class<?> dataClass = data.getClass();
        if (dataClass.isArray()) {
            copy = (T) Array.newInstance(dataClass.getComponentType(), Array.getLength(data));
        } else if (data instanceof ArrayMap<?, ?>) {
            copy = (T) ((ArrayMap<?, ?>) data).clone();
        } else if ("java.util.Arrays$ArrayList".equals(dataClass.getName())) {
            // Arrays$ArrayList does not have a zero-arg constructor, so it has to handled specially.
            // Arrays.asList(x).toArray() may or may not have the same runtime type as x.
            // https://bugs.openjdk.java.net/browse/JDK-6260652
            Object[] arrayCopy = ((List<?>) data).toArray();
            deepCopy(arrayCopy, arrayCopy);
            copy = (T) Arrays.asList(arrayCopy);
            return copy;
        } else {
            copy = (T) Types.newInstance(dataClass);
        }
        deepCopy(data, copy);
        return copy;
    }

    public static void deepCopy(Object src, Object dest) {
        Class<?> srcClass = src.getClass();
        Preconditions.checkArgument(srcClass == dest.getClass());
        if (srcClass.isArray()) {
            // clone array
            Preconditions.checkArgument(Array.getLength(src) == Array.getLength(dest));
            int index = 0;
            for (Object value : Types.iterableOf(src)) {
                Array.set(dest, index++, clone(value));
            }
        } else if (Collection.class.isAssignableFrom(srcClass)) {
            // clone collection
            @SuppressWarnings("unchecked")
            Collection<Object> srcCollection = (Collection<Object>) src;
            if (ArrayList.class.isAssignableFrom(srcClass)) {
                @SuppressWarnings("unchecked")
                ArrayList<Object> destArrayList = (ArrayList<Object>) dest;
                destArrayList.ensureCapacity(srcCollection.size());
            }
            @SuppressWarnings("unchecked")
            Collection<Object> destCollection = (Collection<Object>) dest;
            for (Object srcValue : srcCollection) {
                destCollection.add(clone(srcValue));
            }
        } else {
            // clone generic data or a non-map Object
            boolean isGenericData = GenericData.class.isAssignableFrom(srcClass);
            if (isGenericData || !Map.class.isAssignableFrom(srcClass)) {
                ClassInfo classInfo =
                        isGenericData ? ((GenericData) src).classInfo : ClassInfo.of(srcClass);
                for (String fieldName : classInfo.names) {
                    FieldInfo fieldInfo = classInfo.getFieldInfo(fieldName);
                    // skip final fields
                    if (!fieldInfo.isFinal()) {
                        // generic data already has primitive types copied by clone()
                        if (!isGenericData || !fieldInfo.isPrimitive()) {
                            Object srcValue = fieldInfo.getValue(src);
                            if (srcValue != null) {
                                fieldInfo.setValue(dest, clone(srcValue));
                            }
                        }
                    }
                }
            } else if (ArrayMap.class.isAssignableFrom(srcClass)) {
                // clone array map
                @SuppressWarnings("unchecked")
                ArrayMap<Object, Object> destMap = (ArrayMap<Object, Object>) dest;
                @SuppressWarnings("unchecked")
                ArrayMap<Object, Object> srcMap = (ArrayMap<Object, Object>) src;
                int size = srcMap.size();
                for (int i = 0; i < size; i++) {
                    Object srcValue = srcMap.getValue(i);
                    destMap.set(i, clone(srcValue));
                }
            } else {
                // clone map
                @SuppressWarnings("unchecked")
                Map<String, Object> destMap = (Map<String, Object>) dest;
                @SuppressWarnings("unchecked")
                Map<String, Object> srcMap = (Map<String, Object>) src;
                for (Map.Entry<String, Object> srcEntry : srcMap.entrySet()) {
                    destMap.put(srcEntry.getKey(), clone(srcEntry.getValue()));
                }
            }
        }
    }

    public static boolean isPrimitive(Type type) {
        // TODO(yanivi): support java.net.URI as primitive type?
        if (type instanceof WildcardType) {
            type = Types.getBound((WildcardType) type);
        }
        if (!(type instanceof Class<?>)) {
            return false;
        }
        Class<?> typeClass = (Class<?>) type;
        return typeClass.isPrimitive() || typeClass == Character.class || typeClass == String.class
                || typeClass == Integer.class || typeClass == Long.class || typeClass == Short.class
                || typeClass == Byte.class || typeClass == Float.class || typeClass == Double.class
                || typeClass == BigInteger.class || typeClass == BigDecimal.class
                || typeClass == DateTime.class || typeClass == Boolean.class;
    }

    public static boolean isValueOfPrimitiveType(Object fieldValue) {
        return fieldValue == null || Data.isPrimitive(fieldValue.getClass());
    }


    public static Object parsePrimitiveValue(Type type, String stringValue) {
        Class<?> primitiveClass = type instanceof Class<?> ? (Class<?>) type : null;
        if (type == null || primitiveClass != null) {
            if (primitiveClass == Void.class) {
                return null;
            }
            if (stringValue == null || primitiveClass == null
                    || primitiveClass.isAssignableFrom(String.class)) {
                return stringValue;
            }
            if (primitiveClass == Character.class || primitiveClass == char.class) {
                if (stringValue.length() != 1) {
                    throw new IllegalArgumentException(
                            "expected type Character/char but got " + primitiveClass);
                }
                return stringValue.charAt(0);
            }
            if (primitiveClass == Boolean.class || primitiveClass == boolean.class) {
                return Boolean.valueOf(stringValue);
            }
            if (primitiveClass == Byte.class || primitiveClass == byte.class) {
                return Byte.valueOf(stringValue);
            }
            if (primitiveClass == Short.class || primitiveClass == short.class) {
                return Short.valueOf(stringValue);
            }
            if (primitiveClass == Integer.class || primitiveClass == int.class) {
                return Integer.valueOf(stringValue);
            }
            if (primitiveClass == Long.class || primitiveClass == long.class) {
                return Long.valueOf(stringValue);
            }
            if (primitiveClass == Float.class || primitiveClass == float.class) {
                return Float.valueOf(stringValue);
            }
            if (primitiveClass == Double.class || primitiveClass == double.class) {
                return Double.valueOf(stringValue);
            }
            if (primitiveClass == DateTime.class) {
                return DateTime.parseRfc3339(stringValue);
            }
            if (primitiveClass == BigInteger.class) {
                return new BigInteger(stringValue);
            }
            if (primitiveClass == BigDecimal.class) {
                return new BigDecimal(stringValue);
            }
            if (primitiveClass.isEnum()) {
                if (!ClassInfo.of(primitiveClass).names.contains(stringValue)) {
                    throw new IllegalArgumentException(String.format("given enum name %s not part of " +
                            "enumeration", stringValue));
                }
                @SuppressWarnings({"unchecked", "rawtypes"})
                Enum result = ClassInfo.of(primitiveClass).getFieldInfo(stringValue).<Enum>enumValue();
                return result;
            }
        }
        throw new IllegalArgumentException("expected primitive class, but got: " + type);
    }

    public static Collection<Object> newCollectionInstance(Type type) {
        if (type instanceof WildcardType) {
            type = Types.getBound((WildcardType) type);
        }
        if (type instanceof ParameterizedType) {
            type = ((ParameterizedType) type).getRawType();
        }
        Class<?> collectionClass = type instanceof Class<?> ? (Class<?>) type : null;
        if (type == null || type instanceof GenericArrayType || collectionClass != null
                && (collectionClass.isArray() || collectionClass.isAssignableFrom(ArrayList.class))) {
            return new ArrayList<Object>();
        }
        if (collectionClass == null) {
            throw new IllegalArgumentException("unable to create new instance of type: " + type);
        }
        if (collectionClass.isAssignableFrom(HashSet.class)) {
            return new HashSet<Object>();
        }
        if (collectionClass.isAssignableFrom(TreeSet.class)) {
            return new TreeSet<Object>();
        }
        @SuppressWarnings("unchecked")
        Collection<Object> result = (Collection<Object>) Types.newInstance(collectionClass);
        return result;
    }

    public static Map<String, Object> newMapInstance(Class<?> mapClass) {
        if (mapClass == null || mapClass.isAssignableFrom(ArrayMap.class)) {
            return ArrayMap.create();
        }
        if (mapClass.isAssignableFrom(TreeMap.class)) {
            return new TreeMap<String, Object>();
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> result = (Map<String, Object>) Types.newInstance(mapClass);
        return result;
    }

    public static Type resolveWildcardTypeOrTypeVariable(List<Type> context, Type type) {
        // first deal with a wildcard, e.g. ? extends Number
        if (type instanceof WildcardType) {
            type = Types.getBound((WildcardType) type);
        }
        // next deal with a type variable T
        while (type instanceof TypeVariable<?>) {
            // resolve the type variable
            Type resolved = Types.resolveTypeVariable(context, (TypeVariable<?>) type);
            if (resolved != null) {
                type = resolved;
            }
            // if unable to fully resolve the type variable, use its bounds, e.g. T extends Number
            if (type instanceof TypeVariable<?>) {
                type = ((TypeVariable<?>) type).getBounds()[0];
            }
            // loop in case T extends U and U is also a type variable
        }
        return type;
    }
}
