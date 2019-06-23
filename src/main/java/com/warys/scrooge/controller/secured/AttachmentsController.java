package com.warys.scrooge.controller.secured;

import com.warys.scrooge.command.account.UserCommand;
import com.warys.scrooge.core.model.budget.Attachment;
import com.warys.scrooge.core.service.budget.attachement.AttachmentService;
import com.warys.scrooge.infrastructure.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/me/attachments")
public final class AttachmentsController {

    private final AttachmentService attachmentService;

    AttachmentsController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }


    @PostMapping("")
    @ResponseBody
    public ResponseEntity<Attachment> postAttachment(
            @AuthenticationPrincipal final UserCommand me, @NotNull @RequestParam("file") MultipartFile file) throws ApiException {
        Attachment attachment = attachmentService.create(me, file);
        return new ResponseEntity<>(attachment, HttpStatus.CREATED);
    }
}