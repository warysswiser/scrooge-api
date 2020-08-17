package com.warys.scrooge.infrastructure.spi.ocr;

import com.warys.scrooge.domain.model.ocr.Receipt;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TesseractClientShould {

    public static final String VALID_FILE_SOURCE_PATH = "src/test/resources/test9.jpg";
    public static final String VALID_FILE_PATH = "test9.jpg";

    @Test
    void extract_data() throws IOException, TechnicalException {
        Files.copy(Path.of(VALID_FILE_SOURCE_PATH), Path.of(VALID_FILE_PATH));

        final TesseractClient client = getTesseractClient();
        final File file = Path.of(VALID_FILE_PATH).toAbsolutePath().toFile();
        final Receipt expectedOcrData = client.extractReceipt(file);

        assertThat(expectedOcrData)
                .isNotNull()
                .hasNoNullFieldsOrProperties();

        assertThat(file).doesNotExist();
    }

    @Test
    void throw_IllegalStateException_input_file_does_not_exists() {
        final TesseractClient client = getTesseractClient();
        final File file = Path.of("src/test/resources/bad.jpg").toAbsolutePath().toFile();

        assertThrows(IllegalStateException.class, () -> client.extractReceipt(file));
    }

    private TesseractClient getTesseractClient() {
        ITesseract tesseract = new Tesseract();
        tesseract.setLanguage("fra");
        tesseract.setDatapath("src/main/resources/tessdata");
        nu.pattern.OpenCV.loadLocally();

        return new TesseractClient(tesseract);
    }
}