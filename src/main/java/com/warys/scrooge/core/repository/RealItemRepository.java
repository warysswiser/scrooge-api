package com.warys.scrooge.core.repository;

import com.warys.scrooge.core.model.budget.RealItems;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RealItemRepository extends MongoRepository<RealItems, String> {

    Optional<List<RealItems>> findByOwnerId(String user);

    Optional<List<RealItems>> findByBudgetId(String user);

    Optional<RealItems> findByIdAndOwnerId(String id, String user);
}
