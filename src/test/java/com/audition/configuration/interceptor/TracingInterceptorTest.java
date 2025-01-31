package com.audition.configuration.interceptor;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import com.audition.configuration.ResponseHeaderInjector;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@Getter
class TracingInterceptorTest {

    @InjectMocks
    private TracingInterceptor tracingInterceptor;

    @Mock
    private ResponseHeaderInjector responseHeaderInjector;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tracingInterceptor = new TracingInterceptor(responseHeaderInjector);
    }

    @Test
    void testPreHandle() {
        final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        final Object handler = new Object();

        final boolean result = tracingInterceptor.preHandle(request, response, handler);

        assertTrue(result);
        verify(responseHeaderInjector).injectTraceAndSpanIds(response);
    }

}
