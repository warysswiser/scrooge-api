package com.warys.scrooge.infrastructure.adapter.tesseract;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.nio.file.Path;

import static com.warys.scrooge.infrastructure.adapter.tesseract.ImagePreProcessor.*;

public class ImagePreProcessing {

    private final static String PREPARED_FILE = "./processed_image.png";

    public static File execute(File source) {
        Mat image = Imgcodecs.imread(source.getAbsolutePath());

        final Mat processedImage = RESIZE
                .andThen(GRAY_SCALE)
                .andThen(GAUSSIAN_BLUR)
                .andThen(ADAPTIVE_THRESHOLD)
                .apply(image);

        Imgcodecs.imwrite(PREPARED_FILE, processedImage);

        return Path.of(PREPARED_FILE).toAbsolutePath().toFile();
    }
}
