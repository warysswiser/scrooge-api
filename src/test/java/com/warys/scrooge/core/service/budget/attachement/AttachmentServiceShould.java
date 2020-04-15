package com.warys.scrooge.core.service.budget.attachement;


import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.repository.AttachmentRepository;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;

public class AttachmentServiceShould {

    private static final String CURRENT_USER_ID = "my_current_user_id";
    private static final Path ATTACHMENTS_DIR_PATH = Paths.get("src/test/resources/attachments");
    private static final Path USER_DIR = ATTACHMENTS_DIR_PATH.resolve(CURRENT_USER_ID);
    private static final String ATTACHMENT_FILE_NAME = "attachment_1.png";

    private AttachmentRepository attachmentRepository = mock(AttachmentRepository.class);
    private CrudAttachmentService tested = new AttachmentService(attachmentRepository);
    private SessionUser user = mock(SessionUser.class);


}