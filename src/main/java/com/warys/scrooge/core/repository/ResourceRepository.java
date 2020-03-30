package com.warys.scrooge.core.repository;

import com.warys.scrooge.core.model.budget.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ResourceRepository extends MongoRepository<Resource, String> {

    Optional<List<Resource>> findByOwnerId(String user);

    Optional<Resource> findByIdAndOwnerId(String itemId, String id);
}
