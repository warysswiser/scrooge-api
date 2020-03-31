package com.warys.scrooge.controller.secured;

import com.warys.scrooge.core.model.Inflow;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.service.budget.InflowService;
import com.warys.scrooge.infrastructure.exception.ApiException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/me/inflows")
public final class InflowsController {

    private final InflowService inflowService;

    @GetMapping("")
    public ResponseEntity<List<Inflow>> getAllInflows(@AuthenticationPrincipal final SessionUser me) {
        return new ResponseEntity<>(inflowService.getAll(me), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inflow> getInflow
            (@AuthenticationPrincipal final SessionUser me, @PathVariable String id) throws ApiException {
        return new ResponseEntity<>(inflowService.retrieve(me, id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Inflow> createInflow
            (@AuthenticationPrincipal final SessionUser me, @Valid @RequestBody final Inflow newInflow) {
        return new ResponseEntity<>(inflowService.create(me, newInflow), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inflow> updateInflow
            (@AuthenticationPrincipal final SessionUser me, @PathVariable String id, @Valid @RequestBody final Inflow newInflow) {
        return new ResponseEntity<>(inflowService.update(me, id, newInflow), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Inflow> partialUpdateInflow
            (@AuthenticationPrincipal final SessionUser me, @PathVariable String id, @Valid @RequestBody final Inflow partialNewInflow) throws ApiException {
        Inflow updatedInflow = inflowService.partialUpdate(me, id, partialNewInflow);
        return new ResponseEntity<>(updatedInflow, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Inflow> deleteInflow
            (@AuthenticationPrincipal final SessionUser me, @NotNull @PathVariable String id) throws ApiException {
        inflowService.remove(me, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}