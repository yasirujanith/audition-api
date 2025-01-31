package com.audition.common.logging;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.ProblemDetail;

@Getter
@SuppressWarnings("all")
class AuditionLoggerTest {

    @InjectMocks
    private AuditionLogger auditionLogger;

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInfo() {
        when(logger.isInfoEnabled()).thenReturn(true);
        auditionLogger.info(logger, "Info message");
        verify(logger).info("Info message");
    }

    @Test
    void testInfoWithObject() {
        when(logger.isInfoEnabled()).thenReturn(true);
        auditionLogger.info(logger, "Info message with object", new Object());
        verify(logger).info(eq("Info message with object"), any(Object.class));
    }

    @Test
    void testDebug() {
        when(logger.isDebugEnabled()).thenReturn(true);
        auditionLogger.debug(logger, "Debug message");
        verify(logger).debug("Debug message");
    }

    @Test
    void testWarn() {
        when(logger.isWarnEnabled()).thenReturn(true);
        auditionLogger.warn(logger, "Warn message");
        verify(logger).warn("Warn message");
    }

    @Test
    void testError() {
        when(logger.isErrorEnabled()).thenReturn(true);
        auditionLogger.error(logger, "Error message");
        verify(logger).error("Error message");
    }

    @Test
    void testLogErrorWithException() {
        when(logger.isErrorEnabled()).thenReturn(true);
        final Exception e = new Exception("Exception message");
        auditionLogger.logErrorWithException(logger, "Error message with exception", e);
        verify(logger).error("Error message with exception", e);
    }

    @Test
    void testLogStandardProblemDetail() {
        when(logger.isErrorEnabled()).thenReturn(true);
        final ProblemDetail problemDetail = ProblemDetail.forStatus(500);
        problemDetail.setTitle("Title");
        problemDetail.setDetail("Detail");
        final Exception e = new Exception("Exception message");
        auditionLogger.logStandardProblemDetail(logger, problemDetail, e);
        verify(logger).error("ProblemDetail [status=500, title=Title, detail=Detail]", e);
    }

    @Test
    void testLogHttpStatusCodeError() {
        when(logger.isErrorEnabled()).thenReturn(true);
        auditionLogger.logHttpStatusCodeError(logger, "Error message", 500);
        verify(logger).error("ErrorResponse [errorCode=500, message=Error message]\n");
    }

}
