package com.warys.scrooge.core.model.budget;

import com.warys.scrooge.core.model.GenericModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "planned_items")
public class PlannedItems extends GenericModel {

    private String budgetId;
    private String ownerId;
    private Set<Expense> expenses;
    private Set<Resource> resources;

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }
}
