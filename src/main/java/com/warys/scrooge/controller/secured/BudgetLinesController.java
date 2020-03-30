package com.warys.scrooge.controller.secured;

import com.warys.scrooge.core.model.budget.BudgetLine;
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
@RequestMapping("/me/budgets/{budgetId}/lines")
public final class BudgetLinesController {

    private final BudgetService budgetService;

    @GetMapping("")
    public ResponseEntity<List<BudgetLine>> getAllBudget(@AuthenticationPrincipal final SessionUser me, @PathVariable String budgetId) throws ApiException {
        return new ResponseEntity<>(budgetService.getAllBudgetLines(me, budgetId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetLine> getBudgetLine
            (@AuthenticationPrincipal final SessionUser me, @PathVariable String id, @PathVariable String budgetId) throws ApiException {
        return new ResponseEntity<>(budgetService.getLine(me, budgetId, id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<BudgetLine> createBudgetLine
            (@AuthenticationPrincipal final SessionUser me, @Valid @RequestBody final BudgetLine newLine, @PathVariable String budgetId) throws ApiException {
        return new ResponseEntity<>(budgetService.addLine(me, newLine, budgetId), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetLine> updateBudgetLine
            (@AuthenticationPrincipal final SessionUser me, @PathVariable String id, @Valid @RequestBody final BudgetLine newLine, @PathVariable String budgetId) throws ApiException {
        return new ResponseEntity<>(budgetService.updateLine(me, budgetId, id, newLine), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BudgetLine> partialUpdateBudgetLine
            (@AuthenticationPrincipal final SessionUser me, @PathVariable String id, @Valid @RequestBody final BudgetLine partialNewLine, @PathVariable String budgetId) throws ApiException {
        return new ResponseEntity<>(budgetService.partialUpdateLine(me, budgetId, id, partialNewLine), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BudgetLine> deleteBudgetLine
            (@AuthenticationPrincipal final SessionUser me, @NotNull @PathVariable String id, @PathVariable String budgetId) throws ApiException {
        budgetService.removeLine(me, id, budgetId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}