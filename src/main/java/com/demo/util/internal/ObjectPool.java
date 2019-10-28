package com.demo.util.internal;

import com.demo.util.Recycler;

public abstract class ObjectPool<T> {

    ObjectPool() {
    }

    public abstract T get();

    public interface Handle<T> {

        void recycle(T self);
    }

    public interface ObjectCreator<T> {

        T newObject(Handle<T> handle);
    }


    public static <T> ObjectPool<T> newPool(final ObjectCreator<T> creator) {
        ObjectUtil.checkNotNull(creator, "creator");

        return new RecyclerObjectPool<T>(creator);
    }

    private static final class RecyclerObjectPool<T> extends ObjectPool<T> {
        private final Recycler<T> recycler;

        RecyclerObjectPool(final ObjectCreator<T> creator) {
            recycler = new Recycler<T>() {
                @Override
                protected T newObject(Handle<T> handle) {
                    return creator.newObject(handle);
                }
            };
        }

        @Override
        public T get() {
            return recycler.get();
        }
    }
}
