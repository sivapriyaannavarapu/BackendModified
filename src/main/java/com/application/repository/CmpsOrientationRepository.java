package com.application.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.application.entity.CmpsCourseTrack;
import com.application.entity.CmpsOrientation;
import com.application.entity.CourseBatch;
import com.application.entity.CourseTrack;

@Repository
public interface CmpsOrientationRepository extends JpaRepository<CmpsOrientation, Integer>{
	
	 Optional<CmpsCourseTrack> findByCourseTrack_CourseTrackId(int courseTrackId);//cmpsorientation table
	 Optional<CmpsCourseTrack> findByCourseTrack(CourseTrack courseTrack);//orientation table

	 @Query("SELECT c FROM CmpsCourseTrack c WHERE c.courseTrack.courseTrackId = :courseTrackId AND c.cmpsId = :cmpsId AND c.acdcYearId = :acdcYearId")
	 Optional<CmpsCourseTrack> findByTrackAndCmpsAndYear(
	     @Param("courseTrackId") Integer courseTrackId,
	     @Param("cmpsId") Integer cmpsId,
	     @Param("acdcYearId") Integer acdcYearId);//cmpsorientation table
	 
	 @Query("SELECT c.course_fee FROM CmpsCourseTrack c WHERE c.courseTrack.courseTrackId = :courseTrackId")
	    Float findCourseFeeByCourseTrackId(@Param("courseTrackId") int courseTrackId);
	 
	 Optional<CmpsCourseTrack> findByCmpsIdAndCourseTrack_CourseTrackIdAndCourseBatch_CourseBatchId(
		        int cmpsId, int courseTrackId, int courseBatchId
		    );//cmps orientation table
	 
	 @Query("SELECT c.courseBatch FROM CmpsCourseTrack c WHERE c.courseTrack.courseTrackId = :courseTrackId")
	    List<CourseBatch> findCourseBatchesByCourseTrackId(int courseTrackId);//orientation batch table

	 
	 
}
