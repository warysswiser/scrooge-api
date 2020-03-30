package com.warys.scrooge.core.service.budget;

import com.warys.scrooge.core.common.util.BeanUtil;
import com.warys.scrooge.core.model.budget.Resource;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.repository.ResourceRepository;
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
public class ResourceService implements MyCrudService<Resource, Resource> {

    private final ResourceRepository resourceRepository;

    @Override
    public Resource retrieve(SessionUser me, String budgetId) throws ApiException {
        requireNonNull(budgetId);
        return resourceRepository.findByIdAndOwnerId(budgetId, me.getId())
                .orElseThrow(
                        () -> new ElementNotFoundException("Could not found resource with id : " + budgetId));
    }

    @Override
    public Resource create(SessionUser me, Resource payload) {
        requireNonNull(payload);
        payload.setOwnerId(me.getId());
        return resourceRepository.insert(payload);
    }

    @Override
    public void remove(SessionUser me, String resourceId) throws ApiException {
        Resource budgetToRemove = retrieve(me, resourceId);
        resourceRepository.delete(budgetToRemove);
    }

    @Override
    public Resource update(SessionUser me, String resourceId, Resource payload) {
        requireNonNull(resourceId);
        requireNonNull(payload);
        payload.setOwnerId(me.getId());
        payload.setId(resourceId);
        return resourceRepository.save(payload);
    }

    @Override
    public List<Resource> getAll(SessionUser me) {
        return resourceRepository.findByOwnerId(me.getId()).orElse(Collections.emptyList());
    }

    @Override
    public Resource partialUpdate(SessionUser me, String resourceId, Resource partialPayload) throws ApiException {
        requireNonNull(resourceId);
        requireNonNull(partialPayload);

        Resource budgetToUpdate = retrieve(me, resourceId);
        BeanUtil.copyBean(partialPayload, budgetToUpdate);
        return resourceRepository.save(budgetToUpdate);
    }
}
