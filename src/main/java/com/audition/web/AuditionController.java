package com.audition.web;

import static com.audition.constant.RegexPatterns.REGEX_DIGITS_ONLY;
import static org.springframework.util.ObjectUtils.isEmpty;

import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.service.AuditionService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequiredArgsConstructor
@SuppressWarnings("PMD.GuardLogStatement")
public class AuditionController {

    private static final Logger LOG = LoggerFactory.getLogger(AuditionController.class);
    private final AuditionService auditionService;
    private final AuditionLogger auditionLogger;

    @GetMapping(value = "/posts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AuditionPost>> getPosts(
        @RequestParam(value = "userId", required = false) @Min(1) final Integer userId,
        @RequestParam(value = "id", required = false) @Min(1) final Integer id,
        @RequestParam(value = "title", required = false) final String title,
        @RequestParam(value = "body", required = false) final String body) {

        auditionLogger.info(LOG,
            "Fetching posts with userId: " + userId + ", id: " + id + ", title: " + title + ", body: " + body);
        final List<AuditionPost> posts = auditionService.getPosts(userId, id, title, body);
        if (isEmpty(posts)) {
            auditionLogger.info(LOG, "No posts found with the given parameters.");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

    @GetMapping(value = "/posts/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuditionPost> getPostById(
        @PathVariable("id") @NotNull @Pattern(regexp = REGEX_DIGITS_ONLY, message = "Post ID must be a valid integer") final String postId) {

        auditionLogger.info(LOG, "Fetching post with id: {}", postId);
        final AuditionPost auditionPost = auditionService.getPostById(postId);
        if (isEmpty(auditionPost)) {
            auditionLogger.info(LOG, "No post found with id: {}", postId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(auditionPost);
    }

    @GetMapping(value = "/posts/{id}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Comment>> getCommentsForPost(
        @PathVariable("id") @NotNull @Pattern(regexp = REGEX_DIGITS_ONLY, message = "Post ID must be a valid integer") final String postId) {

        auditionLogger.info(LOG, "Fetching comments for post with id: {}", postId);
        final List<Comment> comments = auditionService.getCommentsForPost(postId);
        if (isEmpty(comments)) {
            auditionLogger.info(LOG, "No comments found for post with id: {}", postId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comments);
    }

    @GetMapping(value = "/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Comment>> getCommentsByPostId(
        @RequestParam("postId") @NotNull @Pattern(regexp = REGEX_DIGITS_ONLY, message = "Post ID must be a valid integer") final String postId) {

        auditionLogger.info(LOG, "Fetching comments with postId: {}", postId);
        final List<Comment> comments = auditionService.getCommentsByPostId(postId);
        if (isEmpty(comments)) {
            auditionLogger.info(LOG, "No comments found with postId: {}", postId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comments);
    }

}
