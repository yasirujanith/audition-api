package com.audition.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@Getter
class AuditionPostServiceTest {

    private static final String TITLE_1 = "title1";
    private static final String TITLE_2 = "title2";
    private static final String BODY_1 = "body1";
    private static final String BODY_2 = "body2";

    @InjectMocks
    private AuditionService auditionService;

    @Mock
    private AuditionIntegrationClient auditionIntegrationClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPostsSuccess() {
        // Setup
        final AuditionPost auditionPost = new AuditionPost(1, 1, TITLE_1, BODY_1);
        final List<AuditionPost> expectedPosts = Collections.singletonList(auditionPost);
        when(auditionIntegrationClient.getPosts()).thenReturn(expectedPosts);

        // Act
        final List<AuditionPost> actualPosts = auditionService.getPosts(1, 1, TITLE_1, BODY_1);

        // Assert
        assertEquals(expectedPosts, actualPosts);
    }

    @Test
    void testGetPostsFilterByUserId() {
        // Setup
        final AuditionPost post1 = new AuditionPost(1, 1, TITLE_1, BODY_1);
        final AuditionPost post2 = new AuditionPost(2, 2, TITLE_2, BODY_2);
        final List<AuditionPost> posts = List.of(post1, post2);
        when(auditionIntegrationClient.getPosts()).thenReturn(posts);

        // Act
        final List<AuditionPost> result = auditionService.getPosts(1, null, null, null);

        // Assert
        assertEquals(1, result.size());
        assertEquals(post1, result.get(0));
    }

    @Test
    void testGetPostsFilterById() {
        // Setup
        final AuditionPost post1 = new AuditionPost(1, 1, TITLE_1, BODY_1);
        final AuditionPost post2 = new AuditionPost(2, 2, TITLE_2, BODY_2);
        final List<AuditionPost> posts = List.of(post1, post2);
        when(auditionIntegrationClient.getPosts()).thenReturn(posts);

        // Act
        final List<AuditionPost> result = auditionService.getPosts(null, 2, null, null);

        // Assert
        assertEquals(1, result.size());
        assertEquals(post2, result.get(0));
    }

    @Test
    void testGetPostsFilterByTitle() {
        // Setup
        final AuditionPost post1 = new AuditionPost(1, 1, TITLE_1, BODY_1);
        final AuditionPost post2 = new AuditionPost(2, 2, TITLE_2, BODY_2);
        final List<AuditionPost> posts = List.of(post1, post2);
        when(auditionIntegrationClient.getPosts()).thenReturn(posts);

        // Act
        final List<AuditionPost> result = auditionService.getPosts(null, null, TITLE_1, null);

        // Assert
        assertEquals(1, result.size());
        assertEquals(post1, result.get(0));
    }

    @Test
    void testGetPostsFilterByBody() {
        // Setup
        final AuditionPost post1 = new AuditionPost(1, 1, TITLE_1, BODY_1);
        final AuditionPost post2 = new AuditionPost(2, 2, TITLE_2, BODY_2);
        final List<AuditionPost> posts = List.of(post1, post2);
        when(auditionIntegrationClient.getPosts()).thenReturn(posts);

        // Act
        final List<AuditionPost> result = auditionService.getPosts(null, null, null, BODY_2);

        // Assert
        assertEquals(1, result.size());
        assertEquals(post2, result.get(0));
    }

    @Test
    void testGetPostsNoFilters() {
        // Setup
        final AuditionPost post1 = new AuditionPost(1, 1, TITLE_1, BODY_1);
        final AuditionPost post2 = new AuditionPost(2, 2, TITLE_2, BODY_2);
        final List<AuditionPost> posts = List.of(post1, post2);
        when(auditionIntegrationClient.getPosts()).thenReturn(posts);

        // Act
        final List<AuditionPost> result = auditionService.getPosts(null, null, null, null);

        // Assert
        assertEquals(2, result.size());
        assertEquals(posts, result);
    }

    @Test
    void testGetPostsEmptyList() {
        // Setup
        when(auditionIntegrationClient.getPosts()).thenReturn(Collections.emptyList());

        // Act
        final List<AuditionPost> result = auditionService.getPosts(null, null, null, null);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetPostsNoContent() {
        // Setup
        when(auditionIntegrationClient.getPosts()).thenReturn(Collections.emptyList());

        // Act
        final List<AuditionPost> actualPosts = auditionService.getPosts(1, 1, TITLE_1, BODY_1);

        // Assert
        assertTrue(actualPosts.isEmpty());
    }

    @Test
    void testGetPostByIdSuccess() {
        // Setup
        final AuditionPost expectedPost = new AuditionPost();
        when(auditionIntegrationClient.getPostById(anyString())).thenReturn(expectedPost);

        // Act
        final AuditionPost actualPost = auditionService.getPostById("1");

        // Assert
        assertEquals(expectedPost, actualPost);
    }

    @Test
    void testGetPostByIdNoContent() {
        // Setup
        when(auditionIntegrationClient.getPostById(anyString())).thenReturn(null);

        // Act
        final AuditionPost actualPost = auditionService.getPostById("1");

        // Assert
        assertNull(actualPost);
    }

}
