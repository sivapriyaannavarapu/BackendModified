package com.application.repository;
 
import java.time.LocalDate; // <-- Make sure to import LocalDate
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.application.dto.AppStatusTrackDTO;
import com.application.entity.AppStatusTrack;
 
@Repository
public interface AppStatusTrackRepository extends JpaRepository<AppStatusTrack, Integer> {
 
 
    
    // Your other existing method
    @Query("SELECT new com.application.dto.AppStatusTrackDTO(" +
            "SUM(a.totalApp), SUM(a.appSold), SUM(a.appConfirmed), " +
            "SUM(a.appAvailable), SUM(a.appIssued), SUM(a.appDamaged), " +
            "SUM(a.appUnavailable)) " +
            "FROM AppStatusTrack a " +
            "WHERE a.issuedByType.appIssuedId = :issuedTypeId AND a.employee.emp_id = :empId")
     Optional<AppStatusTrackDTO> findAggregatedStatsByIssuedTypeAndEmployee(@Param("issuedTypeId") Integer issuedTypeId, @Param("empId") Integer empId);
    
    
    // ▼▼▼ ADD THIS NEW METHOD ▼▼▼
    @Query("SELECT new com.application.dto.AppStatusTrackDTO(" +
            "SUM(t.totalApp), SUM(t.appSold), SUM(t.appConfirmed), " +
            "SUM(t.appAvailable), SUM(t.appIssued), SUM(t.appDamaged), " +
            "SUM(t.appUnavailable)) " +
            "FROM AppStatusTrack t " +
            "WHERE t.academicYear.year = :year")
     Optional<AppStatusTrackDTO> findAggregatedStatsByYear(@Param("year") Integer year);
    
}