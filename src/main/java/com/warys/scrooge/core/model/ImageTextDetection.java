package com.warys.scrooge.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageTextDetection {

    private String description;
    private String boundingPoly;
}
