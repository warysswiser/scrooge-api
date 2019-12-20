package com.warys.scrooge.core.repository;

import com.warys.scrooge.core.model.budget.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends MongoRepository<Expense, String> {

    Optional<List<Expense>> findByOwnerId(String user);

    Optional<Expense> findByIdAndOwnerId(String itemId, String id);
}
