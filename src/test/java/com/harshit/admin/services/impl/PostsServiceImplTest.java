package com.harshit.admin.services.impl;

import com.harshit.admin.dtos.PostsDto;
import com.harshit.admin.dtos.UserDto;
import com.harshit.admin.entities.Posts;
import com.harshit.admin.entities.User;
import com.harshit.admin.exceptions.ResourceNotFoundException;
import com.harshit.admin.repositories.PostsRepo;
import com.harshit.admin.repositories.UserRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostsServiceImplTest {
	
	@Mock
	private PostsRepo postsRepo;
	
	@Mock
	private UserRepo userRepo;

	@InjectMocks
	private PostsServiceImpl postsService;
	
	@Mock
	private ModelMapper modelMapper;

	UserDto userDto = new UserDto(1, "harshit", "harshit@gmail.com", "Backend Dev");
	User user = new User(1, "harshit", "harshit@gmail.com", "Backend Dev", new ArrayList<>());
	Posts post = new Posts(1, "post content", "post category", new Date(), user);
	Posts post2 = new Posts(2, "post 2", "post category 2", new Date(), user);

	List<Posts> postsList = List.of(post, post2);
	PostsDto postsDto = new PostsDto(1, "post content", "post category", new Date(), userDto);
	PostsDto postsDto2 = new PostsDto(2, "post 2", "post category 2", new Date(), userDto);
	List<PostsDto> postsDtoList = List.of(postsDto, postsDto2);
	@Test
	public void createPostTest() {

		when(userRepo.findById(1)).thenReturn(Optional.of(user));
		when(postsService.dtoToPosts(postsDto)).thenReturn(post);
		post.setAddedDate(new Date());
		post.setUser(user);
		when(postsRepo.save(post)).thenReturn(post);
		when(postsService.postsToDto(post)).thenReturn(postsDto);
		PostsDto dto = postsService.createPost(postsDto, 1);
		assertNotNull(dto);
		assertEquals("post content", dto.getPostContent());
	}

	@Test
	public void createUser_failureCase(){
		when(userRepo.findById(-1)).thenThrow(new ResourceNotFoundException("User", "Id", -1));
		try{
			postsService.createPost(postsDto,-1);
		} catch (ResourceNotFoundException e){
			assertEquals("User not found with Id : -1", e.getMessage());
		}
	}

	@Test
	public void updatePostTest(){
		when(postsRepo.findById(1)).thenReturn(Optional.of(post));
		post.setPostContent(postsDto.getPostContent());
		post.setPostCategory(postsDto.getPostCategory());
		when(postsService.postsToDto(post)).thenReturn(postsDto);

		PostsDto dto = postsService.updatePost(postsDto, 1);
		assertEquals("post content", dto.getPostContent());
	}

	@Test
	public void updatePost_failureCase(){
		when(postsRepo.findById(-1)).thenThrow(new ResourceNotFoundException("Post", "Id", -1));
		try{
			postsService.updatePost(postsDto, -1);
		} catch (ResourceNotFoundException e){
			assertEquals("Post not found with Id : -1", e.getMessage());
		}
	}

	@Test
	public void getAllPosts(){
		when(postsRepo.findAll()).thenReturn(postsList);
		when(postsService.postsToDto(post)).thenReturn(postsDto);

		List<PostsDto> returnList = postsService.getAllPosts();
		assertEquals(returnList.size(), postsDtoList.size());
	}

	@Test
	public void deletePosts(){
		when(postsRepo.findById(1)).thenReturn(Optional.of(post));
		postsService.deletePost(1);
		Mockito.verify(postsRepo).delete(post);
	}

	@Test
	public void deletePost_failureCase(){
		when(postsRepo.findById(-1)).thenThrow(new ResourceNotFoundException("Post", "Id", -1));
		try{
			postsService.deletePost(-1);
		} catch (ResourceNotFoundException e){
			assertEquals("Post not found with Id : -1", e.getMessage());
		}
	}

	@Test
	public void getSinglePostTest(){
		when(postsRepo.findById(1)).thenReturn(Optional.of(post));
		when(postsService.postsToDto(post)).thenReturn(postsDto);

		PostsDto dto = postsService.getSinglePost(1);
		assertEquals("post content", dto.getPostContent());
	}

	@Test
	public void getSinglePost_failureCase(){
		when(postsRepo.findById(-1)).thenThrow(new ResourceNotFoundException("Post", "Id", -1));
		try{
			postsService.getSinglePost(-1);
		} catch (ResourceNotFoundException e){
			assertEquals("Post not found with Id : -1", e.getMessage());
		}
	}

	@Test
	public void getPostsByUserTest(){
		when(userRepo.findById(1)).thenReturn(Optional.of(user));
		when(postsRepo.findByUser(user)).thenReturn(postsList);
		when(postsService.postsToDto(post)).thenReturn(postsDto);

		List<PostsDto> dtos = postsService.getPostsByUser(1);
		assertEquals(postsDtoList.size(), dtos.size());
	}
	@Test
	public void getPostsByUser_failureCase(){
		when(userRepo.findById(-1)).thenThrow(new ResourceNotFoundException("User", "Id", -1));
		try{
			postsService.getPostsByUser(-1);
		} catch (ResourceNotFoundException e){
			assertEquals("User not found with Id : -1", e.getMessage());
		}
	}
}
