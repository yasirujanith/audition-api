package com.audition.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@Getter
class AuditionPostIntegrationClientTest {

    private static final String ERROR = "Error";

    @Autowired
    private AuditionIntegrationClient auditionIntegrationClient;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private AuditionLogger auditionLogger;

    @Test
    void testGetPostsSuccess() {
        // Setup
        final List<AuditionPost> expectedPosts = Collections.singletonList(new AuditionPost());
        final ResponseEntity<List<AuditionPost>> responseEntity = ResponseEntity.ok(expectedPosts);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenReturn(responseEntity);

        // Act
        final List<AuditionPost> actualPosts = auditionIntegrationClient.getPosts();

        // Assert
        assertEquals(expectedPosts, actualPosts);
    }

    @Test
    void testGetPostsHttpClientErrorExceptionWithCircuitBreaker() {
        // Setup
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // Act
        final List<AuditionPost> actualPosts = auditionIntegrationClient.getPosts();

        // Assert
        assertEquals(0, actualPosts.size());
    }

    @Test
    void testGetPostsRestClientExceptionWithCircuitBreaker() {
        // Setup
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenThrow(new RestClientException(ERROR));

        // Act
        final List<AuditionPost> actualPosts = auditionIntegrationClient.getPosts();

        // Assert
        assertEquals(0, actualPosts.size());
    }

    @Test
    void testGetPostByIdSuccess() {
        // Setup
        final AuditionPost expectedPost = new AuditionPost();
        when(restTemplate.getForObject(anyString(), eq(AuditionPost.class))).thenReturn(expectedPost);

        // Act
        final AuditionPost actualPost = auditionIntegrationClient.getPostById("1");

        // Assert
        assertEquals(expectedPost, actualPost);
    }

    @Test
    void testGetPostByIdHttpClientErrorExceptionNotFoundWithCircuitBreaker() {
        // Setup
        final HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        when(restTemplate.getForObject(anyString(), eq(AuditionPost.class))).thenThrow(exception);

        // Act
        final AuditionPost actualPost = auditionIntegrationClient.getPostById("1");

        // Assert
        assertNull(actualPost);
    }

    @Test
    void testGetPostByIdHttpClientErrorExceptionOtherWithCircuitBreaker() {
        // Setup
        final HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        when(restTemplate.getForObject(anyString(), eq(AuditionPost.class))).thenThrow(exception);

        // Act
        final AuditionPost actualPost = auditionIntegrationClient.getPostById("1");

        // Assert
        assertNull(actualPost);
    }

    @Test
    void testGetPostByIdRestClientExceptionWithCircuitBreaker() {
        // Setup
        when(restTemplate.getForObject(anyString(), eq(AuditionPost.class))).thenThrow(
            new RestClientException(ERROR));

        // Act
        final AuditionPost actualPost = auditionIntegrationClient.getPostById("1");

        // Assert
        assertNull(actualPost);
    }

}
