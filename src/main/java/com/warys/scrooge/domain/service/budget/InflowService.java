package com.warys.scrooge.domain.service.budget;

import com.warys.scrooge.domain.exception.ElementNotFoundException;
import com.warys.scrooge.domain.model.user.Session;
import com.warys.scrooge.domain.service.MyCrudService;
import com.warys.scrooge.domain.service.Pageable;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.repository.mongo.InflowRepository;
import com.warys.scrooge.infrastructure.repository.mongo.entity.InflowDocument;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.warys.scrooge.domain.common.util.DateAdjuster.adjustedFrom;
import static com.warys.scrooge.domain.common.util.DateAdjuster.adjustedTo;
import static com.warys.scrooge.domain.common.util.Patcher.patch;
import static java.util.Objects.requireNonNull;

@AllArgsConstructor
@Service
public class InflowService implements MyCrudService<InflowDocument, InflowDocument>, Pageable<LocalDate, InflowDocument> {

    private final InflowRepository inflowRepository;

    @Override
    public InflowDocument retrieve(Session me, String budgetId) throws ApiException {
        requireNonNull(budgetId);
        return inflowRepository.findByIdAndOwnerId(budgetId, me.getId())
                .orElseThrow(
                        () -> new ElementNotFoundException("Could not found resource with id : " + budgetId));
    }

    @Override
    public InflowDocument create(Session me, InflowDocument payload) {
        requireNonNull(payload);
        payload.setOwnerId(me.getId());
        return inflowRepository.insert(payload);
    }

    @Override
    public void remove(Session me, String inflowId) throws ApiException {
        InflowDocument budgetToRemove = retrieve(me, inflowId);
        inflowRepository.delete(budgetToRemove);
    }

    @Override
    public InflowDocument update(Session me, String inflowId, InflowDocument payload) {
        requireNonNull(inflowId);
        requireNonNull(payload);
        payload.setOwnerId(me.getId());
        payload.setId(inflowId);
        return inflowRepository.save(payload);
    }

    @Override
    public List<InflowDocument> getAll(Session me) {
        return inflowRepository.findByOwnerId(me.getId()).orElse(Collections.emptyList());
    }

    @Override
    public InflowDocument partialUpdate(Session me, String inflowId, InflowDocument partialPayload) throws ApiException {
        requireNonNull(inflowId);
        requireNonNull(partialPayload);

        InflowDocument budgetToUpdate = retrieve(me, inflowId);
        patch(partialPayload, budgetToUpdate);
        return inflowRepository.save(budgetToUpdate);
    }

    @Override
    public List<InflowDocument> getPagedData(Session me, LocalDate from, LocalDate to) {
        final LocalDate adjustedFrom = adjustedFrom(from);
        return inflowRepository
                .findByOwnerIdAndExecutionDateBetween(me.getId(), adjustedFrom, adjustedTo(adjustedFrom, to))
                .orElse(Collections.emptyList());
    }
}
