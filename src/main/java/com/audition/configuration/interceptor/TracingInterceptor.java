package com.audition.configuration.interceptor;

import com.audition.configuration.ResponseHeaderInjector;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Getter
public class TracingInterceptor implements HandlerInterceptor {

    private final ResponseHeaderInjector responseHeaderInjector;

    public TracingInterceptor(final ResponseHeaderInjector responseHeaderInjector) {
        this.responseHeaderInjector = responseHeaderInjector;
    }

    @Override
    public boolean preHandle(@NonNull final HttpServletRequest request, @NonNull final HttpServletResponse response,
        @NonNull final Object handler) {
        responseHeaderInjector.injectTraceAndSpanIds(response);
        return true;
    }
}
