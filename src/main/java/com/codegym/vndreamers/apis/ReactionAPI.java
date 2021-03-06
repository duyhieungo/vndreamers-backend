package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.exceptions.ReactionExistException;
import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.PostReaction;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.post.PostCRUDService;
import com.codegym.vndreamers.services.reaction.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping(
        value = "/api"
//        produces = MediaType.APPLICATION_JSON_VALUE,
//        consumes = MediaType.APPLICATION_JSON_VALUE
)
@CrossOrigin("*")
public class ReactionAPI {

    @Autowired
    private PostCRUDService postCRUDService;

    @Autowired
    private ReactionService reactionService;

    @PostMapping("/posts/{postId}/reactions")
    public PostReaction createReactionPost(@PathVariable int postId, @RequestBody PostReaction postReaction) throws SQLIntegrityConstraintViolationException, EntityExistException, ReactionExistException {
        Post post = postCRUDService.findById(postId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostReaction postReaction1 = reactionService.findByPostAndUser(post, user);
        if (postReaction1 != null) {
            throw new ReactionExistException();
        }
        postReaction.setPost(post);
        postReaction.setUser(user);
        reactionService.save(postReaction);
        return postReaction;
    }

    @GetMapping("/posts/{postId}/reactions")
    public List<PostReaction> getReactionsPost(@PathVariable int postId) {
        Post post = postCRUDService.findById(postId);
        if (post != null) {
            return reactionService.getAllReactionByPostId(postId);
        } else {
            return null;
        }
    }

    @DeleteMapping("/posts/{postId}/reactions")
    public PostReaction deleteReactionsPost(@PathVariable int postId) {
        Post post = postCRUDService.findById(postId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostReaction postReaction = reactionService.findByPostAndUser(post, user);
        if (post != null) {
            reactionService.delete(postReaction.getId());
//            reactionService.deleteByPostAndUser(post, user);
            return postReaction ;
        } else {
            return null;
        }
    }

    @ExceptionHandler(ReactionExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleException() {
        return "{\"error\":\"reaction exist\"}";
    }
}
