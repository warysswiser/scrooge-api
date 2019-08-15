package com.warys.scrooge.controller.secured;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
public class AttachmentsControllerShould extends SecuredTest {

    @Value("${app.attachments.directory}")
    private String uploadedFolder;

    @Before
    public void setUp() {
        super.init();
    }

    @Test
    public void post_attachments() throws Exception {
        String fileName = "filename.txt";
        final Path filePath = Paths.get(uploadedFolder + "/" + USER_ID + "/" + fileName);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", fileName,
                "text/plain", "test data".getBytes());

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/me/attachments")
                .file(mockMultipartFile)
                .header("Authorization", "Bearer " + VALID_TOKEN);

        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        assertThat(Files.exists(filePath)).isTrue();
        Files.delete(filePath);
    }
}