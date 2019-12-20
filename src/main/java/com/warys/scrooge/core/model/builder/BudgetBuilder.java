package com.warys.scrooge.core.model.builder;

import com.warys.scrooge.core.model.budget.Budget;
import com.warys.scrooge.core.model.budget.BudgetLine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class BudgetBuilder implements ModelBuilder<BudgetBuilder, Budget> {

    public String id;
    public LocalDateTime creationDate;
    public LocalDateTime updateDate;
    public LocalDateTime deletionDate;
    public String name;
    public String ownerId;
    public LocalDate startDate;
    public LocalDate endDate;
    public List<BudgetLine> budgetLines = new ArrayList<>();

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
        budget.setBudgetLines(budgetLines);
        budget.setCreationDate(creationDate);
        budget.setUpdateDate(updateDate);
        budget.setDeletionDate(deletionDate);
        return budget;
    }
}