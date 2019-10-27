package com.demo.util;

/**
 * Created by jiangfei on 2019/10/27.
 */
public interface UncheckedBooleanSupplier extends BooleanSupplier {
    /**
     * Gets a boolean value.
     *
     * @return a boolean value.
     */
    @Override
    boolean get();

    /**
     * A supplier which always returns {@code false} and never throws.
     */
    UncheckedBooleanSupplier FALSE_SUPPLIER = new UncheckedBooleanSupplier() {
        @Override
        public boolean get() {
            return false;
        }
    };

    /**
     * A supplier which always returns {@code true} and never throws.
     */
    UncheckedBooleanSupplier TRUE_SUPPLIER = new UncheckedBooleanSupplier() {
        @Override
        public boolean get() {
            return true;
        }
    };
}
