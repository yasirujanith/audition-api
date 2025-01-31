package com.audition.web.advice;

import static com.audition.constant.ErrorMessages.DEFAULT_TITLE;
import static io.micrometer.common.util.StringUtils.isNotBlank;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@SuppressWarnings("PMD.GuardLogStatement")
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    private static final String ERROR_MESSAGE = " Error Code from Exception could not be mapped to a valid HttpStatus Code - ";
    private static final String DEFAULT_MESSAGE = "API Error occurred. Please contact support or administrator.";
    private final AuditionLogger auditionLogger;

    @ExceptionHandler(HttpClientErrorException.class)
    ProblemDetail handleHttpClientException(final HttpClientErrorException e) {
        return createProblemDetail(e, e.getStatusCode());

    }

    @ExceptionHandler(Exception.class)
    ProblemDetail handleMainException(final Exception e) {
        final HttpStatusCode status = getHttpStatusCodeFromException(e);
        return createProblemDetail(e, status);
    }

    @ExceptionHandler(SystemException.class)
    ProblemDetail handleSystemException(final SystemException e) {
        final HttpStatusCode status = getHttpStatusCodeFromSystemException(e);
        return createProblemDetail(e, status);
    }

    private ProblemDetail createProblemDetail(final Exception exception,
        final HttpStatusCode statusCode) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(statusCode);
        problemDetail.setDetail(getMessageFromException(exception));
        if (exception instanceof SystemException systemException) {
            problemDetail.setTitle(systemException.getTitle());
        } else {
            problemDetail.setTitle(DEFAULT_TITLE);
        }
        auditionLogger.logStandardProblemDetail(LOG, problemDetail, exception);
        return problemDetail;
    }

    private String getMessageFromException(final Exception exception) {
        if (isNotBlank(exception.getMessage())) {
            return exception.getMessage();
        }
        return DEFAULT_MESSAGE;
    }

    private HttpStatusCode getHttpStatusCodeFromSystemException(final SystemException exception) {
        try {
            auditionLogger.logHttpStatusCodeError(LOG, exception.getMessage(), exception.getStatusCode());
            return HttpStatusCode.valueOf(exception.getStatusCode());
        } catch (final IllegalArgumentException iae) {
            auditionLogger.info(LOG, ERROR_MESSAGE + exception.getStatusCode());
            return INTERNAL_SERVER_ERROR;
        }
    }

    private HttpStatusCode getHttpStatusCodeFromException(final Exception exception) {
        if (exception instanceof HttpClientErrorException clientErrorException) {
            auditionLogger.logHttpStatusCodeError(LOG, clientErrorException.getMessage(),
                clientErrorException.getStatusCode().value());
            return clientErrorException.getStatusCode();
        } else if (exception instanceof HttpRequestMethodNotSupportedException notSupportedException) {
            auditionLogger.logHttpStatusCodeError(LOG, notSupportedException.getMessage(),
                notSupportedException.getStatusCode().value());
            return METHOD_NOT_ALLOWED;
        } else if (exception instanceof ConstraintViolationException constraintViolationException) {
            auditionLogger.logHttpStatusCodeError(LOG, constraintViolationException.getMessage(), BAD_REQUEST.value());
            return BAD_REQUEST;
        }
        return INTERNAL_SERVER_ERROR;
    }
}
