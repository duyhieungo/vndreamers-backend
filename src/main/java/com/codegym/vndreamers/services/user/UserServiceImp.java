package com.codegym.vndreamers.services.user;

import com.codegym.vndreamers.exceptions.UserExistException;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserCRUDService, UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public User findById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    @Override
    public User save(User user) throws SQLIntegrityConstraintViolationException, UserExistException {
        if (loadUserByUsername(user.getUsername()) != null) {
            throw new UserExistException();
        }
        if (user.getUsername() == null) {
            throw new SQLIntegrityConstraintViolationException();
        }
        return userRepository.save(user);
    }

    @Override
    public User update(User model) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElse(null);
    }
}