package com.warys.scrooge.application.rest.secured;

import com.warys.scrooge.infrastructure.repository.mongo.AttachmentRepository;
import com.warys.scrooge.infrastructure.repository.mongo.entity.AttachmentDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class AttachmentsControllerShould extends SecuredTest {

    @MockBean
    private AttachmentRepository attachmentRepository;

    @BeforeEach
    void setUp() {
        super.init();
        when(attachmentRepository.insert(any(AttachmentDocument.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void post_attachments() throws Exception {

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "filename.txt",
                "text/plain", "test data".getBytes());

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/me/attachments")
                .file(mockMultipartFile)
                .header("Authorization", "Bearer " + VALID_TOKEN);

        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}