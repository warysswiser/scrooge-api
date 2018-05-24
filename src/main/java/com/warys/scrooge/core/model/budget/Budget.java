package com.warys.scrooge.core.model.budget;

import com.warys.scrooge.core.model.GenericModel;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "budgets")
public class Budget extends GenericModel {

    private String name;
    private String ownerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private PlannedItems plannedItems;
    private RealItems realItems;

    public PlannedItems getPlannedItems() {
        return plannedItems;
    }

    public void setPlannedItems(PlannedItems plannedItems) {
        this.plannedItems = plannedItems;
    }

    public RealItems getRealItems() {
        return realItems;
    }

    public void setRealItems(RealItems realItems) {
        this.realItems = realItems;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
