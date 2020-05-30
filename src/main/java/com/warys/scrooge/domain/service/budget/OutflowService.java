package com.warys.scrooge.domain.service.budget;

import com.warys.scrooge.infrastructure.repository.mongo.entity.OutflowDocument;
import com.warys.scrooge.domain.model.user.Session;
import com.warys.scrooge.infrastructure.repository.mongo.OutflowRepository;
import com.warys.scrooge.domain.service.MyCrudService;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.domain.exception.ElementNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.warys.scrooge.domain.common.util.Patcher.patch;
import static java.util.Objects.requireNonNull;

@AllArgsConstructor
@Service
public class OutflowService implements MyCrudService<OutflowDocument, OutflowDocument> {

    private final OutflowRepository outflowRepository;

    @Override
    public OutflowDocument retrieve(Session me, String outflowId) throws ApiException {
        requireNonNull(outflowId);
        return outflowRepository.findByIdAndOwnerId(outflowId, me.getId())
                .orElseThrow(
                        () -> new ElementNotFoundException("Could not found outflow with id : " + outflowId));
    }

    @Override
    public OutflowDocument create(Session me, OutflowDocument payload) {
        requireNonNull(payload);
        payload.setOwnerId(me.getId());
        return outflowRepository.insert(payload);
    }

    @Override
    public void remove(Session me, String outflowId) throws ApiException {
        OutflowDocument budgetToRemove = retrieve(me, outflowId);
        outflowRepository.delete(budgetToRemove);
    }

    @Override
    public OutflowDocument update(Session me, String outflowId, OutflowDocument payload) {
        requireNonNull(outflowId);
        requireNonNull(payload);
        payload.setOwnerId(me.getId());
        payload.setId(outflowId);
        return outflowRepository.save(payload);
    }

    @Override
    public List<OutflowDocument> getAll(Session me) {
        return outflowRepository.findByOwnerId(me.getId()).orElse(Collections.emptyList());
    }

    @Override
    public OutflowDocument partialUpdate(Session me, String outflowId, OutflowDocument partialPayload) throws ApiException {
        requireNonNull(outflowId);
        requireNonNull(partialPayload);

        OutflowDocument budgetToUpdate = retrieve(me, outflowId);
        patch(partialPayload, budgetToUpdate);
        return outflowRepository.save(budgetToUpdate);
    }
}
