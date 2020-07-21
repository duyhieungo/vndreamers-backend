package com.codegym.vndreamers.security.jwt.issuer;

import com.codegym.vndreamers.dtos.JWTResponse;
import com.codegym.vndreamers.models.User;
import com.codegym.vndreamers.services.auth.AuthService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class JWTIssuerIntegrationTest {
    private static final String VALID_USERNAME = "some_valid_username";
    private static final String VALID_PASSWORD = "some_valid_password";
    private static final String VALID_EMAIL = "some_valid_email@example.com";
    private static final Timestamp VALID_BIRTH_DATE = Timestamp.valueOf(LocalDateTime.now());
    private static final String VALID_PHONE = "0912345678";
    public static final String VALID_AVATAR = "https://giaitri.vn/wp-content/uploads/2019/07/avatar-la-gi-01.jpg";
    public static final int STATUS_ACTIVE = 1;
    public static final String FAIL_PASSWORD = "some_fail_password";
    public static final String FAIL_USERNAME = "some_fail_username";
    private static final String FAIL_EMAIL = "some_fail_email";
    private static final String FAIL_BIRTH_DATE = "some_fail_date";
    public static User userMock;

    @Autowired
    AuthService authService;

    @BeforeAll
    static void mockUser() {
        userMock = new User();
        userMock.setPassword(VALID_PASSWORD);
        userMock.setEmail(VALID_EMAIL);
        userMock.setBirthDate(VALID_BIRTH_DATE);
        userMock.setUsername(VALID_USERNAME);
        userMock.setPhoneNumber(VALID_PHONE);
        userMock.setStatus(STATUS_ACTIVE);
        userMock.setImage(VALID_AVATAR);
    }

    @Test
    @DisplayName("Đăng ký trả về access_token")
    void shouldReturnAccessToken() {
        JWTResponse jwtResponse = authService.register(userMock);
        assertNotNull(jwtResponse.getAccessToken());
    }

    @Test
    @DisplayName("Đăng ký trả về user")
    void shouldReturnUserRegistered() {
        JWTResponse jwtResponse = authService.register(userMock);
        assertNotNull(jwtResponse.getUser());
    }
}
