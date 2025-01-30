package com.audition.integration;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class AuditionIntegrationClient {

    private static final Logger LOG = LoggerFactory.getLogger(AuditionIntegrationClient.class);
    private static final String POSTS_URL = "https://jsonplaceholder.typicode.com/posts";
    private static final String COMMENTS_URL = "https://jsonplaceholder.typicode.com/comments";
    private final RestTemplate restTemplate;
    private final AuditionLogger auditionLogger;

    public AuditionIntegrationClient(RestTemplate restTemplate, AuditionLogger auditionLogger) {
        this.restTemplate = restTemplate;
        this.auditionLogger = auditionLogger;
    }

    public List<AuditionPost> getPosts() {
        try {
            ResponseEntity<List<AuditionPost>> response = restTemplate.exchange(POSTS_URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
            return ObjectUtils.isNotEmpty(response.getBody()) ? response.getBody() : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException("Error fetching posts", e);
        } catch (Exception e) {
            handleUnexpectedException("An unexpected error occurred fetching posts", e);
        }
        return Collections.emptyList();
    }

    public AuditionPost getPostById(final String id) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(POSTS_URL).pathSegment(id).toUriString();
            return restTemplate.getForObject(url, AuditionPost.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + id, SystemException.RESOURCE_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value(), e);
            } else {
                handleHttpClientErrorException("Error fetching post with id " + id, e);
            }
        } catch (Exception e) {
            handleUnexpectedException("An unexpected error occurred fetching post with id " + id, e);
        }
        return null;
    }

    public List<Comment> getCommentsForPost(final String postId) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(POSTS_URL).pathSegment(postId).pathSegment("comments")
                .toUriString();
            ResponseEntity<List<Comment>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
            return ObjectUtils.isNotEmpty(response.getBody()) ? response.getBody() : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException("Error fetching comments for post with id " + postId, e);
        } catch (Exception e) {
            handleUnexpectedException("An unexpected error occurred fetching comments for post with id " + postId, e);
        }
        return Collections.emptyList();
    }

    public List<Comment> getCommentsByPostId(final String postId) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(COMMENTS_URL).queryParam("postId", postId).toUriString();
            ResponseEntity<List<Comment>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
            return ObjectUtils.isNotEmpty(response.getBody()) ? response.getBody() : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException("Error fetching comments for post with id " + postId, e);
        } catch (Exception e) {
            handleUnexpectedException("An unexpected error occurred fetching comments for post with id " + postId, e);
        }
        return Collections.emptyList();
    }

    private void handleHttpClientErrorException(String message, HttpClientErrorException e) {
        String errorMessage = message + ": " + e.getMessage();
        auditionLogger.error(LOG, errorMessage);
        throw new SystemException(errorMessage, SystemException.HTTP_CLIENT_ERROR, e.getStatusCode().value(), e);
    }

    private void handleUnexpectedException(String message, Exception e) {
        String errorMessage = message + ": " + e.getMessage();
        auditionLogger.error(LOG, errorMessage);
        throw new SystemException(errorMessage, SystemException.UNEXPECTED_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
    }
}
