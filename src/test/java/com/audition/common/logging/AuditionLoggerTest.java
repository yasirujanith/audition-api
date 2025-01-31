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
    private Logger LOGGER;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInfo() {
        when(LOGGER.isInfoEnabled()).thenReturn(true);
        auditionLogger.info(LOGGER, "Info message");
        verify(LOGGER).info("Info message");
    }

    @Test
    void testInfoWithObject() {
        when(LOGGER.isInfoEnabled()).thenReturn(true);
        auditionLogger.info(LOGGER, "Info message with object", new Object());
        verify(LOGGER).info(eq("Info message with object"), any(Object.class));
    }

    @Test
    void testDebug() {
        when(LOGGER.isDebugEnabled()).thenReturn(true);
        auditionLogger.debug(LOGGER, "Debug message");
        verify(LOGGER).debug("Debug message");
    }

    @Test
    void testWarn() {
        when(LOGGER.isWarnEnabled()).thenReturn(true);
        auditionLogger.warn(LOGGER, "Warn message");
        verify(LOGGER).warn("Warn message");
    }

    @Test
    void testError() {
        when(LOGGER.isErrorEnabled()).thenReturn(true);
        auditionLogger.error(LOGGER, "Error message");
        verify(LOGGER).error("Error message");
    }

    @Test
    void testLogErrorWithException() {
        when(LOGGER.isErrorEnabled()).thenReturn(true);
        final Exception e = new Exception("Exception message");
        auditionLogger.logErrorWithException(LOGGER, "Error message with exception", e);
        verify(LOGGER).error("Error message with exception", e);
    }

    @Test
    void testLogStandardProblemDetail() {
        when(LOGGER.isErrorEnabled()).thenReturn(true);
        final ProblemDetail problemDetail = ProblemDetail.forStatus(500);
        problemDetail.setTitle("Title");
        problemDetail.setDetail("Detail");
        final Exception e = new Exception("Exception message");
        auditionLogger.logStandardProblemDetail(LOGGER, problemDetail, e);
        verify(LOGGER).error("ProblemDetail [status=500, title=Title, detail=Detail]", e);
    }

    @Test
    void testLogHttpStatusCodeError() {
        when(LOGGER.isErrorEnabled()).thenReturn(true);
        auditionLogger.logHttpStatusCodeError(LOGGER, "Error message", 500);
        verify(LOGGER).error("ErrorResponse [errorCode=500, message=Error message]\n");
    }

}
