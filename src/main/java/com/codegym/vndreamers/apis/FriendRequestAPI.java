package com.codegym.vndreamers.apis;

import com.codegym.vndreamers.exceptions.EntityExistException;
import com.codegym.vndreamers.models.FriendRequest;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.friendrequest.FriendRequestService;
import com.codegym.vndreamers.services.user.UserCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FriendRequestAPI {

    public static final int NO_FRIEND_STATUS = 0;
    public static final int FRIEND_STATUS = 1;
    public static final int BLOCK_STATUS = 2;

    @Autowired
    private UserCRUDService userCRUDService;

    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping("/friends")
    public FriendRequest SendFriendRequest(@RequestBody User userReceive) throws SQLIntegrityConstraintViolationException, EntityExistException {
        User userSend = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FriendRequest isNullFriendRequest = friendRequestService.getFriendRequestByUserSensIdAndUserReceiveId(userSend.getId(), userReceive.getId());
        FriendRequest isNullReverseFriendRequest = friendRequestService.getFriendRequestByUserSensIdAndUserReceiveId(userReceive.getId(), userSend.getId());
        if (isNullFriendRequest == null && isNullReverseFriendRequest == null && userSend.getId() != userReceive.getId()) {
            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setUserSend(userSend);
            friendRequest.setUserReceive(userReceive);
            friendRequest.setStatus(NO_FRIEND_STATUS);
            return friendRequestService.save(friendRequest);
        } else {
            throw new EntityExistException();
        }
    }

    @PutMapping("/friends")
    public FriendRequest ConfirmFriendRequest(@RequestBody User userSend) throws SQLIntegrityConstraintViolationException, EntityExistException {
        User userConfirm = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FriendRequest friendRequest = friendRequestService.getFriendRequestByUserSensIdAndUserReceiveId(userSend.getId(), userConfirm.getId());
        if (friendRequest != null) {
            friendRequest.setStatus(FRIEND_STATUS);
            return friendRequestService.save(friendRequest);
        } else {
            return null;
        }
    }

    @DeleteMapping("/friends/{userOtherId}")
    public FriendRequest deleteFriendRequestOrDeleteFriend(@PathVariable int userOtherId) {
        User myUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        FriendRequest isNullFriendRequest = friendRequestService.getFriendRequestByUserSensIdAndUserReceiveId(myUser.getId(), userOtherId);
        FriendRequest isNullReverseFriendRequest = friendRequestService.getFriendRequestByUserSensIdAndUserReceiveId(userOtherId, myUser.getId());
        if (isNullFriendRequest != null) {
            friendRequestService.delete(isNullFriendRequest.getId());
            return isNullFriendRequest;
        } else if (isNullReverseFriendRequest != null) {
            friendRequestService.delete(isNullReverseFriendRequest.getId());
            return isNullReverseFriendRequest;
        } else {
            return null;
        }
    }

    @GetMapping("/friends/{userId}")
    public List<User> getAllFriend(@PathVariable int userId) {
        List<FriendRequest> friendRequests = friendRequestService.getAllFriendByUserId(userId, FRIEND_STATUS, userId, FRIEND_STATUS);
        List<User> userList = new ArrayList<>();
        for (FriendRequest friendRequest : friendRequests) {
            if (friendRequest.getUserSend().getId() != userId) {
                userList.add(friendRequest.getUserSend());
            } else {
                userList.add(friendRequest.getUserReceive());
            }
        }
        return userList;
    }

    @GetMapping("/friends/send")
    public List<User> getListUserMeRequest() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<User> userList = new ArrayList<>();
        List<FriendRequest> friendRequests = friendRequestService.getAllFriendRequestSentByUser(user.getId(), NO_FRIEND_STATUS);
        for (FriendRequest friendRequest : friendRequests) {
            userList.add(friendRequest.getUserReceive());
        }
        return userList;
    }

    @GetMapping("/friends/receive")
    public List<User> getListUserRequestToMe() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<User> userList = new ArrayList<>();
        List<FriendRequest> friendRequests = friendRequestService.getAllFriendRequestUserReceived(user.getId(), NO_FRIEND_STATUS);
        for (FriendRequest friendRequest : friendRequests) {
            userList.add(friendRequest.getUserSend());
        }
        return userList;
    }

    @ExceptionHandler(EntityExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleExistRequestException() {
        return "{\"error\":\"Friend Request Exist\"}";
    }
}
