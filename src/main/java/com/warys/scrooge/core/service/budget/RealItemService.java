package com.warys.scrooge.core.service.budget;

import com.warys.scrooge.core.common.util.BeanUtil;
import com.warys.scrooge.core.model.budget.RealItems;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.repository.RealItemRepository;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.business.ElementNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class RealItemService implements ItemService<RealItems, RealItems> {


    private final RealItemRepository realItemRepository;

    public RealItemService(RealItemRepository realItemRepository) {
        this.realItemRepository = realItemRepository;
    }

    @Override
    public RealItems retrieve(SessionUser me, String itemId) throws ApiException {
        return realItemRepository.findByIdAndOwnerId(itemId, me.getId())
                .orElseThrow(
                        () -> new ElementNotFoundException("Could not found budget with id : " + itemId));
    }

    @Override
    public RealItems create(SessionUser me, RealItems payload) {
        var realItem = new RealItems();
        BeanUtil.copyBean(payload, realItem);
        realItem.setCreationDate(LocalDateTime.now());
        return realItemRepository.insert(realItem);
    }

    @Override
    public void remove(SessionUser me, String itemId) throws ApiException {
        RealItems itemToRemove = retrieve(me, itemId);
        itemToRemove.setDeletionDate(LocalDateTime.now());
        realItemRepository.save(itemToRemove);
    }

    @Override
    public RealItems update(SessionUser me, String itemId, RealItems item) throws ApiException {
        return realItemRepository.save(partialUpdate(me, itemId, item));
    }

    @Override
    public List<RealItems> getAll(SessionUser me) {
        return realItemRepository.findByOwnerId(me.getId()).orElse(Collections.emptyList());
    }

    @Override
    public RealItems partialUpdate(SessionUser me, String itemId, RealItems partialItem) throws ApiException {
        RealItems itemToUpdate = retrieve(me, itemId);
        BeanUtil.copyBean(partialItem, itemToUpdate);
        return realItemRepository.save(itemToUpdate);
    }
}
