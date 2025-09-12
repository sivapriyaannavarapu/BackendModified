package com.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.entity.CmpsOrientationBatchFeeView;

@Repository
public interface CmpsOrientationBatchFeeViewRepository extends JpaRepository<CmpsOrientationBatchFeeView, Integer>{
	
	 List<CmpsOrientationBatchFeeView> findByOrientationId(int orientationId);

	    // Find a specific batch by its ID
	 List<CmpsOrientationBatchFeeView> findByOrientationBatchId(int orientationBatchId);

	    // Find sections by the batch name
	    List<CmpsOrientationBatchFeeView> findByOrientationBatchName(String orientationBatchName);
	    
	    List<CmpsOrientationBatchFeeView> findByCmpsId(int cmpsId);
}
