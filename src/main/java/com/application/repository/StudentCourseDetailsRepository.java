package com.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.StudentAcademicDetails;
import com.application.entity.StudentCourseDetails;

@Repository
public interface StudentCourseDetailsRepository extends JpaRepository<StudentCourseDetails, Integer>{
	
	Optional<StudentCourseDetails> findByStudentAcademicDetails(StudentAcademicDetails studentAcademicDetails);

}
