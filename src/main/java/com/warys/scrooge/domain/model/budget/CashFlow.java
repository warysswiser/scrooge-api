package com.warys.scrooge.domain.model.budget;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.warys.scrooge.domain.model.GenericModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class CashFlow extends GenericModel {

    private String ownerId;
    private String category;
    private String label;
    private double amount;
    private String frequency;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate executionDate;

}
