package com.warys.scrooge.core.model.builder;

import com.warys.scrooge.core.model.budget.Expense;
import com.warys.scrooge.core.model.budget.PlannedItems;
import com.warys.scrooge.core.model.budget.Resource;

import java.util.Set;
import java.util.function.Consumer;

public class PlannedItemsBuilder implements ModelBuilder<PlannedItemsBuilder, PlannedItems> {

    public String id;
    public String budgetId;
    public String ownerId;
    public Set<Expense> expenses;
    public Set<Resource> resources;

    @Override
    public PlannedItemsBuilder with(Consumer<PlannedItemsBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    @Override
    public PlannedItems build() {
        return new PlannedItems(id, budgetId, ownerId, expenses, resources);
    }
}
