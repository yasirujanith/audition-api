package com.audition.integration;

import static com.audition.constant.ErrorMessages.HTTP_CLIENT_ERROR;
import static com.audition.constant.ErrorMessages.RESOURCE_NOT_FOUND;
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
import com.audition.model.AuditionPost;
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
class AuditionPostIntegrationClientTest {

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
    void testGetPostsHttpClientErrorException() {
        // Setup
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // Act
        final SystemException exception = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getPosts();
        });

        // Assert
        assertEquals(HTTP_CLIENT_ERROR, exception.getTitle());
    }

    @Test
    void testGetPostsRestClientException() {
        // Setup
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
            .thenThrow(new RestClientException(ERROR));

        // Act
        final SystemException exception = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getPosts();
        });

        // Assert
        assertEquals(UNEXPECTED_ERROR, exception.getTitle());
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
    void testGetPostByIdHttpClientErrorExceptionNotFound() {
        // Setup
        final HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        when(restTemplate.getForObject(anyString(), eq(AuditionPost.class))).thenThrow(exception);

        // Act
        final SystemException thrownException = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getPostById("1");
        });

        // Assert
        assertEquals(RESOURCE_NOT_FOUND, thrownException.getTitle());
    }

    @Test
    void testGetPostByIdHttpClientErrorExceptionOther() {
        // Setup
        final HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        when(restTemplate.getForObject(anyString(), eq(AuditionPost.class))).thenThrow(exception);

        // Act
        final SystemException thrownException = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getPostById("1");
        });

        // Assert
        assertEquals(HTTP_CLIENT_ERROR, thrownException.getTitle());
    }

    @Test
    void testGetPostByIdRestClientException() {
        // Setup
        when(restTemplate.getForObject(anyString(), eq(AuditionPost.class))).thenThrow(
            new RestClientException(ERROR));

        // Act
        final SystemException thrownException = assertThrows(SystemException.class, () -> {
            auditionIntegrationClient.getPostById("1");
        });

        // Assert
        assertEquals(UNEXPECTED_ERROR, thrownException.getTitle());
    }

}
