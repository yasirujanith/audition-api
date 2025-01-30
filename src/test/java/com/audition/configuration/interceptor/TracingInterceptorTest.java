package com.audition.configuration.interceptor;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import com.audition.configuration.ResponseHeaderInjector;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.servlet.HandlerInterceptor;

@SpringBootTest
@Getter
class TracingInterceptorTest {

    @Autowired
    private HandlerInterceptor tracingInterceptor;

    @MockBean
    private ResponseHeaderInjector responseHeaderInjector;

    @Test
    void testPreHandle() throws Exception {
        final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        final Object handler = new Object();

        final boolean result = tracingInterceptor.preHandle(request, response, handler);

        assertTrue(result);
        verify(responseHeaderInjector).injectTraceAndSpanIds(response);
    }

}
