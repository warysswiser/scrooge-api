package com.warys.scrooge.core.model.builder;

import com.warys.scrooge.core.model.budget.Expense;
import com.warys.scrooge.core.model.budget.RealItems;
import com.warys.scrooge.core.model.budget.Resource;

import java.util.Set;
import java.util.function.Consumer;

public class RealItemsBuilder implements ModelBuilder<RealItemsBuilder, RealItems> {

    public String id;
    public String budgetId;
    public String ownerId;
    public Set<Expense> expenses;
    public Set<Resource> resources;

    @Override
    public RealItemsBuilder with(Consumer<RealItemsBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    @Override
    public RealItems build() {
        return new RealItems(id, budgetId, ownerId, expenses, resources);
    }
}
