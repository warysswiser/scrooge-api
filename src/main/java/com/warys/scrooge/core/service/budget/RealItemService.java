package com.warys.scrooge.core.service.budget;

import com.warys.scrooge.core.common.util.BeanUtil;
import com.warys.scrooge.core.model.budget.RealItems;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.repository.RealItemRepository;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.business.ElementNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class RealItemService implements ItemService<RealItems, RealItems> {


    private final RealItemRepository realItemRepository;

    public RealItemService(RealItemRepository realItemRepository) {
        this.realItemRepository = realItemRepository;
    }

    @Override
    public RealItems retrieve(SessionUser me, String itemId) throws ApiException {
        Objects.requireNonNull(itemId);
        return realItemRepository.findByIdAndOwnerId(itemId, me.getId())
                .orElseThrow(
                        () -> new ElementNotFoundException("Could not found item with id : " + itemId));
    }

    @Override
    public RealItems create(SessionUser me, RealItems payload) {
        Objects.requireNonNull(payload);
        payload.setOwnerId(me.getId());
        return realItemRepository.insert(payload);
    }

    @Override
    public void remove(SessionUser me, String itemId) throws ApiException {
        Objects.requireNonNull(itemId);
        RealItems itemToRemove = retrieve(me, itemId);
        realItemRepository.delete(itemToRemove);
    }

    @Override
    public RealItems update(SessionUser me, String itemId, RealItems item) {
        Objects.requireNonNull(itemId);
        Objects.requireNonNull(item);
        item.setId(itemId);
        item.setOwnerId(me.getId());
        return realItemRepository.save(item);
    }

    @Override
    public List<RealItems> getAll(SessionUser me) {
        return realItemRepository.findByOwnerId(me.getId()).orElse(Collections.emptyList());
    }

    @Override
    public RealItems partialUpdate(SessionUser me, String itemId, RealItems partialItem) throws ApiException {
        Objects.requireNonNull(itemId);
        Objects.requireNonNull(partialItem);
        RealItems itemToUpdate = retrieve(me, itemId);
        BeanUtil.copyBean(partialItem, itemToUpdate);
        return realItemRepository.save(itemToUpdate);
    }
}
