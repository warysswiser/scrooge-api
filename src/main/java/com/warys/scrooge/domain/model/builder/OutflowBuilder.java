package com.warys.scrooge.domain.model.builder;

import com.warys.scrooge.infrastructure.repository.mongo.entity.OutflowDocument;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public final class OutflowBuilder implements ModelBuilder<OutflowBuilder, OutflowDocument> {

    public String id;
    public LocalDateTime creationDate;
    public LocalDateTime updateDate;
    public LocalDateTime deletionDate;
    public String ownerId;
    public String category;
    public String label;
    public double amount;
    public String frequency;

    public OutflowBuilder with(Consumer<OutflowBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    public OutflowDocument build() {
        OutflowDocument flow = new OutflowDocument();
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