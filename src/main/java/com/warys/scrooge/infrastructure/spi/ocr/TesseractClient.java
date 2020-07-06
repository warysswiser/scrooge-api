package com.warys.scrooge.infrastructure.spi.ocr;

import com.warys.scrooge.domain.model.ocr.Receipt;
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
    public Receipt extractReceipt(File source) throws TesseractException {
        if (!source.exists()) {
            throw new IllegalStateException("File given for processing must exists");
        }
        final File image = ImagePreProcessing.execute(source);
        final String rawData = delegate.doOCR(image);
        return Receipt.fromRawData(rawData);
    }
}
