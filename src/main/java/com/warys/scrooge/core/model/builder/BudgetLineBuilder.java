package com.warys.scrooge.core.model.builder;

import com.warys.scrooge.core.model.budget.BudgetLine;
import com.warys.scrooge.core.model.budget.Expense;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.function.Consumer;

public final class BudgetLineBuilder implements ModelBuilder<BudgetLineBuilder, BudgetLine> {

    public String id;
    public LocalDateTime creationDate;
    public LocalDateTime updateDate;
    public LocalDateTime deletionDate;
    public String label;
    public String ownerId;
    public String budgetId;
    public double projection;
    public double real;
    public Set<Expense> expenses;

    public BudgetLineBuilder with(Consumer<BudgetLineBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    public BudgetLine build() {
        BudgetLine budgetLine = new BudgetLine();
        budgetLine.setId(id);
        budgetLine.setOwnerId(ownerId);
        budgetLine.setLabel(label);
        budgetLine.setBudgetId(budgetId);
        budgetLine.setProjection(projection);
        budgetLine.setReal(real);
        budgetLine.setExpenses(expenses);
        budgetLine.setCreationDate(creationDate);
        budgetLine.setUpdateDate(updateDate);
        budgetLine.setDeletionDate(deletionDate);
        return budgetLine;
    }
}