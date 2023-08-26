package com.harshit.admin.services.impl;

import com.harshit.admin.dtos.PostsDto;
import com.harshit.admin.entities.Posts;
import com.harshit.admin.entities.User;
import com.harshit.admin.exceptions.ResourceNotFoundException;
import com.harshit.admin.repositories.PostsRepo;
import com.harshit.admin.repositories.UserRepo;
import com.harshit.admin.services.PostsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostsServiceImpl implements PostsService{
	
	@Autowired
	private PostsRepo postsRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PostsDto createPost(PostsDto postDto, Integer userId) {
		User user = userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "Id", userId));
		Posts post = dtoToPosts(postDto);
		post.setAddedDate(new Date());
		post.setUser(user);
		Posts savedPost = postsRepo.save(post);
		return postsToDto(savedPost);
	}

	@Override
	public PostsDto updatePost(PostsDto postDto, Integer postId) {
		Posts post = postsRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "Id", postId));
		post.setPostContent(postDto.getPostContent());
		post.setPostCategory(postDto.getPostCategory());
		return postsToDto(post);
	}

	@Override
	public PostsDto getSinglePost(Integer postId) {
		Posts post = postsRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "Id", postId));
		return postsToDto(post);
	}

	@Override
	public List<PostsDto> getAllPosts() {
		List<Posts> posts = postsRepo.findAll();
		List<PostsDto> postsDtos = posts.stream().map(post -> postsToDto(post)).collect(Collectors.toList());
		return postsDtos;
	}

	@Override
	public void deletePost(Integer postId) {
		Posts post = postsRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "Id", postId));
		postsRepo.delete(post);
	}
	
	public Posts dtoToPosts(PostsDto postsDto) {
		Posts post = modelMapper.map(postsDto, Posts.class);
		return post;
	}
	
	public PostsDto postsToDto(Posts post) {
		PostsDto postsDto = modelMapper.map(post, PostsDto.class);
		return postsDto;
	}

	@Override
	public List<PostsDto> getPostsByUser(Integer userId) {
		User user = userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "Id", userId));
		List<Posts> posts = postsRepo.findByUser(user);
		List<PostsDto> postsDtos = posts.stream().map(post -> postsToDto(post)).collect(Collectors.toList());
		return postsDtos;
	}

}
