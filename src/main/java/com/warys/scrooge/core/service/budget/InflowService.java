package com.warys.scrooge.core.service.budget;

import com.warys.scrooge.core.common.util.BeanUtil;
import com.warys.scrooge.core.model.Inflow;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.repository.InflowRepository;
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
public class InflowService implements MyCrudService<Inflow, Inflow> {

    private final InflowRepository inflowRepository;

    @Override
    public Inflow retrieve(SessionUser me, String budgetId) throws ApiException {
        requireNonNull(budgetId);
        return inflowRepository.findByIdAndOwnerId(budgetId, me.getId())
                .orElseThrow(
                        () -> new ElementNotFoundException("Could not found resource with id : " + budgetId));
    }

    @Override
    public Inflow create(SessionUser me, Inflow payload) {
        requireNonNull(payload);
        payload.setOwnerId(me.getId());
        return inflowRepository.insert(payload);
    }

    @Override
    public void remove(SessionUser me, String inflowId) throws ApiException {
        Inflow budgetToRemove = retrieve(me, inflowId);
        inflowRepository.delete(budgetToRemove);
    }

    @Override
    public Inflow update(SessionUser me, String inflowId, Inflow payload) {
        requireNonNull(inflowId);
        requireNonNull(payload);
        payload.setOwnerId(me.getId());
        payload.setId(inflowId);
        return inflowRepository.save(payload);
    }

    @Override
    public List<Inflow> getAll(SessionUser me) {
        return inflowRepository.findByOwnerId(me.getId()).orElse(Collections.emptyList());
    }

    @Override
    public Inflow partialUpdate(SessionUser me, String inflowId, Inflow partialPayload) throws ApiException {
        requireNonNull(inflowId);
        requireNonNull(partialPayload);

        Inflow budgetToUpdate = retrieve(me, inflowId);
        BeanUtil.copyBean(partialPayload, budgetToUpdate);
        return inflowRepository.save(budgetToUpdate);
    }
}
