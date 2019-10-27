package com.demo.util;

/**
 * Created by jiangfei on 2019/10/27.
 */
public interface BooleanSupplier {

    /**
     * Gets a boolean value.
     * @return a boolean value.
     * @throws Exception If an exception occurs.
     */
    boolean get() throws Exception;

    /**
     * A supplier which always returns {@code false} and never throws.
     */
    BooleanSupplier FALSE_SUPPLIER = new BooleanSupplier() {
        @Override
        public boolean get() {
            return false;
        }
    };

    /**
     * A supplier which always returns {@code true} and never throws.
     */
    BooleanSupplier TRUE_SUPPLIER = new BooleanSupplier() {
        @Override
        public boolean get() {
            return true;
        }
    };

}
