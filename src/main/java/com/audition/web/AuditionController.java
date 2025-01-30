package com.audition.web;

import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.service.AuditionService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditionController {

    private static final Logger logger = LoggerFactory.getLogger(AuditionController.class);
    private final AuditionService auditionService;
    private final AuditionLogger auditionLogger;

    public AuditionController(AuditionService auditionService, AuditionLogger auditionLogger) {
        this.auditionService = auditionService;
        this.auditionLogger = auditionLogger;
    }

    @GetMapping(value = "/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditionPost>> getPosts(
        @RequestParam(value = "userId", required = false) @Min(1) Integer userId,
        @RequestParam(value = "id", required = false) @Min(1) Integer id,
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "body", required = false) String body) {

        auditionLogger.info(logger,
            "Fetching posts with userId: " + userId + ", id: " + id + ", title: " + title + ", body: " + body);
        List<AuditionPost> posts = auditionService.getPosts(userId, id, title, body);
        if (ObjectUtils.isEmpty(posts)) {
            auditionLogger.info(logger, "No posts found with the given parameters.");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping(value = "/posts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuditionPost> getPostById(
        @PathVariable("id") @NotNull @Pattern(regexp = "\\d+", message = "Post ID must be a valid integer") final String postId) {

        auditionLogger.info(logger, "Fetching post with id: {}", postId);
        AuditionPost auditionPost = auditionService.getPostById(postId);
        if (ObjectUtils.isEmpty(auditionPost)) {
            auditionLogger.info(logger, "No post found with id: {}", postId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(auditionPost);
    }

    @GetMapping(value = "/posts/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Comment>> getCommentsForPost(
        @PathVariable("id") @NotNull @Pattern(regexp = "\\d+", message = "Post ID must be a valid integer") final String postId) {

        auditionLogger.info(logger, "Fetching comments for post with id: {}", postId);
        List<Comment> comments = auditionService.getCommentsForPost(postId);
        if (ObjectUtils.isEmpty(comments)) {
            auditionLogger.info(logger, "No comments found for post with id: {}", postId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comments);
    }

    @GetMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Comment>> getCommentsByPostId(
        @RequestParam("postId") @NotNull @Pattern(regexp = "\\d+", message = "Post ID must be a valid integer") final String postId) {

        auditionLogger.info(logger, "Fetching comments with postId: {}", postId);
        List<Comment> comments = auditionService.getCommentsByPostId(postId);
        if (ObjectUtils.isEmpty(comments)) {
            auditionLogger.info(logger, "No comments found with postId: {}", postId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comments);
    }

}
