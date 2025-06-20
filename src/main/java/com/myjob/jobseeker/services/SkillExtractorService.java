package com.myjob.jobseeker.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myjob.jobseeker.model.OllamaRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class SkillExtractorService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map extract(String jobDescription) {
        String prompt = """
                Given the job description: "{description}", return a Python dictionary that maps each **tech domain** to a list of relevant tools or skills.
                
                Only return the dictionary in valid Python syntax. Do not include hallucinated categories. If the job does not relate to a domain, omit it.
                
                Example:
                {{
                  "Mobile Development": ["Kotlin", "Android SDK", "Jetpack Compose"],
                  "DevOps": ["Git", "CI/CD"]
                }}
        """.formatted(jobDescription);

        OllamaRequest ollamaRequest = new OllamaRequest("llama2", prompt, false);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<OllamaRequest> entity = new HttpEntity<>(ollamaRequest, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "http://localhost:11434/api/generate",
                HttpMethod.POST,
                entity,
                Map.class
        );

        String rawOutput = (String) response.getBody().get("response");

        try {
            // Convert Python-like dictionary to JSON-compatible (e.g., using single to double quotes)
            String jsonCompatible = rawOutput.replace("'", "\"");
            return objectMapper.readValue(jsonCompatible, Map.class);
        } catch (Exception e) {
            System.out.println("Failed to parse: " + e.getMessage());
            return Map.of("raw_output", List.of(rawOutput));
        }
    }
}