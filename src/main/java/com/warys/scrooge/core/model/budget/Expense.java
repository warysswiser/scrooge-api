package com.warys.scrooge.core.model.budget;

import com.warys.scrooge.core.model.GenericModel;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "expenses")
public class Expense extends GenericModel {

    private String ownerId;
    private float amount;
    private String name;
    private Attachment attachment;
}
