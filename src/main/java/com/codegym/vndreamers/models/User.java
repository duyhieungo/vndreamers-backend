package com.codegym.vndreamers.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "user", schema = "vndreamers")
@Data
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private int id;

    @Basic
    @Column(name = "first_name", nullable = false, length = 50)
    @Pattern(regexp = "\\w+")
    @Size(max = 50, min = 3)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = false, length = 50)
    @Pattern(regexp = "\\w+")
    @Size(max = 50, min = 3)
    private String lastName;

    @Basic
    @Column(name = "phone_number")
    @Size(min = 10, max = 11)
    @Pattern(regexp = "((0|\\+[1-9]{1,3})+([0-9]{9,10}))")
    @JsonProperty(value = "phone")
    private String phoneNumber;

    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "username", nullable = false, length = 50)
    @Size(min = 6, max = 50)
    private String username;

    @Basic
    @Column(name = "email", nullable = false, length = 100)
    @Email
    private String email;

    @Basic
    @Column(name = "password", nullable = false, length = 50)
    @Size(max = 50, min = 8)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Transient
    @Size(max = 50, min = 8)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "confirm_password")
    private String confirmPassword;

    @Basic
    @Column(name = "birth_date", nullable = false)
    @JsonProperty(value = "birth_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Timestamp birthDate;

    @Basic
    @Column(name = "gender", nullable = false)
    private int gender;

    @Basic
    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate = Timestamp.valueOf(LocalDateTime.now());

    @Basic
    @Column(name = "modify_date", nullable = false)
    private Timestamp modifyDate = Timestamp.valueOf(LocalDateTime.now());

    @Basic
    @Column(name = "status", nullable = false)
    private int status;

    @Basic
    @Column(name = "image")
    @JsonProperty(value = "avatar")
    private String image;

    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "user")
    private Set<Post> posts;

    @OneToMany(mappedBy = "user")
    private Set<PostReaction> postLikes;

    @OneToMany(mappedBy = "user")
    private Set<CommentReaction> commentLikes;

    @OneToMany(mappedBy = "userSend")
    private Set<FriendRequest> requestsThisUserSent;

    @OneToMany(mappedBy = "userReceive")
    private Set<FriendRequest> requestsThisUserReceived;

    @ManyToMany
    @JoinTable(
            name = "role_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;
}
