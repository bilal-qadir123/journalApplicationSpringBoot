package com.project.journalProject.service;

import com.project.journalProject.enums.Sentiment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
public class SentimentAnalysisService {

    private final RestTemplate restTemplate;

    @Autowired
    public SentimentAnalysisService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Sentiment getSentiment(String text) {
        try {
            String url = "http://text-processing.com/api/sentiment/";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("text", text);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
            
            if (response != null && response.containsKey("label")) {
                String label = (String) response.get("label");
                return switch (label) {
                    case "pos" -> Sentiment.HAPPY;
                    case "neg" -> Sentiment.SAD;
                    default -> Sentiment.NEUTRAL;
                };
            }
        } catch (Exception e) {
            log.error("Failed to analyze sentiment", e);
        }
        return Sentiment.NEUTRAL;
    }
}
