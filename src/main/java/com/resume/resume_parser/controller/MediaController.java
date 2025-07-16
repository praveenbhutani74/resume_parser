package com.resume.resume_parser.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.resume.resume_parser.response.Response;
import com.resume.resume_parser.util.S3Utility;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class MediaController {

	@RequestMapping(value = { "/presigned" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)

	public Response<Map<String, String>> getPreSignedUrlForMediaUpload(

			@RequestParam String purpose, @RequestParam String contentType) {

		Response<Map<String, String>> response = new Response<>();


		String key =   purpose +"/" + System.currentTimeMillis();

		String preSignedUrl = S3Utility.signBucket(key, contentType);

		Map<String, String> preSignedUrlMap = new HashMap<>();

		preSignedUrlMap.put("preSignedUrl", preSignedUrl);

		preSignedUrlMap.put("key", S3Utility.MEDIA_BASE_CDN_URL + key);

		response.setPayload(preSignedUrlMap);
		
		   response.setSuccess(true);
           response.setMessage("Pre-signed URL generated successfully");

		return response;

	}

}
