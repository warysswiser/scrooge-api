package com.warys.scrooge.core.repository;

import com.warys.scrooge.core.model.budget.PlannedItems;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PlannedItemRepository extends MongoRepository<PlannedItems, String> {

    Optional<List<PlannedItems>> findByOwnerId(String user);

    Optional<List<PlannedItems>> findByBudgetId(String user);

    Optional<PlannedItems> findByIdAndOwnerId(String id, String user);
}
