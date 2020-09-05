package com.warys.scrooge.application.rest.secured;

import com.warys.scrooge.domain.model.ocr.Receipt;
import com.warys.scrooge.domain.model.user.User;
import com.warys.scrooge.domain.service.budget.attachement.OcrService;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@RestController
@RequestMapping("/me/ocr")
@Api(description = "API allowing optical character recognition")
public final class OcrController {

    private final OcrService ocrService;

    @PostMapping("receipt")
    @ResponseBody
    public ResponseEntity<Receipt> getReceiptFromImage(
            @AuthenticationPrincipal final User me, @RequestParam("file") @NotNull MultipartFile file) throws TechnicalException {
        Receipt attachmentId = ocrService.getReceipt(me, file);
        return new ResponseEntity<>(attachmentId, HttpStatus.CREATED);
    }

}