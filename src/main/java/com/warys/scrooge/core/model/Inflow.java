package com.warys.scrooge.core.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inflows")
public class Inflow extends Flow {

}
