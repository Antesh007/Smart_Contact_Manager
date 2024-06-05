package com.smartcontactmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcontactmanager.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity,Integer>{
	
	@Query("select u from UserEntity u where u.email = :email")
	public UserEntity getUserEntityByUserName(@Param("email") String email);
	
}
