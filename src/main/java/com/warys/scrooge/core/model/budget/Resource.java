package com.warys.scrooge.core.model.budget;

import com.warys.scrooge.core.model.GenericModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "resources")
public class Resource extends GenericModel {

    private String ownerId;
    private String label;
    private double projection;
    private double real;

}
