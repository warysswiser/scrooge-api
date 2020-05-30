package com.warys.scrooge.domain.model.builder;

import java.util.function.Consumer;

interface ModelBuilder<B, R> {

    B with(Consumer<B> builderFunction);

    R build();
}
