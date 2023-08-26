package com.harshit.admin.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harshit.admin.dtos.UserDto;
import com.harshit.admin.entities.User;
import com.harshit.admin.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mock;

    private ObjectMapper mapper;

    UserDto userDto = new UserDto(1, "userdto", "userdto@gmail.com", "iamuserdto");
    UserDto userDto2 = new UserDto(2, "user2", "user2@gmail.com", "iamuser2");
    User user = new User(1, "userdto", "userdto@gmail.com", "iamuserdto", new ArrayList<>());
    User user2 = new User(2, "user2", "user2@gmail.com", "iamuser2", new ArrayList<>());

    List<User> users = List.of(user, user2);
    List<UserDto> userDtos = List.of(userDto, userDto2);

    @BeforeEach
    void setup() {
        this.mapper = new ObjectMapper();
        mock = MockMvcBuilders.standaloneSetup(userController).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createUser() {
        assertDoesNotThrow(()-> {
            when(userService.createUser(userDto)).thenReturn(userDto);
            RequestBuilder reqBuilder = MockMvcRequestBuilders
                    .post("/users/")
                    .content(mapper.writeValueAsString(userDto))
                    .contentType(MediaType.APPLICATION_JSON);
            mock.perform(reqBuilder)
                    .andExpect(status().isCreated());
        });
    }

    @Test
    public void updateUser(){
        assertDoesNotThrow(()-> {
            when(userService.updateUser(userDto, 1)).thenReturn(this.userDto);
            RequestBuilder reqBuilder = MockMvcRequestBuilders
                    .put("/users/1")
                    .content(mapper.writeValueAsString(this.userDto))
                    .contentType(MediaType.APPLICATION_JSON);
            mock.perform(reqBuilder)
                    .andExpect(status().isOk());
        });
    }

    @Test
    public void getSingleUserTest(){
        assertDoesNotThrow(()-> {
            when(userService.getUserById(2)).thenReturn(userDto);
            RequestBuilder reqBuilder = MockMvcRequestBuilders
                    .get("/users/2")
                    .contentType(MediaType.APPLICATION_JSON);
            mock.perform(reqBuilder)
                    .andExpect(status().isOk());
        });
    }

    @Test
    public void getAllUserTest(){
        assertDoesNotThrow(()-> {
            when(userService.getAllUsers()).thenReturn(userDtos);
            RequestBuilder reqBuilder = MockMvcRequestBuilders
                    .get("/users/")
                    .contentType(MediaType.APPLICATION_JSON);
            mock.perform(reqBuilder)
                    .andExpect(jsonPath("$[0].userName", is("userdto")));
        });
    }

    @Test
    public void deleteUser(){
        assertDoesNotThrow(()-> {
            doNothing().when(userService).deleteUser(2);
            RequestBuilder reqBuilder = MockMvcRequestBuilders
                    .delete("/users/2")
                    .contentType(MediaType.APPLICATION_JSON);
            mock.perform(reqBuilder)
                    .andExpect(status().isOk());
        });
    }
}