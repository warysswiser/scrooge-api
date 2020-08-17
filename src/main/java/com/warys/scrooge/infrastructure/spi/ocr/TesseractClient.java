package com.warys.scrooge.infrastructure.spi.ocr;

import com.warys.scrooge.domain.model.ocr.Receipt;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class TesseractClient implements OCRClient {

    private final ITesseract delegate;

    public TesseractClient(ITesseract delegate) {
        this.delegate = delegate;
    }

    @Override
    public Receipt extractReceipt(File source) throws TechnicalException {
        if (!source.exists()) {
            throw new IllegalStateException("File given for processing must exists");
        }
        final File image = ImagePreProcessing.execute(source);
        String rawData = "";
        try {
            rawData = delegate.doOCR(image);
        } catch (TesseractException e) {
            throw new TechnicalException("Error occurred on calling tesseract API", e);
        } finally {
            delete(source);
            delete(image);
        }

        return Receipt.fromRawData(rawData);
    }

    private void delete(File file) {
        final boolean hasBeenDeleted = file.delete();
        if (!hasBeenDeleted) {
            log.error("can not delete file {}", file.getName());
        } else {
            log.info("file {} well deleted", file.getName());
        }
    }
}
