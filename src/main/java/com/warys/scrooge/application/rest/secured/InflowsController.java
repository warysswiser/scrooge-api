package com.warys.scrooge.application.rest.secured;

import com.warys.scrooge.domain.model.user.Session;
import com.warys.scrooge.domain.service.budget.InflowService;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.repository.mongo.entity.InflowDocument;
import io.swagger.annotations.Api;
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
@RequestMapping("/me/inflows")
@Api(description = "API for cash entry operations")
public final class InflowsController {

    private final InflowService inflowService;

    @GetMapping("")
    public ResponseEntity<List<InflowDocument>> getAllInflows(@AuthenticationPrincipal final Session me,
                                                              @Nullable @RequestParam LocalDate from, @Nullable @RequestParam LocalDate to) {
        return new ResponseEntity<>(inflowService.getPagedData(me, from, to), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InflowDocument> getInflow
            (@AuthenticationPrincipal final Session me, @PathVariable String id) throws ApiException {
        return new ResponseEntity<>(inflowService.retrieve(me, id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<InflowDocument> createInflow
            (@AuthenticationPrincipal final Session me, @RequestBody @Valid final InflowDocument newInflow) {
        return new ResponseEntity<>(inflowService.create(me, newInflow), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InflowDocument> updateInflow
            (@AuthenticationPrincipal final Session me, @PathVariable String id, @RequestBody @Valid final InflowDocument newInflow) {
        return new ResponseEntity<>(inflowService.update(me, id, newInflow), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InflowDocument> partialUpdateInflow
            (@AuthenticationPrincipal final Session me, @PathVariable String id, @RequestBody @Valid final InflowDocument partialNewInflow) throws ApiException {
        InflowDocument updatedInflow = inflowService.partialUpdate(me, id, partialNewInflow);
        return new ResponseEntity<>(updatedInflow, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<InflowDocument> deleteInflow
            (@AuthenticationPrincipal final Session me, @PathVariable String id) throws ApiException {
        inflowService.remove(me, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}