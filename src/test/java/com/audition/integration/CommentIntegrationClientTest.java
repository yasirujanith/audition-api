package com.audition.integration;

import static com.audition.constant.ErrorMessages.HTTP_CLIENT_ERROR;
import static com.audition.constant.ErrorMessages.UNEXPECTED_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.Comment;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
@Getter
class CommentIntegrationClientTest {

    private static final String ERROR = "Error";

    @InjectMocks
    private AuditionIntegrationClient auditionIntegrationClient;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AuditionLogger auditionLogger;

    @BeforeEach
    void setUp() {
        auditionIntegrationClient = new AuditionIntegrationClient(restTemplate, auditionLogger);
    }

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
    void testGetCommentsForPostHttpClientErrorException() {
        // Setup
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // Act
        final SystemException exception = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getCommentsForPost("1");
        });

        // Assert
        assertEquals(HTTP_CLIENT_ERROR, exception.getTitle());
    }

    @Test
    void testGetCommentsForPostRestClientException() {
        // Setup
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenThrow(new RestClientException(ERROR));

        // Act
        final SystemException exception = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getCommentsForPost("1");
        });

        // Assert
        assertEquals(UNEXPECTED_ERROR, exception.getTitle());
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
    void testGetCommentsByPostIdHttpClientErrorException() {
        // Setup
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // Act
        final SystemException exception = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getCommentsByPostId("1");
        });

        // Assert
        assertEquals(HTTP_CLIENT_ERROR, exception.getTitle());
    }

    @Test
    void testGetCommentsByPostIdRestClientException() {
        // Setup
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenThrow(new RestClientException(ERROR));

        // Act
        final SystemException exception = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getCommentsByPostId("1");
        });

        // Assert
        assertEquals(UNEXPECTED_ERROR, exception.getTitle());
    }

}
