package com.warys.scrooge.core.service.budget;

import com.warys.scrooge.core.common.util.BeanUtil;
import com.warys.scrooge.core.model.budget.Budget;
import com.warys.scrooge.core.model.budget.BudgetLine;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.repository.BudgetRepository;
import com.warys.scrooge.core.service.MyCrudService;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.business.ElementNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

@AllArgsConstructor
@Service
public class BudgetService implements MyCrudService<Budget, Budget> {

    private final BudgetRepository budgetRepository;

    @Override
    public Budget retrieve(SessionUser me, String budgetId) throws ApiException {
        requireNonNull(budgetId);
        return budgetRepository.findByIdAndOwnerId(budgetId, me.getId())
                .orElseThrow(
                        () -> new ElementNotFoundException("Could not found budget with id : " + budgetId));
    }

    public BudgetLine getLine(SessionUser me, String budgetId, String lineId) throws ApiException {
        requireNonNull(budgetId);
        requireNonNull(lineId);
        Budget budget = retrieve(me, budgetId);
        return budget.getLine(lineId);
    }

    @Override
    public Budget create(SessionUser me, Budget payload) {
        requireNonNull(payload);
        payload.setOwnerId(me.getId());
        return budgetRepository.insert(payload);
    }

    public BudgetLine addLine(SessionUser me, BudgetLine payload, String budgetId) throws ApiException {
        requireNonNull(payload);
        payload.setOwnerId(me.getId());
        payload.setBudgetId(budgetId);
        Budget budget = retrieve(me, budgetId);
        budget.addLine(payload);
        update(me, budgetId, budget);
        return payload;
    }

    @Override
    public void remove(SessionUser me, String budgetId) throws ApiException {
        Budget budgetToRemove = retrieve(me, budgetId);
        budgetRepository.delete(budgetToRemove);
    }

    public void removeLine(SessionUser me, String budgetId, String lineId) throws ApiException {
        Budget budget = retrieve(me, budgetId);
        budget.removeLine(lineId);
        update(me, budgetId, budget);
    }

    @Override
    public Budget update(SessionUser me, String budgetId, Budget payload) {
        requireNonNull(budgetId);
        requireNonNull(payload);
        payload.setOwnerId(me.getId());
        payload.setId(budgetId);
        return budgetRepository.save(payload);
    }

    public BudgetLine updateLine(SessionUser me, String budgetId, String lineId, BudgetLine line) throws ApiException {
        requireNonNull(budgetId);
        requireNonNull(lineId);
        requireNonNull(line);
        Budget budget = retrieve(me, budgetId);
        budget.updateLine(lineId, line);
        budgetRepository.save(budget);
        return line;
    }

    public BudgetLine partialUpdateLine(SessionUser me, String budgetId, String lineId, BudgetLine line) throws ApiException {
        requireNonNull(budgetId);
        requireNonNull(lineId);
        requireNonNull(line);
        Budget budget = retrieve(me, budgetId);
        budget.partialUpdateLine(lineId, line);
        budgetRepository.save(budget);
        return line;
    }

    @Override
    public List<Budget> getAll(SessionUser me) {
        return budgetRepository.findByOwnerId(me.getId()).orElse(Collections.emptyList());
    }

    public List<BudgetLine> getAllBudgetLines(SessionUser me, String budgetId) throws ApiException {
        return retrieve(me, budgetId).getBudgetLines();
    }

    @Override
    public Budget partialUpdate(SessionUser me, String budgetId, Budget partialPayload) throws ApiException {
        requireNonNull(budgetId);
        requireNonNull(partialPayload);

        Budget budgetToUpdate = retrieve(me, budgetId);
        BeanUtil.copyBean(partialPayload, budgetToUpdate);
        return budgetRepository.save(budgetToUpdate);
    }
}
