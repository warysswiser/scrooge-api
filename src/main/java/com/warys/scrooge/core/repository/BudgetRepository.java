package com.warys.scrooge.core.repository;

import com.warys.scrooge.core.model.budget.Budget;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends MongoRepository<Budget, String> {

    Optional<List<Budget>> findByOwnerId(String user);

    Optional<Budget> findByIdAndOwnerId(String budgetId, String userId);
}
