package com.warys.scrooge.core.model.builder;

import java.util.function.Consumer;

interface ModelBuilder<E, T> {

    E with(Consumer<E> builderFunction);

    T build();
}
