package com.warys.scrooge.core.model.budget;

import com.warys.scrooge.core.model.GenericModel;

class Expense extends GenericModel {

    private String ownerId;
    private float amount;
    private String name;
    private Attachment attachment;
}
