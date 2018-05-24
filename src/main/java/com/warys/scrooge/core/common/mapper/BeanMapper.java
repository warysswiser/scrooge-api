package com.warys.scrooge.core.common.mapper;

import java.util.function.Function;

public interface BeanMapper<I, O> {

    Function<O, I> mapToOutput();

    Function<I, O> mapToInput();
}
