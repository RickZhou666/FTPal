package com.rick.ftpal.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.rick.ftpal.domain.FTRunSummary;
import okhttp3.Request;
import okhttp3.ResponseBody;

import java.io.IOException;

public class BooneHelper {
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public static FTRunSummary fetchFTRunSummary(String component, String prId) {
        try {
            Request request = new Request.Builder()
                    .url(String.format("https://boone.paypalcorp.com/api/viewFTRunStatus?componentName=%s&pullRequestId=%s", component, prId))
                    .get()
                    .addHeader("DomainName", "risk-bo")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("userID", "runzhou")
                    .build();
            ResponseBody responseBody = FTPalHttpClient.getInstance().newCall(request).execute().body();
            FTRunSummary ftRunSummary = objectMapper.readValue(responseBody.string(), FTRunSummary.class);
            return ftRunSummary;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
