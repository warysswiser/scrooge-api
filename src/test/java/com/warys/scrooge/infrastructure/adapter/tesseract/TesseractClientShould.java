package com.warys.scrooge.infrastructure.adapter.tesseract;

import com.warys.scrooge.domain.model.ocr.Receipt;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.junit.jupiter.api.Test;
import org.opencv.core.CvException;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TesseractClientShould {

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
    void should_throw_Error_when_tesseract_is_not_well_initializes() {
        ITesseract tesseract = new Tesseract();
        assertThrows(Error.class, () -> {
            final TesseractClient client = new TesseractClient(tesseract);
            //ReflectionTestUtils.setField(client, "tesseract", tesseract);
            final File file = Path.of("src/test/resources/test9.jpg").toAbsolutePath().toFile();
            client.extract(file);
        });
    }

    @Test
    void should_throw_CvException_when_input_file_does_not_exists() {
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


        final TesseractClient client = new TesseractClient(tesseract);
        //ReflectionTestUtils.setField(client, "tesseract", tesseract);
        return client;
    }
}