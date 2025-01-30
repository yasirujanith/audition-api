package com.audition.configuration;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ResponseHeaderInjector {

    private final Tracer tracer;

    public void injectTraceAndSpanIds(final HttpServletResponse response) {
        final Span currentSpan = tracer.currentSpan();
        if (currentSpan != null) {
            response.setHeader("X-B3-TraceId", currentSpan.context().traceId());
            response.setHeader("X-B3-SpanId", currentSpan.context().spanId());
            response.setHeader("X-B3-ParentSpanId", currentSpan.context().parentId());
            response.setHeader("X-Span-Export", String.valueOf(currentSpan.context().sampled()));

            MDC.put("X-B3-TraceId", currentSpan.context().traceId());
            MDC.put("X-B3-SpanId", currentSpan.context().spanId());
            MDC.put("X-B3-ParentSpanId", currentSpan.context().parentId());
            MDC.put("X-Span-Export", String.valueOf(currentSpan.context().sampled()));
        }
    }

}

