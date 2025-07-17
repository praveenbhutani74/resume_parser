package com.resume.resume_parser.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.resume.resume_parser.response.Response;
import com.resume.resume_parser.util.S3Utility;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.OPTIONS})
public class MediaController {

    @RequestMapping(value = { "/presigned" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Map<String, String>>> getPreSignedUrlForMediaUpload(
            @RequestParam String purpose, 
            @RequestParam String contentType,
            HttpServletResponse httpResponse) {
        
        // Add CORS headers manually (backup solution)
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        
        Response<Map<String, String>> response = new Response<>();
        
        try {
            String key = purpose + "/" + System.currentTimeMillis();
            String preSignedUrl = S3Utility.signBucket(key, contentType);
            
            Map<String, String> preSignedUrlMap = new HashMap<>();
            preSignedUrlMap.put("preSignedUrl", preSignedUrl);
            preSignedUrlMap.put("key", S3Utility.MEDIA_BASE_CDN_URL + key);
            
            response.setPayload(preSignedUrlMap);
            response.setSuccess(true);
            response.setMessage("Pre-signed URL generated successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Failed to generate pre-signed URL: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    // Handle OPTIONS preflight request
    @RequestMapping(value = "/presigned", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleCorsPreflightRequest(HttpServletResponse httpResponse) {
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        
        return ResponseEntity.ok().build();
    }
}
