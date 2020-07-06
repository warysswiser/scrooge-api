package com.warys.scrooge.infrastructure.spi.ocr;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Function;

public enum ImagePreProcessor implements Function<Mat, Mat> {

    RESIZE {
        @Override
        public Mat apply(Mat source) {
            PROCESSING_LOG.accept(this.name(), source);
            Mat resized = new Mat();
            Imgproc.resize(source, resized, new Size(source.width() * 0.72, source.height() * 0.72));
            return resized;
        }
    },
    GRAY_SCALE {
        @Override
        public Mat apply(Mat source) {
            PROCESSING_LOG.accept(this.name(), source);
            Mat imgGray = new Mat();
            Imgproc.cvtColor(source, imgGray, Imgproc.COLOR_BGR2GRAY);
            return imgGray;
        }
    },
    GAUSSIAN_BLUR {
        @Override
        public Mat apply(Mat source) {
            PROCESSING_LOG.accept(this.name(), source);
            Mat imgGaussianBlur = new Mat();
            Imgproc.GaussianBlur(source, imgGaussianBlur, new Size(5, 5), 0);
            return imgGaussianBlur;
        }
    },
    ADAPTIVE_THRESHOLD {
        @Override
        public Mat apply(Mat source) {
            PROCESSING_LOG.accept(this.name(), source);
            Mat imgAdaptiveThreshold = new Mat();
            Imgproc.adaptiveThreshold(source, imgAdaptiveThreshold, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 115, 15);
            return imgAdaptiveThreshold;
        }
    };

    private static final Logger logger = LoggerFactory.getLogger(ImagePreProcessor.class);

    private static final BiConsumer<String, Mat> PROCESSING_LOG = (name, source) -> logger.debug("Applying {} processing on {}", name, source);
}
