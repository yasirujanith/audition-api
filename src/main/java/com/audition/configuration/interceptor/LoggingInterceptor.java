package com.audition.configuration.interceptor;

import com.audition.common.logging.AuditionLogger;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;

@RequiredArgsConstructor
@SuppressWarnings("PMD.GuardLogStatement")
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingInterceptor.class);
    private final AuditionLogger auditionLogger;

    @Override
    public @NonNull ClientHttpResponse intercept(@NonNull final HttpRequest request, final byte[] body,
        final ClientHttpRequestExecution execution)
        throws IOException {
        logRequest(request, body);
        final ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(final HttpRequest request, final byte[] body) {
        auditionLogger.info(LOG, "Request URI: {}", request.getURI());
        auditionLogger.info(LOG, "Request Method: {}", request.getMethod());
        auditionLogger.info(LOG, "Request Body: {}", new String(body, StandardCharsets.UTF_8));
    }

    private void logResponse(final ClientHttpResponse response) throws IOException {
        auditionLogger.info(LOG, "Response Status Code: {}", response.getStatusCode());
        auditionLogger.info(LOG, "Response Status Text: {}", response.getStatusText());
        // When logging the response body, ensure that sensitive information is masked
        auditionLogger.info(LOG, "Response Body: {}",
            new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8));
    }

}
