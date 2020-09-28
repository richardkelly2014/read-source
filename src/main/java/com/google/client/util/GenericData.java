package com.google.client.util;

import java.util.*;

public class GenericData extends AbstractMap<String, Object> implements Cloneable  {

    /** Map of unknown fields. */
    Map<String, Object> unknownFields = ArrayMap.create();

    final ClassInfo classInfo;

    public GenericData() {
        this(EnumSet.noneOf(Flags.class));
    }

    public enum Flags {

        /** Whether keys are case sensitive. */
        IGNORE_CASE
    }

    public GenericData(EnumSet<Flags> flags) {
        classInfo = ClassInfo.of(getClass(), flags.contains(Flags.IGNORE_CASE));
    }

    @Override
    public final Object get(Object name) {
        if (!(name instanceof String)) {
            return null;
        }
        String fieldName = (String) name;
        FieldInfo fieldInfo = classInfo.getFieldInfo(fieldName);
        if (fieldInfo != null) {
            return fieldInfo.getValue(this);
        }
        if (classInfo.getIgnoreCase()) {
            fieldName = fieldName.toLowerCase(Locale.US);
        }
        return unknownFields.get(fieldName);
    }

    @Override
    public final Object put(String fieldName, Object value) {
        FieldInfo fieldInfo = classInfo.getFieldInfo(fieldName);
        if (fieldInfo != null) {
            Object oldValue = fieldInfo.getValue(this);
            fieldInfo.setValue(this, value);
            return oldValue;
        }
        if (classInfo.getIgnoreCase()) {
            fieldName = fieldName.toLowerCase(Locale.US);
        }
        return unknownFields.put(fieldName, value);
    }

    public GenericData set(String fieldName, Object value) {
        FieldInfo fieldInfo = classInfo.getFieldInfo(fieldName);
        if (fieldInfo != null) {
            fieldInfo.setValue(this, value);
        } else {
            if (classInfo.getIgnoreCase()) {
                fieldName = fieldName.toLowerCase(Locale.US);
            }
            unknownFields.put(fieldName, value);
        }
        return this;
    }

    @Override
    public final void putAll(Map<? extends String, ?> map) {
        for (Map.Entry<? extends String, ?> entry : map.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public final Object remove(Object name) {
        if (!(name instanceof String)) {
            return null;
        }
        String fieldName = (String) name;
        FieldInfo fieldInfo = classInfo.getFieldInfo(fieldName);
        if (fieldInfo != null) {
            throw new UnsupportedOperationException();
        }
        if (classInfo.getIgnoreCase()) {
            fieldName = fieldName.toLowerCase(Locale.US);
        }
        return unknownFields.remove(fieldName);
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        return new EntrySet();
    }

    /**
     * Makes a "deep" clone of the generic data, in which the clone is completely independent of the
     * original.
     */
    @Override
    public GenericData clone() {
        try {
            @SuppressWarnings("unchecked")
            GenericData result = (GenericData) super.clone();
            Data.deepCopy(this, result);
            result.unknownFields = Data.clone(unknownFields);
            return result;
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns the map of unknown data key name to value.
     *
     * @since 1.5
     */
    public final Map<String, Object> getUnknownKeys() {
        return unknownFields;
    }

    /**
     * Sets the map of unknown data key name to value.
     *
     * @since 1.5
     */
    public final void setUnknownKeys(Map<String, Object> unknownFields) {
        this.unknownFields = unknownFields;
    }

    /**
     * Returns the class information.
     *
     * @since 1.10
     */
    public final ClassInfo getClassInfo() {
        return classInfo;
    }

    /** Set of object data key/value map entries. */
    final class EntrySet extends AbstractSet<Map.Entry<String, Object>> {

        private final DataMap.EntrySet dataEntrySet;

        EntrySet() {
            dataEntrySet = new DataMap(GenericData.this, classInfo.getIgnoreCase()).entrySet();
        }

        @Override
        public Iterator<Map.Entry<String, Object>> iterator() {
            return new EntryIterator(dataEntrySet);
        }

        @Override
        public int size() {
            return unknownFields.size() + dataEntrySet.size();
        }

        @Override
        public void clear() {
            unknownFields.clear();
            dataEntrySet.clear();
        }
    }

    /**
     * Iterator over the object data key/value map entries which iterates first over the fields and
     * then over the unknown keys.
     */
    final class EntryIterator implements Iterator<Map.Entry<String, Object>> {

        /** Whether we've started iterating over the unknown keys. */
        private boolean startedUnknown;

        /** Iterator over the fields. */
        private final Iterator<Map.Entry<String, Object>> fieldIterator;

        /** Iterator over the unknown keys. */
        private final Iterator<Map.Entry<String, Object>> unknownIterator;

        EntryIterator(DataMap.EntrySet dataEntrySet) {
            fieldIterator = dataEntrySet.iterator();
            unknownIterator = unknownFields.entrySet().iterator();
        }

        public boolean hasNext() {
            return fieldIterator.hasNext() || unknownIterator.hasNext();
        }

        public Map.Entry<String, Object> next() {
            if (!startedUnknown) {
                if (fieldIterator.hasNext()) {
                    return fieldIterator.next();
                }
                startedUnknown = true;
            }
            return unknownIterator.next();
        }

        public void remove() {
            if (startedUnknown) {
                unknownIterator.remove();
            }
            fieldIterator.remove();
        }
    }
}
