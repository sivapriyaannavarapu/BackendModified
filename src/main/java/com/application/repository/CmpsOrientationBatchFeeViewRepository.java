package com.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.dto.BatchDTO;
import com.application.dto.ClassDTO;
import com.application.dto.GenericDropdownDTO;
import com.application.dto.OrientationBatchDetailsDTO;
import com.application.dto.OrientationDTO;
import com.application.entity.CmpsOrientationBatchFeeView;

@Repository
public interface CmpsOrientationBatchFeeViewRepository extends JpaRepository<CmpsOrientationBatchFeeView, Integer> {

	List<CmpsOrientationBatchFeeView> findByOrientationId(int orientationId);

	// Find a specific batch by its ID
	List<CmpsOrientationBatchFeeView> findByOrientationBatchId(int orientationBatchId);

	// Find sections by the batch name
	List<CmpsOrientationBatchFeeView> findByOrientationBatchName(String orientationBatchName);

	List<CmpsOrientationBatchFeeView> findByCmpsId(int cmpsId);

	@Query("SELECT new com.application.dto.OrientationDTO(c.orientationId, c.orientationName) "
			+ "FROM CmpsOrientationBatchFeeView c WHERE c.classId = :classId")
	List<OrientationDTO> findOrientationsByClassId(int classId);

	@Query("SELECT DISTINCT new com.application.dto.ClassDTO(c.classId, c.className) "
			+ "FROM CmpsOrientationBatchFeeView c WHERE c.cmpsId = :campusId")
	List<ClassDTO> findClassesByCampusId(int campusId);

	List<CmpsOrientationBatchFeeView> findByOrientationIdAndOrientationBatchId(int orientationId,
			int orientationBatchId);

	@Query("SELECT DISTINCT new com.application.dto.GenericDropdownDTO(c.studyTypeId, c.studyTypeName) "
			+ "FROM CmpsOrientationBatchFeeView c " + "WHERE c.cmpsId = :cmpsId AND c.classId = :classId")
	List<GenericDropdownDTO> findDistinctStudyTypesByCmpsIdAndClassId(@Param("cmpsId") int cmpsId,
			@Param("classId") int classId);

	@Query("SELECT DISTINCT new com.application.dto.GenericDropdownDTO(c.orientationId, c.orientationName) "
			+ "FROM CmpsOrientationBatchFeeView c "
			+ "WHERE c.cmpsId = :cmpsId AND c.classId = :classId AND c.studyTypeId = :studyTypeId")
	List<GenericDropdownDTO> findDistinctOrientationsByCmpsIdAndClassIdAndStudyTypeId(@Param("cmpsId") int cmpsId,
			@Param("classId") int classId, @Param("studyTypeId") int studyTypeId);

	@Query("SELECT DISTINCT new com.application.dto.GenericDropdownDTO(c.orientationBatchId, c.orientationBatchName) "
			+ "FROM CmpsOrientationBatchFeeView c "
			+ "WHERE c.cmpsId = :cmpsId AND c.classId = :classId AND c.studyTypeId = :studyTypeId AND c.orientationId = :orientationId")
	List<GenericDropdownDTO> findDistinctOrientationBatchesByCmpsIdAndClassIdAndStudyTypeIdAndOrientationId(
			@Param("cmpsId") int cmpsId, @Param("classId") int classId, @Param("studyTypeId") int studyTypeId,
			@Param("orientationId") int orientationId);

	@Query("SELECT new com.application.dto.OrientationBatchDetailsDTO(c.orientationStartDate, c.orientationEndDate, c.orientationFee) "
			+ "FROM CmpsOrientationBatchFeeView c "
			+ "WHERE c.cmpsId = :cmpsId AND c.classId = :classId AND c.studyTypeId = :studyTypeId AND c.orientationId = :orientationId AND c.orientationBatchId = :orientationBatchId")
	Optional<OrientationBatchDetailsDTO> findBatchDetailsByAllCriteria(@Param("cmpsId") int cmpsId,
			@Param("classId") int classId, @Param("studyTypeId") int studyTypeId,
			@Param("orientationId") int orientationId, @Param("orientationBatchId") int orientationBatchId);
}
