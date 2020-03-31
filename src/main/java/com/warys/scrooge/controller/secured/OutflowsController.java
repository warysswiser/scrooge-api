package com.warys.scrooge.controller.secured;

import com.warys.scrooge.core.model.Outflow;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.service.budget.OutflowService;
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
@RequestMapping("/me/outflows")
public final class OutflowsController {

    private final OutflowService outflowService;

    @GetMapping("")
    public ResponseEntity<List<Outflow>> getAllOutflows(@AuthenticationPrincipal final SessionUser me) {
        return new ResponseEntity<>(outflowService.getAll(me), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Outflow> getOutflow
            (@AuthenticationPrincipal final SessionUser me, @PathVariable String id) throws ApiException {
        return new ResponseEntity<>(outflowService.retrieve(me, id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Outflow> createOutflow
            (@AuthenticationPrincipal final SessionUser me, @Valid @RequestBody final Outflow newOutflow) {
        return new ResponseEntity<>(outflowService.create(me, newOutflow), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Outflow> updateOutflow
            (@AuthenticationPrincipal final SessionUser me, @PathVariable String id, @Valid @RequestBody final Outflow newOutflow) {
        return new ResponseEntity<>(outflowService.update(me, id, newOutflow), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Outflow> partialUpdateOutflow
            (@AuthenticationPrincipal final SessionUser me, @PathVariable String id, @Valid @RequestBody final Outflow partialNewOutflow) throws ApiException {
        Outflow updatedOutflow = outflowService.partialUpdate(me, id, partialNewOutflow);
        return new ResponseEntity<>(updatedOutflow, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Outflow> deleteResource
            (@AuthenticationPrincipal final SessionUser me, @NotNull @PathVariable String id) throws ApiException {
        outflowService.remove(me, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}