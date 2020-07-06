package com.warys.scrooge.infrastructure.repository.mongo.entity;

import com.warys.scrooge.domain.model.budget.CashFlow;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inflows")
public class InflowDocument extends CashFlow {

}
