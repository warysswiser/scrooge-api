package com.warys.scrooge.core.repository;

import com.warys.scrooge.core.model.Outflow;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OutflowRepository extends MongoRepository<Outflow, String> {

    Optional<List<Outflow>> findByOwnerId(String user);

    Optional<Outflow> findByIdAndOwnerId(String itemId, String id);
}
