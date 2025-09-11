package com.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.application.entity.CmpsOrientationProgramView;

@Repository
public interface CmpsOrientationProgramViewRepository extends JpaRepository<CmpsOrientationProgramView, Integer>{
	
	List<CmpsOrientationProgramView> findByOrientationId(int orientationId);

	    
	    // Custom method to find all data related to a specific program
	List<CmpsOrientationProgramView> findByProgramId(int programId);

}
