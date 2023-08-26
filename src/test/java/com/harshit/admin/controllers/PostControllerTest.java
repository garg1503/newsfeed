package com.harshit.admin.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harshit.admin.dtos.PostsDto;
import com.harshit.admin.dtos.UserDto;
import com.harshit.admin.entities.Posts;
import com.harshit.admin.entities.User;
import com.harshit.admin.services.PostsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PostControllerTest {

    @Mock
    private PostsService postsService;

    @InjectMocks
    private PostController postController;

    private MockMvc mock;

    private ObjectMapper mapper;

    UserDto userDto = new UserDto(1, "harshit", "harshit@gmail.com", "Backend Dev");
    User user = new User(1, "harshit", "harshit@gmail.com", "Backend Dev", new ArrayList<>());
    Posts post = new Posts(1, "post content", "post category", new Date(), user);
    Posts post2 = new Posts(2, "post 2", "post category 2", new Date(), user);

    List<Posts> postsList = List.of(post, post2);
    PostsDto postsDto = new PostsDto(1, "post content", "post category", new Date(), userDto);
    PostsDto postsDto2 = new PostsDto(2, "post 2", "post category 2", new Date(), userDto);
    List<PostsDto> postsDtoList = List.of(postsDto, postsDto2);

    @BeforeEach
    void setup() {
        this.mapper = new ObjectMapper();
        mock = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    public void createPostTest() {
        assertDoesNotThrow(()-> {
            when(postsService.createPost(postsDto,1)).thenReturn(postsDto);
            RequestBuilder reqBuilder = MockMvcRequestBuilders
                    .post("/posts/1")
                    .content(mapper.writeValueAsString(postsDto))
                    .contentType(MediaType.APPLICATION_JSON);
            mock.perform(reqBuilder)
                    .andExpect(status().isCreated());
        });
    }

    @Test
    public void updatePostTest(){
        assertDoesNotThrow(()-> {
            when(postsService.updatePost(postsDto, 1)).thenReturn(postsDto);
            RequestBuilder reqBuilder = MockMvcRequestBuilders
                    .put("/posts/1")
                    .content(mapper.writeValueAsString(postsDto))
                    .contentType(MediaType.APPLICATION_JSON);
            mock.perform(reqBuilder)
                    .andExpect(status().isOk());
        });
    }

    @Test
    public void getSinglePostTest(){
        assertDoesNotThrow(()-> {
            when(postsService.getSinglePost(1)).thenReturn(postsDto);
            RequestBuilder reqBuilder = MockMvcRequestBuilders
                    .get("/posts/1")
                    .contentType(MediaType.APPLICATION_JSON);
            mock.perform(reqBuilder)
                    .andExpect(status().isOk());
        });
    }

    @Test
    public void getAllPostsTest(){
        assertDoesNotThrow(()-> {
            when(postsService.getAllPosts()).thenReturn(postsDtoList);
            RequestBuilder reqBuilder = MockMvcRequestBuilders
                    .get("/posts/")
                    .contentType(MediaType.APPLICATION_JSON);
            mock.perform(reqBuilder)
                    .andExpect(jsonPath("$[0].postContent", is("post content")));
        });
    }

    @Test
    public void deletePostTest(){
        assertDoesNotThrow(()-> {
            doNothing().when(postsService).deletePost(2);
            RequestBuilder reqBuilder = MockMvcRequestBuilders
                    .delete("/posts/2")
                    .contentType(MediaType.APPLICATION_JSON);
            mock.perform(reqBuilder)
                    .andExpect(status().isOk());
        });
    }

    @Test
    public void getPostsByUserTest(){
        assertDoesNotThrow(()-> {
            when(postsService.getPostsByUser(1)).thenReturn(postsDtoList);
            RequestBuilder reqBuilder = MockMvcRequestBuilders
                    .get("/posts/users/1")
                    .contentType(MediaType.APPLICATION_JSON);
            mock.perform(reqBuilder)
                    .andExpect(status().isOk());
        });
    }
}
