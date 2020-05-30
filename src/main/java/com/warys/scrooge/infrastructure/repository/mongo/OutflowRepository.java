package com.warys.scrooge.infrastructure.repository.mongo;

import com.warys.scrooge.infrastructure.repository.mongo.entity.OutflowDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OutflowRepository extends MongoRepository<OutflowDocument, String> {

    Optional<List<OutflowDocument>> findByOwnerId(String user);

    Optional<OutflowDocument> findByIdAndOwnerId(String itemId, String id);
}
