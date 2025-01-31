package com.audition.integration;

import static com.audition.constant.ErrorMessages.HTTP_CLIENT_ERROR;
import static com.audition.constant.ErrorMessages.RESOURCE_NOT_FOUND;
import static com.audition.constant.ErrorMessages.UNEXPECTED_ERROR;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@SuppressWarnings({"PMD.UnusedPrivateMethod", "PMD.GuardLogStatement"})
public class AuditionIntegrationClient {

    private static final Logger LOG = LoggerFactory.getLogger(AuditionIntegrationClient.class);
    private static final String POSTS_URL = "https://jsonplaceholder.typicode.com/posts";
    private static final String COMMENTS_URL = "https://jsonplaceholder.typicode.com/comments";
    private final RestTemplate restTemplate;
    private final AuditionLogger auditionLogger;

    @CircuitBreaker(name = "postsService", fallbackMethod = "getPostsFallback")
    public List<AuditionPost> getPosts() {
        try {
            final ResponseEntity<List<AuditionPost>> response = restTemplate.exchange(POSTS_URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
            return ObjectUtils.isNotEmpty(response.getBody()) ? response.getBody() : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException("Error fetching posts", e);
        } catch (RestClientException e) {
            handleUnexpectedException("An unexpected error occurred fetching posts", e);
        }
        return Collections.emptyList();
    }

    @CircuitBreaker(name = "postsService", fallbackMethod = "getPostByIdFallback")
    public AuditionPost getPostById(final String id) {
        try {
            final String url = UriComponentsBuilder.fromHttpUrl(POSTS_URL).pathSegment(id).toUriString();
            return restTemplate.getForObject(url, AuditionPost.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + id, RESOURCE_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value(), e);
            } else {
                handleHttpClientErrorException("Error fetching post with id " + id, e);
            }
        } catch (RestClientException e) {
            handleUnexpectedException("An unexpected error occurred fetching post with id " + id, e);
        }
        return null;
    }

    @CircuitBreaker(name = "commentsService", fallbackMethod = "getCommentsForPostFallback")
    public List<Comment> getCommentsForPost(final String postId) {
        try {
            final String url = UriComponentsBuilder.fromHttpUrl(POSTS_URL).pathSegment(postId).pathSegment("comments")
                .toUriString();
            final ResponseEntity<List<Comment>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
            return ObjectUtils.isNotEmpty(response.getBody()) ? response.getBody() : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException("Error fetching comments for post with id " + postId, e);
        } catch (RestClientException e) {
            handleUnexpectedException("An unexpected error occurred fetching comments for post with id " + postId, e);
        }
        return Collections.emptyList();
    }

    @CircuitBreaker(name = "commentsService", fallbackMethod = "getCommentsByPostIdFallback")
    public List<Comment> getCommentsByPostId(final String postId) {
        try {
            final String url = UriComponentsBuilder.fromHttpUrl(COMMENTS_URL).queryParam("postId", postId)
                .toUriString();
            final ResponseEntity<List<Comment>> response = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
            return ObjectUtils.isNotEmpty(response.getBody()) ? response.getBody() : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException("Error fetching comments for post with id " + postId, e);
        } catch (RestClientException e) {
            handleUnexpectedException("An unexpected error occurred fetching comments for post with id " + postId, e);
        }
        return Collections.emptyList();
    }

    private void handleHttpClientErrorException(final String message, final HttpClientErrorException e) {
        final String errorMessage = message + ": " + e.getMessage();
        auditionLogger.error(LOG, errorMessage);
        throw new SystemException(errorMessage, HTTP_CLIENT_ERROR, e.getStatusCode().value(), e);
    }

    private void handleUnexpectedException(final String message, final Exception e) {
        final String errorMessage = message + ": " + e.getMessage();
        auditionLogger.error(LOG, errorMessage);
        throw new SystemException(errorMessage, UNEXPECTED_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
    }

    private List<AuditionPost> getPostsFallback(final Throwable t) {
        logFallbackError("getPosts", "N/A", t);
        return Collections.emptyList();
    }

    private AuditionPost getPostByIdFallback(final String id, final Throwable t) {
        logFallbackError("getPostById", id, t);
        return null;
    }

    private List<Comment> getCommentsForPostFallback(final String postId, final Throwable t) {
        logFallbackError("getCommentsForPost", postId, t);
        return Collections.emptyList();
    }

    private List<Comment> getCommentsByPostIdFallback(final String postId, final Throwable t) {
        logFallbackError("getCommentsByPostId", postId, t);
        return Collections.emptyList();
    }

    private void logFallbackError(final String methodName, final String identifier, final Throwable t) {
        auditionLogger.error(LOG,
            "Fallback method for " + methodName + " triggered for id " + identifier + " due to: " + t.getMessage());
    }
}
