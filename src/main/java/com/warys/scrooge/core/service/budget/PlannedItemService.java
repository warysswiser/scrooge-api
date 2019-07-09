package com.warys.scrooge.core.service.budget;

import com.warys.scrooge.core.common.util.BeanUtil;
import com.warys.scrooge.core.model.budget.PlannedItems;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.repository.PlannedItemRepository;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.business.ElementNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class PlannedItemService implements ItemService<PlannedItems, PlannedItems> {


    private final PlannedItemRepository plannedItemRepository;

    public PlannedItemService(PlannedItemRepository plannedItemRepository) {
        this.plannedItemRepository = plannedItemRepository;
    }

    @Override
    public PlannedItems retrieve(SessionUser me, String itemId) throws ApiException {
        Objects.requireNonNull(itemId);
        return plannedItemRepository.findByIdAndOwnerId(itemId, me.getId())
                .orElseThrow(
                        () -> new ElementNotFoundException("Could not found budget with id : " + itemId));
    }

    @Override
    public PlannedItems create(SessionUser me, PlannedItems payload) {
        Objects.requireNonNull(payload);
        payload.setOwnerId(me.getId());
        return plannedItemRepository.insert(payload);
    }

    @Override
    public void remove(SessionUser me, String itemId) throws ApiException {
        PlannedItems itemToRemove = retrieve(me, itemId);
        plannedItemRepository.delete(itemToRemove);
    }

    @Override
    public PlannedItems update(SessionUser me, String itemId, PlannedItems payload) {
        Objects.requireNonNull(itemId);
        Objects.requireNonNull(payload);
        payload.setOwnerId(me.getId());
        payload.setId(itemId);
        return plannedItemRepository.save(payload);
    }

    @Override
    public List<PlannedItems> getAll(SessionUser me) {
        return plannedItemRepository.findByOwnerId(me.getId()).orElse(Collections.emptyList());
    }

    @Override
    public PlannedItems partialUpdate(SessionUser me, String itemId, PlannedItems partialItem) throws ApiException {
        Objects.requireNonNull(itemId);
        Objects.requireNonNull(partialItem);
        PlannedItems itemToUpdate = retrieve(me, itemId);
        BeanUtil.copyBean(partialItem, itemToUpdate);
        return plannedItemRepository.save(itemToUpdate);
    }
}
