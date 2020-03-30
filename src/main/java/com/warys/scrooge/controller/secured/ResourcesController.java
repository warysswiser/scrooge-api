package com.warys.scrooge.controller.secured;

import com.warys.scrooge.core.model.budget.Resource;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.service.budget.ResourceService;
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
@RequestMapping("/me/resources")
public final class ResourcesController {

    private final ResourceService resourceService;

    @GetMapping("")
    public ResponseEntity<List<Resource>> getAllResource(@AuthenticationPrincipal final SessionUser me) {
        return new ResponseEntity<>(resourceService.getAll(me), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResource
            (@AuthenticationPrincipal final SessionUser me, @PathVariable String id) throws ApiException {
        return new ResponseEntity<>(resourceService.retrieve(me, id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Resource> createResource
            (@AuthenticationPrincipal final SessionUser me, @Valid @RequestBody final Resource newResource) {
        return new ResponseEntity<>(resourceService.create(me, newResource), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resource> updateResource
            (@AuthenticationPrincipal final SessionUser me, @PathVariable String id, @Valid @RequestBody final Resource newResource) {
        return new ResponseEntity<>(resourceService.update(me, id, newResource), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Resource> partialUpdateResource
            (@AuthenticationPrincipal final SessionUser me, @PathVariable String id, @Valid @RequestBody final Resource partialNewResource) throws ApiException {
        Resource updatedResource = resourceService.partialUpdate(me, id, partialNewResource);
        return new ResponseEntity<>(updatedResource, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Resource> deleteResource
            (@AuthenticationPrincipal final SessionUser me, @NotNull @PathVariable String id) throws ApiException {
        resourceService.remove(me, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}