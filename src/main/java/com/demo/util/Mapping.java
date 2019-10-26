package com.demo.util;

/**
 * Created by jiangfei on 2019/10/26.
 */
public interface Mapping<IN, OUT> {

    OUT map(IN input);

}
