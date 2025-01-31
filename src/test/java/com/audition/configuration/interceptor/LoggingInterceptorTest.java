package com.audition.configuration.interceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.audition.common.logging.AuditionLogger;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

@Getter
class LoggingInterceptorTest {

    @InjectMocks
    AuditionLogger auditionLogger;

    @Mock
    private LoggingInterceptor loggingInterceptor;

    @Mock
    private HttpRequest request;

    @Mock
    private ClientHttpRequestExecution execution;

    @Mock
    private ClientHttpResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loggingInterceptor = new LoggingInterceptor(auditionLogger);
    }

    @Test
    void testIntercept() throws IOException {
        final byte[] body = "Request Body".getBytes(StandardCharsets.UTF_8);
        when(execution.execute(any(HttpRequest.class), any(byte[].class))).thenReturn(response);
        when(response.getStatusCode()).thenReturn(org.springframework.http.HttpStatus.OK);
        when(response.getStatusText()).thenReturn("OK");
        when(response.getBody()).thenReturn(new ByteArrayInputStream("Response Body".getBytes(StandardCharsets.UTF_8)));

        loggingInterceptor.intercept(request, body, execution);

        verify(execution, times(1)).execute(any(HttpRequest.class), any(byte[].class));
    }

}
