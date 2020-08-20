package com.warys.scrooge.infrastructure.spi.ocr;

import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.nio.file.Path;

import static com.warys.scrooge.infrastructure.spi.ocr.ImagePreProcessor.*;

public class ImagePreProcessing {

    private ImagePreProcessing() {
    }

    private static final String PREPARED_FILE = "./processed_image.png";

    public static File execute(File source) throws TechnicalException {
        Mat image = Imgcodecs.imread(source.getAbsolutePath());

        try {
            final Mat processedImage = RESIZE
                    .andThen(GRAY_SCALE)
                    .andThen(GAUSSIAN_BLUR)
                    .andThen(ADAPTIVE_THRESHOLD)
                    .apply(image);

            Imgcodecs.imwrite(PREPARED_FILE, processedImage);

            return Path.of(PREPARED_FILE).toAbsolutePath().toFile();
        } catch (RuntimeException e) {
            source.deleteOnExit();
            final String message = "Exception on reading file " + source.getName();
            throw new TechnicalException(message, e);
        }
    }
}
