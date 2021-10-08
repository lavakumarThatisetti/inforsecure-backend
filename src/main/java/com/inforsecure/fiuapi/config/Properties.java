package com.inforsecure.fiuapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "spring")
@Data
public class Properties {

    private Api api;
    private Credintials credintials;

    @Data
    public static class Api{
         String aaSetuUrl;
         String anumatiUrl;
         String rahasyaUrl;
         String dataLearningAPi;
         String redirectUrl;
    }

    @Data
    public static class Credintials{
        String clientApiKey;
        String signingPrivateKey;
    }
}
