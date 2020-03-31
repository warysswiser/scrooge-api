package com.warys.scrooge.core.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "outflows")
public class Outflow extends Flow {

}
