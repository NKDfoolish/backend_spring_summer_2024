package com.nkd.identity_service.service;

import com.nkd.identity_service.dto.request.UserCreationRequest;
import com.nkd.identity_service.dto.response.UserResponse;
import com.nkd.identity_service.entity.User;
import com.nkd.identity_service.exception.AppException;
import com.nkd.identity_service.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;

    @BeforeEach
    void initData(){
        dob = LocalDate.of(2003, 07, 01);

        request = UserCreationRequest.builder()
                .username("John")
                .firstname("Nguyen")
                .lastname("Duy")
                .password("12345678")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("ABC123xyz")
                .username("John")
                .firstname("Nguyen")
                .lastname("Duy")
                .dob(dob)
                .build();

        user = User.builder()
                .id("ABC123xyz")
                .username("John")
                .firstname("Nguyen")
                .lastname("Duy")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success(){
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = userService.createUser(request);

        // THEN
        Assertions.assertThat(response.getId()).isEqualTo("ABC123xyz");
        Assertions.assertThat(response.getUsername()).isEqualTo("John");
    }

    @Test
    void createUser_userExisted_fail(){
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
//        Mockito.when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var exception = assertThrows(AppException.class,
                () -> userService.createUser(request));

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    }

    @Test
    @WithMockUser(username = "John")
    void getMyInfo_valid_success(){
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        var response = userService.getMyInfo();

        Assertions.assertThat(response.getUsername()).isEqualTo("John");
        Assertions.assertThat(response.getId()).isEqualTo("ABC123xyz");
    }

    @Test
    @WithMockUser(username = "John")
    void getMyInfo_userNotFound_error(){
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));

//        var response = userService.getMyInfo();

        var exception = assertThrows(AppException.class,
                () -> userService.getMyInfo());

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1005);
    }
}
