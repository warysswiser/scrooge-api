package com.warys.scrooge.infrastructure.adapter.tesseract;

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
        assert source.exists();
        final File image = ImagePreProcessing.execute(source);
        final String rawData = delegate.doOCR(image);
        final Receipt receipt = Receipt.fromRawData(rawData);
        source.delete();
        return receipt;
    }
}
