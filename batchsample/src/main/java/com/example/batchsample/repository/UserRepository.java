package com.example.batchsample.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.batchsample.entity.User;



@Repository
public interface UserRepository  extends CrudRepository<User,Integer> {
}