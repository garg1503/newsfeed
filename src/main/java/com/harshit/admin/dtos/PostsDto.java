package com.harshit.admin.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostsDto {

	private int postId;
	private String postContent;
	private String postCategory;
	private Date addedDate;
	private UserDto user;
}
