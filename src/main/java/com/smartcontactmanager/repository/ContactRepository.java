package com.smartcontactmanager.repository;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcontactmanager.entities.Contact;
import com.smartcontactmanager.entities.UserEntity;

import jakarta.transaction.Transactional;

public interface ContactRepository extends JpaRepository<Contact, Integer>{
	
	@Query("from Contact c where c.userEntity.id =:userEntityId")
	public Page<Contact> getContactByUserEntity(@Param("userEntityId")int userEntityId,Pageable pageable);
	
	@Transactional
	@Modifying
	@Query("update Contact c set c.name = :name, c.secondName = :secondName, c.work = :work, c.email = :email, c.phone = :phone, c.image = :image, c.description = :description where c.cId = :contactId")
	public void updateContactByCID(@Param("name") String name,@Param("secondName") String secondName,@Param("work") String work,@Param("email") String email,@Param("phone") String phone,@Param("image") String image,@Param("description") String description,@Param("contactId") int cId);
	
	@Query("select c.image from Contact c where c.cId =:contactId")
	public String getImageName(int contactId);
	
	public List<Contact> findByNameContainingAndAndUserEntity(String keywords,UserEntity userentity);
}
