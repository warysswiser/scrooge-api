package com.warys.scrooge.application.rest.secured;

import com.warys.scrooge.domain.model.user.User;
import com.warys.scrooge.domain.service.budget.attachement.CrudAttachmentService;
import com.warys.scrooge.infrastructure.exception.ApiException;
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
@RequestMapping("/me/attachments")
@Api(description = "API for managing operation receipts")
public final class AttachmentsController {

    private final CrudAttachmentService attachmentService;

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<String> postAttachment(
            @AuthenticationPrincipal final User me, @RequestParam("file") @NotNull MultipartFile file) throws ApiException {
        String attachmentId = attachmentService.createAttachment(me, file);
        return new ResponseEntity<>(attachmentId, HttpStatus.CREATED);
    }

}