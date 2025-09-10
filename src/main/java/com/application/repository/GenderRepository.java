package com.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.Gender;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Integer>{

	Optional<Gender> findByGenderName(String gender);

}
