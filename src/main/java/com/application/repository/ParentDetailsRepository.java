package com.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.ParentDetails;
import com.application.entity.StudentAcademicDetails;

@Repository
public interface ParentDetailsRepository extends JpaRepository<ParentDetails, Integer>{
	 List<ParentDetails> findByStudentAcademicDetails(StudentAcademicDetails academicDetails);
}
