package com.warys.scrooge.domain.model.ocr;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ReceiptItem {

    private String label;
    private double amount;
}
