package com.audition.common.exception;

import static com.audition.constant.ErrorMessages.DEFAULT_TITLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class SystemExceptionTest {

    private static final String TEST_MESSAGE = "Test message";
    private static final String CAUSE = "Cause";
    private static final String DETAIL = "Detail";
    private static final String TITLE = "Title";

    @Test
    void testDefaultConstructor() {
        final SystemException exception = new SystemException();
        assertNull(exception.getMessage());
        assertNull(exception.getTitle());
        assertNull(exception.getStatusCode());
        assertNull(exception.getDetail());
    }

    @Test
    void testMessageConstructor() {
        final SystemException exception = new SystemException(TEST_MESSAGE);
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertEquals(DEFAULT_TITLE, exception.getTitle());
        assertNull(exception.getStatusCode());
        assertNull(exception.getDetail());
    }

    @Test
    void testMessageAndErrorCodeConstructor() {
        final SystemException exception = new SystemException(TEST_MESSAGE, 400);
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertEquals(DEFAULT_TITLE, exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertNull(exception.getDetail());
    }

    @Test
    void testMessageAndThrowableConstructor() {
        final Throwable cause = new Throwable(CAUSE);
        final SystemException exception = new SystemException(TEST_MESSAGE, cause);
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertEquals(DEFAULT_TITLE, exception.getTitle());
        assertNull(exception.getStatusCode());
        assertNull(exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testDetailTitleAndErrorCodeConstructor() {
        final SystemException exception = new SystemException(DETAIL, TITLE, 400);
        assertEquals(DETAIL, exception.getMessage());
        assertEquals(TITLE, exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(DETAIL, exception.getDetail());
    }

    @Test
    void testDetailTitleAndThrowableConstructor() {
        final Throwable cause = new Throwable(CAUSE);
        final SystemException exception = new SystemException(DETAIL, TITLE, cause);
        assertEquals(DETAIL, exception.getMessage());
        assertEquals(TITLE, exception.getTitle());
        assertEquals(500, exception.getStatusCode());
        assertEquals(DETAIL, exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testDetailAndErrorCodeAndThrowableConstructor() {
        final Throwable cause = new Throwable(CAUSE);
        final SystemException exception = new SystemException(DETAIL, 400, cause);
        assertEquals(DETAIL, exception.getMessage());
        assertEquals(DEFAULT_TITLE, exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(DETAIL, exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testDetailTitleErrorCodeAndThrowableConstructor() {
        final Throwable cause = new Throwable(CAUSE);
        final SystemException exception = new SystemException(DETAIL, TITLE, 400, cause);
        assertEquals(DETAIL, exception.getMessage());
        assertEquals(TITLE, exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertEquals(DETAIL, exception.getDetail());
        assertEquals(cause, exception.getCause());
    }

}
