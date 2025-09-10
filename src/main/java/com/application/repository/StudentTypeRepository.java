package com.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.StudentType;

@Repository
public interface StudentTypeRepository extends JpaRepository<StudentType, Integer>{

}
