package com.warys.scrooge.core.repository;

import com.warys.scrooge.core.model.budget.Attachment;
import com.warys.scrooge.core.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AttachmentRepository extends MongoRepository<Attachment, String> {

}
