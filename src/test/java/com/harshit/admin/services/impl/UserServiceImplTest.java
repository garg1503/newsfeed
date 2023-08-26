package com.harshit.admin.services.impl;

import com.harshit.admin.dtos.UserDto;
import com.harshit.admin.entities.User;
import com.harshit.admin.exceptions.ResourceNotFoundException;
import com.harshit.admin.repositories.UserRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private ModelMapper modelMapper;

    UserDto userDto = new UserDto(1, "userdto", "userdto@gmail.com", "iamuserdto");
    UserDto userDto2 = new UserDto(2, "user2", "user2@gmail.com", "iamuser2");
    User user = new User(1, "userdto", "userdto@gmail.com", "iamuserdto", new ArrayList<>());
    User user2 = new User(2, "user2", "user2@gmail.com", "iamuser2", new ArrayList<>());

    List<User> users = List.of(user, user2);
    List<UserDto> userDtos = List.of(userDto, userDto2);

    @Test
    public void createUserTest(){
        when(userService.dtoToUser(userDto)).thenReturn(user);
        when(userRepo.save(user)).thenReturn(user);
        when(userService.userToDto(user)).thenReturn(userDto);

        UserDto dto = userService.createUser(userDto);
        assertNotNull(dto);
        assertEquals("userdto", dto.getUserName());
    }

    @Test
    public void updateUserTest(){
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        user.setUserName(userDto.getUserName());
        user.setUserEmail(userDto.getUserEmail());
        user.setUserAbout(userDto.getUserAbout());
        when(userRepo.save(user)).thenReturn(user);
        when(userService.userToDto(user)).thenReturn(userDto);

        UserDto dto = userService.updateUser(userDto,1);
        assertNotNull(dto);
        assertEquals("iamuserdto", dto.getUserAbout());
    }
    @Test
    public void updateUser_failureCase(){
        when(userRepo.findById(-1)).thenThrow(new ResourceNotFoundException("User", "Id", -1));
        try{
            userService.updateUser(userDto, -1);
        } catch (ResourceNotFoundException e){
            assertEquals("User not found with Id : -1", e.getMessage());
        }
    }

    @Test
    public void getAllUsersTest(){
        when(userRepo.findAll()).thenReturn(users);
        when(userService.userToDto(user)).thenReturn(userDto);

        List<UserDto> returnList = userService.getAllUsers();
        assertEquals(returnList.size(), userDtos.size());
    }

    @Test
    public void deleteUserTest(){
        when(userRepo.findById(1)).thenReturn(Optional.of(user));

        userService.deleteUser(1);
        Mockito.verify(userRepo).delete(user);
    }
    @Test
    public void deleteUser_failureCase(){
        when(userRepo.findById(-1)).thenThrow(new ResourceNotFoundException("User", "Id", -1));
        try{
            userService.deleteUser(-1);
        } catch (ResourceNotFoundException e){
            assertEquals("User not found with Id : -1", e.getMessage());
        }
    }

    @Test
    public void getUserByIdTest(){
        when(userRepo.findById(1)).thenReturn(Optional.of(user));
        when(userService.userToDto(user)).thenReturn(userDto);

        UserDto dto = userService.getUserById(1);
        assertEquals("userdto", dto.getUserName());
    }
    @Test
    public void getUserById_failureCase(){
        when(userRepo.findById(-1)).thenThrow(new ResourceNotFoundException("User", "Id", -1));
        try{
            userService.getUserById(-1);
        } catch (ResourceNotFoundException e){
            assertEquals("User not found with Id : -1", e.getMessage());
        }
    }
}
