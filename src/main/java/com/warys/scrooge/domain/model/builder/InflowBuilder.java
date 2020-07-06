package com.warys.scrooge.domain.model.builder;

import com.warys.scrooge.infrastructure.repository.mongo.entity.InflowDocument;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public final class InflowBuilder implements ModelBuilder<InflowBuilder, InflowDocument> {

    public String id;
    public LocalDateTime creationDate;
    public LocalDateTime updateDate;
    public LocalDateTime deletionDate;
    public String ownerId;
    public String category;
    public String label;
    public double amount;
    public String frequency;

    public InflowBuilder with(Consumer<InflowBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    public InflowDocument build() {
        InflowDocument flow = new InflowDocument();
        flow.setId(id);
        flow.setOwnerId(ownerId);
        flow.setCategory(category);
        flow.setLabel(label);
        flow.setAmount(amount);
        flow.setFrequency(frequency);
        flow.setCreationDate(creationDate);
        flow.setUpdateDate(updateDate);
        flow.setDeletionDate(deletionDate);
        return flow;
    }
}