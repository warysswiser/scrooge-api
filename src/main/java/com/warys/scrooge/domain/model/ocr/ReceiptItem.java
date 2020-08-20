package com.warys.scrooge.domain.model.ocr;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class ReceiptItem implements Serializable {

    private String label;
    private double amount;
}
