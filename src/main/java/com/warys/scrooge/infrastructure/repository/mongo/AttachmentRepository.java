package com.warys.scrooge.infrastructure.repository.mongo;

import com.warys.scrooge.infrastructure.repository.mongo.entity.AttachmentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AttachmentRepository extends MongoRepository<AttachmentDocument, String> {

}
