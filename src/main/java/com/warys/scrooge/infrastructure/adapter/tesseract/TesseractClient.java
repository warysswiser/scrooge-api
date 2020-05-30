package com.warys.scrooge.infrastructure.adapter.tesseract;

import com.warys.scrooge.domain.model.ocr.Receipt;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class TesseractClient implements OCRClient {

    private final ITesseract delegate;

    public TesseractClient(ITesseract delegate) {
        this.delegate = delegate;
    }

    @Override
    public Receipt extract(File source) throws TesseractException {
        final File image = ImagePreProcessing.execute(source);
        final String rawData = delegate.doOCR(image);
        return Receipt.fromRawData(rawData);
    }
}
