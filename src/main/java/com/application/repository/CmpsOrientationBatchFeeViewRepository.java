package com.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.application.dto.ClassDTO;
import com.application.dto.OrientationDTO;
import com.application.entity.CmpsOrientationBatchFeeView;

@Repository
public interface CmpsOrientationBatchFeeViewRepository extends JpaRepository<CmpsOrientationBatchFeeView, Integer>{
	
	 List<CmpsOrientationBatchFeeView> findByOrientationId(int orientationId);

	    // Find a specific batch by its ID
	 List<CmpsOrientationBatchFeeView> findByOrientationBatchId(int orientationBatchId);

	    // Find sections by the batch name
	    List<CmpsOrientationBatchFeeView> findByOrientationBatchName(String orientationBatchName);
	    
	    List<CmpsOrientationBatchFeeView> findByCmpsId(int cmpsId);
	    @Query("SELECT new com.application.dto.OrientationDTO(c.orientationId, c.orientationName) " +
	            "FROM CmpsOrientationBatchFeeView c WHERE c.classId = :classId")
	     List<OrientationDTO> findOrientationsByClassId(int classId);
	    
	    @Query("SELECT DISTINCT new com.application.dto.ClassDTO(c.classId, c.className) " +
	            "FROM CmpsOrientationBatchFeeView c WHERE c.cmpsId = :campusId")
	     List<ClassDTO> findClassesByCampusId(int campusId);
	    
	    List<CmpsOrientationBatchFeeView> findByOrientationIdAndOrientationBatchId(int orientationId, int orientationBatchId);
}
