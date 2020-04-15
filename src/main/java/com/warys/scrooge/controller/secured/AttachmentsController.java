package com.warys.scrooge.controller.secured;

import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.core.model.ImageTextDetection;
import com.warys.scrooge.core.service.budget.attachement.CrudAttachmentService;
import com.warys.scrooge.infrastructure.exception.ApiException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/me/attachments")
public final class AttachmentsController {

    private final CrudAttachmentService attachmentService;

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<String> postAttachment(
            @AuthenticationPrincipal final UserCommand me, @NotNull @RequestParam("file") MultipartFile file) throws ApiException {
        String attachmentId = attachmentService.createAttachment(me, file);
        return new ResponseEntity<>(attachmentId, HttpStatus.CREATED);
    }

    @PostMapping("detect")
    @ResponseBody
    public ResponseEntity<List<ImageTextDetection>> postDetectText(
            @AuthenticationPrincipal final UserCommand me, @NotNull @RequestParam("file") MultipartFile file) throws ApiException {
        List<ImageTextDetection> response = attachmentService.detectText(me, file);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}