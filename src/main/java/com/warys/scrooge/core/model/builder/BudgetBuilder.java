package com.warys.scrooge.core.model.builder;

import com.warys.scrooge.core.model.budget.Budget;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public final class BudgetBuilder implements ModelBuilder<BudgetBuilder, Budget> {

    public String id;
    public String ownerId;
    public String name;
    public LocalDate startDate;
    public LocalDate endDate;
    public LocalDateTime creationDate;
    public LocalDateTime updateDate;
    public LocalDateTime deletionDate;

    public BudgetBuilder with(Consumer<BudgetBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    public Budget build() {
        Budget budget = new Budget();
        budget.setId(id);
        budget.setOwnerId(ownerId);
        budget.setName(name);
        budget.setStartDate(startDate);
        budget.setEndDate(endDate);
        budget.setCreationDate(creationDate);
        budget.setUpdateDate(updateDate);
        budget.setDeletionDate(deletionDate);
        return budget;
    }
}