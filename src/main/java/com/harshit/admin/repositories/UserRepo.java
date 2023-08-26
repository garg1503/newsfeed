package com.harshit.admin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harshit.admin.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {

}
