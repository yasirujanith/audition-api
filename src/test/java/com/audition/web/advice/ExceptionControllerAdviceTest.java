package com.audition.web.advice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.client.HttpClientErrorException;

@SpringBootTest
@Getter
class ExceptionControllerAdviceTest {

    private static final String SYSTEM_ERROR = "System Error";

    @Mock
    private AuditionLogger auditionLogger;

    private ExceptionControllerAdvice exceptionControllerAdvice;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionControllerAdvice = new ExceptionControllerAdvice(auditionLogger);
    }

    @Test
    void testHandleHttpClientException() {
        final HttpClientErrorException exception = mock(HttpClientErrorException.class);
        when(exception.getStatusCode()).thenReturn(HttpStatusCode.valueOf(400));
        when(exception.getMessage()).thenReturn("Bad Request");

        final ProblemDetail problemDetail = exceptionControllerAdvice.handleHttpClientException(exception);

        assertEquals(400, problemDetail.getStatus());
        assertEquals("Bad Request", problemDetail.getDetail());
        verify(auditionLogger, times(1)).logStandardProblemDetail(any(), any(), eq(exception));
    }

    @Test
    void testHandleMainException() {
        final Exception exception = new Exception("Internal Server Error");

        final ProblemDetail problemDetail = exceptionControllerAdvice.handleMainException(exception);

        assertEquals(500, problemDetail.getStatus());
        assertEquals("Internal Server Error", problemDetail.getDetail());
        verify(auditionLogger, times(1)).logStandardProblemDetail(any(), any(), eq(exception));
    }

    @Test
    void testHandleSystemException() {
        final SystemException exception = new SystemException(SYSTEM_ERROR, 500);

        final ProblemDetail problemDetail = exceptionControllerAdvice.handleSystemException(exception);

        assertEquals(500, problemDetail.getStatus());
        assertEquals(SYSTEM_ERROR, problemDetail.getDetail());
        verify(auditionLogger, times(1)).logStandardProblemDetail(any(), any(), eq(exception));
    }

    @Test
    void testGetHttpStatusCodeFromSystemException() {
        final SystemException exception = new SystemException(SYSTEM_ERROR, 500);

        final int statusCode = exceptionControllerAdvice.handleSystemException(exception).getStatus();

        assertEquals(500, statusCode);
        verify(auditionLogger, times(1)).logHttpStatusCodeError(any(), eq(SYSTEM_ERROR), eq(500));
    }

    @Test
    void testGetHttpStatusCodeFromException() {
        final HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException("POST");

        final int statusCode = exceptionControllerAdvice.handleMainException(exception).getStatus();

        assertEquals(405, statusCode);
        verify(auditionLogger, times(1)).logHttpStatusCodeError(any(), eq("Request method 'POST' is not supported"),
            eq(405));
    }

}
