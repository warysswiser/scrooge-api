package com.warys.scrooge.infrastructure.spi.ocr;

import com.warys.scrooge.domain.model.ocr.Receipt;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;

import java.io.File;

public interface OCRClient {

    Receipt extractReceipt(File source) throws TechnicalException;
}
