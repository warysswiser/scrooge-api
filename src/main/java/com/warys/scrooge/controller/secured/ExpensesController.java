package com.warys.scrooge.controller.secured;

import com.warys.scrooge.core.model.budget.Budget;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.service.budget.BudgetService;
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
@RequestMapping("/me/budgets/lines/expenses")
public final class ExpensesController {

    private final BudgetService budgetService;

    @GetMapping("")
    public ResponseEntity<List<Budget>> getAllBudget(@AuthenticationPrincipal final SessionUser me) {
        return new ResponseEntity<>(budgetService.getAll(me), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Budget> getBudget
            (@AuthenticationPrincipal final SessionUser me, @PathVariable String id) throws ApiException {
        return new ResponseEntity<>(budgetService.retrieve(me, id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Budget> createBudget
            (@AuthenticationPrincipal final SessionUser me, @Valid @RequestBody final Budget newBudget) {
        return new ResponseEntity<>(budgetService.create(me, newBudget), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget
            (@AuthenticationPrincipal final SessionUser me, @PathVariable String id, @Valid @RequestBody final Budget newBudget) {
        return new ResponseEntity<>(budgetService.update(me, id, newBudget), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Budget> partialUpdateBudget
            (@AuthenticationPrincipal final SessionUser me, @PathVariable String id, @Valid @RequestBody final Budget partialNewBudget) throws ApiException {
        Budget updatedBudget = budgetService.partialUpdate(me, id, partialNewBudget);
        return new ResponseEntity<>(updatedBudget, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Budget> deleteBudget
            (@AuthenticationPrincipal final SessionUser me, @NotNull @PathVariable String id) throws ApiException {
        budgetService.remove(me, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}