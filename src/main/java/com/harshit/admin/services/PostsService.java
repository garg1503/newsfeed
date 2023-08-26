package com.harshit.admin.services;

import java.util.List;

import com.harshit.admin.dtos.PostsDto;

public interface PostsService {
	
	PostsDto createPost(PostsDto postDto, Integer userId);
	PostsDto updatePost(PostsDto postDto, Integer postId);
	PostsDto getSinglePost(Integer postId);
	List<PostsDto> getAllPosts();
	void deletePost(Integer postId);
	List<PostsDto> getPostsByUser(Integer userId);

}
