package com.warys.scrooge.infrastructure.repository.mongo;

import com.warys.scrooge.infrastructure.repository.mongo.entity.InflowDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface InflowRepository extends MongoRepository<InflowDocument, String> {

    Optional<List<InflowDocument>> findByOwnerId(String user);

    Optional<InflowDocument> findByIdAndOwnerId(String itemId, String id);
}
