package com.harshit.admin.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harshit.admin.entities.Posts;
import com.harshit.admin.entities.User;


public interface PostsRepo extends JpaRepository<Posts, Integer> {

	List<Posts> findByUser(User user);
}
