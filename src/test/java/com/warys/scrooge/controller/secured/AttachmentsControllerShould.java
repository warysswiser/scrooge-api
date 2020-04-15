package com.warys.scrooge.controller.secured;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles(value = "test")
public class AttachmentsControllerShould extends SecuredTest {

/*
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
    }*/
}