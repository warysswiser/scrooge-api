package com.warys.scrooge.domain.service.budget.attachement;

import com.warys.scrooge.domain.model.ocr.Receipt;
import com.warys.scrooge.domain.model.user.Session;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import com.warys.scrooge.infrastructure.spi.ocr.OCRClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class OcrService {

    private OCRClient ocrClient;

    @Autowired
    public OcrService(OCRClient ocrClient) {
        this.ocrClient = ocrClient;
    }

    public Receipt getReceipt(Session me, MultipartFile file) throws TechnicalException {
        Objects.requireNonNull(file, "Non null MultipartFile must be given");
        try {
            Path filepath = Paths.get("./", file.getOriginalFilename());
            file.transferTo(filepath);
            return ocrClient.extractReceipt(filepath.toAbsolutePath().toFile());
        } catch (IOException e) {
            throw new TechnicalException("could not get bytes", e);
        }
    }

}
