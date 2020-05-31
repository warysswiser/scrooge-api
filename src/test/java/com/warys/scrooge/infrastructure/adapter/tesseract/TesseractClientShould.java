package com.warys.scrooge.infrastructure.adapter.tesseract;

public class TesseractClientShould {
/*
    @Test
    void extract_data() throws TesseractException {
        final TesseractClient client = getTesseractClient();
        final File file = Path.of("src/test/resources/test9.jpg").toAbsolutePath().toFile();
        final Receipt expectedOcrData = client.extract(file);
        assertThat(expectedOcrData).isNotNull();
        assertThat(expectedOcrData).hasNoNullFieldsOrProperties();
        assertThat(Path.of("./processed_image.png").toAbsolutePath().toFile().delete()).isTrue();
    }


    @Test
    void throw_Error_when_tesseract_is_not_well_initializes() {
        ITesseract tesseract = new Tesseract();
        assertThrows(Error.class, () -> {
            final TesseractClient client = new TesseractClient(tesseract);
            final File file = Path.of("src/test/resources/test9.jpg").toAbsolutePath().toFile();
            client.extract(file);
        });
    }

    @Test
    void throw_CvException_when_input_file_does_not_exists() {
        assertThrows(CvException.class, () -> {
            final TesseractClient client = getTesseractClient();
            final File file = Path.of("src/test/resources/bad.jpg").toAbsolutePath().toFile();
            client.extract(file);
        });
    }

    private TesseractClient getTesseractClient() {
        ITesseract tesseract = new Tesseract();
        tesseract.setLanguage("fra");
        tesseract.setDatapath("src/main/resources/tessdata");
        nu.pattern.OpenCV.loadLocally();

        return new TesseractClient(tesseract);
    }*/
}