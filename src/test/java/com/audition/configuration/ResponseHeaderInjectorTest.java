package com.audition.configuration;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@Getter
class ResponseHeaderInjectorTest {

    @Autowired
    private ResponseHeaderInjector responseHeaderInjector;

    @MockBean
    private Tracer tracer;

    @Mock
    private Span span;

    @Mock
    private TraceContext traceContext;

    @Test
    void testInjectTraceAndSpanIds() {
        final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        when(tracer.currentSpan()).thenReturn(span);
        when(span.context()).thenReturn(traceContext);
        when(traceContext.traceId()).thenReturn("traceId");
        when(traceContext.spanId()).thenReturn("spanId");
        when(traceContext.parentId()).thenReturn("parentId");
        when(traceContext.sampled()).thenReturn(true);

        responseHeaderInjector.injectTraceAndSpanIds(response);

        verify(response).setHeader("X-B3-TraceId", "traceId");
        verify(response).setHeader("X-B3-SpanId", "spanId");
        verify(response).setHeader("X-B3-ParentSpanId", "parentId");
        verify(response).setHeader("X-Span-Export", "true");
    }

    @Test
    void testInjectTraceAndSpanIdsNoCurrentSpan() {
        final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        when(tracer.currentSpan()).thenReturn(null);

        responseHeaderInjector.injectTraceAndSpanIds(response);

        verify(response, never()).setHeader(anyString(), anyString());
    }

}
