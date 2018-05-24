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

}
