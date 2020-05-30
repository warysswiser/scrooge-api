package com.warys.scrooge.infrastructure.adapter.tesseract;

import com.warys.scrooge.domain.model.ocr.Receipt;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class TesseractClient implements OCRClient {

    @Value("${app.ocr.data.path}")
    private String dataPath;
    @Value("${app.ocr.default.language}")
    private String defaultLanguage;

    private final ITesseract delegate;

    public TesseractClient() {
        ITesseract tesseract = new Tesseract();

        tesseract.setDatapath(new File(dataPath).getPath());
        tesseract.setLanguage(defaultLanguage);
        nu.pattern.OpenCV.loadLocally();
        this.delegate = tesseract;
    }

    @Override
    public Receipt extract(File source) throws TesseractException {
        final File image = ImagePreProcessing.execute(source);
        final String rawData = delegate.doOCR(image);
        return Receipt.fromRawData(rawData);
    }
}
