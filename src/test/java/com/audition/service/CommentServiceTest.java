package com.audition.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.Comment;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@Getter
class CommentServiceTest {

    @Autowired
    private AuditionService auditionService;

    @MockBean
    private AuditionIntegrationClient auditionIntegrationClient;

    @Test
    void testGetCommentsForPostSuccess() {
        // Setup
        final List<Comment> expectedComments = Collections.singletonList(new Comment());
        when(auditionIntegrationClient.getCommentsForPost(anyString())).thenReturn(expectedComments);

        // Act
        final List<Comment> actualComments = auditionService.getCommentsForPost("1");

        // Assert
        assertEquals(expectedComments, actualComments);
    }

    @Test
    void testGetCommentsForPostNoContent() {
        // Setup
        when(auditionIntegrationClient.getCommentsForPost(anyString())).thenReturn(Collections.emptyList());

        // Act
        final List<Comment> actualComments = auditionService.getCommentsForPost("1");

        // Assert
        assertTrue(actualComments.isEmpty());
    }

    @Test
    void testGetCommentsByPostIdSuccess() {
        // Setup
        final List<Comment> expectedComments = Collections.singletonList(new Comment());
        when(auditionIntegrationClient.getCommentsByPostId(anyString())).thenReturn(expectedComments);

        // Act
        final List<Comment> actualComments = auditionService.getCommentsByPostId("1");

        // Assert
        assertEquals(expectedComments, actualComments);
    }

    @Test
    void testGetCommentsByPostIdNoContent() {
        // Setup
        when(auditionIntegrationClient.getCommentsByPostId(anyString())).thenReturn(Collections.emptyList());

        // Act
        final List<Comment> actualComments = auditionService.getCommentsByPostId("1");

        // Assert
        assertTrue(actualComments.isEmpty());
    }

}
