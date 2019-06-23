package com.warys.scrooge.core.service.budget.attachement;


import com.warys.scrooge.core.model.budget.Attachment;
import com.warys.scrooge.core.model.builder.AttachmentBuilder;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.business.InconsistentElementException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AttachmentServiceShould {

    private static final String CURRENT_USER_ID = "5ca62ef75067b67f5832ad02";
    private static final String INVALID_USER_ID = "BAAAAD5ca62ef75067b67f5832ad02";
    private static final Path ATTACHMENTS_DIR_PATH = Paths.get("src/test/resources/attachments");
    private static final Path USER_DIR = ATTACHMENTS_DIR_PATH.resolve(CURRENT_USER_ID);
    private AttachmentService tested = new AttachmentService();
    private SessionUser user = mock(SessionUser.class);

    @BeforeClass
    public static void setUpClass() throws IOException {
        Files.write(Files
                .createDirectories(USER_DIR)
                .resolve("attachment_1.png"), new byte[]{}
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
                    o.uri = "C:\\attachments\\5ca62ef75067b67f5832ad02\\MicrosoftTeamBAAAADs-image (4).png";
                }).build());
    }

    @Test
    public void throws_nothing_when_creation_date_is_set_with_valid_uri_for_check() {
        final Attachment attachment = new AttachmentBuilder().with(o -> {
            o.creationDate = now();
            o.uri = USER_DIR.resolve("attachment_1.png").toFile().getAbsolutePath();
        }).build();

        assertThatCode(() -> tested.check(attachment)).doesNotThrowAnyException();
    }

    @Test(expected = NullPointerException.class)
    public void throws_NullPointerException_when_null_attachment_is_given_for_creation() throws ApiException {
        tested.create(user, null);
    }

    @Test
    public void create_attachment_when_valid_file_has_been_given_for_creation() {
        when(user.getId()).thenReturn(CURRENT_USER_ID);

    }
}