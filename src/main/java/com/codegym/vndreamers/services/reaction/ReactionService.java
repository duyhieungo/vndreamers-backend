package com.codegym.vndreamers.services.reaction;

import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.PostReaction;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.GenericCRUDService;

import java.util.List;

public interface ReactionService extends GenericCRUDService<PostReaction> {
    List<PostReaction> getAllReactionByPostId(Integer id);

    PostReaction findByPostAndUser(Post post, User user);

}
