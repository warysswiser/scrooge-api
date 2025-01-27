package com.warys.scrooge.application.rest.secured;

import com.warys.scrooge.domain.model.user.Session;
import com.warys.scrooge.domain.service.budget.OutflowService;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.repository.mongo.entity.OutflowDocument;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/me/outflows")
@Api(description = "API for cash outflow operations")
public final class OutflowsController {

    private final OutflowService outflowService;

    @GetMapping("")
    @ApiOperation("Returns all user'entry flow. By default, we return the current month movements.")
    public ResponseEntity<List<OutflowDocument>> getAllOutflows(@AuthenticationPrincipal final Session me,
                                                                @Nullable @RequestParam LocalDate from, @Nullable @RequestParam LocalDate to) {
        return new ResponseEntity<>(outflowService.getPagedData(me, from, to), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OutflowDocument> getOutflow
            (@AuthenticationPrincipal final Session me, @PathVariable String id) throws ApiException {
        return new ResponseEntity<>(outflowService.retrieve(me, id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<OutflowDocument> createOutflow
            (@AuthenticationPrincipal final Session me, @RequestBody @Valid final OutflowDocument newOutflow) {
        return new ResponseEntity<>(outflowService.create(me, newOutflow), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OutflowDocument> updateOutflow
            (@AuthenticationPrincipal final Session me, @PathVariable String id, @RequestBody @Valid final OutflowDocument newOutflow) {
        return new ResponseEntity<>(outflowService.update(me, id, newOutflow), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OutflowDocument> partialUpdateOutflow
            (@AuthenticationPrincipal final Session me, @PathVariable String id, @RequestBody @Valid final OutflowDocument partialNewOutflow) throws ApiException {
        OutflowDocument updatedOutflow = outflowService.partialUpdate(me, id, partialNewOutflow);
        return new ResponseEntity<>(updatedOutflow, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OutflowDocument> deleteResource
            (@AuthenticationPrincipal final Session me, @PathVariable String id) throws ApiException {
        outflowService.remove(me, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}