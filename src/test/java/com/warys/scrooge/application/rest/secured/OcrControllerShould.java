package com.warys.scrooge.application.rest.secured;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Path;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class OcrControllerShould extends SecuredTest {

    @BeforeEach
    void setUp() {
        super.init();
    }

    @Test
    void make_ocr() throws Exception {

        final String validFilePath = "test9.jpg";
        final Path inputFile = Path.of(validFilePath);

        Files.copy(Path.of("src/test/resources/test9.jpg"), inputFile);


        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", validFilePath,
                "text/plain", Files.readAllBytes(inputFile));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/me/ocr/receipt")
                .file(mockMultipartFile)
                .header("Authorization", "Bearer " + VALID_TOKEN);

        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void error_on_ocr_with_invalid_file() throws Exception {


        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "file.jpg",
                "text/plain", "file datas".getBytes());

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/me/ocr/receipt")
                .file(mockMultipartFile)
                .header("Authorization", "Bearer " + VALID_TOKEN);

        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}