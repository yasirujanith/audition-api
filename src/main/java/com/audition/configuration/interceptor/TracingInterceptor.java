package com.audition.configuration.interceptor;

import com.audition.configuration.ResponseHeaderInjector;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@AllArgsConstructor
public class TracingInterceptor implements HandlerInterceptor {

    private final ResponseHeaderInjector responseHeaderInjector;

    @Override
    public boolean preHandle(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response,
        @NonNull final Object handler) {
        responseHeaderInjector.injectTraceAndSpanIds(response);
        return true;
    }
}
