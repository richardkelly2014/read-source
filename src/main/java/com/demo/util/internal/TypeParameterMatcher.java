package com.demo.util.internal;

import java.util.Map;

/**
 * Created by jiangfei on 2019/10/17.
 */
public abstract class TypeParameterMatcher {

    private static final TypeParameterMatcher NOOP = new TypeParameterMatcher() {
        @Override
        public boolean match(Object msg) {
            return true;
        }
    };

//    public static TypeParameterMatcher get(final Class<?> parameterType) {
//        final Map<Class<?>, TypeParameterMatcher> getCache =
//                InternalThreadLocalMap.get().typeParameterMatcherGetCache();
//
//        TypeParameterMatcher matcher = getCache.get(parameterType);
//        if (matcher == null) {
//            if (parameterType == Object.class) {
//                matcher = NOOP;
//            } else {
//                matcher = new ReflectiveMatcher(parameterType);
//            }
//            getCache.put(parameterType, matcher);
//        }
//
//        return matcher;
//    }

    private static Class<?> fail(Class<?> type, String typeParamName) {
        throw new IllegalStateException(
                "cannot determine the type of the type parameter '" + typeParamName + "': " + type);
    }

    public abstract boolean match(Object msg);

    private static final class ReflectiveMatcher extends TypeParameterMatcher {
        private final Class<?> type;

        ReflectiveMatcher(Class<?> type) {
            this.type = type;
        }

        @Override
        public boolean match(Object msg) {
            return type.isInstance(msg);
        }
    }

    TypeParameterMatcher() { }

}
