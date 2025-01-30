package com.audition.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuditionPostTest {

    private static final String TITLE = "Title";
    private static final String BODY = "Body";

    @Test
    void testNoArgsConstructor() {
        final AuditionPost post = new AuditionPost();
        assertNotNull(post);
    }

    @Test
    void testAllArgsConstructor() {
        final AuditionPost post = new AuditionPost(1, 1, TITLE, BODY);
        assertEquals(1, post.getUserId());
        assertEquals(1, post.getId());
        assertEquals(TITLE, post.getTitle());
        assertEquals(BODY, post.getBody());
    }

    @Test
    void testSettersAndGetters() {
        final AuditionPost post = new AuditionPost();
        post.setUserId(1);
        post.setId(1);
        post.setTitle(TITLE);
        post.setBody(BODY);

        assertEquals(1, post.getUserId());
        assertEquals(1, post.getId());
        assertEquals(TITLE, post.getTitle());
        assertEquals(BODY, post.getBody());
    }

}
