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

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
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
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(false);
        Mockito.when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = userService.createUser(request);

        // THEN
        Assertions.assertThat(response.getId()).isEqualTo("ABC123xyz");
        Assertions.assertThat(response.getUsername()).isEqualTo("John");
    }

    @Test
    void createUser_userExisted_fail(){
        // GIVEN
        Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(true);
//        Mockito.when(userRepository.save(any())).thenReturn(user);

        // WHEN
        var exception = assertThrows(AppException.class,
                () -> userService.createUser(request));

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    }
}
