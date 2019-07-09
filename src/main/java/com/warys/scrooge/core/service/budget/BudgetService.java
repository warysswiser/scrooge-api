package com.warys.scrooge.core.service.budget;

import com.warys.scrooge.core.common.util.BeanUtil;
import com.warys.scrooge.core.model.budget.Budget;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.repository.BudgetRepository;
import com.warys.scrooge.core.service.MyCrudService;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.business.ElementNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class BudgetService implements MyCrudService<Budget, Budget> {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    @Override
    public Budget retrieve(SessionUser me, String itemId) throws ApiException {
        Objects.requireNonNull(itemId);
        return budgetRepository.findByIdAndOwnerId(itemId, me.getId())
                .orElseThrow(
                        () -> new ElementNotFoundException("Could not found budget with id : " + itemId));
    }


    @Override
    public Budget create(SessionUser me, Budget payload) {
        Objects.requireNonNull(payload);
        payload.setOwnerId(me.getId());
        return budgetRepository.insert(payload);
    }

    @Override
    public void remove(SessionUser me, String itemId) throws ApiException {
        Budget budgetToRemove = retrieve(me, itemId);
        budgetRepository.delete(budgetToRemove);
    }

    @Override
    public Budget update(SessionUser me, String itemId, Budget payload) {
        Objects.requireNonNull(itemId);
        Objects.requireNonNull(payload);
        payload.setOwnerId(me.getId());
        payload.setId(itemId);
        return budgetRepository.save(payload);
    }

    @Override
    public List<Budget> getAll(SessionUser me) {
        return budgetRepository.findByOwnerId(me.getId()).orElse(Collections.emptyList());
    }

    @Override
    public Budget partialUpdate(SessionUser me, String itemId, Budget partialPayload) throws ApiException {
        Objects.requireNonNull(itemId);
        Objects.requireNonNull(partialPayload);

        Budget budgetToUpdate = retrieve(me, itemId);
        BeanUtil.copyBean(partialPayload, budgetToUpdate);
        return budgetRepository.save(budgetToUpdate);
    }
}
