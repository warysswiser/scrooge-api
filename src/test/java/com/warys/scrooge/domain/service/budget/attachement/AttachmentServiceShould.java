package com.warys.scrooge.domain.service.budget.attachement;


import com.warys.scrooge.domain.exception.InconsistentElementException;
import com.warys.scrooge.domain.model.user.Session;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import com.warys.scrooge.infrastructure.repository.mongo.AttachmentRepository;
import com.warys.scrooge.infrastructure.repository.mongo.entity.AttachmentDocument;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AttachmentServiceShould {

    private static final String CURRENT_USER_ID = "my_current_user_id";
    private static final Path ATTACHMENTS_DIR_PATH = Paths.get("src/test/resources/attachments");
    private static final Path USER_DIR = ATTACHMENTS_DIR_PATH.resolve(CURRENT_USER_ID);
    private static final String ATTACHMENT_FILE_NAME = "attachment_1.png";

    private AttachmentRepository attachmentRepository = mock(AttachmentRepository.class);
    private CrudAttachmentService tested = new AttachmentService(attachmentRepository);
    private Session user = mock(Session.class);

    @Test
    void throws_UnsupportedOperationException_when_call_on_retrieve() {
        assertThrows(UnsupportedOperationException.class, () -> tested.retrieve(user, null));
    }

    @Test
    void throws_UnsupportedOperationException_when_call_on_create() {
        assertThrows(UnsupportedOperationException.class, () -> tested.create(user, null));
    }

    @Test
    void throws_UnsupportedOperationException_when_call_on_remove() {
        assertThrows(UnsupportedOperationException.class, () -> tested.remove(user, null));
    }

    @Test
    void throws_UnsupportedOperationException_when_call_on_update() {
        assertThrows(UnsupportedOperationException.class, () -> tested.update(user, "", null));
    }

    @Test
    void throws_UnsupportedOperationException_when_call_on_partialUpdate() {
        assertThrows(UnsupportedOperationException.class, () -> tested.partialUpdate(user, "", null));
        assertThrows(UnsupportedOperationException.class, () -> tested.partialUpdate(user, "", null));
    }

    @Test
    void throw_NullPointerException_when_null_attachment_is_send_for_check() {
        assertThrows(NullPointerException.class, () -> tested.check(null));
    }

    @Test
    void throw_NullPointerException_when_null_attachment_is_send_for_creation() {
        assertThrows(NullPointerException.class, () -> tested.createAttachment(user, null));
    }

    @Test
    void throw_InconsistentElementException_when_attachment_is_not_updated_yet() {
        AttachmentDocument attachment = mock(AttachmentDocument.class);
        when(attachment.hasBeenUploadedYet()).thenReturn(false);
        assertThrows(InconsistentElementException.class, () -> tested.check(attachment));
    }

    @Test
    void validate_when_attachment_is_already_updated() {
        AttachmentDocument attachment = mock(AttachmentDocument.class);
        when(attachment.hasBeenUploadedYet()).thenReturn(true);
        assertDoesNotThrow(() -> tested.check(attachment));
    }

    @Test
    void create_attachment() throws TechnicalException {

        MockMultipartFile file = new MockMultipartFile(
                ATTACHMENT_FILE_NAME,
                ATTACHMENT_FILE_NAME,
                MediaType.IMAGE_JPEG_VALUE,
                "<<pic data>>".getBytes(StandardCharsets.UTF_8));
        AttachmentDocument expectedAttachment = new AttachmentDocument();
        expectedAttachment.setId("id");
        when(attachmentRepository.insert(any(AttachmentDocument.class))).thenReturn(expectedAttachment);

        final String attachmentId = tested.createAttachment(user, file);

        assertThat(attachmentId).isEqualTo(expectedAttachment.getId());
    }
}