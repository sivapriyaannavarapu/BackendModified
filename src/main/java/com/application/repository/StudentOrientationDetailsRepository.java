package com.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.StudentAcademicDetails;
import com.application.entity.StudentCourseDetails;
import com.application.entity.StudentOrientationDetails;

@Repository
public interface StudentOrientationDetailsRepository extends JpaRepository<StudentOrientationDetails, Integer>{
	
	Optional<StudentCourseDetails> findByStudentAcademicDetails(StudentAcademicDetails studentAcademicDetails);//studentorientationdetails table

}
