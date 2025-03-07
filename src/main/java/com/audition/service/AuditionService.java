package com.audition.service;

import static org.springframework.util.ObjectUtils.isEmpty;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditionService {

    private final AuditionIntegrationClient auditionIntegrationClient;

    public List<AuditionPost> getPosts(final Integer userId, final Integer id, final String title, final String body) {
        final List<AuditionPost> posts = auditionIntegrationClient.getPosts();
        return posts.stream()
            .filter(post -> (isEmpty(userId) || post.getUserId() == userId)
                && (isEmpty(id) || post.getId() == id)
                && (isEmpty(title) || post.getTitle().contains(title))
                && (isEmpty(body) || post.getBody().contains(body)))
            .toList();
    }

    public AuditionPost getPostById(final String postId) {
        return auditionIntegrationClient.getPostById(postId);
    }

    public List<Comment> getCommentsForPost(final String postId) {
        return auditionIntegrationClient.getCommentsForPost(postId);
    }

    public List<Comment> getCommentsByPostId(final String postId) {
        return auditionIntegrationClient.getCommentsByPostId(postId);
    }

}
