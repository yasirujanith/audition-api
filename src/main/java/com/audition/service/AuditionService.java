package com.audition.service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
public class AuditionService {

    private final AuditionIntegrationClient auditionIntegrationClient;

    public AuditionService(AuditionIntegrationClient auditionIntegrationClient) {
        this.auditionIntegrationClient = auditionIntegrationClient;
    }

    public List<AuditionPost> getPosts(Integer userId, Integer id, String title, String body) {
        List<AuditionPost> posts = auditionIntegrationClient.getPosts();
        return posts.stream()
            .filter(post -> (ObjectUtils.isEmpty(userId) || post.getUserId() == userId) &&
                (ObjectUtils.isEmpty(id) || post.getId() == id) &&
                (ObjectUtils.isEmpty(title) || post.getTitle().contains(title)) &&
                (ObjectUtils.isEmpty(body) || post.getBody().contains(body)))
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
