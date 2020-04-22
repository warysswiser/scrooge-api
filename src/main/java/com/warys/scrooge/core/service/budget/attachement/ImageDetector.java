package com.warys.scrooge.core.service.budget.attachement;

import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;
import com.warys.scrooge.core.model.ImageTextDetection;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ImageDetector {

    public static List<ImageTextDetection> detectText(InputStream stream) throws Exception {
        return getDetections(stream, Feature.Type.TEXT_DETECTION);
    }

    private static List<ImageTextDetection> getDetections(InputStream stream, Type type) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        List<ImageTextDetection> results = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(stream);

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(type).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                    results.add(new ImageTextDetection(res.getError().getMessage(), "ERROR"));
                    return results;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    results.add(new ImageTextDetection(annotation.getDescription(), annotation.getBoundingPoly().toString()));
                    System.out.printf("Text: %s\n", annotation.getDescription());
                    System.out.printf("Position : %s\n", annotation.getBoundingPoly());
                }
            }
        }
        return results;
    }
}
