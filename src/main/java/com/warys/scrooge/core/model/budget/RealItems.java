package com.warys.scrooge.core.model.budget;

import com.warys.scrooge.core.model.GenericModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "real_items")
public class RealItems extends GenericModel {

    private String budgetId;
    private String ownerId;
    private Set<Expense> expenses;
    private Set<Resource> resources;

    public RealItems(String id, String budgetId, String ownerId, Set<Expense> expenses, Set<Resource> resources) {
        super(id);
        this.budgetId = budgetId;
        this.ownerId = ownerId;
        this.expenses = expenses;
        this.resources = resources;
    }

    public RealItems() {

    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
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

    public Set<Resource> getResources() {
        return resources;
    }
}
