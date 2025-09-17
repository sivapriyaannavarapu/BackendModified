package com.application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.entity.Distribution;

@Repository
public interface DistributionRepository extends JpaRepository<Distribution, Integer> {

    @Query(value = "SELECT MAX(d.app_end_no) FROM sce_application.sce_app_distrubution d WHERE d.state_id = :stateId AND d.created_by = :userId AND d.acdc_year_id = :academicYearId", nativeQuery = true)
    Integer findMaxAppEndNo(@Param("stateId") int stateId, @Param("userId") int userId, @Param("academicYearId") int academicYearId);
    
    @Query(value = "SELECT * FROM sce_application.sce_app_distrubution d WHERE d.created_by = :empId", nativeQuery = true)
    List<Distribution> findByCreatedBy(@Param("empId") int empId); 

}