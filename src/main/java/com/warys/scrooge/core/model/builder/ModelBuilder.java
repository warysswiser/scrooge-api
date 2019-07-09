package com.warys.scrooge.core.model.builder;

import java.util.function.Consumer;

interface ModelBuilder<B, R> {

    B with(Consumer<B> builderFunction);

    R build();
}
