package com.resume.resume_parser.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.resume.resume_parser.response.Response;
import com.resume.resume_parser.util.S3Utility;

@RestController
@RequestMapping("/api/auth")
public class MediaController {

    @RequestMapping(value = "/presigned", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Map<String, String>>> getPreSignedUrlForMediaUpload(
            @RequestParam String purpose, 
            @RequestParam String contentType,
            HttpServletRequest request,
            HttpServletResponse response) {

        setCorsHeaders(request, response);
        Response<Map<String, String>> resp = new Response<>();

        try {
            String key = purpose + "/" + System.currentTimeMillis();
            String preSignedUrl = S3Utility.signBucket(key, contentType);

            Map<String, String> preSignedUrlMap = new HashMap<>();
            preSignedUrlMap.put("preSignedUrl", preSignedUrl);
            preSignedUrlMap.put("key", S3Utility.MEDIA_BASE_CDN_URL + key);

            resp.setPayload(preSignedUrlMap);
            resp.setSuccess(true);
            resp.setMessage("Pre-signed URL generated successfully");
            return ResponseEntity.ok(resp);

        } catch (Exception e) {
            resp.setSuccess(false);
            resp.setMessage("Failed to generate pre-signed URL: " + e.getMessage());
            return ResponseEntity.status(500).body(resp);
        }
    }

    // Handle OPTIONS preflight request (CORS)
    @RequestMapping(value = "/presigned", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleCorsPreflightRequest(
            HttpServletRequest request, HttpServletResponse response) {

        setCorsHeaders(request, response);
        return ResponseEntity.ok().build();
    }

    // Utility method to set CORS headers
    private void setCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        if (origin != null) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Vary", "Origin");
        } else {
            response.setHeader("Access-Control-Allow-Origin", "*");
        }
        // List all methods and headers you need
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
        response.setHeader("Access-Control-Max-Age", "3600");
        // Only include if you use cookies or Authorization (recommended if using JWT):
        // response.setHeader("Access-Control-Allow-Credentials", "true");
    }
}
