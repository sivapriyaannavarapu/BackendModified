package com.application.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.entity.StudentRelation;

@Repository
public interface StudentRelationRepository extends JpaRepository<StudentRelation, Integer>{
	
	 @Query("SELECT sr FROM StudentRelation sr WHERE sr.student_relation_type = :type")
	 Optional<StudentRelation> findByStudentRelationType(@Param("type") String studentRelationType);
}
