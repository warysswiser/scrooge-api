package com.warys.scrooge.core.model.budget;

import com.warys.scrooge.core.common.util.BeanUtil;
import com.warys.scrooge.core.model.GenericModel;
import com.warys.scrooge.infrastructure.exception.business.ElementNotFoundException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@NoArgsConstructor
@Document(collection = "budgets")
public class Budget extends GenericModel {

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String ownerId;
    @Getter
    @Setter
    private LocalDate startDate;
    @Getter
    @Setter
    private LocalDate endDate;
    @Getter
    @Setter
    private List<BudgetLine> budgetLines = new ArrayList<>();

    public void addLine(BudgetLine line) {
        getBudgetLines().add(line);
    }

    public void removeLine(String lineId) throws ElementNotFoundException {
        BudgetLine line = getLine(lineId);
        removeLine(line);
    }

    private void removeLine(BudgetLine line) {
        requireNonNull(line);
        getBudgetLines().remove(line);
    }

    public BudgetLine getLine(String lineId) throws ElementNotFoundException {
        return getBudgetLines().stream().filter(l -> l.getId().equals(lineId)).findFirst().orElseThrow(
                () -> new ElementNotFoundException("Could not found budget line with id : " + lineId));
    }

    public void updateLine(String lineId, BudgetLine newLine) throws ElementNotFoundException {
        BudgetLine lineToUpdate = getLine(lineId);
        lineToUpdate.setLabel(newLine.getLabel());
        lineToUpdate.setExpenses(newLine.getExpenses());
        lineToUpdate.setReal(newLine.getReal());
        lineToUpdate.setProjection(newLine.getProjection());
    }

    public void partialUpdateLine(String lineId, BudgetLine newLine) throws ElementNotFoundException {
        BudgetLine lineToUpdate = getLine(lineId);
        BeanUtil.copyBean(newLine, lineToUpdate);
    }
}
