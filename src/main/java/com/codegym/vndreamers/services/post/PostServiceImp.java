package com.codegym.vndreamers.services.post;

import com.codegym.vndreamers.models.Post;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.repositories.PostRepository;
import com.codegym.vndreamers.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImp implements PostCRUDService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findAll(Sort sort) {
        return postRepository.findAll(sort);
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Post findById(int id) {
        return postRepository.findById(id).get();
    }


    @Override
    public Post save(Post model){
        return postRepository.save(model);
    }

    @Override
    public Post update(Post model) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public List<Post> getAllByUserIdAndStatus(Integer id, Integer status) {
        return postRepository.findAllByUserIdAndStatus(id, status);
    }

    @Override
    public Post deletePostByIdAndUserId(Integer postId, Integer userId) {
        Post post = postRepository.findById(postId).get();
        User user = userRepository.findById(userId).get();
        if (post != null && user != null){
           Post post1 =  postRepository.findById(postId).get();
           post1.setStatus(0);
            return postRepository.save(post1);
        }else {
            return null;
        }
    }

    @Override
    public List<Post> getAllByUSerIdAndRelationShip(Integer id1, Integer status1, Integer id2, Integer status2, Integer id3, Integer status3) {
        return postRepository.findAllByUserIdAndStatusOrUserIdAndStatusOrUserIdAndStatus(id1,status1, id2, status2, id3, status3);
    }


}
