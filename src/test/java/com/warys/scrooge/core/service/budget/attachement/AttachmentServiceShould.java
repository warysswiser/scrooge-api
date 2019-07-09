package com.warys.scrooge.core.service.budget.attachement;


import com.warys.scrooge.core.model.budget.Attachment;
import com.warys.scrooge.core.model.builder.AttachmentBuilder;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.business.InconsistentElementException;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AttachmentServiceShould {

    private static final String CURRENT_USER_ID = "my_current_user_id";
    private static final Path ATTACHMENTS_DIR_PATH = Paths.get("src/test/resources/attachments");
    private static final Path USER_DIR = ATTACHMENTS_DIR_PATH.resolve(CURRENT_USER_ID);
    private static final String ATTACHMENT_FILE_NAME = "attachment_1.png";

    private CrudAttachmentService tested = new AttachmentService(ATTACHMENTS_DIR_PATH.toFile().getAbsolutePath());
    private SessionUser user = mock(SessionUser.class);

    @BeforeClass
    public static void setUpClass() throws IOException {
        Files.write(Files
                .createDirectories(USER_DIR)
                .resolve(ATTACHMENT_FILE_NAME), new byte[]{}
        );
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        Files.walk(ATTACHMENTS_DIR_PATH)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_attachment_is_given_for_check() throws ApiException {
        tested.check(null);
    }

    @Test(expected = InconsistentElementException.class)
    public void throws_InconsistentElementException_when_empty_attachment_is_given_for_check() throws ApiException {
        tested.check(new Attachment());
    }

    @Test(expected = InconsistentElementException.class)
    public void throws_InconsistentElementException_when_creation_date_is_set_with_invalid_uri_for_check() throws ApiException {
        tested.check(
                new AttachmentBuilder().with(o -> {
                    o.creationDate = now();
                    o.uri = "C:\\invalid\\URI";
                }).build());
    }

    @Test(expected = InconsistentElementException.class)
    public void throws_InconsistentElementException_when_uri_path_is_valid_but_file_name_is_invalid_for_check() throws ApiException {
        tested.check(
                new AttachmentBuilder().with(o -> {
                    o.creationDate = now();
                    o.uri = USER_DIR.resolve("attachment_not_created.png").toFile().getAbsolutePath();
                }).build());
    }

    @Test(expected = TechnicalException.class)
    public void throws_TechnicalException_when_invalid_file_is_set_for_creation() throws ApiException, IOException {
        var file = mock(MultipartFile.class);
        when(user.getId()).thenReturn(CURRENT_USER_ID);
        when(file.getBytes()).thenThrow(IOException.class);
        when(file.getOriginalFilename()).thenReturn("filename.txt");
        when(file.getContentType()).thenReturn("text/plain");

        tested.create(user, file);
    }

    @Test
    public void throws_nothing_when_creation_date_is_set_with_valid_uri_for_check() {
        final Attachment attachment = new AttachmentBuilder().with(o -> {
            o.creationDate = now();
            o.uri = USER_DIR.resolve(ATTACHMENT_FILE_NAME).toFile().getAbsolutePath();
        }).build();

        assertThatCode(() -> tested.check(attachment)).doesNotThrowAnyException();
    }

    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_attachment_is_given_for_creation() throws ApiException {
        tested.create(user, null);
    }

    @Test
    public void create_attachment_when_valid_file_has_been_given_for_creation() throws TechnicalException {
        var file = new MockMultipartFile("created_1", "filename.txt", "text/plain", "hello 1".getBytes(StandardCharsets.UTF_8));
        when(user.getId()).thenReturn(CURRENT_USER_ID);

        final Attachment attachment = tested.create(user, file);

        assertThat(attachment).isNotNull();
        assertThat(attachment.getUri()).isNotNull();
        assertThat(attachment.hasBeenUploadedYet()).isTrue();
    }

    @Test
    public void create_attachment_and_directory_for_the_firs_creation() throws TechnicalException {
        var file = new MockMultipartFile("created_1", "filename.txt", "text/plain", "hello 1".getBytes(StandardCharsets.UTF_8));
        when(user.getId()).thenReturn("an_other_user_id");

        final Attachment attachment = tested.create(user, file);

        assertThat(attachment).isNotNull();
        assertThat(attachment.getUri()).isNotNull();
        assertThat(attachment.hasBeenUploadedYet()).isTrue();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throws_UnsupportedOperationException_on_retrieve() {
        tested.retrieve(user,"anId");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throws_UnsupportedOperationException_on_remove() {
        tested.remove(user,"anId");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throws_UnsupportedOperationException_on_update() {
        tested.update(user,"anId", null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throws_UnsupportedOperationException_on_partialUpdate() {
        tested.partialUpdate(user,"anId", null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void throws_UnsupportedOperationException_on_getAll() {
        tested.getAll(user);
    }
}