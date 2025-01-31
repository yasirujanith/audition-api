package com.audition.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void testNoArgsConstructor() {
        final Comment comment = new Comment();
        assertNotNull(comment);
    }

    @Test
    void testSettersAndGetters() {
        final Comment comment = new Comment();
        comment.setPostId(1);
        comment.setId(1);
        comment.setName("Name");
        comment.setEmail("email@example.com");
        comment.setBody("Body");

        assertEquals(1, comment.getPostId());
        assertEquals(1, comment.getId());
        assertEquals("Name", comment.getName());
        assertEquals("email@example.com", comment.getEmail());
        assertEquals("Body", comment.getBody());
    }

}
