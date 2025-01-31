package com.audition.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import com.audition.common.logging.AuditionLogger;
import com.audition.model.Comment;
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
class CommentIntegrationClientWithCBTest {

    private static final String ERROR = "Error";

    @Autowired
    private AuditionIntegrationClient auditionIntegrationClient;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private AuditionLogger auditionLogger;

    @Test
    void testGetCommentsForPostSuccess() {
        // Setup
        final List<Comment> expectedComments = Collections.singletonList(new Comment());
        final ResponseEntity<List<Comment>> responseEntity = ResponseEntity.ok(expectedComments);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenReturn(responseEntity);

        // Act
        final List<Comment> actualComments = auditionIntegrationClient.getCommentsForPost("1");

        // Assert
        assertEquals(expectedComments, actualComments);
    }

    @Test
    void testGetCommentsForPostHttpClientErrorExceptionWithCircuitBreaker() {
        // Setup
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // Act
        final List<Comment> actualComments = auditionIntegrationClient.getCommentsForPost("1");

        // Assert
        assertEquals(0, actualComments.size());
    }

    @Test
    void testGetCommentsForPostRestClientExceptionWithCircuitBreaker() {
        // Setup
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenThrow(new RestClientException(ERROR));

        // Act
        final List<Comment> actualComments = auditionIntegrationClient.getCommentsForPost("1");

        // Assert
        assertEquals(0, actualComments.size());
    }

    @Test
    void testGetCommentsByPostIdSuccess() {
        // Setup
        final List<Comment> expectedComments = Collections.singletonList(new Comment());
        final ResponseEntity<List<Comment>> responseEntity = ResponseEntity.ok(expectedComments);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenReturn(responseEntity);

        // Act
        final List<Comment> actualComments = auditionIntegrationClient.getCommentsByPostId("1");

        // Assert
        assertEquals(expectedComments, actualComments);
    }

    @Test
    void testGetCommentsByPostIdHttpClientErrorExceptionWithCircuitBreaker() {
        // Setup
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // Act
        final List<Comment> actualComments = auditionIntegrationClient.getCommentsByPostId("1");

        // Assert
        assertEquals(0, actualComments.size());
    }

    @Test
    void testGetCommentsByPostIdRestClientExceptionWithCircuitBreaker() {
        // Setup
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenThrow(new RestClientException(ERROR));

        // Act
        final List<Comment> actualComments = auditionIntegrationClient.getCommentsByPostId("1");

        // Assert
        assertEquals(0, actualComments.size());
    }

}
