package com.google.client.util;


import com.google.common.base.Preconditions;

import java.lang.reflect.Field;
import java.util.*;

public final class ClassInfo {

    /**
     * Class information cache, with case-sensitive field names.
     */
    private static final Map<Class<?>, ClassInfo> CACHE = new WeakHashMap<Class<?>, ClassInfo>();

    /**
     * Class information cache, with case-insensitive fields names.
     */
    private static final Map<Class<?>, ClassInfo> CACHE_IGNORE_CASE =
            new WeakHashMap<Class<?>, ClassInfo>();

    private final Class<?> clazz;

    /**
     * Whether field names are case sensitive.
     */
    private final boolean ignoreCase;

    /**
     * Map from {@link FieldInfo#getName()} to the field information.
     */
    private final IdentityHashMap<String, FieldInfo> nameToFieldInfoMap =
            new IdentityHashMap<String, FieldInfo>();

    final List<String> names;


    public static ClassInfo of(Class<?> underlyingClass) {

        return of(underlyingClass, false);
    }


    public static ClassInfo of(Class<?> underlyingClass, boolean ignoreCase) {
        if (underlyingClass == null) {
            return null;
        }
        final Map<Class<?>, ClassInfo> cache = ignoreCase ? CACHE_IGNORE_CASE : CACHE;
        ClassInfo classInfo;
        synchronized (cache) {
            classInfo = cache.get(underlyingClass);
            if (classInfo == null) {
                classInfo = new ClassInfo(underlyingClass, ignoreCase);
                cache.put(underlyingClass, classInfo);
            }
        }
        return classInfo;
    }

    public Class<?> getUnderlyingClass() {
        return clazz;
    }

    public final boolean getIgnoreCase() {
        return ignoreCase;
    }

    public FieldInfo getFieldInfo(String name) {
        if (name != null) {
            if (ignoreCase) {
                name = name.toLowerCase(Locale.US);
            }
            name = name.intern();
        }
        return nameToFieldInfoMap.get(name);
    }

    public Field getField(String name) {
        FieldInfo fieldInfo = getFieldInfo(name);
        return fieldInfo == null ? null : fieldInfo.getField();
    }

    public boolean isEnum() {
        return clazz.isEnum();
    }


    public Collection<String> getNames() {
        return names;
    }

    private ClassInfo(Class<?> srcClass, boolean ignoreCase) {
        clazz = srcClass;
        this.ignoreCase = ignoreCase;
        Preconditions.checkArgument(
                !ignoreCase || !srcClass.isEnum(), "cannot ignore case on an enum: " + srcClass);
        // name set has a special comparator to keep null first
        TreeSet<String> nameSet = new TreeSet<String>(new Comparator<String>() {
            public int compare(String s0, String s1) {
                return Objects.equal(s0, s1) ? 0 : s0 == null ? -1 : s1 == null ? 1 : s0.compareTo(s1);
            }
        });
        // iterate over declared fields
        for (Field field : srcClass.getDeclaredFields()) {
            FieldInfo fieldInfo = FieldInfo.of(field);
            if (fieldInfo == null) {
                continue;
            }
            String fieldName = fieldInfo.getName();
            if (ignoreCase) {
                fieldName = fieldName.toLowerCase(Locale.US).intern();
            }
            FieldInfo conflictingFieldInfo = nameToFieldInfoMap.get(fieldName);
            Preconditions.checkArgument(conflictingFieldInfo == null,
                    "two fields have the same %sname <%s>: %s and %s",
                    ignoreCase ? "case-insensitive " : "",
                    fieldName,
                    field,
                    conflictingFieldInfo == null ? null : conflictingFieldInfo.getField());
            nameToFieldInfoMap.put(fieldName, fieldInfo);
            nameSet.add(fieldName);
        }
        // inherit from super class and add only fields not already in nameToFieldInfoMap
        Class<?> superClass = srcClass.getSuperclass();
        if (superClass != null) {
            ClassInfo superClassInfo = ClassInfo.of(superClass, ignoreCase);
            nameSet.addAll(superClassInfo.names);
            for (Map.Entry<String, FieldInfo> e : superClassInfo.nameToFieldInfoMap.entrySet()) {
                String name = e.getKey();
                if (!nameToFieldInfoMap.containsKey(name)) {
                    nameToFieldInfoMap.put(name, e.getValue());
                }
            }
        }
        names = nameSet.isEmpty() ? Collections.<String>emptyList() : Collections.unmodifiableList(
                new ArrayList<String>(nameSet));
    }

    /**
     * Returns an unmodifiable collection of the {@code FieldInfo}s for this class, without any
     * guarantee of order.
     *
     * <p>
     * If you need sorted order, instead use {@link #getNames()} with {@link #getFieldInfo(String)}.
     * </p>
     *
     * @since 1.16
     */
    public Collection<FieldInfo> getFieldInfos() {
        return Collections.unmodifiableCollection(nameToFieldInfoMap.values());
    }

}
