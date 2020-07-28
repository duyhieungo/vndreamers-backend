package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.user.UserCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class UserAPI {
    @Autowired
    private UserCRUDService userCRUDService;

    @GetMapping("/users/{username}")
    public User getUserByUsername(@PathVariable("username") String username) {
        return userCRUDService.findByUsername(username);
    }

    @PutMapping("/users")
    public User updateProfileUser(@RequestBody User user) throws SQLIntegrityConstraintViolationException, EntityExistException {
        User userToken = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userToken.getId() == user.getId()) {
            User myUser = userCRUDService.findById(userToken.getId());
            myUser.setFirstName(user.getFirstName());
            myUser.setLastName(user.getLastName());
            myUser.setBirthDate(user.getBirthDate());
            myUser.setConfirmPassword(myUser.getPassword());
            myUser.setUsername(user.getUsername());
            myUser.setAddress(user.getAddress());
            myUser.setPhoneNumber(user.getPhoneNumber());
            myUser.setAboutMe(user.getAboutMe());
            return userCRUDService.updateProfileUser(myUser);
        } else {
            return null;
        }
    }

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
}
