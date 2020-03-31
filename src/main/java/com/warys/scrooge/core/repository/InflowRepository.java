package com.warys.scrooge.core.repository;

import com.warys.scrooge.core.model.Inflow;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface InflowRepository extends MongoRepository<Inflow, String> {

    Optional<List<Inflow>> findByOwnerId(String user);

    Optional<Inflow> findByIdAndOwnerId(String itemId, String id);
}
