package com.warys.scrooge.infrastructure.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component
public class StartupApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupApplicationListener.class);
    public static final String DESTINATION_FILE = "./main/resources/credentials.json";

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        LOGGER.info("Application is now ready");
        final String authProviderX509CertUrl = System.getenv("GOOGLE_APPLICATION_AUTH_PROVIDER_X509_CERT_URL");
        final String authUri = System.getenv("GOOGLE_APPLICATION_AUTH_URI");
        final String clientEmail = System.getenv("GOOGLE_APPLICATION_CLIENT_EMAIL");
        final String clientId = System.getenv("GOOGLE_APPLICATION_CLIENT_ID");
        final String clientX509CertUrl = System.getenv("GOOGLE_APPLICATION_CLIENT_X509_CERT_URL");
        final String type = System.getenv("GOOGLE_APPLICATION_TYPE");
        final String privateKey = System.getenv("GOOGLE_APPLICATION_PRIVATE_KEY");
        final String privateKeyId = System.getenv("GOOGLE_APPLICATION_PRIVATE_KEY_ID");
        final String projectId = System.getenv("GOOGLE_APPLICATION_PROJECT_ID");
        final String tokenUri = System.getenv("GOOGLE_APPLICATION_TOKEN_URI");

        try {
            // create a map
            Map<String, Object> map = new HashMap<>();
            map.put("type", type);
            map.put("project_id", projectId);
            map.put("private_key", privateKey);
            map.put("private_key_id", privateKeyId);
            map.put("client_email", clientEmail);
            map.put("client_id", clientId);
            map.put("auth_uri", authUri);
            map.put("token_uri", tokenUri);
            map.put("auth_provider_x509_cert_url", authProviderX509CertUrl);
            map.put("client_x509_cert_url", clientX509CertUrl);

            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();

            // convert map to JSON file
            mapper.writeValue(Paths.get(DESTINATION_FILE).toFile(), map);

        } catch (Exception ex) {
            LOGGER.error("Problem occurred on config file creation", ex);
        }
    }
}
