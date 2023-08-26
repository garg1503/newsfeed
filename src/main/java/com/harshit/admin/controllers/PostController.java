package com.harshit.admin.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harshit.admin.dtos.PostsDto;
import com.harshit.admin.services.PostsService;

@RequestMapping("/posts")
@RestController

public class PostController {

	@Autowired
	private PostsService postsService;
	
	@PostMapping("/{userId}")
	public ResponseEntity<PostsDto> createPost(@RequestBody PostsDto postsDto, @PathVariable ("userId") Integer uid){
		PostsDto postsDto2 = postsService.createPost(postsDto, uid);
		return new ResponseEntity<PostsDto>(postsDto2, HttpStatus.CREATED);
	}
	
	@PutMapping("/{postId}")
	public ResponseEntity<PostsDto> updatePost(@RequestBody PostsDto postsDto, @PathVariable ("postId") Integer pid){
		PostsDto uPostsDto = postsService.updatePost(postsDto, pid);
		return ResponseEntity.ok(uPostsDto);
	}
	
	@GetMapping("/{postId}")
	public ResponseEntity<PostsDto> getSinglePost(@PathVariable ("postId") Integer pid){
		PostsDto postsDto = postsService.getSinglePost(pid);
		return ResponseEntity.ok(postsDto);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<PostsDto>> getAllPosts(){
		List<PostsDto> postsDtos = postsService.getAllPosts();
		return ResponseEntity.ok(postsDtos);
	}
	
	@DeleteMapping("/{postId}")
	public ResponseEntity<?> deletePost(@PathVariable ("postId") Integer pid){
		postsService.deletePost(pid);
		return new ResponseEntity<>("Post Deleted", HttpStatus.OK);
	}
	
	@GetMapping("/users/{userId}")
	public ResponseEntity<List<PostsDto>> getPostByUser(@PathVariable ("userId") Integer uid){
		List<PostsDto> userPostsDtos = postsService.getPostsByUser(uid);
		return ResponseEntity.ok(userPostsDtos);
	}
}
