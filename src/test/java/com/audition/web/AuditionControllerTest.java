package com.audition.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.service.AuditionService;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@Getter
class AuditionControllerTest {

    @Autowired
    private AuditionController auditionController;

    @MockBean
    private AuditionService auditionService;

    @Test
    void testGetPostsSuccess() {
        // Setup
        final List<AuditionPost> expectedPosts = Collections.singletonList(new AuditionPost());
        when(auditionService.getPosts(any(), any(), any(), any())).thenReturn(expectedPosts);

        // Act
        final ResponseEntity<List<AuditionPost>> response = auditionController.getPosts(1, 1, "title", "body");

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedPosts, response.getBody());
    }

    @Test
    void testGetPostsNoContent() {
        // Setup
        when(auditionService.getPosts(any(), any(), any(), any())).thenReturn(Collections.emptyList());

        // Act
        final ResponseEntity<List<AuditionPost>> response = auditionController.getPosts(1, 1, "title", "body");

        // Assert
        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void testGetPostByIdSuccess() {
        // Setup
        final AuditionPost expectedPost = new AuditionPost();
        when(auditionService.getPostById(anyString())).thenReturn(expectedPost);

        // Act
        final ResponseEntity<AuditionPost> response = auditionController.getPostById("1");

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedPost, response.getBody());
    }

    @Test
    void testGetPostByIdNoContent() {
        // Setup
        when(auditionService.getPostById(anyString())).thenReturn(null);

        // Act
        final ResponseEntity<AuditionPost> response = auditionController.getPostById("1");

        // Assert
        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void testGetCommentsForPostSuccess() {
        // Setup
        final List<Comment> expectedComments = Collections.singletonList(new Comment());
        when(auditionService.getCommentsForPost(anyString())).thenReturn(expectedComments);

        // Act
        final ResponseEntity<List<Comment>> response = auditionController.getCommentsForPost("1");

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedComments, response.getBody());
    }

    @Test
    void testGetCommentsForPostNoContent() {
        // Setup
        when(auditionService.getCommentsForPost(anyString())).thenReturn(Collections.emptyList());

        // Act
        final ResponseEntity<List<Comment>> response = auditionController.getCommentsForPost("1");

        // Assert
        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void testGetCommentsByPostIdSuccess() {
        // Setup
        final List<Comment> expectedComments = Collections.singletonList(new Comment());
        when(auditionService.getCommentsByPostId(anyString())).thenReturn(expectedComments);

        // Act
        final ResponseEntity<List<Comment>> response = auditionController.getCommentsByPostId("1");

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedComments, response.getBody());
    }

    @Test
    void testGetCommentsByPostIdNoContent() {
        // Setup
        when(auditionService.getCommentsByPostId(anyString())).thenReturn(Collections.emptyList());

        // Act
        final ResponseEntity<List<Comment>> response = auditionController.getCommentsByPostId("1");

        // Assert
        assertEquals(204, response.getStatusCode().value());
    }

}
