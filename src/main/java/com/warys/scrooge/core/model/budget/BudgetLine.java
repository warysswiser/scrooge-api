package com.warys.scrooge.core.model.budget;

import com.warys.scrooge.core.model.GenericModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "budget_lines")
public class BudgetLine extends GenericModel {

    private String label;
    private String budgetId;
    private String ownerId;
    private double projection;
    private double real;
    private Set<Expense> expenses;

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public double getProjection() {
        return projection;
    }

    public void setProjection(double projection) {
        this.projection = projection;
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
