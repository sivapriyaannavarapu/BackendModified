package com.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.entity.StudentAcademicDetails;

@Repository
public interface StudentAcademicDetailsRepository  extends JpaRepository<StudentAcademicDetails, Integer>{

	Optional<StudentAcademicDetails> findByStudAdmsNo(String studAdmissionNo);
	
	@Query("SELECT sad.stud_adms_id FROM StudentAcademicDetails sad WHERE sad.studAdmsNo = ?1")
    Optional<Integer> findIdByStudAdmsNo(String studAdmsNo);
	
	boolean existsByStudAdmsNo(String studAdmsNo);
	
	



}
