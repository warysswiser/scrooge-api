package com.warys.scrooge.core.service.budget;

import com.warys.scrooge.core.common.util.BeanUtil;
import com.warys.scrooge.core.model.Outflow;
import com.warys.scrooge.core.model.Outflow;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.repository.OutflowRepository;
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
public class OutflowService implements MyCrudService<Outflow, Outflow> {

    private final OutflowRepository outflowRepository;

    @Override
    public Outflow retrieve(SessionUser me, String outflowId) throws ApiException {
        requireNonNull(outflowId);
        return outflowRepository.findByIdAndOwnerId(outflowId, me.getId())
                .orElseThrow(
                        () -> new ElementNotFoundException("Could not found outflow with id : " + outflowId));
    }

    @Override
    public Outflow create(SessionUser me, Outflow payload) {
        requireNonNull(payload);
        payload.setOwnerId(me.getId());
        return outflowRepository.insert(payload);
    }

    @Override
    public void remove(SessionUser me, String outflowId) throws ApiException {
        Outflow budgetToRemove = retrieve(me, outflowId);
        outflowRepository.delete(budgetToRemove);
    }

    @Override
    public Outflow update(SessionUser me, String outflowId, Outflow payload) {
        requireNonNull(outflowId);
        requireNonNull(payload);
        payload.setOwnerId(me.getId());
        payload.setId(outflowId);
        return outflowRepository.save(payload);
    }

    @Override
    public List<Outflow> getAll(SessionUser me) {
        return outflowRepository.findByOwnerId(me.getId()).orElse(Collections.emptyList());
    }

    @Override
    public Outflow partialUpdate(SessionUser me, String outflowId, Outflow partialPayload) throws ApiException {
        requireNonNull(outflowId);
        requireNonNull(partialPayload);

        Outflow budgetToUpdate = retrieve(me, outflowId);
        BeanUtil.copyBean(partialPayload, budgetToUpdate);
        return outflowRepository.save(budgetToUpdate);
    }
}
