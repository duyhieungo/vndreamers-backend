package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.user.UserCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class AdminUserAPI {

    public static final int BLOCK_STATUS = 2;

    @Autowired
    private UserCRUDService userCRUDService;

    @GetMapping("/users/date/{quantity}")
    public List<User> getAllByDate(@PathVariable("quantity") int date) {
        long currentTime = System.currentTimeMillis();
        long currentTimeWant = date * 24 * 60 * 60 * 1000;
        long timeWant = currentTime - currentTimeWant;
        Timestamp dateWant = new Timestamp(timeWant);
        System.out.println(dateWant);
        List<User> users = userCRUDService.getAllUserByTimeStamp(dateWant);
        return users;
    }

    @GetMapping("/users")
    public List<User> getAllUser() {
        return userCRUDService.findAll();
    }

    @DeleteMapping("/users/{id}")
    public boolean deleteUserById(@PathVariable int id) {
        User user = userCRUDService.findById(id);
        if (user != null) {
            boolean isDeleted = userCRUDService.delete(id);
            if (isDeleted) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @DeleteMapping("/users/block/{id}")
    public User blockUserById(@PathVariable int id) throws SQLIntegrityConstraintViolationException, EntityExistException {
        User user = userCRUDService.findById(id);
        user.setStatus(BLOCK_STATUS);
        user.setConfirmPassword(user.getPassword());
        return userCRUDService.updateProfileUser(user);
    }
}
