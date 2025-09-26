//package com.application.repository;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import com.application.entity.Distribution;
//
//@Repository
//public interface DistributionRepository extends JpaRepository<Distribution, Integer> {
//
//    @Query(value = "SELECT MAX(d.app_end_no) FROM sce_application.sce_app_distrubution d WHERE d.state_id = :stateId AND d.created_by = :userId AND d.acdc_year_id = :academicYearId", nativeQuery = true)
//    Integer findMaxAppEndNo(@Param("stateId") int stateId, @Param("userId") int userId, @Param("academicYearId") int academicYearId);
//    
//    @Query(value = "SELECT * FROM sce_application.sce_app_distrubution d WHERE d.created_by = :empId", nativeQuery = true)
//    List<Distribution> findByCreatedBy(@Param("empId") int empId); 
//    
// // In DistributionRepository.java
////    Optional<Integer> findMaxAppEndNoByIssuedToEmpIdAndAcademicYearId(int empId, int acdYearId);
//    
//    @Query("SELECT MAX(d.appEndNo) FROM Distribution d WHERE d.issued_to_emp_id = :empId AND d.academicYear.acdcYearId = :acdYearId AND d.isActive = 1")
//    Optional<Integer> findMaxAppEndNoByIssuedToEmpIdAndAcademicYearId(@Param("empId") int empId, @Param("acdYearId") int acdYearId);
//    
//    @Query("SELECT MIN(d.appStartNo) FROM Distribution d WHERE d.issued_to_emp_id = :empId AND d.academicYear.acdcYearId = :acdYearId AND d.isActive = 1")
//    Optional<Integer> findMinAppStartNoByIssuedToEmpIdAndAcademicYearId(@Param("empId") int empId, @Param("acdYearId") int acdYearId);
//}
package com.application.repository;

import com.application.entity.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistributionRepository extends JpaRepository<Distribution, Integer> {

	@Query(value = "SELECT MAX(d.app_end_no) FROM sce_application.sce_app_distrubution d WHERE d.state_id = :stateId AND d.created_by = :userId AND d.acdc_year_id = :academicYearId", nativeQuery = true)
	Integer findMaxAppEndNo(@Param("stateId") int stateId, @Param("userId") int userId,
			@Param("academicYearId") int academicYearId);

	@Query(value = "SELECT * FROM sce_application.sce_app_distrubution d WHERE d.created_by = :empId", nativeQuery = true)
	List<Distribution> findByCreatedBy(@Param("empId") int empId);

	// --- EXISTING METHODS (use these names) ---
	@Query("SELECT MAX(d.appEndNo) FROM Distribution d WHERE d.issued_to_emp_id = :empId AND d.academicYear.acdcYearId = :acdYearId AND d.isActive = 1")
	Optional<Integer> findMaxAppEndNoByIssuedToEmpIdAndAcademicYearId(@Param("empId") int empId,
			@Param("acdYearId") int acdYearId);

	@Query("SELECT MIN(d.appStartNo) FROM Distribution d WHERE d.issued_to_emp_id = :empId AND d.academicYear.acdcYearId = :acdYearId AND d.isActive = 1")
	Optional<Integer> findMinAppStartNoByIssuedToEmpIdAndAcademicYearId(@Param("empId") int empId,
			@Param("acdYearId") int acdYearId);

	// --- REQUIRED METHODS (add these) ---
	@Query("SELECT SUM(d.totalAppCount) FROM Distribution d WHERE d.issued_to_emp_id = :employeeId AND d.academicYear.acdcYearId = :academicYearId")
	Optional<Integer> sumTotalAppCountByIssuedToEmpId(@Param("employeeId") int employeeId,
			@Param("academicYearId") int academicYearId);

	@Query("SELECT SUM(d.totalAppCount) FROM Distribution d WHERE d.created_by = :employeeId AND d.academicYear.acdcYearId = :academicYearId")
	Optional<Integer> sumTotalAppCountByCreatedBy(@Param("employeeId") int employeeId,
			@Param("academicYearId") int academicYearId);

	@Query("SELECT d FROM Distribution d " + "WHERE d.academicYear.acdcYearId = :academicYearId "
			+ "AND d.appStartNo <= :endNo AND d.appEndNo >= :startNo")
	List<Distribution> findOverlappingDistributions(@Param("academicYearId") int academicYearId,
			@Param("startNo") int startNo, @Param("endNo") int endNo);
	
	

}